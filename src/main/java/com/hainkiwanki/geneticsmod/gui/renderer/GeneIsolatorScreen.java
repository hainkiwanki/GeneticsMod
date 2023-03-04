package com.hainkiwanki.geneticsmod.gui.renderer;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.gui.menus.GeneIsolatorMenu;
import com.hainkiwanki.geneticsmod.gui.renderer.components.EnergyInfoArea;
import com.hainkiwanki.geneticsmod.util.Utils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.advancements.AdvancementTab;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

public class GeneIsolatorScreen extends AbstractContainerScreen<GeneIsolatorMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(GeneticsMod.MOD_ID, "textures/gui/gene_isolator.png");
    private EnergyInfoArea energyInfoArea;

    public GeneIsolatorScreen(GeneIsolatorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        assignEnergyInfoArea();
    }

    private void assignEnergyInfoArea() {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        energyInfoArea = new EnergyInfoArea(x + 21, y + 26, menu.blockEntity.getEnergyStorage());
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        super.renderLabels(pPoseStack, pMouseX, pMouseY);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderEnergyAreaTooltips(pPoseStack, pMouseX, pMouseY, x, y);

    }

    private void renderEnergyAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(Utils.isMouseAboveArea(pMouseX, pMouseY, x, y, 21, 26, 14, 41)) {
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
        Component ownerText = new TextComponent("test component text");
        int textX = leftPos + 78;
        int textY = topPos + 28;
        // font.draw(pPoseStack, ownerText, (float)textX, (float)textY, 0);

        //Line line = new Line(0, 0,  20, 20);
        //line.draw(pPoseStack);

        if(menu.isCrafting()) {
            // start top left corner x, y,
            // offset to part of image x, y
            // width to draw, height to draw
            blit(pPoseStack, x + 103, y + 38,
                    176, 30,
                    menu.getCraftingProgress(), 16);
        }

        if(menu.hasFuel()) {
            blit(pPoseStack, x + 46, y + 31 + 14 - menu.getScaledFuelProgress(),
                    176, 14 - menu.getScaledFuelProgress(),
                    14, menu.getScaledFuelProgress());
        }

        this.blit(pPoseStack, x + 21, y + 26 + 41 - menu.getEnergyProgress(),
                176, 88 - menu.getEnergyProgress(),
                14, menu.getEnergyProgress());
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }

    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pButton == 0) {
            System.out.println("Clicked: " + pMouseX + ", " + pMouseY);
            System.out.println("GUI Mouse Position: " + ((int)pMouseX - leftPos) + ", " + ((int)pMouseY - topPos));
            System.out.println("Width " + width + ", imagewidth: " + imageWidth);
            System.out.println("Height " + height + ", imageheight: " + imageHeight);
            System.out.println("Clicked inside: " + Utils.isMouseAboveArea((int)pMouseX, (int)pMouseY, leftPos, topPos,
                    0, 0, imageWidth, imageHeight));
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
}
