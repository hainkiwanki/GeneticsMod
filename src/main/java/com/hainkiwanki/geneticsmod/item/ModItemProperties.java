package com.hainkiwanki.geneticsmod.item;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public class ModItemProperties {
    public static void addCustomItemProperties() {
        ItemProperties.register(ModItems.DNA_SAMPLER_SWAB.get(), new ResourceLocation(GeneticsMod.MOD_ID, "used"),
                ((pStack, pLevel, pEntity, pSeed) -> pStack.hasTag() ? 1f : 0f));
    }
}
