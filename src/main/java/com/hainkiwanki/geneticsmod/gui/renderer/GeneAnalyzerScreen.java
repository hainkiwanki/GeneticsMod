package com.hainkiwanki.geneticsmod.gui.renderer;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.gui.menus.GeneAnalyzerMenu;
import com.hainkiwanki.geneticsmod.gui.renderer.components.EnergyInfoArea;
import com.hainkiwanki.geneticsmod.util.Utils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

public class GeneAnalyzerScreen extends AbstractContainerScreen<GeneAnalyzerMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(GeneticsMod.MOD_ID, "textures/gui/gene_analyzer.png");
    private EnergyInfoArea energyInfoArea;

    public GeneAnalyzerScreen(GeneAnalyzerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        imageHeight = 180;
    }

    @Override
    protected void init() {
        super.init();
        assignEnergyInfoArea();
    }

    private void assignEnergyInfoArea() {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        energyInfoArea = new EnergyInfoArea(x + 6, y + 6, menu.blockEntity.getEnergyStorage());
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        // super.renderLabels(pPoseStack, pMouseX, pMouseY);

        this.font.draw(pPoseStack, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY + 12, 4210752);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderEnergyAreaTooltips(pPoseStack, pMouseX, pMouseY, x, y);

    }

    private void renderEnergyAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(Utils.isMouseAboveArea(pMouseX, pMouseY, x, y, 6, 6, energyInfoArea.DEFAULT_WIDTH, energyInfoArea.DEFAULT_HEIGHT)) {
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

        // energyInfoArea.draw(pPoseStack);

        // Draw some text
        /*
        pPoseStack.pushPose();
        Component ownerText = new TextComponent("test component text");
        int textX = leftPos + 78;
        int textY = topPos + 28;
        font.draw(pPoseStack, ownerText, (float)textX, (float)textY, 0);
        pPoseStack.popPose();
        */

        if(menu.isCrafting()) {
            blit(pPoseStack, x + 52, y + 33,
                    176, 0,
                    14, menu.getCraftingProgress());
        }

        if(menu.hasFuel()) {
            blit(pPoseStack, x + 22, y + 45 - menu.getScaledFuelProgress(),
                    188, 55 - menu.getScaledFuelProgress(),
                    2, menu.getScaledFuelProgress());
        }

        this.blit(pPoseStack, x + 6, y + 6 + 39 - menu.getEnergyProgress(),
                176, 16 + 39 - menu.getEnergyProgress(),
                12, menu.getEnergyProgress());
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }
}
