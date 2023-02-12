package com.hainkiwanki.geneticsmod.util;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.item.ModItems;
import com.hainkiwanki.geneticsmod.sound.ModSounds;
import com.hainkiwanki.geneticsmod.tags.ModTags;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Utils {
    public static HashMap<Item, TagKey<EntityType<?>>> entityTypesPerSamplerItem = new HashMap<>();
    static {
        entityTypesPerSamplerItem.put(ModItems.DNA_SAMPLER_CLIPBONE.get(), ModTags.EntityTypeTags.CAN_CLIPBONE);
        entityTypesPerSamplerItem.put(ModItems.DNA_SAMPLER_KNIFE.get(), ModTags.EntityTypeTags.CAN_KNIFE);
        entityTypesPerSamplerItem.put(ModItems.DNA_SAMPLER_SWAB.get(), ModTags.EntityTypeTags.CAN_SWAB);
        entityTypesPerSamplerItem.put(ModItems.DNA_SAMPLER_SYRINGE.get(), ModTags.EntityTypeTags.CAN_SYRINGE);
    }

    public static HashMap<Item, SoundEvent> samplerSoundEffects = new HashMap<>();
    static {
        samplerSoundEffects.put(ModItems.DNA_SAMPLER_CLIPBONE.get(), ModSounds.CLIPBONE_CRACK.get());
        samplerSoundEffects.put(ModItems.DNA_SAMPLER_KNIFE.get(), ModSounds.KNIFE_STAB.get());
        samplerSoundEffects.put(ModItems.DNA_SAMPLER_SWAB.get(), ModSounds.SWAB.get());
        samplerSoundEffects.put(ModItems.DNA_SAMPLER_SYRINGE.get(), ModSounds.SYRINGE_SUCK.get());
    }

    public static HashMap<Item, HashMap<String, String>> entityDrops = new HashMap<>();
    static {
        HashMap<String, String> clipboneMap = new HashMap<>();
        clipboneMap.put("minecraft:blaze", GeneticsMod.MOD_ID + ":blaze_clipping");
        clipboneMap.put("minecraft:cave_spider", GeneticsMod.MOD_ID + ":cave_spider_leg");
        clipboneMap.put("minecraft:cow", GeneticsMod.MOD_ID + ":cow_horn");
        clipboneMap.put("minecraft:creeper", GeneticsMod.MOD_ID + ":creeper_chunk");
        clipboneMap.put("minecraft:elder_guardian", GeneticsMod.MOD_ID + ":elder_guardian_rod");
        clipboneMap.put("minecraft:endermite", GeneticsMod.MOD_ID + ":endermite_tail");
        clipboneMap.put("minecraft:drowned", GeneticsMod.MOD_ID + ":rib_bone");
        clipboneMap.put("minecraft:goat", "minecraft:goat_horn");
        clipboneMap.put("minecraft:guardian", GeneticsMod.MOD_ID + ":guardian_rod");
        clipboneMap.put("minecraft:hoglin", GeneticsMod.MOD_ID + ":hoglin_ear");
        clipboneMap.put("minecraft:husk", GeneticsMod.MOD_ID + ":rib_bone");
        clipboneMap.put("minecraft:iron_golem", GeneticsMod.MOD_ID + ":iron_golem_chunk");
        clipboneMap.put("minecraft:mooshroom", GeneticsMod.MOD_ID + ":cow_horn");
        clipboneMap.put("minecraft:piglin", GeneticsMod.MOD_ID + ":piglin_tusk");
        clipboneMap.put("minecraft:piglin_brute", GeneticsMod.MOD_ID + ":piglin_tusk");
        clipboneMap.put("minecraft:pufferfish", GeneticsMod.MOD_ID + ":pufferfish_spikes");
        clipboneMap.put("minecraft:ravager", GeneticsMod.MOD_ID + ":ravager_horn");
        clipboneMap.put("minecraft:silverfish", GeneticsMod.MOD_ID + ":silverfish_tail");
        clipboneMap.put("minecraft:skeleton", GeneticsMod.MOD_ID + ":rib_bone");
        clipboneMap.put("minecraft:skeleton_horse", GeneticsMod.MOD_ID + ":rib_bone");
        clipboneMap.put("minecraft:snow_golem", GeneticsMod.MOD_ID + ":snow_golem_chunk");
        clipboneMap.put("minecraft:spider", GeneticsMod.MOD_ID + ":spider_leg");
        clipboneMap.put("minecraft:stray", GeneticsMod.MOD_ID + ":rib_bone");
        clipboneMap.put("minecraft:turtle", GeneticsMod.MOD_ID + ":turtle_shell");
        clipboneMap.put("minecraft:wither_skeleton", GeneticsMod.MOD_ID + ":wither_rib_bone");
        clipboneMap.put("minecraft:zoglin", GeneticsMod.MOD_ID + ":hoglin_ear");
        clipboneMap.put("minecraft:zombie", GeneticsMod.MOD_ID + ":rib_bone");
        clipboneMap.put("minecraft:zombie_horse", GeneticsMod.MOD_ID + ":rib_bone");
        clipboneMap.put("minecraft:zombie_villager", GeneticsMod.MOD_ID + ":rib_bone");
        clipboneMap.put("minecraft:zombified_piglin", GeneticsMod.MOD_ID + ":piglin_tusk");
        entityDrops.put(ModItems.DNA_SAMPLER_CLIPBONE.get(), clipboneMap);

        HashMap<String, String> knifeMap = new HashMap<>();
        knifeMap.put("minecraft:cave_spider", GeneticsMod.MOD_ID + ":cave_spider_skin");
        knifeMap.put("minecraft:cod", GeneticsMod.MOD_ID + ":scales_cod");
        knifeMap.put("minecraft:creeper", GeneticsMod.MOD_ID + ":creeper_skin");
        knifeMap.put("minecraft:dolphin", GeneticsMod.MOD_ID + ":dolphin_flesh");
        knifeMap.put("minecraft:drowned", GeneticsMod.MOD_ID + ":drowned_flesh");
        knifeMap.put("minecraft:enderman", GeneticsMod.MOD_ID + ":enderman_flesh");
        knifeMap.put("minecraft:ghast", GeneticsMod.MOD_ID + ":ghast_tentacle");
        knifeMap.put("minecraft:glow_squid", GeneticsMod.MOD_ID + ":glow_squid_tentacle");
        knifeMap.put("minecraft:hoglin", GeneticsMod.MOD_ID + ":hoglin_hair");
        knifeMap.put("minecraft:husk", GeneticsMod.MOD_ID + ":husk_flesh");
        knifeMap.put("minecraft:phantom", GeneticsMod.MOD_ID + ":phantom_flesh");
        knifeMap.put("minecraft:piglin", GeneticsMod.MOD_ID + ":hoglin_ear");
        knifeMap.put("minecraft:piglin_brute", GeneticsMod.MOD_ID + ":hoglin_ear");
        knifeMap.put("minecraft:pufferfish", GeneticsMod.MOD_ID + ":scales_pufferfish");
        knifeMap.put("minecraft:salmon", GeneticsMod.MOD_ID + ":scales_salmon");
        knifeMap.put("minecraft:shulker", GeneticsMod.MOD_ID + ":shulker_fragment");
        knifeMap.put("minecraft:spider", GeneticsMod.MOD_ID + ":spider_skin");
        knifeMap.put("minecraft:squid", GeneticsMod.MOD_ID + ":squid_tentacle");
        knifeMap.put("minecraft:strider", GeneticsMod.MOD_ID + ":strider_hair");
        knifeMap.put("minecraft:tropical_fish", GeneticsMod.MOD_ID + ":scales_tropical_fish");
        knifeMap.put("minecraft:zoglin", GeneticsMod.MOD_ID + ":zombie_flesh");
        knifeMap.put("minecraft:zombie", GeneticsMod.MOD_ID + ":zombie_flesh");
        knifeMap.put("minecraft:zombie_horse", GeneticsMod.MOD_ID + ":zombie_horse_meat");
        knifeMap.put("minecraft:zombie_villager", GeneticsMod.MOD_ID + ":zombie_flesh");
        knifeMap.put("minecraft:zombified_piglin", GeneticsMod.MOD_ID + ":zombie_flesh");
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
