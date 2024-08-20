package com.hainkiwanki.geneticsmod.gui.renderer;

import com.hainkiwanki.geneticsmod.GeneticsMod;
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

    // Node Information
    private final int nodeInfoTop = 12;
    private final int nodeIntoLeft = 150;
    private final int nodeInfoWidth = 99;
    private final int nodeInfoHeight = 158;
    private final int nodeInfoNameHeight = 12;

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
        this.renderInside(pPoseStack);
        this.renderWindow(pPoseStack);
        this.renderUnlockButton(pPoseStack);
        this.renderNodeInformation(pPoseStack);
    }

    private void renderInside(PoseStack pPoseStack) {
        int pOffsetX = (this.width - this.imageWidth) / 2;
        int pOffsetY = (this.height - this.imageHeight) / 2;
        pPoseStack.pushPose();
        pPoseStack.translate((double)(pOffsetX + 9), (double)(pOffsetY + 18), 0.0D);
        RenderSystem.applyModelViewMatrix();
        // begin draw contents
        Minecraft mc = Minecraft.getInstance();
        double scale = mc.getWindow().getGuiScale();
        int xPos = (int) (scale * (this.leftPos + 9));
        int yPos = (int) (scale * (this.topPos + 10));
        int maskWidth = (int)(scale * 140);
        int maskHeight = (int)(scale * 150);

        RenderSystem.enableScissor(xPos, yPos, maskWidth, maskHeight);
        this.renderCustomBackground(pPoseStack);
        this.renderPlayerNodes(pPoseStack);
        RenderSystem.disableScissor();

        // end draw contents
        pPoseStack.popPose();
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

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        this.blit(pPoseStack, x, y, 0, 0, this.imageWidth, this.imageHeight);

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    private void renderUnlockButton(PoseStack pPoseStack) {

    }

    private void renderNodeInformation(PoseStack pPoseStack) {
        ResearchNode node = this.researchButtonManager.getSelectedNode();
        if(node == null) {
            return;
        }
        this.renderNodeName(node, pPoseStack);

    }

    private void renderNodeName(ResearchNode node, PoseStack pPoseStack) {
        int onScreenWidth = 99 - 10; // 99 = actual this.width, 10 = 5 px offset both sides
        int nameWidth = this.font.width(node.name);
        List<String> results = new ArrayList<>();

        if(nameWidth > onScreenWidth) {
            String[] splitName = node.name.split(" ");
            int wordWidth = 0;
            StringBuilder name = new StringBuilder();
            for(int i = 0; i < splitName.length; i++) {
                int splitWordLength = this.font.width(splitName[i]);
                wordWidth += splitWordLength;
                if(wordWidth < onScreenWidth) {
                    name.append(splitName[i]);
                } else {
                    if(name.charAt(name.length() - 1) == ' ') {
                        name.deleteCharAt(name.length() - 1);
                    }
                    results.add(name.toString());
                    name.setLength(0);
                    name.append(splitName[i]);
                    wordWidth = splitWordLength;
                }
                if(wordWidth + 1 < onScreenWidth && i != splitName.length - 1) {
                    wordWidth++;
                    name.append(" ");
                }
                if (i == splitName.length - 1){
                    results.add(name.toString());
                }
            }

        } else {
            results.add(node.name);
        }

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        int titleOffsetX = 5;
        int titleOffsetY = this.nodeInfoNameHeight / 2;
        int titleHeight = results.size() * this.nodeInfoNameHeight + titleOffsetY;
        int informationOffsetY = this.nodeInfoNameHeight / 2;

        // Information background,
        this.render9Splice(pPoseStack,
                x + this.nodeIntoLeft,
                y + this.nodeInfoTop + titleHeight - 2 - informationOffsetY, // 1 empty pixel in texture on both sides
                this.nodeInfoHeight - titleHeight + 4 + informationOffsetY, // 1 empty pixel in texture on both sides
                this.nodeInfoWidth,
                234, 234, 8, 22, 22);

        // Title background
        this.render9Splice(pPoseStack,
                x + this.nodeIntoLeft - titleOffsetX,
                y + this.nodeInfoTop,
                results.size() * this.nodeInfoNameHeight + titleOffsetY * 2,
                this.nodeInfoWidth + titleOffsetX * 2,
                234, 190, 8, 22, 22);

        for(int j = 0; j < results.size(); j++) {
            int fontWidth = this.font.width(results.get(j));
            int left = 155 + ((onScreenWidth - fontWidth) / 2);
            int top = this.nodeInfoNameHeight + titleOffsetY + 2 + j * this.nodeInfoNameHeight;
            this.font.draw(pPoseStack, results.get(j), x + left, y + top, 0xffffff);
        }
    }

    private void render9Splice(PoseStack pPoseStack, int left, int top, int pHeight, int pWidth, int pUOffset, int pVOffset, int pPadding, int pUWidth, int pVHeight) {
        // Top Strip
        this.blit(pPoseStack, left, top, pUOffset, pVOffset, pPadding, pPadding);
        this.renderRepeating(pPoseStack, left + pPadding, top, pWidth - pPadding - pPadding, pPadding, pUOffset + pPadding, pVOffset, pUWidth - pPadding - pPadding, pVHeight);
        this.blit(pPoseStack, left + pWidth - pPadding, top, pUOffset + pUWidth - pPadding, pVOffset, pPadding, pPadding);
        // Bottom Strip
        this.blit(pPoseStack, left, top + pHeight - pPadding, pUOffset, pVOffset + pVHeight - pPadding, pPadding, pPadding);
        this.renderRepeating(pPoseStack, left + pPadding, top + pHeight - pPadding, pWidth - pPadding - pPadding, pPadding, pUOffset + pPadding, pVOffset + pVHeight - pPadding, pUWidth - pPadding - pPadding, pVHeight);
        this.blit(pPoseStack, left + pWidth - pPadding, top + pHeight - pPadding, pUOffset + pUWidth - pPadding, pVOffset + pVHeight - pPadding, pPadding, pPadding);
        // Center Strip
        this.renderRepeating(pPoseStack, left, top + pPadding, pPadding, pHeight - pPadding - pPadding, pUOffset, pVOffset + pPadding, pUWidth, pVHeight - pPadding - pPadding);
        this.renderRepeating(pPoseStack, left + pPadding, top + pPadding, pWidth - pPadding - pPadding, pHeight - pPadding - pPadding, pUOffset + pPadding, pVOffset + pPadding, pUWidth - pPadding - pPadding, pVHeight - pPadding - pPadding);
        this.renderRepeating(pPoseStack, left + pWidth - pPadding, top + pPadding, pPadding, pHeight - pPadding - pPadding, pUOffset + pUWidth - pPadding, pVOffset + pPadding, pUWidth, pVHeight - pPadding - pPadding);
    }

    protected void renderRepeating(PoseStack pPoseStack, int pX, int pY, int pBorderToU, int pBorderToV, int pUOffset, int pVOffset, int pUWidth, int pVHeight) {
        for(int i = 0; i < pBorderToU; i += pUWidth) {
            int j = pX + i;
            int k = Math.min(pUWidth, pBorderToU - i);

            for(int l = 0; l < pBorderToV; l += pVHeight) {
                int i1 = pY + l;
                int j1 = Math.min(pVHeight, pBorderToV - l);
                this.blit(pPoseStack, j, i1, pUOffset, pVOffset, k, j1);
            }
        }
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
