package com.hainkiwanki.geneticsmod.event;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.network.mobdata.MobData;
import com.hainkiwanki.geneticsmod.network.mobdata.MobDataProvider;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GeneticsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RenderEvents {
    @SubscribeEvent
    public static void onPreLivingRender(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> e) {
        if(e.getEntity() == null && !(e.getEntity() instanceof Mob)) return;
        try {
            LivingEntity livingEntity = e.getEntity();
            LazyOptional<MobData> capability = livingEntity.getCapability(MobDataProvider.MOB_DATA);
            capability.ifPresent(mobData -> {
                if(mobData.hasStat(MobData.SIZE)) {
                    float s = mobData.getStat(MobData.SIZE);
                    e.getPoseStack().pushPose();
                    e.getPoseStack().scale(s, s, s);
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void onPostLivingRender(RenderLivingEvent.Post<LivingEntity, EntityModel<LivingEntity>> e) {
        if(e.getEntity() == null && !(e.getEntity() instanceof Mob)) return;
        try {
            LivingEntity livingEntity = e.getEntity();
            if (livingEntity.getCapability(MobDataProvider.MOB_DATA).isPresent()) {
                livingEntity.getCapability(MobDataProvider.MOB_DATA).ifPresent(modData -> {
                    if (modData.hasStat(MobData.SIZE)) {
                        e.getPoseStack().popPose();
                    }
                });
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
