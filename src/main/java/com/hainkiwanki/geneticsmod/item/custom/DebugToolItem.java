package com.hainkiwanki.geneticsmod.item.custom;

import com.hainkiwanki.geneticsmod.cap.mobdata.MobDataImpl;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
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
            if(Screen.hasShiftDown()) {
                pInteractionTarget.getCapability(MobDataImpl.MOB_DATA_CAPABILITY).ifPresent(mobDataProvider -> {
                    mobDataProvider.setSize(mobDataProvider.getSize() + 0.1f);
                });
            }
            else if(Screen.hasControlDown()) {
                pInteractionTarget.getCapability(MobDataImpl.MOB_DATA_CAPABILITY).ifPresent(mobDataProvider -> {
                    mobDataProvider.setSize(mobDataProvider.getSize() - 0.1f);

                });
            }
            else
            {
                pInteractionTarget.getCapability(MobDataImpl.MOB_DATA_CAPABILITY).ifPresent(mobDataProvider -> {
                    System.out.println(mobDataProvider.getSize());
                });
            }

        }
        return InteractionResult.SUCCESS;
    }
}
