package com.hainkiwanki.geneticsmod.gui.renderer.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.energy.IEnergyStorage;

/*
 *  BluSunrize
 *  Copyright (c) 2021
 *
 *  This code is licensed under "Blu's License of Common Sense"
 *  Details can be found in the license file in the root folder of this project
 */
import java.util.List;

public class EnergyInfoArea extends InfoArea{
    private final IEnergyStorage energy;

    public static int DEFAULT_WIDTH = 12;
    public static int DEFAULT_HEIGHT = 39;

    public EnergyInfoArea(int xMin, int yMin)  {
        this(xMin, yMin, null, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public EnergyInfoArea(int xMin, int yMin, IEnergyStorage energy)  {
        this(xMin, yMin, energy, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public EnergyInfoArea(int xMin, int yMin, IEnergyStorage energy, int width, int height)  {
        super(new Rect2i(xMin, yMin, width, height));
        this.energy = energy;
    }

    public List<Component> getTooltips() {
        return List.of(new TextComponent(energy.getEnergyStored()+"/"+energy.getMaxEnergyStored()+" FE"));
    }

    @Override
    public void draw(PoseStack transform) {
        final int height = area.getHeight();
        int stored = (int)(height*(energy.getEnergyStored()/(float)energy.getMaxEnergyStored()));
        fillGradient(
                transform,
                area.getX(), area.getY()+(height-stored),
                area.getX() + area.getWidth(), area.getY() +area.getHeight(),
                0xffb51500, 0xff600b00
        );
    }
}
