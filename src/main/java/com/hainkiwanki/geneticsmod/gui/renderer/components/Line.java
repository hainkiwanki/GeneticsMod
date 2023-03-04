package com.hainkiwanki.geneticsmod.gui.renderer.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;

public class Line extends GuiComponent {

    private Pos2i point1;
    private Pos2i point2;

    public Line(int x1, int y1, int x2, int y2) {
        this(new Pos2i(x1, y1), new Pos2i(x2, y2));
    }

    public Line(Pos2i pos1, Pos2i pos2) {
        point1 = pos1;
        point2 = pos2;
    }

    public void draw(PoseStack poseStack) {
        int color = (int)(255 << 24 | 255 << 16 | 255 << 8 | 255);
        // fill(pPoseStack, 10, 10, 14, 40, 0xFFFF0000);
    }
}
