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
    private int gridSearchRows = 32, gridSearchCols = 32;
    private int gridSearchWidth = 131, gridSearchHeight = 76;
    private float maskPosX = 0, maskPosY = 0;
    private int currentTraitIndex = 0;
    private float mousePosX, mousePosY;
    private List<Character> dnaRandomChars;
    private TextComponent dnaRandomText;
    private List<Pos2i> gridPoints;

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
        gridPoints = new ArrayList<>();
        fillCharList();
    }
    private void assignEnergyInfoArea() {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        energyInfoArea = new EnergyInfoArea(x + 21, y + 26, menu.blockEntity.getEnergyStorage());
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
        int width = (int)(scale * gridSearchWidth);
        int height = (int)(scale * gridSearchHeight);
        RenderSystem.enableScissor(xPos,  yPos, width, height);

        drawSearchGridSelectionSquare(pPoseStack);
        drawSearchGrid();
        drawSearchGridPoints(pPoseStack);

        RenderSystem.disableScissor();
    }
    @Override
    public void mouseMoved(double pMouseX, double pMouseY) {
        super.mouseMoved(pMouseX, pMouseY);
        mousePosX = (float)pMouseX;
        mousePosY = (float)pMouseY;
    }
    private void drawSearchGridSelectionSquare(PoseStack pPosetack)
    {
        int offsetX = 38;
        int offsetY = 53;
        int xPos = leftPos + offsetX;
        int yPos = topPos + offsetY;
        int size = 9;
        int rHeight = 9;
        int cWidth = 10;
        if(Utils.isMouseAboveArea((int)mousePosX, (int)mousePosY, xPos, yPos, 0, 0, 133, 78))
        {
            int r = Mth.clamp((((int)mousePosY - yPos + Mth.abs((int)maskPosY)) / rHeight), 0, gridSearchRows - 1);
            int c = Mth.clamp((((int)mousePosX - xPos + Mth.abs((int)maskPosX)) / cWidth), 0, gridSearchCols - 1);
            int x = c * cWidth;
            int y = r * rHeight;
            fill(pPosetack,
                    xPos + x + (int)maskPosX + 1,
                    yPos + y + (int)maskPosY + 1,
                    xPos + x + size + (int)maskPosX + 1,
                    yPos + y + size + (int)maskPosY + 1,
                0x77FFFFFF);

            int rDown = r - 1;
            int rUp = r + 1;
            int cRight = c + 1;
            int cLeft = c - 1;
            if(cRight >= 0 && cRight <= gridSearchCols - 1)
            {
                fill(pPosetack,
                        xPos + x + (int)maskPosX + 1 + cWidth,
                        yPos + y + (int)maskPosY + 1,
                        xPos + x + size + (int)maskPosX + 1 + cWidth,
                        yPos + y + size + (int)maskPosY + 1,
                        0x77FFFF00);
            }
            if(cLeft >= 0 && cLeft <= gridSearchCols - 1)
            {
                fill(pPosetack,
                        xPos + x + (int)maskPosX + 1 - cWidth,
                        yPos + y + (int)maskPosY + 1,
                        xPos + x + size + (int)maskPosX + 1 - cWidth,
                        yPos + y + size + (int)maskPosY + 1,
                        0x77FFFF00);
            }
            if(rDown >= 0 && rDown <= gridSearchRows - 1)
            {
                fill(pPosetack,
                        xPos + x + (int)maskPosX + 1,
                        yPos + y + (int)maskPosY + 1 - rHeight,
                        xPos + x + size + (int)maskPosX + 1,
                        yPos + y + size + (int)maskPosY + 1 - rHeight,
                        0x77FFFF00);
            }
            if(rUp >= 0 && rUp <= gridSearchRows - 1)
            {
                fill(pPosetack,
                        xPos + x + (int)maskPosX + 1,
                        yPos + y + (int)maskPosY + 1 + rHeight,
                        xPos + x + size + (int)maskPosX + 1,
                        yPos + y + size + (int)maskPosY + 1 + rHeight,
                        0x77FFFF00);
            }
        }
    }
    private void drawSearchGrid() {
        int textWidth = font.width(dnaRandomText)/ gridSearchRows;
        font.drawWordWrap(dnaRandomText, leftPos + 38 + 3 + (int)maskPosX, topPos + 53 + 2 + (int)maskPosY, textWidth, 0xFFFFFFFF);
    }
    private void drawSearchGridPoints(PoseStack pPosestack) {
        if(gridPoints.size() > 1) {
            int offset = 5;
            int offsetX = 38;
            int offsetY = 53;
            for (int i = 0; i < gridPoints.size() - 1; i++) {
                int posX1 = gridPoints.get(i).getX() + leftPos + offsetX + offset;
                int posX2 = gridPoints.get(i + 1).getX() + leftPos + offsetX + offset;

                int posY1 = gridPoints.get(i).getY() + topPos + offsetY + offset;
                int posY2 = gridPoints.get(i + 1).getY() + topPos + offsetY + offset;
                if (posX1 != posX2) {
                    hLine(pPosestack, Math.min(posX1, posX2) + (int)maskPosX, Math.max(posX1, posX2)  + (int)maskPosX, posY1 + (int)maskPosY, 0xFFFF0000);
                } else {
                    vLine(pPosestack, posX1 + (int)maskPosX, Math.min(posY1, posY2) - 1  + (int)maskPosY, Math.max(posY1, posY2) + + (int)maskPosY, 0xFFFF0000);
                }
            }
        }
    }
    private int diffY;
    private int diffX;
    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if(pButton == 1) {
            maskPosX += pDragX;
            maskPosX = Mth.clamp(maskPosX, -diffX - 3, 0);
            maskPosY += pDragY;
            maskPosY = Mth.clamp(maskPosY, -diffY - 2, 0);
        }
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }
    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }
    private ArrayList<Integer> selectedIndices = new ArrayList<>();
    private ArrayList<Integer> adjacentIndices = new ArrayList<>();
    private int currentSelectedIndex = -1;
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pButton == 0) {
            int xPos = leftPos + 38;
            int yPos = topPos + 53;
            if(Utils.isMouseAboveArea((int)pMouseX, (int)pMouseY, xPos, yPos,0, 0, 133, 78)) {
                int row = ((int)pMouseY - yPos + Mth.abs((int)maskPosY)) / 9;
                int col = ((int)pMouseX - xPos + Mth.abs((int)maskPosX)) / 10;
                int index = Utils.Convert2DTo1D(row, col, gridSearchCols);
                if(currentSelectedIndex > 0) {
                    if(adjacentIndices.contains(index) && !selectedIndices.contains(index)) {
                        selectedIndices.add(index);
                        gridPoints.add(new Pos2i(col * 10,row * 9));
                        currentSelectedIndex = index;
                        calculateAdjacentIndices(index);
                    }
                } else {
                    currentSelectedIndex = index;
                    calculateAdjacentIndices(index);
                    selectedIndices.add(index);
                    gridPoints.add(new Pos2i(col * 10,row * 9));
                }
            }
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
    private void calculateAdjacentIndices(int index) {
        adjacentIndices.clear();
        int rowAbove = index - gridSearchCols;
        if(rowAbove > 0) {
            adjacentIndices.add(rowAbove);
        }
        int rowBelow = index + gridSearchCols;
        if(rowBelow < gridSearchCols * gridSearchRows) {
            adjacentIndices.add(rowBelow);
        }
        int colRight = index + 1;
        if(index % gridSearchCols != 0) {
            adjacentIndices.add(colRight);
        }
        int colLeft = index - 1;
        if(colLeft % gridSearchCols != 0) {
            adjacentIndices.add(colLeft);
        }
    }
    private void fillCharList() {
        dnaRandomText = new TextComponent("");
        int max = gridSearchCols * gridSearchRows;
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

        int textWidth = font.width(dnaRandomText) / gridSearchRows;
        diffX = textWidth - gridSearchWidth;
        diffY = font.wordWrapHeight(dnaRandomText.getString(), textWidth) - gridSearchHeight;
    }
}
