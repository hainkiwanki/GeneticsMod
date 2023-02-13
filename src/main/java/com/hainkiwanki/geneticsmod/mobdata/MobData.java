package com.hainkiwanki.geneticsmod.mobdata;

import com.hainkiwanki.geneticsmod.tags.ModTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MobData {
    public static final String SIZE = "size";
    public static final String HEALTH = "hp";
    public static final String MOB_TYPE = "mob_type";
    public static final String DROPS_MODIFIER = "drops_mod";
    public static final String EXP_MODIFIER = "exp_mod";
    public static final String FERTILITY = "fertility";
    public static final String BREATH_UNDER_WATER = "gills";
    public static final String IMMUNE_FIRE = "fire_immune";
    public static final String CAN_LAY_EGG = "oviparous";
    public static final String CAN_MILK = "lactation";

    private Map<String, Float> mobDataMap = new HashMap<String, Float>(){};
    private String modDataType;

    public void initialize(LivingEntity pEntity) {
        if(!mobDataMap.containsKey(SIZE))
            mobDataMap.put(SIZE, 1.0f);
        if(!mobDataMap.containsKey(HEALTH))
            mobDataMap.put(HEALTH, pEntity.getHealth());

        modDataType = pEntity.getClass().getSimpleName();

        if(!mobDataMap.containsKey(DROPS_MODIFIER))
            mobDataMap.put(DROPS_MODIFIER, 1.0f);
        if(!mobDataMap.containsKey(EXP_MODIFIER))
            mobDataMap.put(EXP_MODIFIER, 1.0f);
        if(!mobDataMap.containsKey(FERTILITY))
            mobDataMap.put(FERTILITY, 1.0f);

        if(!mobDataMap.containsKey(BREATH_UNDER_WATER)) {
            float canBreath = pEntity.canBreatheUnderwater() ? 1.0f : 0.0f;
            mobDataMap.put(BREATH_UNDER_WATER, canBreath);
        }
        if(!mobDataMap.containsKey(IMMUNE_FIRE)) {
            float isImmuneToFire = pEntity.fireImmune() ? 1.0f : 0.0f;
            mobDataMap.put(IMMUNE_FIRE, isImmuneToFire);
        }
        if(!mobDataMap.containsKey(CAN_LAY_EGG)) {
            float canLayEggs = 0.0f;
            if(pEntity.getType().is(ModTags.EntityTypeTags.CAN_LAY_EGG))
                canLayEggs = 1.0f;
            mobDataMap.put(CAN_LAY_EGG, canLayEggs);
        }
        if(!mobDataMap.containsKey(CAN_MILK)){
            float canMilk = 0.0f;
            if(pEntity.getType().is(ModTags.EntityTypeTags.CAN_MILK))
                canMilk = 1.0f;
            mobDataMap.put(CAN_MILK, canMilk);

        }
    }

    public float getStat(String stat) {
        if(!mobDataMap.containsKey(stat)) {
            setStat(stat, 1.0f);
        }
        return mobDataMap.get(stat);
    }

    public boolean hasStat(String stat) {
        return mobDataMap.containsKey(stat);
    }

    public void setStat(String stat, float f) {
        mobDataMap.put(stat, f);
    }

    public void saveNBTData(CompoundTag nbt) {
        for (Map.Entry<String, Float> entry : mobDataMap.entrySet()) {
            nbt.putFloat(entry.getKey(), entry.getValue());
        }
        nbt.putString(MOB_TYPE, modDataType);
    }

    public void loadNBTData(CompoundTag nbt) {
        Set<String> allKeys = nbt.getAllKeys();
        for (String key : allKeys) {
            setStat(key, nbt.getFloat(key));
        }
        modDataType = nbt.getString(MOB_TYPE);
    }
}
