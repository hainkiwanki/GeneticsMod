package com.hainkiwanki.geneticsmod.item.custom;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.sound.ModSounds;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;


public class SwabItem extends DnaSamplerItem {

    public static HashMap<String, String> entityDrops = new HashMap<>();

    static {
        entityDrops.put("minecraft:axolotl", "");
        entityDrops.put("minecraft:bat", "");
        entityDrops.put("minecraft:bee", "");
        entityDrops.put("minecraft:cat", "");
        entityDrops.put("minecraft:chicken", "");
        entityDrops.put("minecraft:cow", "");
        entityDrops.put("minecraft:donkey", "");
        entityDrops.put("minecraft:evoker", "");
        entityDrops.put("minecraft:fox", "");
        entityDrops.put("minecraft:goat", "");
        entityDrops.put("minecraft:hoglin", "");
        entityDrops.put("minecraft:horse", "");
        entityDrops.put("minecraft:llama", "");
        entityDrops.put("minecraft:mule", "");
        entityDrops.put("minecraft:ocelot", "");
        entityDrops.put("minecraft:panda", "");
        entityDrops.put("minecraft:parrot", "");
        entityDrops.put("minecraft:pig", "");
        entityDrops.put("minecraft:piglin", "");
        entityDrops.put("minecraft:piglin_brute", "");
        entityDrops.put("minecraft:pillager", "");
        entityDrops.put("minecraft:polar_bear", "");
        entityDrops.put("minecraft:rabbit", "");
        entityDrops.put("minecraft:ravager", "");
        entityDrops.put("minecraft:sheep", "");
        entityDrops.put("minecraft:trader_llama", "");
        entityDrops.put("minecraft:turtle", "");
        entityDrops.put("minecraft:villager", "");
        entityDrops.put("minecraft:vindicator", "");
        entityDrops.put("minecraft:wandering_trader", "");
        entityDrops.put("minecraft:witch", "");
        entityDrops.put("minecraft:wolf", "");
    }

    public SwabItem(TagKey<EntityType<?>> tagList, Properties pProperties) {
        super(tagList, entityDrops, pProperties);
    }

    @Override
    public void OnUseCorrectTool(LivingEntity pInteractionTarget, Player pPlayer) {
        pPlayer.sendMessage(new TextComponent("Nothing happens"), pPlayer.getUUID());
    }
}
