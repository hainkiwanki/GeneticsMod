package com.hainkiwanki.geneticsmod.mobdata;

import com.hainkiwanki.geneticsmod.tags.ModTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MobData {
    public static final String SIZE = "size";
    public static final String HEALTH = "hp";
    public static final String ATTACK_DAMAGE = "attack_damage";
    public static final String ATTACK_SPEED = "attack_speed";
    public static final String MOVE_SPEED = "move_speed";
    public static final String MOB_TYPE = "mob_type";
    public static final String DROPS_MODIFIER = "drops_mod";
    public static final String EXP_MODIFIER = "exp_mod";
    public static final String FERTILITY = "fertility";
    public static final String BREATH_UNDER_WATER = "gills";
    public static final String IMMUNE_FIRE = "fire_immune";
    public static final String CAN_LAY_EGG = "oviparous";
    public static final String CAN_MILK = "lactation";
    public static final String MATURING_TIME = "maturing_time";
    public static final String IS_HOSTILE = "hostility";

    private Map<String, Float> mobDataMap = new HashMap<String, Float>(){};
    private String mobDataType;

    public void initialize(LivingEntity pEntity) {
        if(!mobDataMap.containsKey(SIZE))
            mobDataMap.put(SIZE, 1.0f);
        if(!mobDataMap.containsKey(HEALTH))
            mobDataMap.put(HEALTH, pEntity.getMaxHealth());

        mobDataType = pEntity.getClass().getSimpleName();

        if(!mobDataMap.containsKey(DROPS_MODIFIER))
            mobDataMap.put(DROPS_MODIFIER, 1.0f);
        if(!mobDataMap.containsKey(EXP_MODIFIER))
            mobDataMap.put(EXP_MODIFIER, 1.0f);
        if(!mobDataMap.containsKey(FERTILITY))
            mobDataMap.put(FERTILITY, pEntity instanceof AgeableMob ? 1.0f: 0.0f);

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
        if(!mobDataMap.containsKey(CAN_MILK)) {
            float canMilk = 0.0f;
            if (pEntity.getType().is(ModTags.EntityTypeTags.CAN_MILK))
                canMilk = 1.0f;
            mobDataMap.put(CAN_MILK, canMilk);
        }
        if(!mobDataMap.containsKey(MATURING_TIME)) {
            /*Animal animal = (Animal)pEntity;
            float value = 0.0f;
            if(animal != null) {
                value = 24000.0f;
            }*/
            mobDataMap.put(MATURING_TIME, 0.0f);
        }
        if(!mobDataMap.containsKey(IS_HOSTILE)) {
            if(NeutralMob.class.isAssignableFrom(pEntity.getClass())) {
                mobDataMap.put(IS_HOSTILE, 1.0f);
            } else if(Enemy.class.isAssignableFrom(pEntity.getClass())) {
                mobDataMap.put(IS_HOSTILE, 0.5f);
            } else {
                mobDataMap.put(IS_HOSTILE, 0.0f);
            }
        }
        if(!mobDataMap.containsKey(ATTACK_DAMAGE)) {
            mobDataMap.put(ATTACK_DAMAGE, (float)pEntity.getAttributeBaseValue(Attributes.ATTACK_DAMAGE));
        }
        if(!mobDataMap.containsKey(ATTACK_SPEED)) {
            mobDataMap.put(ATTACK_SPEED, (float)pEntity.getAttributeBaseValue(Attributes.ATTACK_SPEED));
        }
        if(!mobDataMap.containsKey(MOVE_SPEED)) {
            mobDataMap.put(MOVE_SPEED, (float)pEntity.getAttributeBaseValue(Attributes.MOVEMENT_SPEED));
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
        nbt.putString(MOB_TYPE, mobDataType);
    }

    public void loadNBTData(CompoundTag nbt) {
        Set<String> allKeys = nbt.getAllKeys();
        for (String key : allKeys) {
            setStat(key, nbt.getFloat(key));
        }
        mobDataType = nbt.getString(MOB_TYPE);
    }
}
