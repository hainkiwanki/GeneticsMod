package com.hainkiwanki.geneticsmod.item.custom;

import net.minecraft.nbt.CompoundTag;
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

            CompoundTag nbt = new CompoundTag();

            if(pInteractionTarget instanceof Animal) {
                msg = "Right clicked an animal";
            } else {
                msg = "Right clicked a monster";
            }
            msg += ", class: " + pInteractionTarget.getClass().getSimpleName();
            pPlayer.sendMessage(new TextComponent(msg), pPlayer.getUUID());
        }


        return InteractionResult.SUCCESS;
    }
}
