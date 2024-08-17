package com.hainkiwanki.geneticsmod.research;

import java.util.List;

public class ResearchNode {
    public String id;
    public String name;
    public String description;
    public List<String> requirements;

    public ResearchNode(String id, String name, String description, List<String> requirements) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.requirements = requirements;
    }
}
