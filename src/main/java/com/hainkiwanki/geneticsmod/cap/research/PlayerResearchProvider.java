package com.hainkiwanki.geneticsmod.cap.research;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(GeneticsMod.MOD_ID, "playerResearchData");

    public PlayerResearchData researchData = null;
    private final LazyOptional<PlayerResearchData> optional = LazyOptional.of(this::createPlayerResearchData);

    private PlayerResearchData createPlayerResearchData() {
        if(this.researchData == null) {
            this.researchData = new PlayerResearchData();
        }
        return this.researchData;
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
        return this.researchData.serialize();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.researchData.deserialize(nbt);
    }
}
