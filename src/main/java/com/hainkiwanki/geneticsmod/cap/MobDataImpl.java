package com.hainkiwanki.geneticsmod.cap;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.network.ModMessages;
import com.hainkiwanki.geneticsmod.network.packet.ChangeMobDataC2SPacket;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MobDataImpl {
    private static class DefaultImpl implements IMobDataProvider {
        private final LivingEntity livingEntity;
        private float size = 1.0f;

        private DefaultImpl(LivingEntity livingEntity) {
            this.livingEntity = livingEntity;
        }

        @Override
        public void sync(LivingEntity livingEntity) {
            ModMessages.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> livingEntity), new ChangeMobDataC2SPacket(serializeNBT(), livingEntity.getId()));
        }

        @Override
        public float getSize() {
            return this.size;
        }

        @Override
        public void setSize(float f) {
            this.size = f;
            livingEntity.refreshDimensions();
            sync(this.livingEntity);
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putFloat("size", this.size);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.size = nbt.getFloat("size");
        }
    }

    public static class Provider implements ICapabilitySerializable<CompoundTag> {
        public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(GeneticsMod.MOD_ID, "mobdata");

        private final DefaultImpl impl;
        private final LazyOptional<IMobDataProvider> cap;

        public Provider(LivingEntity livingEntity) {
            impl = new DefaultImpl(livingEntity);
            cap = LazyOptional.of(() -> impl);
        }

        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
            if (capability == GeneticsMod.MOB_DATA_CAPABILITY) {
                return cap.cast();
            }
            return LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            return impl.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            impl.deserializeNBT(nbt);
        }
    }
}
