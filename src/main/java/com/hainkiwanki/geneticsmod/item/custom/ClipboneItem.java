package com.hainkiwanki.geneticsmod.item.custom;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;


public class ClipboneItem extends GeneSamplerItem {

    public static HashMap<String, String> entityDrops = new HashMap<>();

    static {
        entityDrops.put("minecraft:blaze", GeneticsMod.MOD_ID + ":blaze_clipping");
        entityDrops.put("minecraft:cave_spider", GeneticsMod.MOD_ID + ":cave_spider_leg");
        entityDrops.put("minecraft:cow", GeneticsMod.MOD_ID + ":cow_horn");
        entityDrops.put("minecraft:creeper", GeneticsMod.MOD_ID + ":creeper_chunk");
        entityDrops.put("minecraft:elder_guardian", GeneticsMod.MOD_ID + ":elder_guardian_rod");
        entityDrops.put("minecraft:endermite", GeneticsMod.MOD_ID + ":endermite_tail");
        entityDrops.put("minecraft:drowned", GeneticsMod.MOD_ID + ":rib_bone");
        entityDrops.put("minecraft:goat", "minecraft:goat_horn");
        entityDrops.put("minecraft:guardian", GeneticsMod.MOD_ID + ":guardian_rod");
        entityDrops.put("minecraft:hoglin", GeneticsMod.MOD_ID + ":hoglin_ear");
        entityDrops.put("minecraft:husk", GeneticsMod.MOD_ID + ":rib_bone");
        entityDrops.put("minecraft:iron_golem", GeneticsMod.MOD_ID + ":iron_golem_chunk");
        entityDrops.put("minecraft:mooshroom", GeneticsMod.MOD_ID + ":cow_horn");
        entityDrops.put("minecraft:piglin", GeneticsMod.MOD_ID + ":piglin_tusk");
        entityDrops.put("minecraft:piglin_brute", GeneticsMod.MOD_ID + ":piglin_tusk");
        entityDrops.put("minecraft:pufferfish", GeneticsMod.MOD_ID + ":pufferfish_spikes");
        entityDrops.put("minecraft:ravager", GeneticsMod.MOD_ID + ":ravager_horn");
        entityDrops.put("minecraft:silverfish", GeneticsMod.MOD_ID + ":silverfish_tail");
        entityDrops.put("minecraft:skeleton", GeneticsMod.MOD_ID + ":rib_bone");
        entityDrops.put("minecraft:skeleton_horse", GeneticsMod.MOD_ID + ":rib_bone");
        entityDrops.put("minecraft:snow_golem", GeneticsMod.MOD_ID + ":snow_golem_chunk");
        entityDrops.put("minecraft:spider", GeneticsMod.MOD_ID + ":spider_leg");
        entityDrops.put("minecraft:stray", GeneticsMod.MOD_ID + ":rib_bone");
        entityDrops.put("minecraft:turtle", GeneticsMod.MOD_ID + ":turtle_shell");
        entityDrops.put("minecraft:wither_skeleton", GeneticsMod.MOD_ID + ":wither_rib_bone");
        entityDrops.put("minecraft:zoglin", GeneticsMod.MOD_ID + ":hoglin_ear");
        entityDrops.put("minecraft:zombie", GeneticsMod.MOD_ID + ":rib_bone");
        entityDrops.put("minecraft:zombie_horse", GeneticsMod.MOD_ID + ":rib_bone");
        entityDrops.put("minecraft:zombie_villager", GeneticsMod.MOD_ID + ":rib_bone");
        entityDrops.put("minecraft:zombified_piglin", GeneticsMod.MOD_ID + ":piglin_tusk");
    }

    public ClipboneItem(TagKey<EntityType<?>> tagList, Properties pProperties) {
        super(tagList, entityDrops, pProperties);
        damageDealt = 4.0f;
    }
}
