package com.hainkiwanki.geneticsmod.event;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.network.ModMessages;
import com.hainkiwanki.geneticsmod.network.packet.ModifyPlayerResearchDataPacket;
import net.minecraft.advancements.Advancement;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GeneticsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AdvancementEventsHandler {
    @SubscribeEvent
    public static void onAdvancementCompleted(AdvancementEvent e) {
        ServerPlayer player = (ServerPlayer) e.getEntity();
        Advancement advancement = e.getAdvancement();

        if (isSpecificAdvancement(advancement, "geneticsmod:geneticsmod")) {
            player.sendMessage(new TextComponent("For completing this advancement, you are rewarded some RP: 50"), player.getUUID());
            ModMessages.sendToServer(new ModifyPlayerResearchDataPacket(50));
        }
    }

    private static boolean isSpecificAdvancement(Advancement advancement, String advancementId) {
        ResourceLocation id = advancement.getId();
        return id.toString().equals(advancementId);
    }
}
