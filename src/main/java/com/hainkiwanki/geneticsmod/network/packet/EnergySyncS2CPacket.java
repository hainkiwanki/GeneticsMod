package com.hainkiwanki.geneticsmod.network.packet;

import com.hainkiwanki.geneticsmod.block.entity.GeneAnalyzerBlockEntity;
import com.hainkiwanki.geneticsmod.block.entity.GeneIsolatorBlockEntity;
import com.hainkiwanki.geneticsmod.gui.menus.GeneAnalyzerMenu;
import com.hainkiwanki.geneticsmod.gui.menus.GeneIsolatorMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class EnergySyncS2CPacket {

    private final int energy;
    private final BlockPos pos;

    public EnergySyncS2CPacket(int energy, BlockPos pos) {
        this.energy = energy;
        this.pos = pos;
    }

    public EnergySyncS2CPacket(FriendlyByteBuf buf) {
        this.energy = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(energy);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof GeneAnalyzerBlockEntity blockEntity) {
                blockEntity.setEnergyLevel(energy);

                if(Minecraft.getInstance().player.containerMenu instanceof GeneAnalyzerMenu menu &&
                        menu.blockEntity.getBlockPos().equals(pos)) {
                    blockEntity.setEnergyLevel(energy);
                }
            }
            else if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof GeneIsolatorBlockEntity blockEntity) {
                blockEntity.setEnergyLevel(energy);

                if(Minecraft.getInstance().player.containerMenu instanceof GeneIsolatorMenu menu &&
                        menu.blockEntity.getBlockPos().equals(pos)) {
                    blockEntity.setEnergyLevel(energy);
                }
            }
        });
        return true;
    }
}
