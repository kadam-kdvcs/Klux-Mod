package org.kdvcs.klux.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
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

import org.kdvcs.klux.block.custom.LiquidReactorBlock;
import org.kdvcs.klux.fluid.ModFluids;
import org.kdvcs.klux.networking.ModMessages;

import org.kdvcs.klux.networking.packet.LiquidReactorSyncS2CPacket;
import org.kdvcs.klux.recipe.LiquidReactorFluidInventory;
import org.kdvcs.klux.recipe.LiquidReactorRecipe;
import org.kdvcs.klux.screen.LiquidReactorMenu;
import org.kdvcs.klux.sound.ModSounds;

import java.util.Map;
import java.util.Optional;

public class LiquidReactorBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
        }
    };

    private final FluidTank FLUID_TANK_1 = new FluidTank(48000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (!level.isClientSide()) {
                ModMessages.sendToClients(new LiquidReactorSyncS2CPacket(
                        FLUID_TANK_1.getFluid(),
                        FLUID_TANK_2.getFluid(),
                        OUTPUT_FLUID_TANK.getFluid(),
                        worldPosition));
            }
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            if (stack.isEmpty()) return false;
            FluidStack current = getFluid();
            if (current.isEmpty()) {
                return true;
            } else {
                return current.getFluid() == stack.getFluid();
            }
        }
    };

    private final FluidTank FLUID_TANK_2 = new FluidTank(48000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (!level.isClientSide()) {
                ModMessages.sendToClients(new LiquidReactorSyncS2CPacket(
                        FLUID_TANK_1.getFluid(),
                        FLUID_TANK_2.getFluid(),
                        OUTPUT_FLUID_TANK.getFluid(),
                        worldPosition));
            }
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            if (stack.isEmpty()) return false;
            FluidStack current = getFluid();
            if (current.isEmpty()) {
                return true;
            } else {
                return current.getFluid() == stack.getFluid();
            }
        }
    };

    private final FluidTank OUTPUT_FLUID_TANK = new FluidTank(96000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (!level.isClientSide()) {
                ModMessages.sendToClients(new LiquidReactorSyncS2CPacket(
                        FLUID_TANK_1.getFluid(),
                        FLUID_TANK_2.getFluid(),
                        OUTPUT_FLUID_TANK.getFluid(),
                        worldPosition));
            }
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return true;
        }
    };

    public void setFluids(FluidStack fluid1, FluidStack fluid2, FluidStack outputFluid) {
        this.FLUID_TANK_1.setFluid(fluid1);
        this.FLUID_TANK_2.setFluid(fluid2);
        this.OUTPUT_FLUID_TANK.setFluid(outputFluid);
    }

    public FluidStack getInputFluidStack1() {
        return this.FLUID_TANK_1.getFluid();
    }

    public FluidStack getInputFluidStack2() {
        return this.FLUID_TANK_2.getFluid();
    }

    public FluidStack getOutputFluidStack() {
        return this.OUTPUT_FLUID_TANK.getFluid();
    }

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap = Map.of(
            Direction.UP, LazyOptional.of(() -> new WrappedHandler(itemHandler,
                    slot -> slot == 0 || slot == 1,
                    (slot, stack) -> itemHandler.isItemValid(slot, stack))),
            Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemHandler,
                    slot -> slot == 2,
                    (slot, stack) -> false))
    );

    private LazyOptional<IFluidHandler> lazyFluidHandler1 = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyFluidHandler2 = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyOutputFluidHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;

    private long lastSoundTime = -1;

    public LiquidReactorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LIQUID_REACTOR_BE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> LiquidReactorBlockEntity.this.progress;
                    case 1 -> LiquidReactorBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> LiquidReactorBlockEntity.this.progress = value;
                    case 1 -> LiquidReactorBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.klux.liquid_reactor");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        ModMessages.sendToClients(new LiquidReactorSyncS2CPacket(
                this.getInputFluidStack1(),
                this.getInputFluidStack2(),
                this.getOutputFluidStack(),
                worldPosition));
        return new LiquidReactorMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) return lazyItemHandler.cast();
            if (directionWrappedHandlerMap.containsKey(side)) {
                return directionWrappedHandlerMap.get(side).cast();
            }
        }
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            if (side == Direction.UP) return lazyFluidHandler1.cast();
            if (side == Direction.NORTH) return lazyFluidHandler2.cast();
            if (side == Direction.DOWN) return lazyOutputFluidHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyFluidHandler1 = LazyOptional.of(() -> FLUID_TANK_1);
        lazyFluidHandler2 = LazyOptional.of(() -> FLUID_TANK_2);
        lazyOutputFluidHandler = LazyOptional.of(() -> OUTPUT_FLUID_TANK);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyFluidHandler1.invalidate();
        lazyFluidHandler2.invalidate();
        lazyOutputFluidHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("liquid_reactor.progress", this.progress);
        nbt.putInt("liquid_reactor.max_progress", this.maxProgress);

        CompoundTag tank1Tag = new CompoundTag();
        FLUID_TANK_1.writeToNBT(tank1Tag);
        nbt.put("fluid_tank_1", tank1Tag);

        CompoundTag tank2Tag = new CompoundTag();
        FLUID_TANK_2.writeToNBT(tank2Tag);
        nbt.put("fluid_tank_2", tank2Tag);

        CompoundTag outputTankTag = new CompoundTag();
        OUTPUT_FLUID_TANK.writeToNBT(outputTankTag);
        nbt.put("output_fluid_tank", outputTankTag);

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("liquid_reactor.progress");
        maxProgress = nbt.contains("liquid_reactor.max_progress") ? nbt.getInt("liquid_reactor.max_progress") : 78;

        if (nbt.contains("fluid_tank_1")) {
            FLUID_TANK_1.readFromNBT(nbt.getCompound("fluid_tank_1"));
        }

        if (nbt.contains("fluid_tank_2")) {
            FLUID_TANK_2.readFromNBT(nbt.getCompound("fluid_tank_2"));
        }

        if (nbt.contains("output_fluid_tank")) {
            OUTPUT_FLUID_TANK.readFromNBT(nbt.getCompound("output_fluid_tank"));
        }
    }


    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, LiquidReactorBlockEntity pEntity) {
        boolean wasWorking = state.getValue(LiquidReactorBlock.WORKING);

        if (level.isClientSide()) {
            return;
        }

        LiquidReactorFluidInventory fluidInventory = new LiquidReactorFluidInventory(
                pEntity.getInputFluidStack1(),
                pEntity.getInputFluidStack2()
        );

        Optional<LiquidReactorRecipe> recipeOpt = level.getRecipeManager()
                .getRecipeFor(LiquidReactorRecipe.Type.INSTANCE, fluidInventory, level);

        if (recipeOpt.isPresent() && hasRecipe(pEntity, recipeOpt.get())) {
            if (level instanceof ServerLevel serverLevel) {
                long gameTime = level.getGameTime();
                if (pEntity.lastSoundTime == -1 || gameTime - pEntity.lastSoundTime >= 41) {
                    serverLevel.playSound(
                            null,
                            pos,
                            ModSounds.LIQUID_REACTOR_WORKING.get(),
                            SoundSource.BLOCKS,
                            1.4f,
                            1.0f
                    );
                    pEntity.lastSoundTime = gameTime;
                }
            }

            LiquidReactorRecipe recipe = recipeOpt.get();
            pEntity.maxProgress = recipe.getMaxProgress();
            pEntity.progress++;

            setChanged(level, pos, state);

            if (pEntity.progress >= pEntity.maxProgress) {
                craftItem(pEntity, recipe);
            }
        } else {
            pEntity.resetProgress();
            setChanged(level, pos, state);
        }

        transferItemFluidToFluidTank(pEntity, 0, pEntity.FLUID_TANK_1);
        transferItemFluidToFluidTank(pEntity, 1, pEntity.FLUID_TANK_2);

        boolean isWorkingNow = recipeOpt.isPresent() && hasRecipe(pEntity, recipeOpt.get());
        if (wasWorking != isWorkingNow) {
            level.setBlock(pos, state.setValue(LiquidReactorBlock.WORKING, isWorkingNow), 3);
        }

        transferOutputFluidToContainer(pEntity);
    }


    private static void transferItemFluidToFluidTank(LiquidReactorBlockEntity pEntity, int slot, FluidTank tank) {
        ItemStack stack = pEntity.itemHandler.getStackInSlot(slot);
        if(stack.isEmpty()) return;

        stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {
            int drainAmount = Math.min(tank.getSpace(), 1000);

            FluidStack simulatedDrain = handler.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
            if (tank.isFluidValid(simulatedDrain) && !simulatedDrain.isEmpty()) {
                FluidStack drained = handler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
                int filled = tank.fill(drained, IFluidHandler.FluidAction.EXECUTE);

                ItemStack container = handler.getContainer();
                if(filled > 0) {
                    pEntity.itemHandler.extractItem(slot, 1, false);
                    pEntity.itemHandler.insertItem(slot, container, false);
                }
            }
        });
    }

    private static void transferOutputFluidToContainer(LiquidReactorBlockEntity pEntity) {
        ItemStack extractStack = pEntity.itemHandler.getStackInSlot(2);
        if (extractStack.isEmpty()) return;

        extractStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {
            FluidStack extractable = pEntity.OUTPUT_FLUID_TANK.drain(1000, IFluidHandler.FluidAction.SIMULATE);
            if (extractable.isEmpty()) return;

            int filled = handler.fill(extractable, IFluidHandler.FluidAction.EXECUTE);
            if (filled > 0) {
                pEntity.OUTPUT_FLUID_TANK.drain(filled, IFluidHandler.FluidAction.EXECUTE);

                ItemStack resultContainer = handler.getContainer();
                pEntity.itemHandler.extractItem(2, 1, false);
                pEntity.itemHandler.insertItem(2, resultContainer, false);
            }
        });
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static void craftItem(LiquidReactorBlockEntity pEntity, LiquidReactorRecipe recipe) {
        pEntity.FLUID_TANK_1.drain(recipe.getFluidInput1Amount(), IFluidHandler.FluidAction.EXECUTE);
        pEntity.FLUID_TANK_2.drain(recipe.getFluidInput2Amount(), IFluidHandler.FluidAction.EXECUTE);

        pEntity.OUTPUT_FLUID_TANK.fill(recipe.getOutputFluid(), IFluidHandler.FluidAction.EXECUTE);

        ItemStack outputItem = recipe.getResultItem(pEntity.level.registryAccess());
        if (!outputItem.isEmpty()) {
            pEntity.itemHandler.insertItem(2, outputItem, false);
        }

        pEntity.resetProgress();
    }

    private static boolean hasRecipe(LiquidReactorBlockEntity entity, LiquidReactorRecipe recipe) {
        if (entity.FLUID_TANK_1.getFluidAmount() < recipe.getFluidInput1Amount()) return false;
        if (entity.FLUID_TANK_2.getFluidAmount() < recipe.getFluidInput2Amount()) return false;
        if (entity.OUTPUT_FLUID_TANK.getSpace() < recipe.getOutputFluid().getAmount()) return false;
        return true;
    }

}
