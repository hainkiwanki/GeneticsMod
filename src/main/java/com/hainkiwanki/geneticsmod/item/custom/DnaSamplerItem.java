package com.hainkiwanki.geneticsmod.item.custom;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.util.Utils;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class DnaSamplerItem extends Item {


    public DnaSamplerItem(Properties pProperties) {
        super(pProperties);

    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if(!pPlayer.level.isClientSide() && pUsedHand == InteractionHand.MAIN_HAND) {
            var entityTypes = Utils.entityTypesPerItem.get(pStack.getItem());
            boolean checkEntity = pInteractionTarget.getType().is(entityTypes);
            pPlayer.sendMessage(new TextComponent("Mob is " + ((checkEntity) ? "correct" : "incorrect")),
                    pPlayer.getUUID());

            var entities = ForgeRegistries.ENTITIES;
            int size = entities.getEntries().size();
            var typeArr = entities.getValues().toArray(new EntityType<?>[size]);

            var locArr = entities.getKeys().toArray(new ResourceLocation[size]);

            for(int i = 0; i < typeArr.length; i++) {
                pPlayer.sendMessage(new TextComponent(
                        typeArr[i].getCategory().getName() +
                        " > " +
                        locArr[i].getPath()
                ), pPlayer.getUUID());
            }
            /*for (var location : test) {
                //pPlayer.sendMessage(new TextComponent(location.getRegistryType().toString() + " > " + location.getCategory().getName()), pPlayer.getUUID());
                // pPlayer.sendMessage(new TextComponent(location.getPath()), pPlayer.getUUID());
            }*/

            // TODO: Drop item
            /*ItemStack item = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(GeneticsMod.MOD_ID, Utils.getItemPath(pStack))));
            ItemEntity ent = pInteractionTarget.spawnAtLocation(item, 1.0f);
            Random rand = new java.util.Random();
            ent.setDeltaMovement(ent.getDeltaMovement().add(
                    (double)((rand.nextFloat() - rand.nextFloat()) * 0.1F),
                    (double)(rand.nextFloat() * 0.05F),
                    (double)((rand.nextFloat() - rand.nextFloat()) * 0.1F)));*/
        }
        return InteractionResult.CONSUME;
    }
}
