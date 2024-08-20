package com.hainkiwanki.geneticsmod.research;

import com.hainkiwanki.geneticsmod.gui.renderer.components.ResearchNodeButton;

import java.util.ArrayList;
import java.util.List;

public class ResearchButtonManager {
    private ResearchNodeButton selectedButton = null;
    public List<ResearchNodeButton> buttons = new ArrayList<>();

    public void registerButton(ResearchNodeButton button) {
        this.buttons.add(button);
    }

    public ResearchNode getSelectedNode() {
        if(this.selectedButton == null) {
            return null;
        }
        return this.selectedButton.researchNode;
    }

    public void selectButton(ResearchNodeButton button) {
        if (this.selectedButton != null) {
            this.selectedButton.setSelected(false);
        }
        this.selectedButton = button;
        if(this.selectedButton != null) {
            this.selectedButton.setSelected(true);
        }
    }
}
