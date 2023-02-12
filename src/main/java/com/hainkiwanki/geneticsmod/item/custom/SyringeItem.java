package com.hainkiwanki.geneticsmod.item.custom;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.sound.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;


public class SyringeItem extends DnaSamplerItem {

    public static HashMap<String, String> entityDrops = new HashMap<>();

    static {
        entityDrops.put("minecraft:glow_squid", GeneticsMod.MOD_ID + ":squid_ink");
        entityDrops.put("minecraft:magma_cube", GeneticsMod.MOD_ID + ":magma_slime_blob");
        entityDrops.put("minecraft:slime", GeneticsMod.MOD_ID + ":slime_blob");
        entityDrops.put("minecraft:squid", GeneticsMod.MOD_ID + ":squid_ink");
        entityDrops.put("minecraft:vex", GeneticsMod.MOD_ID + ":vex_ectoplasm");
    }

    public SyringeItem(TagKey<EntityType<?>> tagList, Properties pProperties) {
        super(tagList, entityDrops, pProperties);
    }
}
