package com.hainkiwanki.geneticsmod.genetics.gene;

import com.hainkiwanki.geneticsmod.genetics.EBiologicalTag;
import com.hainkiwanki.geneticsmod.genetics.EGeneCategory;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;

public class GeneDefinition {

    private final ResourceLocation id;
    private final EGeneCategory category;
    private final Set<EBiologicalTag> addedTags;
    private final Set<EBiologicalTag> removedTags;
    private final Set<ResourceLocation> requiredGenes;

    private final Set<EBiologicalTag> compatibilityTags;
    private final Set<EBiologicalTag> incompatibilityTags;
    private final Set<EBiologicalTag> requiredTags;

    public GeneDefinition(
            ResourceLocation id,
            EGeneCategory category,
            Set<EBiologicalTag> addedTags,
            Set<EBiologicalTag> removedTags,
            Set<ResourceLocation> requiredGenes,
            Set<EBiologicalTag> compatibilityTags,
            Set<EBiologicalTag> incompatibilityTags,
            Set<EBiologicalTag> requiredTags
    ) {
        this.id = id;
        this.category = category;
        this.addedTags = addedTags;
        this.removedTags = removedTags;
        this.requiredGenes = requiredGenes;
        this.compatibilityTags = compatibilityTags;
        this.incompatibilityTags = incompatibilityTags;
        this.requiredTags = requiredTags;
    }

    public ResourceLocation getId() {
        return id;
    }

    public EGeneCategory getCategory() {
        return category;
    }

    public Set<EBiologicalTag> getAddedTags() {
        return addedTags;
    }

    public Set<EBiologicalTag> getRemovedTags() {
        return removedTags;
    }

    public Set<ResourceLocation> getRequiredGenes() {
        return requiredGenes;
    }

    public Set<EBiologicalTag> getCompatibilityTags() {
        return compatibilityTags;
    }

    public Set<EBiologicalTag> getIncompatibilityTags() {
        return incompatibilityTags;
    }

    public Set<EBiologicalTag> getRequiredTags() {
        return requiredTags;
    }
}
