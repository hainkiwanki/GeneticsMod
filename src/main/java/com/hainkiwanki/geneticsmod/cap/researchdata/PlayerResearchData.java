package com.hainkiwanki.geneticsmod.cap.researchdata;

import com.hainkiwanki.geneticsmod.network.ModMessages;
import com.hainkiwanki.geneticsmod.network.packet.ChangeMobDataC2SPacket;
import com.hainkiwanki.geneticsmod.network.packet.SyncPlayerResearchDataPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerResearchData {
    private final String UNLOCKED_RESEARCH = "unlocked_research";
    private final String RESEARCH_POINTS = "research_points";
    private final String PLAYER_ID = "player_id";

    private Set<String> unlockedNodes = new HashSet<>();
    private int researchPoints;
    private int playerId;

    public void setPlayerId(int id) {
        this.playerId = id;
        serialize();
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public void setResearchPoints(int amount) {
        this.researchPoints = amount;
    }

    public int getResearchPoints() {
        return this.researchPoints;
    }

    public void increaseResearchPoints(int amount) {
        this.researchPoints += amount;
    }

    public boolean isNodeUnlocked(String nodeId) {
        return unlockedNodes.contains(nodeId);
    }

    public void unlockNode(String nodeId) {
        if(!unlockedNodes.contains(nodeId)) {
            unlockedNodes.add(nodeId);
        }
    }

    public void copy(PlayerResearchData data) {
        this.playerId = data.getPlayerId();
        this.researchPoints = data.getResearchPoints();
        this.unlockedNodes = new HashSet<>(data.unlockedNodes);
    }

    public CompoundTag serialize() {
        CompoundTag nbt = new CompoundTag();
        ListTag list = new ListTag();
        for(String researchId : unlockedNodes) {
            list.add(StringTag.valueOf(researchId));
        }
        nbt.put(UNLOCKED_RESEARCH, list);
        nbt.putInt(RESEARCH_POINTS, this.researchPoints);
        nbt.putInt(PLAYER_ID, this.playerId);
        return nbt;
    }

    public void deserialize(CompoundTag nbt) {
        unlockedNodes.clear();
        ListTag list = nbt.getList(UNLOCKED_RESEARCH, 8);
        for (int i =0; i < list.size(); i++) {
            unlockedNodes.add(list.getString(i));
        }
        this.researchPoints = nbt.getInt(RESEARCH_POINTS);
        this.playerId = nbt.getInt(PLAYER_ID);
    }
}
