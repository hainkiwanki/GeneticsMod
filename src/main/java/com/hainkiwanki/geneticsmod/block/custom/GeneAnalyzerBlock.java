package com.hainkiwanki.geneticsmod.block.custom;

import com.hainkiwanki.geneticsmod.block.entity.GeneAnalyzerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GeneAnalyzerBlock extends FacingEntityBlock<GeneAnalyzerBlockEntity> {
    public GeneAnalyzerBlock(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new GeneAnalyzerBlockEntity(pPos, pState);
    }
}
