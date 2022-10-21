package com.hainkiwanki.geneticsmod.mobdata;

import net.minecraft.nbt.CompoundTag;

public class MobData {
    private int stat = 2;

    public int getStat() {
        return stat;
    }

    public void setStat(int i) {
        stat = i;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("stat", stat);
    }

    public void loadNBTData(CompoundTag nbt) {
        stat = nbt.getInt("stat");
    }
}
