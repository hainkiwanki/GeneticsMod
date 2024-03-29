package com.hainkiwanki.geneticsmod.network.mobdata;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MobDataProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<MobData> MOB_DATA = CapabilityManager.get(new CapabilityToken<MobData>() {});

    private MobData mobData = null;
    private final LazyOptional<MobData> optional = LazyOptional.of(this::createMobData);

    private MobData createMobData() {
        if(mobData == null) {
            mobData = new MobData();
        }

        return mobData;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == MOB_DATA) {
            return optional.cast();
        }
        System.out.println("Found non empty");
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createMobData();
        mobData.saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createMobData().loadNBTData(nbt);
    }
}
