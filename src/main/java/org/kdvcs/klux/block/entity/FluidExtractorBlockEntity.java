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

import org.kdvcs.klux.block.custom.FluidExtractorBlock;
import org.kdvcs.klux.networking.ModMessages;
import org.kdvcs.klux.networking.packet.FluidExtractorSyncS2CPacket;
import org.kdvcs.klux.recipe.FluidExtractorRecipe;
import org.kdvcs.klux.screen.FluidExtractorMenu;
import org.kdvcs.klux.sound.ModSounds;

import java.util.Map;
import java.util.Optional;

import org.kdvcs.klux.item.ModItems;

public class FluidExtractorBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot == 0) return true;
            if (slot == 1) return stack.getItem() == Items.BUCKET || stack.getItem() == ModItems.MULTIPHASE_FLUID_CONTAINER.get();
            return false;
        }

        @Override
        public int getSlotLimit(int slot) {
            if (slot == 1) return 1;
            return super.getSlotLimit(slot);
        }
    };

    private final FluidTank FLUID_TANK = new FluidTank(64000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (!level.isClientSide()) {
                ModMessages.sendToClients(new FluidExtractorSyncS2CPacket(this.fluid, worldPosition));
            }
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return true;
        }
    };

    public void setFluid(FluidStack stack) {
        this.FLUID_TANK.setFluid(stack);
    }

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> itemHandler);
    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.of(() -> FLUID_TANK);

    private final Map<Direction, LazyOptional<IItemHandler>> directionItemHandlerMap = Map.of(
            Direction.DOWN, LazyOptional.of(() -> itemHandler)
    );

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;
    private long lastSoundTime = -1;

    public FluidExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FLUID_EXTRACTOR_BE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> FluidExtractorBlockEntity.this.progress;
                    case 1 -> FluidExtractorBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> FluidExtractorBlockEntity.this.progress = value;
                    case 1 -> FluidExtractorBlockEntity.this.maxProgress = value;
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
        return Component.translatable("block.klux.fluid_extractor");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        ModMessages.sendToClients(new FluidExtractorSyncS2CPacket(this.getFluidStack(), worldPosition));
        return new FluidExtractorMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null || side == Direction.DOWN) {
                return lazyItemHandler.cast();
            }
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
        lazyFluidHandler = LazyOptional.of(() -> FLUID_TANK);
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
        nbt.putInt("fluid_extractor.progress", this.progress);
        nbt.putInt("fluid_extractor.max_progress", this.maxProgress);
        FLUID_TANK.writeToNBT(nbt);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("fluid_extractor.progress");
        maxProgress = nbt.contains("fluid_extractor.max_progress") ? nbt.getInt("fluid_extractor.max_progress") : 78;
        FLUID_TANK.readFromNBT(nbt);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public FluidStack getFluidStack() {
        FluidStack fluid = this.FLUID_TANK.getFluid();
        return fluid;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FluidExtractorBlockEntity entity) {
        boolean wasWorking = state.getValue(FluidExtractorBlock.WORKING);

        if (level.isClientSide()) return;

        if (hasRecipe(entity)) {
            if (level instanceof ServerLevel serverLevel) {
                long gameTime = level.getGameTime();
                if (entity.lastSoundTime == -1 || gameTime - entity.lastSoundTime >= 46) {
                    serverLevel.playSound(null, pos, ModSounds.FLUID_EXTRACTOR_WORKING.get(), SoundSource.BLOCKS, 0.8f, 1.0f);
                    entity.lastSoundTime = gameTime;
                }
            }

            FluidExtractorRecipe recipe = getRecipe(entity).get();
            entity.maxProgress = recipe.getMaxProgress();
            entity.progress++;

            setChanged(level, pos, state);

            if (entity.progress >= entity.maxProgress) {
                craftItem(entity);
            }
        } else {
            entity.resetProgress();
            setChanged(level, pos, state);
        }

        extractFluidWithBucket(entity);
        extractFluidWithMultiphaseContainer(entity);

        boolean isWorkingNow = hasRecipe(entity);
        if (wasWorking != isWorkingNow) {
            level.setBlock(pos, state.setValue(FluidExtractorBlock.WORKING, isWorkingNow), 3);
        }
    }

    private static Optional<FluidExtractorRecipe> getRecipe(FluidExtractorBlockEntity entity) {
        Level level = entity.level;
        ItemStack input = entity.itemHandler.getStackInSlot(0);
        if (input.isEmpty()) return Optional.empty();

        SimpleContainer inventory = new SimpleContainer(1);
        inventory.setItem(0, input);

        return level.getRecipeManager()
                .getRecipeFor(FluidExtractorRecipe.Type.INSTANCE, inventory, level);
    }

    private static boolean hasRecipe(FluidExtractorBlockEntity entity) {
        Optional<FluidExtractorRecipe> recipeOpt = getRecipe(entity);
        if (recipeOpt.isEmpty()) return false;

        FluidExtractorRecipe recipe = recipeOpt.get();
        ItemStack input = entity.itemHandler.getStackInSlot(0);

        boolean hasEnoughItem = input.getCount() >= recipe.ingredient.count;
        boolean hasFluidSpace = entity.FLUID_TANK.getSpace() >= recipe.getFluid().getAmount();

        FluidStack currentFluid = entity.FLUID_TANK.getFluid();
        FluidStack recipeFluid = recipe.getFluid();

        boolean isSameFluidOrEmpty =
                currentFluid.isEmpty() || currentFluid.getFluid().isSame(recipeFluid.getFluid());

        return hasEnoughItem && hasFluidSpace && isSameFluidOrEmpty;
    }


    private void resetProgress() {
        this.progress = 0;
    }

    private static void craftItem(FluidExtractorBlockEntity entity) {
        Optional<FluidExtractorRecipe> recipeOpt = getRecipe(entity);
        if (recipeOpt.isEmpty()) return;

        FluidExtractorRecipe recipe = recipeOpt.get();

        entity.itemHandler.extractItem(0, recipe.ingredient.count, false);
        entity.FLUID_TANK.fill(recipe.getFluid(), IFluidHandler.FluidAction.EXECUTE);

        entity.resetProgress();
    }

    private static void extractFluidWithBucket(FluidExtractorBlockEntity entity) {

        ItemStack bucket = entity.itemHandler.getStackInSlot(1);
        FluidStack fluid = entity.FLUID_TANK.getFluid();

        if (bucket.getItem() == Items.BUCKET && fluid.getAmount() >= 1000) {
            ItemStack filledBucket = fluid.getFluid().getBucket().getDefaultInstance();

            if (!filledBucket.isEmpty()) {
                entity.itemHandler.setStackInSlot(1, filledBucket);
                entity.FLUID_TANK.drain(1000, IFluidHandler.FluidAction.EXECUTE);

                if (entity.level instanceof ServerLevel serverLevel) {
                    serverLevel.playSound(null, entity.worldPosition, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
                }

                entity.setChanged();
            }
        }
    }

    private static void extractFluidWithMultiphaseContainer(FluidExtractorBlockEntity entity) {
        ItemStack containerStack = entity.itemHandler.getStackInSlot(1);
        FluidStack fluidInTank = entity.FLUID_TANK.getFluid();

        if (containerStack.getItem() == ModItems.MULTIPHASE_FLUID_CONTAINER.get() && !fluidInTank.isEmpty()) {

            containerStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(containerFluidHandler -> {
                int containerSpace = containerFluidHandler.getTankCapacity(0) - containerFluidHandler.getFluidInTank(0).getAmount();

                if (containerSpace > 0) {

                    int fillAmount = Math.min(containerSpace, fluidInTank.getAmount());

                    FluidStack fluidToFill = new FluidStack(fluidInTank, fillAmount);

                    int filled = containerFluidHandler.fill(fluidToFill, IFluidHandler.FluidAction.EXECUTE);

                    if (filled > 0) {

                        entity.FLUID_TANK.drain(filled, IFluidHandler.FluidAction.EXECUTE);

                        entity.setChanged();

                        if (entity.level instanceof ServerLevel serverLevel) {
                            serverLevel.playSound(null, entity.worldPosition, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
                        }
                    }
                }
            });
        }
    }

}
