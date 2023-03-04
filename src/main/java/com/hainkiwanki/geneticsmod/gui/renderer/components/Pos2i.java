package com.hainkiwanki.geneticsmod.gui.renderer.components;

public class Pos2i {
    private int xPos;
    private int yPos;

    public Pos2i(int x, int y) {
        xPos = x;
        yPos = y;
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    public void setX(int value) {
        xPos = value;
    }

    public void setY(int value) {
        yPos = value;
    }
}
