package com.hainkiwanki.geneticsmod.gui.renderer.components;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.research.ResearchNode;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

public class ResearchNodeButton extends Button {
    ResourceLocation buttonTexture = new ResourceLocation(GeneticsMod.MOD_ID, "textures/gui/research_table.png");
    private boolean isSelected = false;
    private ResearchNode researchNode = null;

    private int u = 228, v = 0;
    private final int textureWidth = 28;

    public ResearchNodeButton(ResearchNode node, int x, int y, Button.OnPress pOnPress) {
        super(x, y, 28, 28, TextComponent.EMPTY, pOnPress);
        this.researchNode = node;
    }

    @Override
    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        pPoseStack.pushPose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, buttonTexture);
        if (this.isSelected) {
                this.blit(pPoseStack, x, y, 0, 178, 26, 26);
        } else {
            if (this.isHovered) {
            this.blit(pPoseStack, x, y, 0, 204, 26, 26);
            } else {
                this.blit(pPoseStack, x, y, 0, 230, 26, 26);
            }
        }
        pPoseStack.popPose();
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if(pButton == 0) {
            this.isSelected = this.clicked(pMouseX, pMouseY);
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    public void isSelected() {
        this.isSelected = true;
    }

    public void deselect() {
        this.isSelected = false;
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

    }
}
