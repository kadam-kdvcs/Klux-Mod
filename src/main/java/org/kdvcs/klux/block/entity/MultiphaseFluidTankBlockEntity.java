package org.kdvcs.klux.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kdvcs.klux.networking.ModMessages;
import org.kdvcs.klux.networking.packet.MultiphaseFluidTankSyncS2CPacket;
import org.kdvcs.klux.screen.MultiphaseFluidTankMenu;

public class MultiphaseFluidTankBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {

            if (slot == 0 || slot == 1) {
                return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
            }
            return false;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };

    private final FluidTank fluidTank = new FluidTank(256000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (!level.isClientSide()) {
                ModMessages.sendToClients(new MultiphaseFluidTankSyncS2CPacket(this.fluid, worldPosition));
            }
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return true;
        }
    };

    public void setFluid(FluidStack fluidStack) {
        this.fluidTank.setFluid(fluidStack);
    }

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> itemHandler);
    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.of(() -> fluidTank);

    public MultiphaseFluidTankBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MULTIPHASE_FLUID_TANK_BE.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.klux.multiphase_fluid_tank");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        ModMessages.sendToClients(new MultiphaseFluidTankSyncS2CPacket(this.getFluidStack(), worldPosition));
        return new MultiphaseFluidTankMenu(id, inventory, this);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return lazyFluidHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyFluidHandler = LazyOptional.of(() -> fluidTank);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyFluidHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        fluidTank.writeToNBT(nbt);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        fluidTank.readFromNBT(nbt);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public FluidStack getFluidStack() {
        return fluidTank.getFluid();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, MultiphaseFluidTankBlockEntity entity) {

        if (level.isClientSide()) return;
        entity.tryInputFluid();
        entity.tryExtractFluid();

    }

    private void tryInputFluid() {
        ItemStack inputStack = itemHandler.getStackInSlot(0);
        if (inputStack.isEmpty()) return;

        inputStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(fluidHandler -> {

            FluidStack simulatedDrain = fluidHandler.drain(fluidTank.getSpace(), IFluidHandler.FluidAction.SIMULATE);
            if (!simulatedDrain.isEmpty()) {
                int filled = fluidTank.fill(simulatedDrain, IFluidHandler.FluidAction.EXECUTE);
                if (filled > 0) {
                    FluidStack actualDrain = fluidHandler.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                    ItemStack drainedContainer = fluidHandler.getContainer();
                    itemHandler.setStackInSlot(0, drainedContainer.copy());
                    setChanged();
                }
            }
        });
    }

    private void tryExtractFluid() {
        ItemStack outputStack = itemHandler.getStackInSlot(1);
        if (outputStack.isEmpty()) return;

        FluidStack fluidInTank = fluidTank.getFluid();
        if (fluidInTank.isEmpty()) return;

        outputStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(fluidHandler -> {

            if (outputStack.getItem() == Items.BUCKET) {
                if (fluidInTank.getAmount() >= 1000) {
                    FluidStack toFill = new FluidStack(fluidInTank, 1000);
                    int filled = fluidHandler.fill(toFill, IFluidHandler.FluidAction.EXECUTE);
                    if (filled == 1000) {

                        fluidTank.drain(filled, IFluidHandler.FluidAction.EXECUTE);

                        ItemStack filledContainer = fluidHandler.getContainer();
                        itemHandler.setStackInSlot(1, filledContainer.copy());

                        setChanged();
                    }
                }
                return;
            }

            int space = fluidHandler.getTankCapacity(0) - fluidHandler.getFluidInTank(0).getAmount();
            if (space > 0) {
                FluidStack toFill = new FluidStack(fluidInTank, Math.min(space, fluidInTank.getAmount()));
                int filled = fluidHandler.fill(toFill, IFluidHandler.FluidAction.EXECUTE);
                if (filled > 0) {
                    fluidTank.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                    setChanged();
                }
            }
        });
    }

}
