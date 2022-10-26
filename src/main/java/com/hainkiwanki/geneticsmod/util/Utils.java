package com.hainkiwanki.geneticsmod.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;

public class Utils {
    public static HashMap<String, TagKey<EntityType<?>>> entityTypesPerItem = new HashMap<>();

    public static void registerSampler(ItemStack pItemStack) {
        String item = ForgeRegistries.ITEMS.getKey(pItemStack.getItem()).getPath();
        if(!entityTypesPerItem.containsKey(item)) {
        }
    }
}
