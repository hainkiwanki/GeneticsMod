package com.hainkiwanki.geneticsmod.network.mobdata;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.network.ModMessages;
import com.hainkiwanki.geneticsmod.network.packet.ChangeMobDataC2SPacket;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class MobDataProvider implements ICapabilitySerializable<CompoundTag> {
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(GeneticsMod.MOD_ID, "mobdata");
    public static Capability<MobData> MOB_DATA_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    private MobData mobData = null;
    private final LazyOptional<MobData> optional;

    public MobDataProvider(LivingEntity livingEntity) {
        mobData = new MobData();
        optional = LazyOptional.of(() -> mobData);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == MOB_DATA_CAPABILITY) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return mobData.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        mobData.deserializeNBT(nbt);
    }
}
