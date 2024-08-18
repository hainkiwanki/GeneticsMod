package com.hainkiwanki.geneticsmod.gui.menus;

import com.hainkiwanki.geneticsmod.block.ModBlocks;
import com.hainkiwanki.geneticsmod.block.entity.ResearchTableBlockEntity;
import com.hainkiwanki.geneticsmod.cap.researchdata.PlayerResearchData;
import com.hainkiwanki.geneticsmod.cap.researchdata.PlayerResearchProvider;
import com.hainkiwanki.geneticsmod.gui.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;

public class ResearchTableMenu extends AbstractContainerMenu {
    private final ResearchTableBlockEntity blockEntity;
    private final Level level;
    private PlayerResearchData playerResearchData;

    public ResearchTableMenu(int windowId, Inventory inv, FriendlyByteBuf extraData) {
        this(windowId, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()));
    }

    public ResearchTableMenu(int windowId, Inventory inv, BlockEntity entity) {
        super(ModMenuTypes.RESEARCH_TABLE_MENU.get(), windowId);
        this.blockEntity = (ResearchTableBlockEntity) entity;
        this.level = inv.player.level;

        if(inv.player instanceof ServerPlayer) {
            LazyOptional<PlayerResearchData> researchDataOptional = inv.player.getCapability(PlayerResearchProvider.PLAYER_RESEARCH_DATA);
            this.playerResearchData =  researchDataOptional.orElseThrow(() -> new IllegalArgumentException("PlayerResearchData not found"));
        }
    }

    public PlayerResearchData getPlayerResearchData() {
        System.out.println(this.playerResearchData);
        return this.playerResearchData;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, ModBlocks.RESEARCH_TABLE.get());
    }
}
