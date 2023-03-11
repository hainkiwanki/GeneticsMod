package com.hainkiwanki.geneticsmod.event;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.item.ModItems;
import com.hainkiwanki.geneticsmod.mobdata.MobData;
import com.hainkiwanki.geneticsmod.mobdata.MobDataProvider;
import com.hainkiwanki.geneticsmod.network.ModMessages;
import com.hainkiwanki.geneticsmod.network.packet.ChangeMobDataC2SPacket;
import com.hainkiwanki.geneticsmod.tags.ModTags;
import com.hainkiwanki.geneticsmod.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;


@Mod.EventBusSubscriber(modid = GeneticsMod.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onAttackCapabilitiesMobData(AttachCapabilitiesEvent<Entity> e) {
        if(!(e.getObject() instanceof Mob)) return;
            e.addCapability(new ResourceLocation(GeneticsMod.MOD_ID, "mobdata"), new MobDataProvider());
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent e) {
        e.register(MobData.class);
    }

    @SubscribeEvent
    public static void onMobSpawn(LivingSpawnEvent e) {
        if(e.getEntityLiving() == null || !(e.getEntityLiving() instanceof Mob)) return;
        /*AgeableMob mob = (AgeableMob)e.getEntityLiving();
        if(mob.isBaby()) {
            mob.
        }*/
        e.getEntityLiving().getCapability(MobDataProvider.MOB_DATA).ifPresent(data -> {
            CompoundTag nbt = new CompoundTag();
            data.initialize(e.getEntityLiving());
            data.saveNBTData(nbt);
            ModMessages.send(PacketDistributor.TRACKING_ENTITY.with(() -> e.getEntityLiving()), new ChangeMobDataC2SPacket(nbt, e.getEntity().getId()));
        });
    }

    @SubscribeEvent
    public static void onMobSizeChange(EntityEvent.Size e) {
        if(!(e.getEntity() instanceof LivingEntity)) return;

        LivingEntity livingEntity = (LivingEntity)e.getEntity();
        livingEntity.getCapability(MobDataProvider.MOB_DATA).ifPresent(mobData -> {
            if(mobData.hasStat(MobData.SIZE)) {
                e.setNewSize(e.getNewSize().scale(mobData.getStat(MobData.SIZE)));
                e.setNewEyeHeight(e.getNewEyeHeight() * mobData.getStat(MobData.SIZE));
            }
        });
    }

    @SubscribeEvent
    public static void onMobJoinWorld(EntityJoinWorldEvent e) {
        // Check type before adding goals
        if(e.getEntity().getType() == EntityType.COW){
            Mob cow = (Mob)e.getEntity();
            // cow.goalSelector.addGoal();
        }
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
