package com.hainkiwanki.geneticsmod.util;

import com.hainkiwanki.geneticsmod.item.ModItems;
import com.hainkiwanki.geneticsmod.mobdata.MobData;
import com.hainkiwanki.geneticsmod.mobdata.MobDataProvider;
import com.hainkiwanki.geneticsmod.tags.ModTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Utils {
    public static HashMap<Item, TagKey<EntityType<?>>> entityTypesPerItem = new HashMap<>();
    static {
        entityTypesPerItem.put(ModItems.DNA_SAMPLER_CLIPBONE.get(), ModTags.EntityTypeTags.CAN_CLIPBONE);
        entityTypesPerItem.put(ModItems.DNA_SAMPLER_KNIFE.get(), ModTags.EntityTypeTags.CAN_KNIFE);
        entityTypesPerItem.put(ModItems.DNA_SAMPLER_SWAB.get(), ModTags.EntityTypeTags.CAN_SWAB);
        entityTypesPerItem.put(ModItems.DNA_SAMPLER_SYRINGE.get(), ModTags.EntityTypeTags.CAN_SYRINGE);
    }

    public static String getItemPath(ItemStack item) {
        return getItemPath(item.getItem());
    }

    public static String getItemPath(Item item) {
        return ForgeRegistries.ITEMS.getKey(item).getPath();
    }

    public static String getItemNamespace(Item item) {
        return ForgeRegistries.ITEMS.getKey(item).getNamespace();
    }

    public static String getMobName(@NotNull LivingEntity entity) {
        return entity.getClass().getSimpleName();
    }
}
