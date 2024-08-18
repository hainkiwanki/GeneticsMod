package com.hainkiwanki.geneticsmod.gui.renderer.components;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.research.ResearchNode;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

public class ResearchNodeButton extends GuiComponent {
    ResourceLocation buttonTexture = new ResourceLocation(GeneticsMod.MOD_ID, "textures/gui/research_table.png");
    private boolean isSelected;
    private boolean isHovered;
    private ResearchNode researchNode = null;

    public ResearchNodeButton(ResearchNode node) {
        this.researchNode = node;
    }

    public void draw(PoseStack pPoseStack, int x, int y) {
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

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public void setHovered(boolean hovered) {
        this.isHovered = hovered;
    }


}
