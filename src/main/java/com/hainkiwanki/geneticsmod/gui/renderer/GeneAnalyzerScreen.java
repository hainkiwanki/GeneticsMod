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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

public class GeneAnalyzerScreen extends AbstractContainerScreen<GeneAnalyzerMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(GeneticsMod.MOD_ID, "textures/gui/gene_analyzer.png");
    private EnergyInfoArea energyInfoArea;

    public GeneAnalyzerScreen(GeneAnalyzerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
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
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        this.font.draw(poseStack, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY + 14, 4210752);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        renderEnergyAreaTooltips(poseStack, mouseX, mouseY, x, y);
    }

    private void renderEnergyAreaTooltips(PoseStack poseStack, int mouseX, int mouseY, int x, int y) {
        if(Utils.isMouseAboveArea(mouseX, mouseY, x, y, 6, 6, energyInfoArea.DEFAULT_WIDTH, energyInfoArea.DEFAULT_HEIGHT)) {
            renderTooltip(poseStack, energyInfoArea.getTooltips(),
                    Optional.empty(), mouseX - x, mouseY - y);
        }
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);

        if(menu.isCrafting()) {
            blit(poseStack, x + 52, y + 33,
                    176, 0,
                    14, menu.getCraftingProgress());
        }
        if(menu.hasFuel()) {
            blit(poseStack, x + 22, y + 45 - menu.getScaledFuelProgress(),
                    188, 55 - menu.getScaledFuelProgress(),
                    2, menu.getScaledFuelProgress());
        }
        this.blit(poseStack, x + 6, y + 6 + 39 - menu.getEnergyProgress(),
                176, 16 + 39 - menu.getEnergyProgress(),
                12, menu.getEnergyProgress());
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }
}
