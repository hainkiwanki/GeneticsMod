package com.hainkiwanki.geneticsmod.event;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.cap.mobdata.MobDataImpl;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = GeneticsMod.MOD_ID)
public class MobDataEventsHandler {
    private static final List<LivingEntity> entitiesToUpdate = new ArrayList<>();

    @SubscribeEvent
    public static void onEntityJoinWorldEvent(EntityJoinWorldEvent e) {
        if(e.getEntity() == null || !(e.getEntity() instanceof Mob) || e.getWorld().isClientSide()) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity) e.getEntity();
        livingEntity.refreshDimensions();
        entitiesToUpdate.add(livingEntity);
        livingEntity.getCapability(MobDataImpl.MOB_DATA_CAPABILITY).ifPresent(data -> {
            data.sync(livingEntity);
        });
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

    // Any mobs that are added in the EntityJoinWorldEvent are here updated again
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!entitiesToUpdate.isEmpty()) {
            List<LivingEntity> entitiesToUpdateCopy = new ArrayList<>(entitiesToUpdate);
            for (LivingEntity entity : entitiesToUpdateCopy) {
                entity.getCapability(MobDataImpl.MOB_DATA_CAPABILITY).ifPresent(data -> data.forceSync(entity));
            }
            entitiesToUpdate.clear();
        }
    }
}
