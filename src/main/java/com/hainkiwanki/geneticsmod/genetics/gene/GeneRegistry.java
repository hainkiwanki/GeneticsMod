package com.hainkiwanki.geneticsmod.genetics.gene;

import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class GeneRegistry {
    private static final Map<ResourceLocation, GeneDefinition> DEFINITIONS = new HashMap<>();

    private GeneRegistry() {}

    public static void clear() {
        DEFINITIONS.clear();
    }

    public static void register(GeneDefinition definition) {
        DEFINITIONS.put(definition.getId(), definition);
    }

    public static GeneDefinition get(ResourceLocation id) {
        return DEFINITIONS.get(id);
    }

    public static Collection<GeneDefinition> getAll() {
        return Collections.unmodifiableCollection(DEFINITIONS.values());
    }
}
