package com.hainkiwanki.geneticsmod.gui.renderer;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.gui.menus.ResearchTableMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class ResearchTableScreen extends AbstractContainerScreen<ResearchTableMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(GeneticsMod.MOD_ID, "textures/gui/research_table.png");
    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(GeneticsMod.MOD_ID, "textures/gui/bg.png");

    private int xOffset = 0;
    private int yOffset = 0;
    private boolean dragging = false;
    private int dragX = 0;
    private int dragY = 0;

    public ResearchTableScreen(ResearchTableMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 140;
        this.imageWidth = 252;
    }


    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        font.draw(pPoseStack, title.getString(), 8, 6, 4210752);

    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        this.renderBackground(pPoseStack);
        this.renderInside(pPoseStack);
        this.renderWindow(pPoseStack);
    }

    private void renderInside(PoseStack pPoseStack) {
        int pOffsetX = (width - imageWidth) / 2;
        int pOffsetY = (height - imageHeight) / 2;
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate((double)(pOffsetX + 9), (double)(pOffsetY + 18), 0.0D);
        RenderSystem.applyModelViewMatrix();
        // begin draw contents

        Minecraft mc = Minecraft.getInstance();
        double scale = mc.getWindow().getGuiScale();
        int xPos = (int) (scale * (leftPos + 9));
        int yPos = (int) (scale * (topPos + 10));
        int width = (int)(scale * 234);
        int height = (int)(scale * 113);
        RenderSystem.enableScissor(xPos, yPos, width, height);

        pPoseStack.pushPose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BG_TEXTURE);
        int i = Mth.floor(0);
        int j = Mth.floor(0);
        int k = i % 16;
        int l = j % 16;

        for(int i1 = -1; i1 <= 15; ++i1) {
            for (int j1 = -1; j1 <= 8; ++j1) {
                blit(pPoseStack, k + 16 * i1, l + 16 * j1, 0.0F, 0.0F, 16, 16, 16, 16);
            }
        }

        RenderSystem.disableScissor();

        // end draw contents
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.depthFunc(515);
        RenderSystem.disableDepthTest();
    }

    private void renderWindow(PoseStack pPoseStack) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if(pButton == 0) {
            this.dragging = true;
            this.dragX = (int)pMouseX - xOffset;
            this.dragY = (int)pMouseY - yOffset;
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        if(pButton == 0) {
            this.dragging = false;
        }

        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if(dragging && pButton == 0) {
            this.xOffset = (int)pMouseX - this.dragX;
            this.yOffset = (int)pMouseY - this.dragY;
            return true;
        }

        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }
}
