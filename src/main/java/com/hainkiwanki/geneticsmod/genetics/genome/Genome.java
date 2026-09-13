package com.hainkiwanki.geneticsmod.genetics.genome;

import com.hainkiwanki.geneticsmod.genetics.EBiologicalTag;
import com.hainkiwanki.geneticsmod.genetics.gene.GeneDefinition;
import com.hainkiwanki.geneticsmod.genetics.gene.GeneRegistry;
import net.minecraft.resources.ResourceLocation;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class Genome {

    private final ResourceLocation baseEntity;
    private int generation;

    private final Set<ResourceLocation> genes;

    public Genome(ResourceLocation baseEntity) {
        this.baseEntity = baseEntity;
        this.generation = 0;
        this.genes = new HashSet<>();
    }

    public Genome(BaseGenomeDefinition definition) {
        this.baseEntity = definition.getEntity();
        this.generation = 0;
        this.genes = new HashSet<>(definition.getGenes());
    }

    public ResourceLocation getBaseEntity() {
        return baseEntity;
    }

    public int getGeneration() {
        return generation;
    }

    public Set<ResourceLocation> getGenes() {
        return genes;
    }

    public boolean addGene(ResourceLocation gene) {
        if (!canAddGene(gene)) {
            return false;
        }

        return genes.add(gene);
    }

    public boolean removeGene(ResourceLocation gene) {
        if (!canRemoveGene(gene)) {
            return false;
        }

        return genes.remove(gene);
    }

    public Set<EBiologicalTag> getTags() {
        BaseGenomeDefinition definition = BaseGenomeRegistry.get(baseEntity);

        if (definition == null) {
            return Set.of();
        }

        Set<EBiologicalTag> tags = EnumSet.noneOf(EBiologicalTag.class);
        tags.addAll(definition.getTags());

        Set<EBiologicalTag> addedTags = EnumSet.noneOf(EBiologicalTag.class);
        Set<EBiologicalTag> removedTags = EnumSet.noneOf(EBiologicalTag.class);

        for (ResourceLocation geneId : genes) {
            GeneDefinition geneDefinition = GeneRegistry.get(geneId);

            if (geneDefinition == null) {
                continue;
            }

            addedTags.addAll(geneDefinition.getAddedTags());
            removedTags.addAll(geneDefinition.getRemovedTags());
        }

        tags.addAll(addedTags);
        tags.removeAll(removedTags);

        return tags;
    }

    public boolean canAddGene(ResourceLocation geneId) {
        GeneDefinition geneDefinition = GeneRegistry.get(geneId);

        if (geneDefinition == null) {
            return false;
        }

        return genes.containsAll(geneDefinition.getRequiredGenes());
    }

    public boolean canRemoveGene(ResourceLocation geneId) {
        if (!genes.contains(geneId)) {
            return false;
        }

        for (ResourceLocation existingGeneId : genes) {
            if (existingGeneId.equals(geneId)) {
                continue;
            }

            GeneDefinition existingGene = GeneRegistry.get(existingGeneId);

            if (existingGene == null) {
                continue;
            }

            if (existingGene.getRequiredGenes().contains(geneId)) {
                return false;
            }
        }

        return true;
    }
}