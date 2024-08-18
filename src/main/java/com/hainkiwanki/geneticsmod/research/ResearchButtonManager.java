package com.hainkiwanki.geneticsmod.research;

import com.hainkiwanki.geneticsmod.gui.renderer.components.ResearchNodeButton;

import java.util.ArrayList;
import java.util.List;

public class ResearchButtonManager {
    private ResearchNodeButton selectedButton = null;
    public List<ResearchNodeButton> buttons = new ArrayList<>();

    public void registerButton(ResearchNodeButton button) {
        buttons.add(button);
    }

    public void selectButton(ResearchNodeButton button) {
        if (selectedButton != null) {
            selectedButton.setSelected(false);
        }
        if(selectedButton == button) {
            selectedButton = null;
            return;
        }

        selectedButton = button;
        if(selectedButton != null) {
            selectedButton.setSelected(true);
        }
    }
}
