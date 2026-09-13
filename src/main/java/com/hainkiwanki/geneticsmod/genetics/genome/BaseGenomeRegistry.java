package com.hainkiwanki.geneticsmod.genetics.genome;

import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class BaseGenomeRegistry {

    private static final Map<ResourceLocation, BaseGenomeDefinition> DEFINITIONS = new HashMap<>();

    private BaseGenomeRegistry() {}

    public static void clear() {
        DEFINITIONS.clear();
    }

    public static void register(BaseGenomeDefinition definition) {
        DEFINITIONS.put(definition.getEntity(), definition);
    }

    public static BaseGenomeDefinition get(ResourceLocation entity) {
        return DEFINITIONS.get(entity);
    }

    public static Collection<BaseGenomeDefinition> getAll() {
        return Collections.unmodifiableCollection(DEFINITIONS.values());
    }
}