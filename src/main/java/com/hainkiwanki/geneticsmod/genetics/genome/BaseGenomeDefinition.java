package com.hainkiwanki.geneticsmod.genetics.genome;

import com.hainkiwanki.geneticsmod.genetics.EBiologicalTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;

public class BaseGenomeDefinition {

    private final ResourceLocation entity;
    private final Set<EBiologicalTag> tags;
    private final Set<ResourceLocation> genes;

    public BaseGenomeDefinition(
            ResourceLocation entity,
            Set<EBiologicalTag> tags,
            Set<ResourceLocation> genes
    ) {
        this.entity = entity;
        this.tags = tags;
        this.genes = genes;
    }

    public ResourceLocation getEntity() {
        return entity;
    }

    public Set<EBiologicalTag> getTags() {
        return tags;
    }

    public Set<ResourceLocation> getGenes() {
        return genes;
    }
}