package com.hainkiwanki.geneticsmod.gui.renderer;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.cap.researchdata.PlayerResearchData;
import com.hainkiwanki.geneticsmod.cap.researchdata.PlayerResearchProvider;
import com.hainkiwanki.geneticsmod.gui.menus.ResearchTableMenu;
import com.hainkiwanki.geneticsmod.gui.renderer.components.ResearchNodeButton;
import com.hainkiwanki.geneticsmod.research.ResearchButtonManager;
import com.hainkiwanki.geneticsmod.research.ResearchNode;
import com.hainkiwanki.geneticsmod.util.Utils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ResearchTableScreen extends AbstractContainerScreen<ResearchTableMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(GeneticsMod.MOD_ID, "textures/gui/research_table.png");
    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(GeneticsMod.MOD_ID, "textures/gui/bg.png");

    private int xOffset = 0;
    private int yOffset = 0;
    private boolean dragging = false;
    private int dragX = 0;
    private int dragY = 0;
    private final ResearchButtonManager researchButtonManager;

    public ResearchTableScreen(ResearchTableMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 177;
        this.imageWidth = 252;
        this.researchButtonManager = new ResearchButtonManager();
    }

    @Override
    protected void init() {
        super.init();

        this.researchButtonManager.buttons.clear();
        Player player = Minecraft.getInstance().player;
        if(player != null) {
            player.getCapability(PlayerResearchProvider.PLAYER_RESEARCH_DATA).ifPresent(data -> {
                List<ResearchNode> playerDataNodes = data.getResearchNodes();
                int offsetX = (this.width - this.imageWidth) / 2;
                int offsetY = (this.height - this.imageHeight) / 2;
                for(int i = 0; i < playerDataNodes.size(); i++) {
                    ResearchNode node = playerDataNodes.get(i);
                    ResearchNodeButton button = new ResearchNodeButton(node, offsetX, offsetY);
                    this.researchButtonManager.registerButton(button);
                }
            });
        }
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        this.font.draw(pPoseStack, this.title.getString(), 8, 6, 4210752);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        this.renderBackground(pPoseStack);
        this.renderInside(pPoseStack, pMouseX, pMouseY);
        this.renderWindow(pPoseStack);
    }

    private void renderInside(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        int pOffsetX = (this.width - this.imageWidth) / 2;
        int pOffsetY = (this.height - this.imageHeight) / 2;
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate((double)(pOffsetX + 9), (double)(pOffsetY + 18), 0.0D);
        RenderSystem.applyModelViewMatrix();
        // begin draw contents

        Minecraft mc = Minecraft.getInstance();
        double scale = mc.getWindow().getGuiScale();
        int xPos = (int) (scale * (this.leftPos + 9));
        int yPos = (int) (scale * (this.topPos + 10));
        int width = (int)(scale * 140);
        int height = (int)(scale * 150);

        RenderSystem.enableScissor(xPos, yPos, width, height);
        this.renderCustomBackground(pPoseStack);
        this.renderPlayerNodes(pPoseStack);
        RenderSystem.disableScissor();


        // end draw contents
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.depthFunc(515);
        RenderSystem.disableDepthTest();
    }

    private void renderCustomBackground(PoseStack pPoseStack) {
        pPoseStack.pushPose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BG_TEXTURE);
        int i = Mth.floor(0);
        int j = Mth.floor(0);
        int k = i % 16;
        int l = j % 16;

        for(int i1 = -1; i1 <= 17; ++i1) {
            for (int j1 = -1; j1 <= 10; ++j1) {
                blit(pPoseStack, k + 16 * i1, l + 16 * j1, 0.0F, 0.0F, 16, 16, 16, 16);
            }
        }
        pPoseStack.popPose();
    }

    private void renderPlayerNodes(PoseStack pPoseStack) {
        for(int i = 0; i < this.researchButtonManager.buttons.size(); i++) {
            int xPos = i * 30 + this.xOffset;
            int yPos = this.yOffset;
            ResearchNodeButton button = this.researchButtonManager.buttons.get(i);
            button.draw(pPoseStack, xPos, yPos);
        }
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
    public void mouseMoved(double pMouseX, double pMouseY) {
        for(int i = 0; i < this.researchButtonManager.buttons.size(); i++) {
            this.researchButtonManager.buttons.get(i).mouseMoved(pMouseX, pMouseY, 24, 1, 1);
        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if(this.isAboveWindow(pMouseX, pMouseY) && pButton == 0) {
            this.dragging = !this.isAboveNode();
            this.dragX = (int)pMouseX - this.xOffset;
            this.dragY = (int)pMouseY - this.yOffset;
            for (int i = 0; i < this.researchButtonManager.buttons.size(); i++) {
                ResearchNodeButton btn = this.researchButtonManager.buttons.get(i);
                if (btn.getHovered()) {
                    this.researchButtonManager.selectButton(btn);
                }
            }
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

    private boolean isAboveWindow(double pMouseX, double pMouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        return Utils.isMouseAboveArea((int)pMouseX, (int)pMouseY, x, y, 9 ,18, 140, 150);
    }

    private boolean isAboveNode() {
        for(int i = 0; i < this.researchButtonManager.buttons.size(); i++) {
            ResearchNodeButton btn = this.researchButtonManager.buttons.get(i);
            if(btn.getHovered()) {
                return true;
            }
        }
        return false;
    }
}
