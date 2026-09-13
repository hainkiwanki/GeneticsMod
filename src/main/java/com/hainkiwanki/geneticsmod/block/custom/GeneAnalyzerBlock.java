package com.hainkiwanki.geneticsmod.block.custom;

import com.hainkiwanki.geneticsmod.block.entity.GeneAnalyzerBlockEntity;
import com.hainkiwanki.geneticsmod.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class GeneAnalyzerBlock extends FacingEntityBlock {
    public GeneAnalyzerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GeneAnalyzerBlockEntity(pos, state);
    }

    @Override
    public void onRemove(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState newState,
            boolean isMoving
    ) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof GeneAnalyzerBlockEntity entity) {
                entity.drops();
                entity.resetFuelAndEnergy();
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public InteractionResult use(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit
    ) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(pos);

            if (entity instanceof GeneAnalyzerBlockEntity geneAnalyzer) {
                NetworkHooks.openGui((ServerPlayer) player, geneAnalyzer, pos);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(blockEntityType != ModBlockEntities.GENE_ANALYZER.get()) {
            return null;
        }
        if(level.isClientSide) {
            return null;
        }
        return createTickerHelper(blockEntityType, ModBlockEntities.GENE_ANALYZER.get(), GeneAnalyzerBlockEntity::tick);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {

        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if(!dropsOriginal.isEmpty()) {
            return dropsOriginal;
        }
        return Collections.singletonList(new ItemStack(this));
    }
}
