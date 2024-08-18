package com.hainkiwanki.geneticsmod.event;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.cap.researchdata.PlayerResearchData;
import com.hainkiwanki.geneticsmod.cap.researchdata.PlayerResearchProvider;
import com.hainkiwanki.geneticsmod.network.ModMessages;
import com.hainkiwanki.geneticsmod.network.packet.ModifyPlayerResearchDataPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.client.event.InputEvent;
import org.lwjgl.glfw.GLFW;

public class Events {

    @Mod.EventBusSubscriber(modid = GeneticsMod.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.KeyInputEvent e) {
            if(e.getKey() == GLFW.GLFW_KEY_J && e.getAction() == GLFW.GLFW_RELEASE) {
                Player player = Minecraft.getInstance().player;
                if(player != null) {
                    player.getCapability(PlayerResearchProvider.PLAYER_RESEARCH_DATA).ifPresent(researchCap -> {
                        researchCap.increaseResearchPoints(1);
                        ModMessages.sendToServer(new ModifyPlayerResearchDataPacket(1));
                    });
                }
            }
        }
    }
}
