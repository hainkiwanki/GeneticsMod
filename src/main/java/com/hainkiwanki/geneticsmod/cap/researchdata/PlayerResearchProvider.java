package com.hainkiwanki.geneticsmod.cap.researchdata;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerResearchProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerResearchData> PLAYER_RESEARCH_DATA = CapabilityManager.get(new CapabilityToken<>() { });
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(GeneticsMod.MOD_ID, "researchdata");

    public PlayerResearchData researchData = null;
    private final LazyOptional<PlayerResearchData> optional;

    public PlayerResearchProvider(LivingEntity entity) {
        if(this.researchData == null) {
            this.researchData = new PlayerResearchData();
            this.researchData.setPlayerId(entity.getId());
        }
        optional = LazyOptional.of(() -> this.researchData);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
        if (capability == PLAYER_RESEARCH_DATA) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        PlayerResearchData data = this.researchData;
        return data.serialize();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        PlayerResearchData data = this.researchData;
        data.deserialize(nbt);
    }

    public void sync(LivingEntity entity) {

    }
}