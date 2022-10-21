package com.hainkiwanki.geneticsmod.item.custom;

import com.hainkiwanki.geneticsmod.mobdata.MobDataProvider;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DebugToolItem extends Item {

    public DebugToolItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if(!pPlayer.level.isClientSide() && pUsedHand == InteractionHand.MAIN_HAND) {
            String msg = "";
            var wrapper = new Object(){ String mData = ""; };

            if(Screen.hasShiftDown()) {
                pInteractionTarget.getCapability(MobDataProvider.MOB_DATA).ifPresent(data -> {
                    data.setStat(data.getStat() + 1);
                    wrapper.mData = data.getStat() + "";
                });
            }
            else {
                pInteractionTarget.getCapability(MobDataProvider.MOB_DATA).ifPresent(data -> {
                    wrapper.mData = data.getStat() + "";
                });
            }

            if(pInteractionTarget instanceof Animal) {
                msg = "Right clicked an animal";
            } else {
                msg = "Right clicked a monster";
            }
            msg += ", class: " + pInteractionTarget.getClass().getSimpleName();
            msg += ", DATA: " + wrapper.mData;
            pPlayer.sendMessage(new TextComponent(msg), pPlayer.getUUID());
        }


        return InteractionResult.SUCCESS;
    }
}
