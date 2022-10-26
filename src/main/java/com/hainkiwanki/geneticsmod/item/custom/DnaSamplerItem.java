package com.hainkiwanki.geneticsmod.item.custom;

import com.hainkiwanki.geneticsmod.util.Utils;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraftforge.registries.ForgeRegistries;

public class DnaSamplerItem extends Item {


    public DnaSamplerItem(Properties pProperties) {
        super(pProperties);

    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if(!pPlayer.level.isClientSide() && pUsedHand == InteractionHand.MAIN_HAND) {
            var entityTypes = Utils.entityTypesPerItem.get(pStack.getItem());
            boolean checkEntity = pInteractionTarget.getType().is(entityTypes);
            pPlayer.sendMessage(new TextComponent("Mob is " + ((checkEntity) ? "correct" : "incorrect" + " entity")),
                    pPlayer.getUUID());

            // TODO: Drop item
            /*java.util.List<ItemStack> drops = target.onSheared(playerIn, stack, entity.level, pos,
                    net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(net.minecraft.world.item.enchantment.Enchantments.BLOCK_FORTUNE, stack));
            java.util.Random rand = new java.util.Random();
            drops.forEach(d -> {
                net.minecraft.world.entity.item.ItemEntity ent = entity.spawnAtLocation(d, 1.0F);
                ent.setDeltaMovement(ent.getDeltaMovement().add((double)((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double)(rand.nextFloat() * 0.05F), (double)((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
            });
            stack.hurtAndBreak(1, playerIn, e -> e.broadcastBreakEvent(hand));*/
        }
        return InteractionResult.CONSUME;
    }
}
