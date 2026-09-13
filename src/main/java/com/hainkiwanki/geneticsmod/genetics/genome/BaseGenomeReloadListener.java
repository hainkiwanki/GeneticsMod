package com.hainkiwanki.geneticsmod.genetics.genome;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hainkiwanki.geneticsmod.genetics.EBiologicalTag;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BaseGenomeReloadListener
        extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new Gson();
    private static final Logger LOGGER = LogUtils.getLogger();

    public BaseGenomeReloadListener() {
        super(GSON, "genomes");
    }

    @Override
    protected void apply(
            Map<ResourceLocation, JsonElement> objects,
            ResourceManager resourceManager,
            ProfilerFiller profiler
    ) {
        BaseGenomeRegistry.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : objects.entrySet()) {
            try {
                JsonObject json = entry.getValue().getAsJsonObject();
                ResourceLocation entity = new ResourceLocation(json.get("entity").getAsString());
                Set<EBiologicalTag> tags = EnumSet.noneOf(EBiologicalTag.class);

                for (JsonElement tagElement : json.getAsJsonArray("tags")) {
                    tags.add(EBiologicalTag.fromString(tagElement.getAsString()));
                }

                Set<ResourceLocation> genes = new HashSet<>();
                if (json.has("genes")) {
                    JsonArray geneArray = json.getAsJsonArray("genes");
                    for (JsonElement geneElement : geneArray) {
                        genes.add(new ResourceLocation(geneElement.getAsString()));
                    }
                }
                BaseGenomeDefinition definition = new BaseGenomeDefinition(entity, tags, genes);
                BaseGenomeRegistry.register(definition);
                LOGGER.info(
                        "Loaded base genome definition '{}' for entity '{}' with {} tags and {} genes",
                        entry.getKey(),
                        entity,
                        tags.size(),
                        genes.size()
                );

            } catch (Exception e) {
                LOGGER.error(
                        "Failed to load base genome definition '{}'",
                        entry.getKey(),
                        e
                );
            }
        }
        LOGGER.info(
                "Loaded {} base genome definition(s)",
                BaseGenomeRegistry.getAll().size()
        );
    }
}