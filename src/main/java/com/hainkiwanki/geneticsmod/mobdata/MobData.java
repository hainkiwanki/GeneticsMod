package com.hainkiwanki.geneticsmod.mobdata;

import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MobData {
    public static final String SIZE = "attr_size";

    private Map<String, Float> mobDataMap = new HashMap<String, Float>(){};

    public void initialize() {
        if(!mobDataMap.containsKey(SIZE))
            mobDataMap.put(SIZE, 1.0f);
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
    }

    public void loadNBTData(CompoundTag nbt) {
        Set<String> allKeys = nbt.getAllKeys();
        for (String key : allKeys) {
            setStat(key, nbt.getFloat(key));
        }
    }
}
