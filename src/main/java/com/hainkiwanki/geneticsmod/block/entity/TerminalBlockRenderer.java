package com.hainkiwanki.geneticsmod.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.HashMap;
import java.util.Map;

public class TerminalBlockRenderer implements BlockEntityRenderer<TerminalBlockEntity> {
    private final Font font;
    private final HashMap<Direction, Vector3f> facingDirOffset = new HashMap<>();

    public TerminalBlockRenderer(BlockEntityRendererProvider.Context ctx) {
        font = ctx.getFont();
        facingDirOffset.put(Direction.NORTH, Vector3f.ZERO);
        facingDirOffset.put(Direction.EAST, Vector3f.XN);
        facingDirOffset.put(Direction.SOUTH, new Vector3f(-1, 0, -1));
        facingDirOffset.put(Direction.WEST, Vector3f.ZN);
    }

    @Override
    public void render(TerminalBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {

        pPoseStack.pushPose();
        Direction facingDir = pBlockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getClockWise().getCounterClockWise();
        Quaternion rot = Vector3f.YN.rotationDegrees(pBlockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180.0f);
        float scale = -0.005f;
        int textWidth = 128;

        pPoseStack.mulPose(rot);
        Vector3f facing = facingDirOffset.get(facingDir);
        Vector3f translation;
        switch (facingDir) {
            default:
            case NORTH:
                translation = new Vector3f(0.9375f, 0.9375f, -0.001F);
                break;
            case EAST:
                translation = new Vector3f(0.9375f, 0.9375f, -1.001f);
                break;
            case SOUTH:
                translation = new Vector3f(-0.0625f, 0.9375f, -1.001f);
                break;
            case WEST:
                translation = new Vector3f(-0.0625f, 0.9375f, -0.001f);
                break;
        }
        pPoseStack.translate(translation.x(), translation.y(), translation.z());

        pPoseStack.scale(scale, scale, 1f);
        font.drawInBatch(font.plainSubstrByWidth(
                "testabcdefghijklmnopqrstuvw", textWidth),
                0f, 0f, 0xffffff, false,
                pPoseStack.last().pose(), pBufferSource, false, 0, 140);
        pPoseStack.popPose();
    }
}
