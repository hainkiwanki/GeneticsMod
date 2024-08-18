package com.hainkiwanki.geneticsmod.gui.renderer.components;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.research.ResearchNode;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class ResearchNodeButton extends GuiComponent {
    ResourceLocation buttonTexture = new ResourceLocation(GeneticsMod.MOD_ID, "textures/gui/research_table.png");
    private boolean isSelected;
    private boolean isHovered;
    public ResearchNode researchNode = null;
    private int x, y, offsetX, offsetY;

    public ResearchNodeButton(ResearchNode node, int offsetX, int offsetY) {
        this.researchNode = node;
        this.offsetX = offsetX + 9;
        this.offsetY = offsetY + 18;
    }

    public void draw(PoseStack pPoseStack, int x, int y) {
        pPoseStack.pushPose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, buttonTexture);
        this.x = x;
        this.y = y;

        if (this.isSelected) {
                this.blit(pPoseStack, this.x, this.y, 0, 178, 26, 26);
        } else {
            if (this.isHovered) {
                this.blit(pPoseStack,this.x, this.y, 0, 204, 26, 26);
            } else {
                this.blit(pPoseStack, this.x, this.y, 0, 230, 26, 26);
            }
        }
        pPoseStack.popPose();
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public boolean getHovered() {
        return this.isHovered;
    }

    public void mouseMoved(double pMouseX, double pMouseY, int size, int uOffset, int vOffset) {
        int left = this.x + this.offsetX + uOffset;
        int right = left + size;
        int top = this.y + this.offsetY + vOffset;
        int bottom = top + size;
        this.isHovered = pMouseX >= left && pMouseY >= top && pMouseX < right && pMouseY < bottom;
    }
}
