package com.hainkiwanki.geneticsmod.event;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.cap.researchdata.PlayerResearchData;
import com.hainkiwanki.geneticsmod.cap.researchdata.PlayerResearchProvider;
import com.hainkiwanki.geneticsmod.network.ModMessages;
import com.hainkiwanki.geneticsmod.network.packet.ModifyPlayerResearchDataPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
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
                if(Minecraft.getInstance().player != null) {
                    ModMessages.sendToServer(new ModifyPlayerResearchDataPacket(1));
                }
            }
        }
    }
}
