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
import net.minecraft.world.item.crafting.Recipe;
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

import org.kdvcs.klux.block.custom.LiquidReactorBlock;
import org.kdvcs.klux.networking.ModMessages;

import org.kdvcs.klux.networking.packet.LiquidReactorSyncS2CPacket;
import org.kdvcs.klux.recipe.LiquidReactorFluidInventory;
import org.kdvcs.klux.recipe.LiquidReactorRecipe;
import org.kdvcs.klux.screen.LiquidReactorMenu;
import org.kdvcs.klux.sound.ModSounds;

import java.util.List;
import java.util.Map;

public class LiquidReactorBlockEntity extends BlockEntity implements MenuProvider {

    public record MatchedRecipe(LiquidReactorRecipe recipe, boolean swapped) {}

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

    private LazyOptional<IFluidHandler> lazyFluidHandler1 = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyFluidHandler2 = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyOutputFluidHandler = LazyOptional.empty();

    //FLUID WRAPPER
    private final Map<Direction, LazyOptional<IFluidHandler>> fluidHandlerMap = Map.of(
            Direction.EAST, LazyOptional.of(() -> new WrappedFluidHandler(
                    List.of(FLUID_TANK_1),
                    i -> false,
                    (i, s) -> i == 0
            )),

            Direction.SOUTH, LazyOptional.of(() -> new WrappedFluidHandler(
                    List.of(OUTPUT_FLUID_TANK),
                    i -> true,
                    (i, s) -> false
            )),

            Direction.UP, LazyOptional.of(() -> new WrappedFluidHandler(
                    List.of(OUTPUT_FLUID_TANK),
                    i -> true,
                    (i, s) -> false
            )),

            Direction.WEST, LazyOptional.of(() -> new WrappedFluidHandler(
                    List.of(FLUID_TANK_2),
                    i -> false,
                    (i, s) -> i == 1
            ))
    );

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
        return Component.translatable("block.klux.title_liquid_reactor");
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
        }

        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            if (side == null) {
                return LazyOptional.of(() -> new WrappedFluidHandler(
                        List.of(FLUID_TANK_1, FLUID_TANK_2, OUTPUT_FLUID_TANK),
                        (i) -> true,
                        (i, s) -> true)).cast();
            }

            Direction localDir = this.getBlockState().getValue(LiquidReactorBlock.FACING);
            Direction actualDirection;

            if (side == Direction.UP || side == Direction.DOWN) {
                actualDirection = side;
            } else {
                actualDirection = switch (localDir) {
                    default -> side.getOpposite();
                    case EAST -> side.getClockWise();
                    case SOUTH -> side;
                    case WEST -> side.getCounterClockWise();
                };
            }

            LazyOptional<IFluidHandler> fluidHandler = fluidHandlerMap.get(actualDirection);
            if (fluidHandler != null) {
                return fluidHandler.cast();
            }
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

        MatchedRecipe matched = findMatchingRecipe(level, fluidInventory);

        if (matched != null && hasRecipe(pEntity, matched)) {
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

            pEntity.maxProgress = matched.recipe().getMaxProgress();
            pEntity.progress++;

            setChanged(level, pos, state);

            if (pEntity.progress >= pEntity.maxProgress) {
                craftItem(pEntity, matched);
            }
        } else {
            pEntity.resetProgress();
            setChanged(level, pos, state);
        }

        transferItemFluidToFluidTank(pEntity, 0, pEntity.FLUID_TANK_1);
        transferItemFluidToFluidTank(pEntity, 1, pEntity.FLUID_TANK_2);

        boolean isWorkingNow = matched != null && hasRecipe(pEntity, matched);
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

    private static void craftItem(LiquidReactorBlockEntity pEntity, MatchedRecipe matched) {
        LiquidReactorRecipe recipe = matched.recipe();
        boolean swapped = matched.swapped();

        FluidTank tank1 = pEntity.FLUID_TANK_1;
        FluidTank tank2 = pEntity.FLUID_TANK_2;
        FluidTank outputTank = pEntity.OUTPUT_FLUID_TANK;

        FluidStack recipeOutput = recipe.getOutputFluid();
        FluidStack currentOutput = outputTank.getFluid();

        if (!currentOutput.isEmpty() && !currentOutput.getFluid().isSame(recipeOutput.getFluid())) {
            return;
        }

        int drain1 = swapped ? recipe.getFluidInput2Amount() : recipe.getFluidInput1Amount();
        int drain2 = swapped ? recipe.getFluidInput1Amount() : recipe.getFluidInput2Amount();

        tank1.drain(drain1, IFluidHandler.FluidAction.EXECUTE);
        tank2.drain(drain2, IFluidHandler.FluidAction.EXECUTE);

        outputTank.fill(recipeOutput, IFluidHandler.FluidAction.EXECUTE);

        ItemStack outputItem = recipe.getResultItem(pEntity.level.registryAccess());
        if (!outputItem.isEmpty()) {
            pEntity.itemHandler.insertItem(2, outputItem, false);
        }

        pEntity.resetProgress();
    }

    private static @Nullable MatchedRecipe findMatchingRecipe(Level level, LiquidReactorFluidInventory inv) {
        for (Recipe<?> raw : level.getRecipeManager().getAllRecipesFor(LiquidReactorRecipe.Type.INSTANCE)) {
            if (raw instanceof LiquidReactorRecipe recipe) {
                FluidStack in1 = inv.getInput1();
                FluidStack in2 = inv.getInput2();

                if (in1.getFluid().isSame(recipe.getFluidInput1()) &&
                        in2.getFluid().isSame(recipe.getFluidInput2()) &&
                        in1.getAmount() >= recipe.getFluidInput1Amount() &&
                        in2.getAmount() >= recipe.getFluidInput2Amount()) {
                    return new MatchedRecipe(recipe, false);
                }

                if (in1.getFluid().isSame(recipe.getFluidInput2()) &&
                        in2.getFluid().isSame(recipe.getFluidInput1()) &&
                        in1.getAmount() >= recipe.getFluidInput2Amount() &&
                        in2.getAmount() >= recipe.getFluidInput1Amount()) {
                    return new MatchedRecipe(recipe, true);
                }
            }
        }
        return null;
    }

    private static boolean hasRecipe(LiquidReactorBlockEntity entity, MatchedRecipe matched) {
        LiquidReactorRecipe recipe = matched.recipe();
        boolean swapped = matched.swapped();

        FluidTank tank1 = entity.FLUID_TANK_1;
        FluidTank tank2 = entity.FLUID_TANK_2;
        FluidTank outputTank = entity.OUTPUT_FLUID_TANK;

        int required1 = swapped ? recipe.getFluidInput2Amount() : recipe.getFluidInput1Amount();
        int required2 = swapped ? recipe.getFluidInput1Amount() : recipe.getFluidInput2Amount();

        if (tank1.getFluidAmount() < required1) return false;
        if (tank2.getFluidAmount() < required2) return false;
        if (outputTank.getSpace() < recipe.getOutputFluid().getAmount()) return false;

        FluidStack currentOutput = outputTank.getFluid();
        FluidStack recipeOutput = recipe.getOutputFluid();

        if (!currentOutput.isEmpty() && !currentOutput.getFluid().isSame(recipeOutput.getFluid())) {
            return false;
        }

        return true;
    }

}
