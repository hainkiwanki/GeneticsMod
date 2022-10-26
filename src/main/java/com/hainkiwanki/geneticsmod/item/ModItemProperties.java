package com.hainkiwanki.geneticsmod.item;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class ModItemProperties extends Item.Properties {
    TagKey<EntityType<?>> entityTpes;

    public ModItemProperties entityTypes(TagKey<EntityType<?>> types) {
        this.entityTpes = types;
        return this;
    }

}
