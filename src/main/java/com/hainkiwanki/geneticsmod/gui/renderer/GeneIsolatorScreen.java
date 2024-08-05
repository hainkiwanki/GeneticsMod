package com.hainkiwanki.geneticsmod.gui.renderer;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.config.CommonConfig;
import com.hainkiwanki.geneticsmod.gui.menus.GeneIsolatorMenu;
import com.hainkiwanki.geneticsmod.gui.renderer.components.EnergyInfoArea;
import com.hainkiwanki.geneticsmod.gui.renderer.components.Pos2i;
import com.hainkiwanki.geneticsmod.util.Utils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import java.lang.reflect.Array;
import java.util.*;

public class GeneIsolatorScreen extends AbstractContainerScreen<GeneIsolatorMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(GeneticsMod.MOD_ID, "textures/gui/gene_isolator.png");
    private EnergyInfoArea energyInfoArea;
    private Button traitButton;
    private int gridSize = 16, gridWidth = 76, gridHeight = 76, gridOffsetX = 110, gridOffsetY = 48;
    private int gridRowHeight = 9, gridColWidth = 10;
    private ArrayList<Integer> selectedIndices = new ArrayList<>();
    private ArrayList<Integer> adjacentIndices = new ArrayList<>();
    private ArrayList<Pos2i> toFindShapePattern = new ArrayList<>();
    private ArrayList<Character> toFindShapeString = new ArrayList<>();
    private int currentSelectedIndex = -1;
    private float maskPosX = 0, maskPosY = 0;
    private float mousePosX, mousePosY;
    // TODO: make mouse dragging only works inside little window

    private int currentTraitIndex = 0;
    private TextComponent dnaRandomTextComp;
    private String dnaRandomString;
    private TextComponent dnaToFindText;
    private int gridSizeToFind;

    private int randIndex;
    private int restrictedRandIndex;
    private boolean isMouseOverSearchGrid = false;

    public GeneIsolatorScreen(GeneIsolatorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        imageHeight = 235;
        imageWidth = 194;
    }

    @Override
    protected void init() {
        super.init();
        assignEnergyInfoArea();
        createButton();
        fillCharList();
    }

    private void createButton() {
        traitButton = new Button(leftPos + 37, topPos + 108, 56, 20, new TextComponent("Trait"), (btn) -> {
            List<String> tags = menu.getSampleTags();
            if(tags != null) {
                currentTraitIndex++;
                currentTraitIndex = currentTraitIndex % tags.size();
                TranslatableComponent textComponent = new TranslatableComponent("tooltip.geneticsmod.genesampleitem." + tags.get(currentTraitIndex));
                this.traitButton.setWidth(56);
                this.traitButton.setHeight(20);
                this.traitButton.setMessage(textComponent);

                generateToFindDnaString(tags.get(currentTraitIndex));
            }
        });
        addRenderableWidget(traitButton);
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
        this.font.draw(pPoseStack, this.playerInventoryTitle, (float)this.inventoryLabelX + 9, (float)this.inventoryLabelY + 69, 4210752);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        renderEnergyAreaTooltips(pPoseStack, pMouseX, pMouseY, x, y);
    }

    private void renderEnergyAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(Utils.isMouseAboveArea(pMouseX, pMouseY, x, y, 5, 55, energyInfoArea.DEFAULT_WIDTH, energyInfoArea.DEFAULT_HEIGHT)) {
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
            blit(pPoseStack, x + 21, y + 94 - menu.getScaledFuelProgress(),
                    206, 39 - menu.getScaledFuelProgress(),
                    2, menu.getScaledFuelProgress());
        }
        this.blit(pPoseStack, x + 5, y + 94 - menu.getEnergyProgress(),
                194, 39 - menu.getEnergyProgress(),
                12, menu.getEnergyProgress());

        int xPos = leftPos + gridOffsetX;
        int yPos = topPos + gridOffsetY;
        isMouseOverSearchGrid = Utils.isMouseAboveArea(pMouseX, pMouseY, xPos, yPos, 0, 0, 78, 78);

        startMaskArea();
        drawHighlightedSquares(pPoseStack);
        drawSearchGrid();
        drawTrackedLine(pPoseStack);
        // drawGridSquare(pPoseStack, restrictedRandIndex, 0x770000FF);
        endMaskArea();
        drawToFindGrid();
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
            int xPos = leftPos + gridOffsetX;
            int yPos = topPos + gridOffsetY;
            if(Utils.isMouseAboveArea((int)pMouseX, (int)pMouseY, xPos, yPos,0, 0, 78, 78)) {
                int row = ((int)pMouseY - yPos + Mth.abs((int)maskPosY)) / 9;
                int col = ((int)pMouseX - xPos + Mth.abs((int)maskPosX)) / 10;
                int index = Utils.Convert2DTo1D(row, col, gridSize);

                if(selectedIndices.contains(index)) {
                    int lastIndex = selectedIndices.size() - 1;
                    if(selectedIndices.get(lastIndex) == index) {
                        selectedIndices.remove(lastIndex);
                        if(selectedIndices.size() >= 1) {
                            calculateAdjacentIndices(selectedIndices.get(selectedIndices.size() - 1));
                            currentSelectedIndex = selectedIndices.get(selectedIndices.size() - 1);
                        } else {
                            adjacentIndices.clear();
                            currentSelectedIndex = -1;
                        }
                    }
                } else {
                    if(selectedIndices.size() == 0 || adjacentIndices.contains(index)) {
                        selectedIndices.add(index);
                        calculateAdjacentIndices(index);
                        currentSelectedIndex = index;
                    }
                }
            }
        }
        hasFoundCorrectDnaString();
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
    //endregion

    //region Draw Highlighted Squares
    private void drawTrackedLine(PoseStack poseStack) {
        if(selectedIndices.size() <= 0) return;

        for (int i = 0; i < selectedIndices.size() - 1; i++) {
            Pos2i first = Utils.Convert1DTo2D(selectedIndices.get(i), gridSize);
            Pos2i second = Utils.Convert1DTo2D(selectedIndices.get(i + 1), gridSize);

            if(first.getX() == second.getX()) {
                int x = leftPos + gridOffsetX + (int)maskPosX + first.getX() * gridColWidth + (int)(0.5f * gridColWidth);
                int y1 = topPos + gridOffsetY + (int)maskPosY + first.getY() * gridRowHeight + (int)(0.5f * gridRowHeight);
                int y2 = topPos + gridOffsetY + (int)maskPosY + second.getY() * gridRowHeight + (int)(0.5f * gridRowHeight) + 1;
                if(y2 < y1) {
                    y1++;
                    y2--;
                }
                vLine(poseStack, x, y1, y2, 0xFFFF0000);
            } else if(first.getY() == first.getY()) {
                int x1 = leftPos + gridOffsetX + (int)maskPosX + first.getX() * gridColWidth + (int)(0.5f * gridColWidth);
                int x2 = leftPos + gridOffsetX + (int)maskPosX + second.getX() * gridColWidth + (int)(0.5f * gridColWidth);
                int y = topPos + gridOffsetY + (int)maskPosY + first.getY() * gridRowHeight + (int)(0.5f * gridRowHeight);
                hLine(poseStack, x1, x2, y, 0xFFFF0000);
            } else {
                // ERROR
            }

        }
    }

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
            if(isMouseOverSearchGrid) {
                drawGridSquare(poseStack, convertMousePosTo2DIndex(), 0x7700FF00);
            }
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
        int maxX = minX + size + 1;
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
        dnaRandomTextComp = new TextComponent("");
        int max = gridSize * gridSize;
        dnaRandomString = "";
        List<Character> dnaLetters = Arrays.asList(new Character[]{'C', 'G', 'T', 'A'});
        Random rand = new Random();
        for (int i = 0; i < max; i++) {
            Character cLetter = dnaLetters.get(rand.nextInt(4));
            addCharacterToTextcomponent(cLetter, dnaRandomTextComp);
            dnaRandomString += cLetter;
        }
        int textWidth = font.width(dnaRandomTextComp) / gridSize;
        diffX = textWidth - gridWidth;
        diffY = font.wordWrapHeight(dnaRandomTextComp.getString(), textWidth) - gridHeight;
    }

    private void generateToFindDnaString(String attribute) {
        Random rand = new Random();
        gridSizeToFind = 2;
        List<String> randomShape = new ArrayList<>();
        List<? extends List<String>> easyShapes;
//        if(CommonConfig.EASY_ATTRIBUTES.get().contains(attribute)) {
//            easyShapes = CommonConfig.EASY_SHAPES.get();
//            randomShape = easyShapes.get(rand.nextInt(easyShapes.size()));
//            gridSizeToFind = 2;
//        }
//        else if(CommonConfig.NORMAL_ATTRIBUTES.get().contains(attribute)) {
//            easyShapes = CommonConfig.NORMAL_SHAPES.get();
//            randomShape = easyShapes.get(rand.nextInt(easyShapes.size()));
//            gridSizeToFind = 3;
//        }
//        else if(CommonConfig.HARD_ATTRIBUTES.get().contains(attribute)) {
//            easyShapes = CommonConfig.HARD_SHAPES.get();
//            randomShape = easyShapes.get(rand.nextInt(easyShapes.size()));
//            gridSizeToFind = 4;
//        }
//        else if(CommonConfig.EXPERT_ATTRIBUTES.get().contains(attribute)) {
//            easyShapes = CommonConfig.EXPERT_SHAPES.get();
//            randomShape = easyShapes.get(rand.nextInt(easyShapes.size()));
//            gridSizeToFind = 5;
//        }
//        else if(CommonConfig.NIGHTMARE_ATTRIBUTES.get().contains(attribute)) {
//            easyShapes = CommonConfig.NIGHTMARE_SHAPES.get();
//            randomShape = easyShapes.get(rand.nextInt(easyShapes.size()));
//            gridSizeToFind = 6;
//        }

        randIndex = rand.nextInt(gridSize * gridSize); // 0 to (x*x-1)
        Pos2i gridPos = Utils.Convert1DTo2D(randIndex, gridSize); // 0-based
        int row = gridPos.getY() + 1; // 1-based
        int col = gridPos.getX() + 1; // 1-based

        // Col check
        int colDiff = (col + (gridSizeToFind - 1)) - gridSize;
        if (colDiff > 0) {
            col -= colDiff;
        }

        // Row check
        int rowDiff = (row + (gridSizeToFind - 1)) - gridSize;
        if(rowDiff > 0) {
            row -= rowDiff;
        }
        restrictedRandIndex = Utils.Convert2DTo1D(row - 1, col - 1, gridSize); // -1 => 1-based to 0-based

        dnaToFindText = new TextComponent("");
        int rowExtra = 0;
        toFindShapePattern = new ArrayList<>();
        toFindShapeString = new ArrayList<>();
        for (int i = 0; i < randomShape.size(); i++) {
            rowExtra = i / gridSizeToFind;
            if(!randomShape.get(i).isBlank() && !randomShape.get(i).isEmpty()) {
                Character c = dnaRandomString.charAt(restrictedRandIndex + (rowExtra * gridSize) + (i % gridSizeToFind));
                addCharacterToTextcomponent(c, dnaToFindText);
                toFindShapePattern.add(Utils.Convert1DTo2D(i, gridSizeToFind));
                toFindShapeString.add(c);
            } else {
                addCharacterToTextcomponent(' ', dnaToFindText);
            }
        }
    }

    private boolean hasFoundCorrectDnaString() {
        if(selectedIndices.size() == 0 || toFindShapePattern.size() == 0 || toFindShapeString.size() == 0) return false;

        // Check length
        boolean correctShape = true;
        boolean correctString = true;

        if(selectedIndices.size() == toFindShapeString.size()) {
            ArrayList<Integer> sortedList = new ArrayList<>(selectedIndices);
            Collections.sort(sortedList);
            // Shape check
            int firstIndex = sortedList.get(0);
            for (int i = 0; i < toFindShapePattern.size(); i++) {
                int toCompare = Utils.Add2DTo1D(toFindShapePattern.get(i), firstIndex, gridSize);
                if(sortedList.get(i) != toCompare) {
                    correctShape = false;
                    break;
                }
            }
            // System.out.println("Found correct shape: " + correctShape);

            // String check
            for (int i = 0; i < toFindShapeString.size(); i++) {
                if (toFindShapeString.get(i) != dnaRandomString.charAt(sortedList.get(i))) {
                    correctString = false;
                    break;
                }
            }
            // System.out.println("Found correct string: " + correctString);

        }
        return correctShape && correctString;
    }

    private void addCharacterToTextcomponent(char c, TextComponent textComponent) {
        Component aComp = new TextComponent("A ").withStyle(ChatFormatting.BLUE);
        Component tComp = new TextComponent("T ").withStyle(ChatFormatting.GREEN);
        Component gComp = new TextComponent("G ").withStyle(ChatFormatting.RED);
        Component cComp = new TextComponent("C ").withStyle(ChatFormatting.YELLOW);
        Component spaceComp = new TextComponent("_ ").withStyle(style -> style.withColor(0x00000000));
        if (c == 'G') {
            textComponent.append(gComp);
        } else if (c == 'T') {
            textComponent.append(tComp);
        } else if (c == 'C') {
            textComponent.append(cComp);
        } else if (c == 'A') {
            textComponent.append(aComp);
        } else {
            textComponent.append(spaceComp);
        }
    }
    //endregion

    //region Mask Area
    private void startMaskArea() {
        Minecraft mc = Minecraft.getInstance();
        double scale = mc.getWindow().getGuiScale();
        // 110, 112 to the bottom left position of grid starting from bottom left of inventory gui
        int xPos = (int)(scale * (leftPos + 110));
        int yPos = mc.getWindow().getHeight() - (int)(scale * topPos) - (int)(scale * imageHeight) + (int)(scale * 111);
        int width = (int)(scale * gridWidth);
        int height = (int)(scale * gridHeight);
        RenderSystem.enableScissor(xPos,  yPos, width, height);
    }

    private void endMaskArea() {
        RenderSystem.disableScissor();
    }
    //endregion

    //region Draw Letters
    private void drawSearchGrid() {
        int textWidth = font.width(dnaRandomTextComp) / gridSize;
        font.drawWordWrap(dnaRandomTextComp, leftPos + gridOffsetX + 3 + (int)maskPosX, topPos + gridOffsetY + 2 + (int)maskPosY, textWidth, 0xFFFFFFFF);
    }

    private void drawToFindGrid() {
        if(dnaToFindText == null) {
            return;
        }
        int width = 56 / 2;
        int height = 56 / 2;
        if(!dnaToFindText.getString().isEmpty() && !dnaToFindText.getString().isBlank()) {
            int textWidth = font.width(dnaToFindText) / gridSizeToFind;
            int xPos = leftPos + 38 + width - (textWidth / 2) + 1;
            int yPos = topPos + 49 + height - (gridRowHeight * gridSizeToFind / 2);
            font.drawWordWrap(dnaToFindText, xPos, yPos, textWidth, 0xFFFFFFFF);
        }
    }
    //endregion
}
