package com.hainkiwanki.geneticsmod.recipe;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, GeneticsMod.MOD_ID);

    public static final RegistryObject<RecipeSerializer<GeneAnalyzerRecipe>> GENE_ANALYZER_SERIALIZER =
            SERIALIZERS.register("gene_analyzing", () -> GeneAnalyzerRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
