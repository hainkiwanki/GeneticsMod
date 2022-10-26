package com.hainkiwanki.geneticsmod.block.entity;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, GeneticsMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<TerminalBlockEntity>> TERMINAL =
            BLOCK_ENTITIES.register("terminal", () ->
                    BlockEntityType.Builder.of(TerminalBlockEntity::new,
                            ModBlocks.TERMINAL.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
