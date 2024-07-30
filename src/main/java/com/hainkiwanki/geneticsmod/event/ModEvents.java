package com.hainkiwanki.geneticsmod.event;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.network.ModMessages;
import com.hainkiwanki.geneticsmod.network.mobdata.EMobStat;
import com.hainkiwanki.geneticsmod.network.mobdata.MobData;
import com.hainkiwanki.geneticsmod.network.mobdata.MobDataProvider;
import com.hainkiwanki.geneticsmod.network.packet.ChangeMobDataC2SPacket;
import com.hainkiwanki.geneticsmod.tags.ModTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;


@Mod.EventBusSubscriber(modid = GeneticsMod.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onAttackCapabilitiesMobData(AttachCapabilitiesEvent<Entity> e) {
        if(!(e.getObject() instanceof Mob)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity) e.getObject();
        if(livingEntity == null) return;
        e.addCapability(MobDataProvider.RESOURCE_LOCATION, new MobDataProvider(livingEntity));
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent e) {
        e.register(MobData.class);
    }

    @SubscribeEvent
    public static void onEntityJoinWorldEvent(EntityJoinWorldEvent e) {
        if(e.getEntity() == null || !(e.getEntity() instanceof LivingEntity) || e.getWorld().isClientSide()) {
            return;
        }
        System.out.println("EntityJoinWorldEvent");
        LivingEntity livingEntity = (LivingEntity) e.getEntity();
        livingEntity.getCapability(MobDataProvider.MOB_DATA_CAPABILITY).ifPresent(data -> {
            data.sync(livingEntity);
        });
    }

    @SubscribeEvent
    public static void onEntityConstructing(EntityEvent.EntityConstructing e) {
        if (!(e.getEntity() instanceof LivingEntity)) {
            return;
        }
        System.out.println("EntityEvent.EntityConstructing");
        LivingEntity livingEntity = (LivingEntity) e.getEntity();
        livingEntity.getCapability(MobDataProvider.MOB_DATA_CAPABILITY).ifPresent(data -> {
            CompoundTag nbt = new CompoundTag();
            data.initialize(livingEntity);
            ModMessages.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> livingEntity), new ChangeMobDataC2SPacket(data.serializeNBT(), e.getEntity().getId()));
        });
    }


    @SubscribeEvent
    public static void onMobSizeChange(EntityEvent.Size e) {
        if(!(e.getEntity() instanceof LivingEntity)) return;

        LivingEntity livingEntity = (LivingEntity)e.getEntity();
        livingEntity.getCapability(MobDataProvider.MOB_DATA_CAPABILITY).ifPresent(mobData -> {
            if(mobData.hasStat(EMobStat.SIZE)) {
                e.setNewSize(e.getNewSize().scale(mobData.getStat(EMobStat.SIZE)));
                e.setNewEyeHeight(e.getNewEyeHeight() * mobData.getStat(EMobStat.SIZE));
            }
        });
    }

    @SubscribeEvent
    public static void onInteraction(InputEvent.ClickInputEvent e) {
        if(e.isUseItem()) {
            LocalPlayer player = Minecraft.getInstance().player;
            ItemStack inHandItem = player.getItemInHand(InteractionHand.MAIN_HAND);
            if(inHandItem.is(ModTags.ItemTags.SAMPLER_ITEM)) {
                if (player.getCooldowns().isOnCooldown(inHandItem.getItem())) {
                    e.setCanceled(true);
                    e.setSwingHand(false);
                }
            }
        }
    }
}
