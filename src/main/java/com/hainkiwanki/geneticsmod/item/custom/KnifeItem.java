package com.hainkiwanki.geneticsmod.item.custom;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.sound.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;


public class KnifeItem extends DnaSamplerItem {

    public static HashMap<String, String> entityDrops = new HashMap<>();

    static {
        entityDrops.put("minecraft:cave_spider", GeneticsMod.MOD_ID + ":cave_spider_skin");
        entityDrops.put("minecraft:cod", GeneticsMod.MOD_ID + ":scales_cod");
        entityDrops.put("minecraft:creeper", GeneticsMod.MOD_ID + ":creeper_skin");
        entityDrops.put("minecraft:dolphin", GeneticsMod.MOD_ID + ":dolphin_flesh");
        entityDrops.put("minecraft:drowned", GeneticsMod.MOD_ID + ":drowned_flesh");
        entityDrops.put("minecraft:enderman", GeneticsMod.MOD_ID + ":enderman_flesh");
        entityDrops.put("minecraft:ghast", GeneticsMod.MOD_ID + ":ghast_tentacle");
        entityDrops.put("minecraft:glow_squid", GeneticsMod.MOD_ID + ":glow_squid_tentacle");
        entityDrops.put("minecraft:hoglin", GeneticsMod.MOD_ID + ":hoglin_hair");
        entityDrops.put("minecraft:husk", GeneticsMod.MOD_ID + ":husk_flesh");
        entityDrops.put("minecraft:phantom", GeneticsMod.MOD_ID + ":phantom_flesh");
        entityDrops.put("minecraft:piglin", GeneticsMod.MOD_ID + ":hoglin_ear");
        entityDrops.put("minecraft:piglin_brute", GeneticsMod.MOD_ID + ":hoglin_ear");
        entityDrops.put("minecraft:pufferfish", GeneticsMod.MOD_ID + ":scales_pufferfish");
        entityDrops.put("minecraft:salmon", GeneticsMod.MOD_ID + ":scales_salmon");
        entityDrops.put("minecraft:shulker", GeneticsMod.MOD_ID + ":shulker_fragment");
        entityDrops.put("minecraft:spider", GeneticsMod.MOD_ID + ":spider_skin");
        entityDrops.put("minecraft:squid", GeneticsMod.MOD_ID + ":squid_tentacle");
        entityDrops.put("minecraft:strider", GeneticsMod.MOD_ID + ":strider_hair");
        entityDrops.put("minecraft:tropical_fish", GeneticsMod.MOD_ID + ":scales_tropical_fish");
        entityDrops.put("minecraft:zoglin", GeneticsMod.MOD_ID + ":zombie_flesh");
        entityDrops.put("minecraft:zombie", GeneticsMod.MOD_ID + ":zombie_flesh");
        entityDrops.put("minecraft:zombie_horse", GeneticsMod.MOD_ID + ":zombie_horse_meat");
        entityDrops.put("minecraft:zombie_villager", GeneticsMod.MOD_ID + ":zombie_flesh");
        entityDrops.put("minecraft:zombified_piglin", GeneticsMod.MOD_ID + ":zombie_flesh");
    }

    public KnifeItem(TagKey<EntityType<?>> tagList, Properties pProperties) {
        super(tagList, entityDrops, pProperties);
    }
}
