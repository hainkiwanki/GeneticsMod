package com.hainkiwanki.geneticsmod.item.custom;

import com.hainkiwanki.geneticsmod.item.ModItemProperties;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DnaSamplerItem extends Item {


    public DnaSamplerItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if(!pPlayer.level.isClientSide() && pUsedHand == InteractionHand.MAIN_HAND) {
            /*if(entityTpes == null) {
                return InteractionResult.FAIL;
            } else {
                boolean checkEntity = pInteractionTarget.getType().is(entityTpes);
                pPlayer.sendMessage(new TextComponent("Mob is " + ((checkEntity) ? "correct" : "incorrect" + " entity")), pPlayer.getUUID());
            }*/
        }
        return InteractionResult.CONSUME;
    }
}
