package com.hainkiwanki.geneticsmod.block.entity;

import com.hainkiwanki.geneticsmod.block.custom.TerminalBlock;
import com.hainkiwanki.geneticsmod.gui.TerminalMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class TerminalBlockEntity extends BlockEntity implements MenuProvider {

    private static ItemStack currentItem;

    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public TerminalBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.TERMINAL.get(), pPos, pBlockState);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.geneticsmod.terminal_block");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        // TODO: Create Gene Analyzer Menu
        return new TerminalMenu(pContainerId, pPlayerInventory, this);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps()  {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    /*public static void tick(Level pLevel, BlockPos pPos, BlockState pState, TerminalBlockEntity pBlockEntity) {
        ItemStack item = pBlockEntity.itemHandler.extractItem(0, 1, true);
        if(currentItem == null || !item.sameItem(currentItem) && !item.is(Items.AIR)) {
            currentItem = item;

            System.out.println(ForgeRegistries.ITEMS.getKey(currentItem.getItem()).getPath());
        }

        // Custom Code
        //if(hasRecipe(pBlockEntity) && hasNotReachedStackLimit(pBlockEntity)) {
            //craftItem(pBlockEntity);
        //}
    }*/

    /*private static void craftItem(CobaltBlasterBlockEntity entity) {
        entity.itemHandler.extractItem(0, 1, false);
        entity.itemHandler.extractItem(1, 1, false);

        entity.itemHandler.setStackInSlot(3, new ItemStack(ModItems.COBALT_INGOT.get(),
                entity.itemHandler.getStackInSlot(3).getCount() + 1));
    }

    private static boolean hasRecipe(CobaltBlasterBlockEntity entity) {
        boolean hasItemInSecondSlot = entity.itemHandler.getStackInSlot(0).getItem() == ModItems.COAL_SLIVER.get();
        boolean hasItemInFirstSlot = entity.itemHandler.getStackInSlot(1).getItem() == ModItems.RAW_COBALT.get();

        return hasItemInFirstSlot && hasItemInSecondSlot;
    }

    private static boolean hasNotReachedStackLimit(CobaltBlasterBlockEntity entity) {
        return entity.itemHandler.getStackInSlot(3).getCount() < entity.itemHandler.getStackInSlot(3).getMaxStackSize();
    }*/
}
