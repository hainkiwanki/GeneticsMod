package com.hainkiwanki.geneticsmod.event;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.network.mobdata.EMobStat;
import com.hainkiwanki.geneticsmod.network.mobdata.MobDataProvider;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GeneticsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RenderEvents {
    @SubscribeEvent
    public static void onPreLivingRender(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> e) {
        if(e.getEntity() == null && !(e.getEntity() instanceof Mob)) return;
        try {
            LivingEntity livingEntity = e.getEntity();
            livingEntity.getCapability(MobDataProvider.MOB_DATA_CAPABILITY).ifPresent(mobData -> {
                if(mobData.hasStat(EMobStat.SIZE)) {
                    float s = mobData.getStat(EMobStat.SIZE);
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
            livingEntity.getCapability(MobDataProvider.MOB_DATA_CAPABILITY).ifPresent(modData -> {
                if (modData.hasStat(EMobStat.SIZE)) {
                    e.getPoseStack().popPose();
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
