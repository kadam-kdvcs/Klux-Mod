package org.kdvcs.klux.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import org.kdvcs.klux.block.custom.ExtractorBlock;

import org.kdvcs.klux.block.custom.FluxSynthesizerBlock;
import org.kdvcs.klux.fluid.ModFluids;
import org.kdvcs.klux.item.ModItems;
import org.kdvcs.klux.networking.ModMessages;


import org.kdvcs.klux.networking.packet.FluxSynthesizerSyncS2CPacket;
import org.kdvcs.klux.recipe.FluxSynthesizerRecipe;
import org.kdvcs.klux.screen.FluxSynthesizerMenu;
import org.kdvcs.klux.sound.ModSounds;

import java.util.Map;
import java.util.Optional;

public class FluxSynthesizerBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(5) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0 -> stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
                case 1, 2 -> true;
                case 3 -> false;
                case 4 -> stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()
                        || stack.getItem() == Items.BUCKET;
                default -> false;
            };
        }
    };

    private final FluidTank FLUID_TANK = new FluidTank(32000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (!level.isClientSide()) {
                ModMessages.sendToClients(new FluxSynthesizerSyncS2CPacket(FLUID_TANK.getFluid(), OUTPUT_FLUID_TANK.getFluid(), worldPosition));
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

    private final FluidTank OUTPUT_FLUID_TANK = new FluidTank(32000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (!level.isClientSide()) {
                ModMessages.sendToClients(new FluxSynthesizerSyncS2CPacket(FLUID_TANK.getFluid(), OUTPUT_FLUID_TANK.getFluid(), worldPosition));
            }
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return true;
        }
    };

    public void setFluids(FluidStack inputFluid, FluidStack outputFluid) {
        this.FLUID_TANK.setFluid(inputFluid);
        this.OUTPUT_FLUID_TANK.setFluid(outputFluid);
    }

    public FluidStack getInputFluidStack() {
        return this.FLUID_TANK.getFluid();
    }

    public FluidStack getOutputFluidStack() {
        return this.OUTPUT_FLUID_TANK.getFluid();
    }

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 2, (i, s) -> false)),
                    Direction.NORTH, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 1,
                            (index, stack) -> itemHandler.isItemValid(1, stack))),
                    Direction.SOUTH, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 2, (i, s) -> false)),
                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 1,
                            (index, stack) -> itemHandler.isItemValid(1, stack))),
                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0 || index == 1,
                            (index, stack) -> itemHandler.isItemValid(0, stack) || itemHandler.isItemValid(1, stack))));

    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyOutputFluidHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;

    //  INITIALIZE THE SOUND PLAYER
    private long lastSoundTime = -1;

    public FluxSynthesizerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FLUX_SYNTHESIZER_BE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> FluxSynthesizerBlockEntity.this.progress;
                    case 1 -> FluxSynthesizerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> FluxSynthesizerBlockEntity.this.progress = value;
                    case 1 -> FluxSynthesizerBlockEntity.this.maxProgress = value;
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
        return Component.translatable("block.klux.flux_synthesizer");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {

        ModMessages.sendToClients(new FluxSynthesizerSyncS2CPacket(this.getInputFluidStack(), this.getOutputFluidStack(), worldPosition));

        return new FluxSynthesizerMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {

        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if(side == null) {
                return lazyItemHandler.cast();
            }
        }

        if(cap == ForgeCapabilities.FLUID_HANDLER) {
            return lazyFluidHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyFluidHandler = LazyOptional.of(() -> FLUID_TANK);
        lazyOutputFluidHandler = LazyOptional.of(() -> OUTPUT_FLUID_TANK);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyFluidHandler.invalidate();
        lazyOutputFluidHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("flux_synthesizer.progress", this.progress);
        nbt.putInt("flux_synthesizer.max_progress", this.maxProgress);

        CompoundTag fluidTankTag = new CompoundTag();
        FLUID_TANK.writeToNBT(fluidTankTag);
        nbt.put("fluid_tank", fluidTankTag);

        CompoundTag outputTankTag = new CompoundTag();
        OUTPUT_FLUID_TANK.writeToNBT(outputTankTag);
        nbt.put("output_fluid_tank", outputTankTag);

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("flux_synthesizer.progress");
        maxProgress = nbt.contains("flux_synthesizer.max_progress") ? nbt.getInt("flux_synthesizer.max_progress") : 78;

        if (nbt.contains("fluid_tank")) {
            FLUID_TANK.readFromNBT(nbt.getCompound("fluid_tank"));
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

    public static void tick(Level level, BlockPos pos, BlockState state, FluxSynthesizerBlockEntity pEntity) {
        boolean wasWorking = state.getValue(ExtractorBlock.WORKING);

        if(level.isClientSide()) {
            return;
        }

        SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());
        for (int i = 0; i < pEntity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, pEntity.itemHandler.getStackInSlot(i));
        }

        FluidStack machineFluidInput = pEntity.FLUID_TANK.getFluid();

        Optional<FluxSynthesizerRecipe> recipeOpt = level.getRecipeManager()
                .getAllRecipesFor(FluxSynthesizerRecipe.Type.INSTANCE).stream()
                .filter(recipe -> recipe.matches(inventory, level, machineFluidInput))
                .findFirst();

        if (hasRecipe(pEntity)) {
            FluxSynthesizerRecipe recipe = recipeOpt.get();

            if (level instanceof ServerLevel serverLevel) {
                long gameTime = level.getGameTime();
                if (pEntity.lastSoundTime == -1 || gameTime - pEntity.lastSoundTime >= 34) {
                    serverLevel.playSound(
                            null,
                            pos,
                            ModSounds.FLUX_SYNTHESIZER_WORKING.get(),
                            SoundSource.BLOCKS,
                            1.8f,
                            1.0f
                    );
                    pEntity.lastSoundTime = gameTime;
                }
            }

            pEntity.maxProgress = recipe.getMaxProgress();
            pEntity.progress++;

            setChanged(level, pos, state);

            if(pEntity.progress >= pEntity.maxProgress) {
                craftItem(pEntity, recipe);
            }

        } else {
            pEntity.resetProgress();
            setChanged(level, pos, state);
        }

        if(hasFluidItemInSourceSlot(pEntity)) {
            transferItemFluidToFluidTank(pEntity);
        }

        boolean isWorkingNow = hasRecipe(pEntity);
        if (wasWorking != isWorkingNow) {
            level.setBlock(pos, state.setValue(FluxSynthesizerBlock.WORKING, isWorkingNow), 3);
        }

        transferOutputFluidToContainer(pEntity);
    }

    private static void transferItemFluidToFluidTank(FluxSynthesizerBlockEntity pEntity) {
        pEntity.itemHandler.getStackInSlot(0).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {
            int drainAmount = Math.min(pEntity.FLUID_TANK.getSpace(), 1000);

            FluidStack stack = handler.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
            if(pEntity.FLUID_TANK.isFluidValid(stack)) {
                stack = handler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
                fillTankWithFluid(pEntity, stack, handler.getContainer());
            }

        });
    }

    private static void fillTankWithFluid(FluxSynthesizerBlockEntity pEntity, FluidStack stack, ItemStack container) {
        int filled = pEntity.FLUID_TANK.fill(stack, IFluidHandler.FluidAction.EXECUTE);

        ItemStack inputStack = pEntity.itemHandler.getStackInSlot(0);
        boolean isMultiphaseContainer = !inputStack.isEmpty() &&
                inputStack.is(ModItems.MULTIPHASE_FLUID_CONTAINER.get());

        if (filled > 0 && pEntity.level instanceof ServerLevel serverLevel && !isMultiphaseContainer) {
            if (stack.getFluid() == Fluids.LAVA) {
                serverLevel.playSound(
                        null,
                        pEntity.worldPosition,
                        SoundEvents.BUCKET_FILL_LAVA,
                        SoundSource.BLOCKS,
                        0.8f,
                        1.0f
                );
            } else {
                serverLevel.playSound(
                        null,
                        pEntity.worldPosition,
                        SoundEvents.BUCKET_FILL,
                        SoundSource.BLOCKS,
                        0.8f,
                        1.0f
                );
            }
        }

        pEntity.itemHandler.extractItem(0, 1, false);
        pEntity.itemHandler.insertItem(0, container, false);
    }

    private static void transferOutputFluidToContainer(FluxSynthesizerBlockEntity pEntity) {
        ItemStack extractStack = pEntity.itemHandler.getStackInSlot(4);
        if (extractStack.isEmpty()) return;

        extractStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {
            FluidStack extractable = pEntity.OUTPUT_FLUID_TANK.drain(1000, IFluidHandler.FluidAction.SIMULATE);
            if (extractable.isEmpty()) return;

            int filled = handler.fill(extractable, IFluidHandler.FluidAction.EXECUTE);
            if (filled > 0) {
                pEntity.OUTPUT_FLUID_TANK.drain(filled, IFluidHandler.FluidAction.EXECUTE);

                ItemStack resultContainer = handler.getContainer();
                pEntity.itemHandler.extractItem(4, 1, false);
                pEntity.itemHandler.insertItem(4, resultContainer, false);
            }
        });
    }

    private static boolean hasFluidItemInSourceSlot(FluxSynthesizerBlockEntity pEntity) {
        return pEntity.itemHandler.getStackInSlot(0).getCount() > 0;
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static void craftItem(FluxSynthesizerBlockEntity pEntity, FluxSynthesizerRecipe recipe) {
        FluidStack resultFluid = recipe.getResultFluid();
        FluidStack currentFluid = pEntity.OUTPUT_FLUID_TANK.getFluid();

        boolean canFill = currentFluid.isEmpty()
                || (currentFluid.getFluid() == resultFluid.getFluid()
                && currentFluid.getAmount() + resultFluid.getAmount() <= pEntity.OUTPUT_FLUID_TANK.getCapacity());

        if (!canFill) {
            pEntity.resetProgress();
            return;
        }

        ItemStack output = recipe.getResultItem(null);
        ItemStack currentOutput = pEntity.itemHandler.getStackInSlot(3);

        if (!currentOutput.isEmpty() && (!ItemStack.isSameItemSameTags(currentOutput, output)
                || currentOutput.getCount() + output.getCount() > currentOutput.getMaxStackSize())) {
            pEntity.resetProgress();
            return;
        }

        pEntity.FLUID_TANK.drain(recipe.getFluid().getAmount(), IFluidHandler.FluidAction.EXECUTE);
        pEntity.OUTPUT_FLUID_TANK.fill(resultFluid, IFluidHandler.FluidAction.EXECUTE);
        pEntity.itemHandler.extractItem(1, recipe.ingredient1.count, false);
        pEntity.itemHandler.extractItem(2, recipe.ingredient2.count, false);

        if (currentOutput.isEmpty()) {
            pEntity.itemHandler.setStackInSlot(3, output.copy());
        } else {
            currentOutput.grow(output.getCount());
            pEntity.itemHandler.setStackInSlot(3, currentOutput);
        }

        pEntity.resetProgress();
    }

    private static boolean hasRecipe(FluxSynthesizerBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        FluidStack machineFluidInput = entity.FLUID_TANK.getFluid();

        Optional<FluxSynthesizerRecipe> recipeOpt = level.getRecipeManager()
                .getAllRecipesFor(FluxSynthesizerRecipe.Type.INSTANCE).stream()
                .filter(recipe -> recipe.matches(inventory, level, machineFluidInput))
                .findFirst();

        if (recipeOpt.isEmpty()) {
            return false;
        }

        FluxSynthesizerRecipe recipe = recipeOpt.get();

        ItemStack outputItem = recipe.getResultItem(null);
        boolean canInsertItem = canInsertOutput(inventory, outputItem);

        FluidStack resultFluid = recipe.getResultFluid();
        FluidStack currentFluid = entity.OUTPUT_FLUID_TANK.getFluid();

        boolean canInsertFluid = currentFluid.isEmpty()
                || (currentFluid.getFluid() == resultFluid.getFluid()
                && currentFluid.getAmount() + resultFluid.getAmount() <= entity.OUTPUT_FLUID_TANK.getCapacity());

        return canInsertItem && canInsertFluid;
    }

    private static boolean hasCorrectFluidAmountInTank(FluxSynthesizerBlockEntity entity, Optional<FluxSynthesizerRecipe> recipe) {
        return entity.FLUID_TANK.getFluidAmount() >= recipe.get().getFluid().getAmount();
    }

    private static boolean hasCorrectFluidInTank(FluxSynthesizerBlockEntity entity, Optional<FluxSynthesizerRecipe> recipe) {
        return recipe.get().getFluid().equals(entity.FLUID_TANK.getFluid());
    }

    private static boolean canInsertOutput(SimpleContainer inventory, ItemStack stack) {
        ItemStack current = inventory.getItem(3);
        if (current.isEmpty()) {
            return true;
        }
        return ItemStack.isSameItemSameTags(current, stack) && current.getCount() + stack.getCount() <= current.getMaxStackSize();
    }

}
