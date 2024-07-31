package com.hainkiwanki.geneticsmod.event;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.cap.MobDataImpl;
import com.hainkiwanki.geneticsmod.tags.ModTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;


@Mod.EventBusSubscriber(modid = GeneticsMod.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onAttackCapabilitiesMobData(AttachCapabilitiesEvent<Entity> e) {
        if(!(e.getObject() instanceof Mob)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity) e.getObject();
        if(livingEntity == null) return;
        e.addCapability(MobDataImpl.Provider.RESOURCE_LOCATION, new MobDataImpl.Provider(livingEntity));
    }

    @SubscribeEvent
    public static void onEntityJoinWorldEvent(EntityJoinWorldEvent e) {
        if(e.getEntity() == null || !(e.getEntity() instanceof Mob) || e.getWorld().isClientSide()) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity) e.getEntity();
        livingEntity.refreshDimensions();
        entitiesToUpdate.add(livingEntity);
        livingEntity.getCapability(GeneticsMod.MOB_DATA_CAPABILITY).ifPresent(data -> {
            data.sync(livingEntity);
        });
    }

    @SubscribeEvent
    public static void onMobSizeChange(EntityEvent.Size e) {
        if(!(e.getEntity() instanceof Mob)) return;

        LivingEntity livingEntity = (LivingEntity)e.getEntity();
        livingEntity.getCapability(GeneticsMod.MOB_DATA_CAPABILITY).ifPresent(mobData -> {
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
        if(!entitiesToUpdate.isEmpty()) {
//            System.out.println("Updating some mobs: " + entitiesToUpdate.size());
            if (event.phase == TickEvent.Phase.END) {
                for (LivingEntity entity : entitiesToUpdate) {
                    entity.getCapability(GeneticsMod.MOB_DATA_CAPABILITY).ifPresent(data -> {
                        data.forceSync(entity);
                    });
                }
                entitiesToUpdate.clear();
            }
        }
    }

}
