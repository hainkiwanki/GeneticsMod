package com.hainkiwanki.geneticsmod.block.entity;

import com.hainkiwanki.geneticsmod.gui.menus.GeneAnalyzerMenu;
import com.hainkiwanki.geneticsmod.network.mobdata.MobData;
import com.hainkiwanki.geneticsmod.network.ModMessages;
import com.hainkiwanki.geneticsmod.network.packet.EnergySyncS2CPacket;
import com.hainkiwanki.geneticsmod.recipe.GeneAnalyzerRecipe;
import com.hainkiwanki.geneticsmod.util.capabilities.EnergyStorageCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Optional;

public class GeneAnalyzerBlockEntity extends BlockEntity implements MenuProvider {


    private final ItemStackHandler itemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private final EnergyStorageCapability energyHandler = new EnergyStorageCapability(64000, 256) {
        @Override
        public void onEnergyChanged() {
            setChanged();
            ModMessages.sendToClients(PacketDistributor.ALL.noArg(), new EnergySyncS2CPacket(this.energy, getBlockPos()));
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 1;
    private float successRate = 0;

    private int fuelTime = 0;
    private int maxFuelTime = 0;

    private int energyPerTick = 32;
    private int brunRate = 8;
    private static int receivedEnergy = 128;

    public GeneAnalyzerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GENE_ANALYZER.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                switch (pIndex) {
                    case 0: return fuelTime;
                    case 1: return maxFuelTime;
                    case 2: return progress;
                    case 3: return maxProgress;
                    default: return 0;
                }
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch(pIndex) {
                    case 0: fuelTime = pValue; break;
                    case 1: maxFuelTime = pValue; break;
                    case 2: progress = pValue; break;
                    case 3: maxProgress = pValue; break;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.geneticsmod.gene_analyzer_block");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new GeneAnalyzerMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {
        if(cap == CapabilityEnergy.ENERGY) {
            return lazyEnergyHandler.cast();
        }

        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> energyHandler);
    }

    @Override
    public void invalidateCaps()  {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("gene_analyzer.energyStorage", energyHandler.getEnergyStored());
        tag.putInt("gene_analyzer.fuelTime", fuelTime);
        tag.putInt("gene_analyzer.maxFuelTime", maxFuelTime);
        tag.putInt("gene_analyzer.progress", progress);
        tag.putInt("gene_analyzer.maxProgress", maxProgress);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        energyHandler.setEnergy(nbt.getInt("gene_analyzer.energyStorage"));
        fuelTime = nbt.getInt("gene_analyzer.fuelTime");
        maxFuelTime = nbt.getInt("gene_analyzer.maxFuelTime");
        progress = nbt.getInt("gene_analyzer.progress");
        maxProgress = nbt.getInt("gene_analyzer.maxProgress");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public IEnergyStorage getEnergyStorage() {
        return energyHandler;
    }

    public void setEnergyLevel(int energy) {
        energyHandler.setEnergy(energy);
    }

    private void resetProgress() {
        this.progress = 0;
        this.maxProgress = 1;
        this.successRate = 0;
    }

    private void consumeFuel() {
        if(!itemHandler.getStackInSlot(0).isEmpty()) {
            this.fuelTime = ForgeHooks.getBurnTime(this.itemHandler.extractItem(0, 1, false),
                    RecipeType.SMELTING) / brunRate;
            this.maxFuelTime = this.fuelTime;
        }
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, GeneAnalyzerBlockEntity blockEntity) {
        // Getting energy
        if(hasFuelInFuelSlot(blockEntity) && !isConsumingFuel(blockEntity)) {
            blockEntity.consumeFuel();
            setChanged(level, blockPos, blockState);
        }

        if(isConsumingFuel(blockEntity)) {
            if (blockEntity.energyHandler.hasStorageForEnergy()) {
                blockEntity.fuelTime--;
                blockEntity.energyHandler.receiveEnergy(receivedEnergy, false);
                setChanged(level, blockPos, blockState);
            }
        }

        // Analyzing gene sample
        if(hasRecipe(blockEntity) && hasEnergyToWork(blockEntity)) {
            blockEntity.progress++;
            extractEnergy(blockEntity);
            setChanged(level, blockPos, blockState);

            if(blockEntity.progress >= blockEntity.maxProgress) {
                craftItem(blockEntity);
            }
        }
        else {
            blockEntity.resetProgress();
            setChanged(level, blockPos, blockState);
        }
    }

    private static void extractEnergy(GeneAnalyzerBlockEntity pEntity) {
        pEntity.energyHandler.extractEnergy(pEntity.energyPerTick, false);
    }

    private static boolean hasFuelInFuelSlot(GeneAnalyzerBlockEntity entity) {
        return !entity.itemHandler.getStackInSlot(0).isEmpty();
    }

    private static boolean isConsumingFuel(GeneAnalyzerBlockEntity entity) {
        return entity.fuelTime > 0;
    }

    private static boolean hasEnergyToWork(GeneAnalyzerBlockEntity entity) {
        return entity.energyHandler.getEnergyStored() > entity.energyPerTick * entity.maxProgress;
    }

    private static boolean hasRecipe(GeneAnalyzerBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        Optional<GeneAnalyzerRecipe> match = level.getRecipeManager()
                .getRecipeFor(GeneAnalyzerRecipe.Type.INSTANCE, inventory, level);

        // Check if item is identified
        boolean isIdentified = false;
        ItemStack input = inventory.getItem(1);
        if(input != null) {
            CompoundTag tag = input.getTag();
            if(tag != null) {
                if(tag.getInt("identified") != 0)
                    isIdentified = true;
            }
        }

        if(!match.isEmpty()) {
            entity.maxProgress = match.get().getAnalyzeTime();
            entity.successRate = match.get().getSuccessRate();
        }

        return !isIdentified && match.isPresent() && canInsertAmountIntoOutputSlot(inventory)
                && canInsertItemIntoOutputSlot(inventory, match.get().getResultItem());
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack output) {
        return inventory.getItem(1).getItem() == output.getItem() || inventory.getItem(1).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(1).getMaxStackSize() > inventory.getItem(1).getCount();
    }

    private static void craftItem(GeneAnalyzerBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        Optional<GeneAnalyzerRecipe> match = level.getRecipeManager()
                .getRecipeFor(GeneAnalyzerRecipe.Type.INSTANCE, inventory, level);

        if(match.isPresent()) {
            ItemStack resultItem = new ItemStack(match.get().getResultItem().getItem(), 1);

            ItemStack outputItem = entity.itemHandler.getStackInSlot(2);
            if(outputItem.getItem() == Items.AIR.asItem()) {
                ItemStack input = entity.itemHandler.extractItem(1,1, false);
                resultItem.setTag(input.getTag());
                resultItem.getTag().putFloat(MobData.IDENTIFIED, 1.0f);
                entity.itemHandler.setStackInSlot(2, resultItem);
            } else if (outputItem.getItem() == resultItem.getItem()){
                entity.itemHandler.extractItem(1,1, false);
                outputItem.setCount(outputItem.getCount() + 1);
            }
            entity.resetProgress();
        }
    }

    //region Fix for empty energy bar after world reload
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, blockEntity -> this.getUpdateTag());
    }
    //endregion
}
