package com.hainkiwanki.geneticsmod.event;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.cap.mobdata.MobDataImpl;
import com.hainkiwanki.geneticsmod.cap.researchdata.PlayerResearchProvider;
import com.hainkiwanki.geneticsmod.network.ModMessages;
import com.hainkiwanki.geneticsmod.network.packet.SyncPlayerResearchDataPacket;
import com.hainkiwanki.geneticsmod.tags.ModTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = GeneticsMod.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            serverPlayer.getCapability(PlayerResearchProvider.PLAYER_RESEARCH_DATA).ifPresent(researchCap -> {
                ModMessages.sendToClients(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> serverPlayer), new SyncPlayerResearchDataPacket(researchCap));
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinWorldEvent event) {
        if(!event.getWorld().isClientSide()) {
            if(event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerResearchProvider.PLAYER_RESEARCH_DATA).ifPresent(researchCap -> {
                    ModMessages.sendToClients(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new SyncPlayerResearchDataPacket(researchCap));
                });
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().getCapability(PlayerResearchProvider.PLAYER_RESEARCH_DATA).ifPresent(oldData -> {
                event.getEntity().getCapability(PlayerResearchProvider.PLAYER_RESEARCH_DATA).ifPresent(newData -> {
                    newData.copy(oldData);
                });
            });
        }
    }

    @SubscribeEvent
    public static void onAttackCapabilitiesMobData(AttachCapabilitiesEvent<Entity> e) {
        if (e.getObject() instanceof Mob) {
            e.addCapability(MobDataImpl.Provider.RESOURCE_LOCATION, new MobDataImpl.Provider((LivingEntity) e.getObject()));
        }

        if (e.getObject() instanceof ServerPlayer) {
            e.addCapability(PlayerResearchProvider.RESOURCE_LOCATION, new PlayerResearchProvider((Player) e.getObject()));
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent e) {
        e.register(MobDataImpl.Provider.class);
        e.register(PlayerResearchProvider.class);
    }

    @SubscribeEvent
    public static void onEntityJoinWorldEvent(EntityJoinWorldEvent e) {
        if(e.getEntity() == null || !(e.getEntity() instanceof Mob) || e.getWorld().isClientSide()) {
            return;
        }

        if(e.getEntity() instanceof Mob) {
            LivingEntity livingEntity = (LivingEntity) e.getEntity();
            livingEntity.refreshDimensions();
            entitiesToUpdate.add(livingEntity);
            livingEntity.getCapability(MobDataImpl.MOB_DATA_CAPABILITY).ifPresent(data -> {
                data.sync(livingEntity);
            });
        }
    }

    @SubscribeEvent
    public static void onMobSizeChange(EntityEvent.Size e) {
        if(!(e.getEntity() instanceof Mob)) return;

        LivingEntity livingEntity = (LivingEntity)e.getEntity();
        livingEntity.getCapability(MobDataImpl.MOB_DATA_CAPABILITY).ifPresent(mobData -> {
            e.setNewSize(e.getNewSize().scale(mobData.getSize()));
            e.setNewEyeHeight(e.getNewEyeHeight() * mobData.getSize());
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

    private static final List<LivingEntity> entitiesToUpdate = new ArrayList<>();

    // Any mobs that are added in the EntityJoinWorldEvent are here updated again
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
//            System.out.println("Updating some mobs: " + entitiesToUpdate.size());
        if (!entitiesToUpdate.isEmpty()) {
            List<LivingEntity> entitiesToUpdateCopy = new ArrayList<>(entitiesToUpdate);
            for (LivingEntity entity : entitiesToUpdateCopy) {
                entity.getCapability(MobDataImpl.MOB_DATA_CAPABILITY).ifPresent(data -> data.forceSync(entity));
            }
            entitiesToUpdate.clear();
        }
    }

}
