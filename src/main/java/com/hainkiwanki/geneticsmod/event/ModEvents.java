package com.hainkiwanki.geneticsmod.event;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.mobdata.MobData;
import com.hainkiwanki.geneticsmod.mobdata.MobDataProvider;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = GeneticsMod.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onPreLivingRender(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> e) {
        if(e.getEntity() == null && !(e.getEntity() instanceof Mob)) return;
        e.getEntity().getCapability(MobDataProvider.MOB_DATA).ifPresent(data -> {
            float s = data.getStat(MobData.SIZE);
            e.getPoseStack().pushPose();
            e.getPoseStack().scale(s, s, s);
        });
    }

    @SubscribeEvent
    public static void onPostLivingRender(RenderLivingEvent.Post<LivingEntity, EntityModel<LivingEntity>> e) {
        if(e.getEntity() == null && !(e.getEntity() instanceof Mob)) return;
        e.getPoseStack().popPose();
    }

    @SubscribeEvent
    public static void onSizeChange(EntityEvent.Size e) {
        if(e.getEntity() == null && !(e.getEntity() instanceof Mob)) return;
    }

    @SubscribeEvent
    public static void onAttackCapabilitiesMobData(AttachCapabilitiesEvent<Entity> e) {
        if(!(e.getObject() instanceof Mob)) return;
        if(!e.getObject().getCapability(MobDataProvider.MOB_DATA).isPresent()) {
            e.addCapability(new ResourceLocation(GeneticsMod.MOD_ID, "mobdata"), new MobDataProvider());
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent e) {
        e.register(MobData.class);
    }
}
