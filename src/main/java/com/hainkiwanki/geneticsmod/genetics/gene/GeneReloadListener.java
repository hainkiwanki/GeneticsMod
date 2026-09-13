package com.hainkiwanki.geneticsmod.genetics.gene;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hainkiwanki.geneticsmod.genetics.EBiologicalTag;
import com.hainkiwanki.geneticsmod.genetics.EGeneCategory;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;

import java.util.*;

public class GeneReloadListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();
    private static final Logger LOGGER = LogUtils.getLogger();

    public GeneReloadListener() {
        super(GSON, "genes");
    }

    @Override
    protected void apply(
            Map<ResourceLocation, JsonElement> jsonMap,
            ResourceManager resourceManager,
            ProfilerFiller profiler
    ) {
        GeneRegistry.clear();
        for(Map.Entry<ResourceLocation, JsonElement> entry : jsonMap.entrySet()) {
            try {
                Set<EBiologicalTag> addedTags = EnumSet.noneOf(EBiologicalTag.class);
                Set<EBiologicalTag> removedTags = EnumSet.noneOf(EBiologicalTag.class);
                Set<ResourceLocation> requiredGenes = new HashSet<>();

                Set<EBiologicalTag> compatibilityTags = EnumSet.noneOf(EBiologicalTag.class);
                Set<EBiologicalTag> incompatibilityTags = EnumSet.noneOf(EBiologicalTag.class);
                Set<EBiologicalTag> requiredTags = EnumSet.noneOf(EBiologicalTag.class);

                JsonObject json = entry.getValue().getAsJsonObject();

                if (json.has("add_tags")) {
                    for (JsonElement element : json.getAsJsonArray("add_tags")) {
                        addedTags.add(EBiologicalTag.fromString(element.getAsString()));
                    }
                }

                if (json.has("remove_tags")) {
                    for (JsonElement element : json.getAsJsonArray("remove_tags")) {
                        removedTags.add(EBiologicalTag.fromString(element.getAsString()));
                    }
                }

                if (json.has("required_genes")) {
                    for (JsonElement element : json.getAsJsonArray("required_genes")) {
                        requiredGenes.add(new ResourceLocation(element.getAsString()));
                    }
                }

                if (json.has("compatibility_tags")) {
                    for (JsonElement element : json.getAsJsonArray("compatibility_tags")) {
                        compatibilityTags.add(EBiologicalTag.fromString(element.getAsString()));
                    }
                }

                if (json.has("incompatibility_tags")) {
                    for (JsonElement element : json.getAsJsonArray("incompatibility_tags")) {
                        incompatibilityTags.add(EBiologicalTag.fromString(element.getAsString()));
                    }
                }

                if (json.has("required_tags")) {
                    for (JsonElement element : json.getAsJsonArray("required_tags")) {
                        requiredTags.add(EBiologicalTag.fromString(element.getAsString()));
                    }
                }

                EGeneCategory category = EGeneCategory.valueOf(
                        json.get("category").getAsString().toUpperCase(Locale.ROOT)
                );
                ResourceLocation geneId = entry.getKey(); // Gene ID = the file name
                GeneDefinition definition = new GeneDefinition(
                        geneId,
                        category,
                        addedTags,
                        removedTags,
                        requiredGenes,
                        compatibilityTags,
                        incompatibilityTags,
                        requiredTags
                );
                GeneRegistry.register(definition);
                LOGGER.info("Loaded gene definition {}", geneId);
            } catch (Exception e) {
                LOGGER.error("Failed to load gene definition {}", entry.getKey(), e);
            }
        }
        LOGGER.info("Loaded {} gene definition(s)", GeneRegistry.getAll().size());
    }
}
