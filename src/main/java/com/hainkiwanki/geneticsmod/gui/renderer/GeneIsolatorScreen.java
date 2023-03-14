package com.hainkiwanki.geneticsmod.gui.renderer;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.gui.menus.GeneIsolatorMenu;
import com.hainkiwanki.geneticsmod.gui.renderer.components.EnergyInfoArea;
import com.hainkiwanki.geneticsmod.mobdata.MobData;
import com.hainkiwanki.geneticsmod.tags.ModTags;
import com.hainkiwanki.geneticsmod.util.Utils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class GeneIsolatorScreen extends AbstractContainerScreen<GeneIsolatorMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(GeneticsMod.MOD_ID, "textures/gui/gene_isolator.png");
    private EnergyInfoArea energyInfoArea;

    private Button traitButton;

    private int gridHeight = 64;
    private int gridWidth = 48;
    private int gridSize = 8;
    private int gridOffsetX = 39;
    private int gridOffsetY = 64;

    private int currentTraitIndex = 0;

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


        // Line line = new Line(0, 0,  20, 20);
        // line.draw(pPoseStack);
        addRenderableWidget(traitButton);
        // Title Screen => ImageButton

        /*
        if(menu.isCrafting()) {
            blit(pPoseStack, x + 52, y + 33 + 16 - menu.getCraftingProgress(),
                    176, 16 - menu.getCraftingProgress(),
                    14, menu.getCraftingProgress());
        }
        */
        if(menu.hasFuel()) {
            blit(pPoseStack, x + 22, y + 107 - menu.getScaledFuelProgress(),
                    188, 55 - menu.getScaledFuelProgress(),
                    2, menu.getScaledFuelProgress());
        }
        this.blit(pPoseStack, x + 6, y + 68 + 39 - menu.getEnergyProgress(),
                176, 15 + 39 - menu.getEnergyProgress(),
                12, menu.getEnergyProgress());

        drawGrid(pPoseStack, 39, 64, 8, 6);

        // Masking overlapping boundaries
        Minecraft mc = Minecraft.getInstance();
        double scale = mc.getWindow().getGuiScale();
        int xPos = (int)(scale * leftPos);
        int yPos = mc.getWindow().getHeight() - (int)(scale * topPos) - (int)(scale * imageHeight);
        int width = (int)(scale * imageWidth);
        int height = (int)(scale * imageHeight);
        RenderSystem.enableScissor(xPos,  yPos, width, height);
        drawGrid(pPoseStack, 93, 4, 16, 16);
        RenderSystem.disableScissor();
    }

    private void drawGrid(PoseStack pPoseStack, int offsetX, int offsetY, int rows, int cols) {
        for(int r = 0; r < rows; r++) {
            int textY = topPos + offsetY + r * gridSize;
            for (int c = 0; c < cols; c++) {
                int textX = leftPos + offsetX + c * gridSize;
                Component textComponent = new TextComponent("G");
                int textWidth = font.width(textComponent);
                font.draw(pPoseStack, textComponent, (float)textX + ((float)gridSize *0.5f - (float)(textWidth*0.5f)), (float)textY, 0xFFFFFFFF);
            }
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }

    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pButton == 0) {
            int xPos = (int)pMouseX - leftPos - gridOffsetX;
            int yPos = (int)pMouseY - topPos - gridOffsetY;
            boolean clickInGrid = Utils.isMouseAboveArea((int)pMouseX, (int)pMouseY, xPos, yPos,0, 0, gridWidth, gridHeight);
            if(clickInGrid){
                int row = xPos / gridSize;
                int col = yPos / gridSize;
            }
            //System.out.println("GUI Mouse Position: " + xPos + ", " + yPos);
            //System.out.println("Grid pos: " + row + ", " + col);
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
}
