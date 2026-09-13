package com.hainkiwanki.geneticsmod.genetics.genome;

import net.minecraft.resources.ResourceLocation;

public final class GenomeFactory {
    private GenomeFactory() {}

    public static Genome create(ResourceLocation entityId) {
        BaseGenomeDefinition definition = BaseGenomeRegistry.get(entityId);
        if(definition == null) {
            return null;
        }

        return new Genome(definition);
    }
}
