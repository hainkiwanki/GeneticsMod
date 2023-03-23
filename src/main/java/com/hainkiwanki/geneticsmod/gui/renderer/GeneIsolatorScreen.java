package com.hainkiwanki.geneticsmod.gui.renderer;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.gui.menus.GeneIsolatorMenu;
import com.hainkiwanki.geneticsmod.gui.renderer.components.EnergyInfoArea;
import com.hainkiwanki.geneticsmod.gui.renderer.components.Pos2i;
import com.hainkiwanki.geneticsmod.util.Utils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import java.util.*;

public class GeneIsolatorScreen extends AbstractContainerScreen<GeneIsolatorMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(GeneticsMod.MOD_ID, "textures/gui/gene_isolator.png");
    private EnergyInfoArea energyInfoArea;
    private Button traitButton;
    private int gridSize = 5, gridWidth = 131, gridHeight = 76, gridOffsetX = 38, gridOffsetY = 53;
    private int gridRowHeight = 9, gridColWidth = 10;
    private ArrayList<Integer> selectedIndices = new ArrayList<>();
    private ArrayList<Integer> adjacentIndices = new ArrayList<>();
    private int currentSelectedIndex = -1;
    private float maskPosX = 0, maskPosY = 0;
    private float mousePosX, mousePosY;

    private int currentTraitIndex = 0;
    private List<Character> dnaRandomChars;
    private TextComponent dnaRandomText;

    public GeneIsolatorScreen(GeneIsolatorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        imageHeight = 241;
    }

    @Override
    protected void init() {
        super.init();
        assignEnergyInfoArea();

        int paddingOffset = 3;
        int maxWidth = 55 + paddingOffset;
        traitButton = new Button(leftPos + 36, topPos + 5, maxWidth, 20, new TextComponent("Trait"), (btn) -> {
            List<String> tags = menu.getSampleTags();
            if(tags != null) {
                currentTraitIndex++;
                currentTraitIndex = currentTraitIndex % tags.size();
                TranslatableComponent textComponent = new TranslatableComponent("tooltip.geneticsmod.genesampleitem." + tags.get(currentTraitIndex));
                int textWidth = font.width(textComponent) + paddingOffset;
                if(textWidth > maxWidth)
                    textWidth = maxWidth;
                this.traitButton.setWidth(maxWidth);
                this.traitButton.setMessage(textComponent);

            }
        });
        addRenderableWidget(traitButton);

        dnaRandomChars = new ArrayList<>();
        fillCharList();
    }

    private void assignEnergyInfoArea() {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        energyInfoArea = new EnergyInfoArea(x + 21, y + 26, menu.blockEntity.getEnergyStorage());
    }

    //region Render Functions
    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        this.font.draw(pPoseStack, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY + 73, 4210752);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        renderEnergyAreaTooltips(pPoseStack, pMouseX, pMouseY, x, y);

    }

    private void renderEnergyAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(Utils.isMouseAboveArea(pMouseX, pMouseY, x, y, 6, 68, energyInfoArea.DEFAULT_WIDTH, energyInfoArea.DEFAULT_HEIGHT)) {
            renderTooltip(pPoseStack, energyInfoArea.getTooltips(),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTicks, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

        if(menu.hasFuel()) {
            blit(pPoseStack, x + 22, y + 107 - menu.getScaledFuelProgress(),
                    188, 55 - menu.getScaledFuelProgress(),
                    2, menu.getScaledFuelProgress());
        }
        this.blit(pPoseStack, x + 6, y + 68 + 39 - menu.getEnergyProgress(),
                176, 15 + 39 - menu.getEnergyProgress(),
                12, menu.getEnergyProgress());

        Minecraft mc = Minecraft.getInstance();
        double scale = mc.getWindow().getGuiScale();
        // 38, 112 to the bottom left position of grid
        int xPos = (int)(scale * (leftPos + 38));
        int yPos = mc.getWindow().getHeight() - (int)(scale * topPos) - (int)(scale * imageHeight) + (int)(scale * 112);
        int width = (int)(scale * gridWidth);
        int height = (int)(scale * gridHeight);
        RenderSystem.enableScissor(xPos,  yPos, width, height);

        // drawSearchGridSelectionSquare(pPoseStack);
        drawHighlightedSquares(pPoseStack);
        drawSearchGrid();
        RenderSystem.disableScissor();
    }
    //endregion

    //region Mouse Events
    @Override
    public void mouseMoved(double pMouseX, double pMouseY) {
        super.mouseMoved(pMouseX, pMouseY);
        mousePosX = (float)pMouseX;
        mousePosY = (float)pMouseY;
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if(pButton == 1 && gridSize >= 16) {
            maskPosX += pDragX;
            maskPosX = Mth.clamp(maskPosX, -diffX - 3, 0);
            maskPosY += pDragY;
            maskPosY = Mth.clamp(maskPosY, -diffY - 2, 0);
        }
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pButton == 0) {
            int xPos = leftPos + 38;
            int yPos = topPos + 53;
            if(Utils.isMouseAboveArea((int)pMouseX, (int)pMouseY, xPos, yPos,0, 0, 133, 78)) {
                int row = ((int)pMouseY - yPos + Mth.abs((int)maskPosY)) / 9;
                int col = ((int)pMouseX - xPos + Mth.abs((int)maskPosX)) / 10;
                int index = Utils.Convert2DTo1D(row, col, gridSize);

                if(currentSelectedIndex < 0) {
                    selectedIndices.add(index);
                    calculateAdjacentIndices(index);
                }

                if (adjacentIndices.contains(index) && !selectedIndices.contains(index)) {
                    selectedIndices.add(index);
                    calculateAdjacentIndices(index);
                }



                currentSelectedIndex = index;
            }
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
    //endregion

    //region Draw Highlighted Squares
    private void drawHighlightedSquares(PoseStack poseStack) {
        drawSelectedSquare(poseStack);
        if(selectedIndices.size() >= 1) {
            drawGridSquare(poseStack, selectedIndices.get(0), 0x7700FF00);
        }
        drawListOfIndices(poseStack, adjacentIndices, 0x77FFFF00, false);
        drawListOfIndices(poseStack, selectedIndices, 0x77777777, true);
    }

    private void drawSelectedSquare(PoseStack poseStack) {
        if(currentSelectedIndex < 0) {
            drawGridSquare(poseStack, convertMousePosTo2DIndex(), 0x7700FF00);
        } else {
            drawGridSquare(poseStack, currentSelectedIndex, 0x7700FF00);
        }
    }

    private void drawListOfIndices(PoseStack poseStack, List<Integer> list,  int color, boolean ignoreEnds) {
        if(list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (ignoreEnds && (i == 0 || i == list.size() - 1)) {
                    continue;
                }
                drawGridSquare(poseStack, list.get(i), color);
            }
        }
    }

    private void drawGridSquare(PoseStack poseStack, int index, int color) {
        Pos2i rc = Utils.Convert1DTo2D(index, gridSize);
        drawGridSquare(poseStack, rc.getY(), rc.getX(), color);
    }

    private void drawGridSquare(PoseStack poseStack, Pos2i gridPos, int color) {
        drawGridSquare(poseStack, gridPos.getY(), gridPos.getX(), color);
    }

    private void drawGridSquare(PoseStack poseStack, int row, int col, int color) {
        int size = 9;
        int minX = leftPos + gridOffsetX + (col * gridColWidth) + (int)maskPosX + 1;
        int minY = topPos + gridOffsetY + (row * gridRowHeight) + (int)maskPosY + 1;
        int maxX = minX + size;
        int maxY = minY + size;
        fill(poseStack, minX, minY, maxX, maxY, color);
    }
    //endregion

    //region Help Functions
    private Pos2i convertMousePosTo2DIndex() {
        int row = (int)mousePosY - topPos - gridOffsetY + Mth.abs((int)maskPosY);
        row /= gridRowHeight;
        row = Mth.clamp(row, 0, gridSize - 1);

        int col = (int)mousePosX - leftPos - gridOffsetX + Mth.abs((int)maskPosX);
        col /= gridColWidth;
        col = Mth.clamp(col, 0, gridSize - 1);

        return new Pos2i(col, row);
    }

    private void calculateAdjacentIndices(int index) {
        adjacentIndices.clear();
        int rowAbove = index - gridSize;
        if(rowAbove >= 0 && !selectedIndices.contains(rowAbove)) {
            adjacentIndices.add(rowAbove);
        }
        int rowBelow = index + gridSize;
        if(rowBelow < gridSize * gridSize && !selectedIndices.contains(rowBelow)) {
            adjacentIndices.add(rowBelow);
        }
        int colRight = index + 1;
        if((index + 1) % gridSize != 0 && !selectedIndices.contains(colRight)) {
            adjacentIndices.add(colRight);
        }
        int colLeft = index - 1;
        if(colLeft % gridSize != gridSize - 1 && !selectedIndices.contains(colLeft)) {
            adjacentIndices.add(colLeft);
        }
    }

    private int diffY;
    private int diffX;
    private void fillCharList() {
        dnaRandomText = new TextComponent("");
        int max = gridSize * gridSize;
        List<Character> dnaLetters = Arrays.asList(new Character[]{'C', 'G', 'T', 'A'});
        Random rand = new Random();

        Component aComp = new TextComponent("A ").withStyle(ChatFormatting.BLUE);
        Component tComp = new TextComponent("T ").withStyle(ChatFormatting.GREEN);
        Component gComp = new TextComponent("G ").withStyle(ChatFormatting.RED);
        Component cComp = new TextComponent("C ").withStyle(ChatFormatting.YELLOW);

        for (int i = 0; i < max; i++) {
            Character cLetter = dnaLetters.get(rand.nextInt(4));
            dnaRandomChars.add(cLetter);
            if (cLetter == 'G') {
                dnaRandomText.append(gComp);
            } else if (cLetter == 'T') {
                dnaRandomText.append(tComp);
            } else if (cLetter == 'C') {
                dnaRandomText.append(cComp);
            } else if (cLetter == 'A') {
                dnaRandomText.append(aComp);
            }
        }

        int textWidth = font.width(dnaRandomText) / gridSize;
        diffX = textWidth - gridWidth;
        diffY = font.wordWrapHeight(dnaRandomText.getString(), textWidth) - gridHeight;
    }
    //endregion

    private void drawSearchGrid() {
        int textWidth = font.width(dnaRandomText)/ gridSize;
        font.drawWordWrap(dnaRandomText, leftPos + 38 + 3 + (int)maskPosX, topPos + 53 + 2 + (int)maskPosY, textWidth, 0xFFFFFFFF);
    }
}
