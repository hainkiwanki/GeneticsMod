package com.hainkiwanki.geneticsmod.item.custom;

import com.hainkiwanki.geneticsmod.network.mobdata.EMobStat;
import com.hainkiwanki.geneticsmod.network.mobdata.MobData;
import com.hainkiwanki.geneticsmod.network.mobdata.MobDataProvider;
import com.hainkiwanki.geneticsmod.network.ModMessages;
import com.hainkiwanki.geneticsmod.network.packet.ChangeMobDataC2SPacket;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

public class DebugToolItem extends Item {

    public DebugToolItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if(!pPlayer.level.isClientSide() && pUsedHand == InteractionHand.MAIN_HAND) {
            if(Screen.hasShiftDown()) {
                pInteractionTarget.getCapability(MobDataProvider.MOB_DATA_CAPABILITY).ifPresent(mobDataProvider -> {
                    mobDataProvider.setStat(EMobStat.SIZE, mobDataProvider.getStat(EMobStat.SIZE) + 0.1f);
                    mobDataProvider.serializeNBT();
                    ModMessages.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> pInteractionTarget), new ChangeMobDataC2SPacket(mobDataProvider.serializeNBT(), pInteractionTarget.getId()));
                    pPlayer.sendMessage(new TextComponent("Mobsize increased: " + mobDataProvider.getStat(EMobStat.SIZE)), pPlayer.getUUID());
                });
            } else
            {
                pInteractionTarget.getCapability(MobDataProvider.MOB_DATA_CAPABILITY).ifPresent(mobDataProvider -> {
                    mobDataProvider.setStat(EMobStat.SIZE, mobDataProvider.getStat(EMobStat.SIZE) - 0.1f);
                    ModMessages.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> pInteractionTarget), new ChangeMobDataC2SPacket( mobDataProvider.serializeNBT(), pInteractionTarget.getId()));
                    pPlayer.sendMessage(new TextComponent("Mobsize decreased: " + mobDataProvider.getStat(EMobStat.SIZE)), pPlayer.getUUID());
                });
            }
        }
        return InteractionResult.SUCCESS;
    }
}
