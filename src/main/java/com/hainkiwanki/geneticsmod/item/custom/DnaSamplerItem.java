package com.hainkiwanki.geneticsmod.item.custom;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.mobdata.MobDataProvider;
import com.hainkiwanki.geneticsmod.util.Utils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Random;

public class DnaSamplerItem extends Item {
    protected SoundEvent SOUNDEVENT;
    protected TagKey<EntityType<?>> ENTITY_CAN_USE_CLIPBONE;
    protected HashMap<String, String> DROP_BY_ENTITY;

    public DnaSamplerItem(TagKey<EntityType<?>> tagList, HashMap<String, String> entityDropList, Properties pProperties) {
        super(pProperties);
        this.ENTITY_CAN_USE_CLIPBONE = tagList;
        this.DROP_BY_ENTITY = entityDropList;
    }

    public void SetSoundEvent(SoundEvent pSoundEvent) {
        this.SOUNDEVENT = pSoundEvent;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if(!pPlayer.level.isClientSide() && pUsedHand == InteractionHand.MAIN_HAND) {
            boolean usedCorrectSampler = pInteractionTarget.getType().is(ENTITY_CAN_USE_CLIPBONE);

            if(usedCorrectSampler) {
                var entityType = pInteractionTarget.getType();
                var mobPath = ForgeRegistries.ENTITIES.getKey(entityType).toString();
                if(DROP_BY_ENTITY.containsKey(mobPath)) {
                    ItemStack item = CreateItemStack(DROP_BY_ENTITY.get(mobPath));
                    AddNbtToItem(pInteractionTarget, item);
                    SpawnSampledItem(pInteractionTarget, item);

                    // Play Sound
                    pPlayer.getLevel().playSound(null, pPlayer.blockPosition(), SOUNDEVENT, SoundSource.BLOCKS, 1f, 1f);

                    return net.minecraft.world.InteractionResult.SUCCESS;
                }
            }
            else {
                pPlayer.sendMessage(new TextComponent("This dna sampler cannot be used on " + pInteractionTarget.getClass().getSimpleName()), pPlayer.getUUID());
            }
        }
        return InteractionResult.CONSUME;
    }

    public ItemStack CreateItemStack(String resLocation) {
        ResourceLocation resourceLocation = new ResourceLocation(resLocation);
        ItemStack itemStack = new ItemStack(ForgeRegistries.ITEMS.getValue(resourceLocation));
        return itemStack;
    }

    public void AddNbtToItem(LivingEntity entity, ItemStack item) {
        entity.getCapability(MobDataProvider.MOB_DATA).ifPresent(data -> {
            CompoundTag tag = new CompoundTag();
            data.saveNBTData(tag);
            item.setTag(tag);
        });
    }


    public void SpawnSampledItem(LivingEntity entity, ItemStack item) {
        ItemEntity ent = entity.spawnAtLocation(item, 1.0f);
        Random rand = new java.util.Random();
        ent.setDeltaMovement(ent.getDeltaMovement().add(
                (double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F),
                (double) (rand.nextFloat() * 0.05F),
                (double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F)));

    }
}
