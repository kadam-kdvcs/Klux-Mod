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
import org.kdvcs.klux.block.custom.LiquidFilterBlock;
import org.kdvcs.klux.item.ModItems;
import org.kdvcs.klux.networking.ModMessages;
import org.kdvcs.klux.networking.packet.LiquidFilterSyncS2CPacket;
import org.kdvcs.klux.recipe.LiquidFilterHandler;
import org.kdvcs.klux.recipe.LiquidFilterRecipe;
import org.kdvcs.klux.screen.LiquidFilterMenu;
import org.kdvcs.klux.sound.ModSounds;
import org.kdvcs.klux.util.ModTags;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LiquidFilterBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(7) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0 -> stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
                case 1 -> true;
                case 2 -> stack.is(ModTags.Items.EXTRACTION_MESH_BASIC) || stack.is(ModTags.Items.EXTRACTION_MESH_ADVANCED)
                        || stack.is(ModTags.Items.EXTRACTION_MESH_ULTIMATE);
                case 3, 4, 5, 6 -> false;
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    private final FluidTank FLUID_TANK = new FluidTank(64000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if(!level.isClientSide()) {
                ModMessages.sendToClients(new LiquidFilterSyncS2CPacket(this.fluid, worldPosition));
            }
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            if (stack.isEmpty()) return false;

            FluidStack current = getFluid();
            if (current.isEmpty()) {
                return true;
            } else {
                //DON'T MIX DIFFERENT LIQUID!
                return current.getFluid() == stack.getFluid();
            }

        }
    };

    public void setFluid(FluidStack stack) {
        this.FLUID_TANK.setFluid(stack);
    }

    public FluidStack getFluidStack() {
        return this.FLUID_TANK.getFluid();
    }

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(
                    Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemHandler, i -> i >= 3 && i <= 6, (i, s) -> false)),
                    Direction.NORTH, LazyOptional.of(() -> new WrappedHandler(itemHandler, i -> i == 1, (i, s) -> itemHandler.isItemValid(1, s))),
                    Direction.SOUTH, LazyOptional.of(() -> new WrappedHandler(itemHandler, i -> i >= 3 && i <= 6, (i, s) -> false)),
                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemHandler, i -> i == 1, (i, s) -> itemHandler.isItemValid(1, s))),
                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemHandler, i -> i == 0 || i == 1, (i, s) -> itemHandler.isItemValid(i, s)))
            );

    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;

    //  INITIALIZE THE SOUND PLAYER
    private long lastSoundTime = -1;

    public LiquidFilterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LIQUID_FILTER_BE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> LiquidFilterBlockEntity.this.progress;
                    case 1 -> LiquidFilterBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> LiquidFilterBlockEntity.this.progress = value;
                    case 1 -> LiquidFilterBlockEntity.this.maxProgress = value;
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
        return Component.translatable("block.klux.liquid_filter");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        ModMessages.sendToClients(new LiquidFilterSyncS2CPacket(this.getFluidStack(), worldPosition));
        return new LiquidFilterMenu(id, inventory, this, this.data);
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
        nbt.putInt("liquid_filter.progress", this.progress);
        nbt.putInt("liquid_filter.max_progress", this.maxProgress);
        nbt = FLUID_TANK.writeToNBT(nbt);

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("liquid_filter.progress");
        maxProgress = nbt.contains("liquid_filter.max_progress") ? nbt.getInt("liquid_filter.max_progress") : 78;
        FLUID_TANK.readFromNBT(nbt);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, LiquidFilterBlockEntity pEntity) {             //TODO
        if (level.isClientSide()) return;

        boolean wasWorking = state.getValue(LiquidFilterBlock.WORKING);
        boolean isWorkingNow = false;

        SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());
        for (int i = 0; i < pEntity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, pEntity.itemHandler.getStackInSlot(i).copy());
        }

        Optional<LiquidFilterRecipe> recipeOpt = level.getRecipeManager()
                .getAllRecipesFor(LiquidFilterRecipe.Type.INSTANCE).stream()
                .filter(recipe -> recipe.matches(inventory, level, pEntity.getFluidStack()))
                .findFirst();

        if (recipeOpt.isPresent()) {
            LiquidFilterRecipe recipe = recipeOpt.get();
            List<ItemStack> outputs = LiquidFilterHandler.getFilteredOutputs(recipe, inventory.getItem(2));

            boolean canWork = canInsertAllOutputs(pEntity.itemHandler, outputs);

            boolean outputFull = isOutputSlotsFull(pEntity);

            if (canWork && !outputFull) {
                isWorkingNow = true;

                if (pEntity.progress == 0) {
                    pEntity.maxProgress = recipe.getMaxProgress();
                }

                pEntity.progress++;

                if (level instanceof ServerLevel serverLevel) {
                    long gameTime = level.getGameTime();
                    if (pEntity.lastSoundTime == -1 || gameTime - pEntity.lastSoundTime >= 22) {
                        serverLevel.playSound(
                                null,
                                pos,
                                ModSounds.LIQUID_FILTER_WORKING.get(),
                                SoundSource.BLOCKS,
                                0.8f,
                                1.0f
                        );
                        pEntity.lastSoundTime = gameTime;
                    }
                }

                if (pEntity.progress >= pEntity.maxProgress) {
                    craftItem(pEntity);
                    pEntity.resetProgress();
                }
            } else {
                pEntity.resetProgress();
                isWorkingNow = false;
            }
        } else {
            pEntity.resetProgress();
            isWorkingNow = false;
        }

        if (hasFluidItemInSourceSlot(pEntity)) {
            transferItemFluidToFluidTank(pEntity);
        }

        if (wasWorking != isWorkingNow) {
            level.setBlock(pos, state.setValue(LiquidFilterBlock.WORKING, isWorkingNow), 3);
            setChanged(level, pos, state);
        } else {

            setChanged(level, pos, state);
        }
    }

    private static boolean isOutputSlotsFull(LiquidFilterBlockEntity pEntity) {
        for (int slot = 3; slot <= 6; slot++) {
            ItemStack stack = pEntity.itemHandler.getStackInSlot(slot);
            if (stack.isEmpty() || stack.getCount() < stack.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }


    private static void transferItemFluidToFluidTank(LiquidFilterBlockEntity pEntity) {
        pEntity.itemHandler.getStackInSlot(0).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {
            int drainAmount = Math.min(pEntity.FLUID_TANK.getSpace(), 1000);

            FluidStack stack = handler.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
            if(pEntity.FLUID_TANK.isFluidValid(stack)) {
                stack = handler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
                fillTankWithFluid(pEntity, stack, handler.getContainer());
            }

        });
    }

    private static void fillTankWithFluid(LiquidFilterBlockEntity pEntity, FluidStack stack, ItemStack container) {
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

    private static boolean hasFluidItemInSourceSlot(LiquidFilterBlockEntity pEntity) {
        return pEntity.itemHandler.getStackInSlot(0).getCount() > 0;
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static void craftItem(LiquidFilterBlockEntity pEntity) {
        Level level = pEntity.level;
        SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());
        for (int i = 0; i < pEntity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, pEntity.itemHandler.getStackInSlot(i).copy());
        }

        Optional<LiquidFilterRecipe> recipeOpt = level.getRecipeManager()
                .getAllRecipesFor(LiquidFilterRecipe.Type.INSTANCE).stream()
                .filter(recipe -> recipe.matches(inventory, level, pEntity.getFluidStack()))
                .findFirst();

        if (recipeOpt.isPresent()) {
            LiquidFilterRecipe recipe = recipeOpt.get();

            ItemStack mesh = pEntity.itemHandler.getStackInSlot(2);
            if (mesh.isEmpty() && !recipe.getMesh().isEmpty()) {
                return;
            }
            List<ItemStack> outputs = LiquidFilterHandler.getFilteredOutputs(recipe, mesh);

            pEntity.FLUID_TANK.drain(recipe.getFluid().getAmount(), IFluidHandler.FluidAction.EXECUTE);
            pEntity.itemHandler.extractItem(1, recipe.ingredient.count, false);

            for (ItemStack output : outputs) {
                int remaining = output.getCount();

                for (int slot = 3; slot <= 6 && remaining > 0; slot++) {
                    ItemStack current = pEntity.itemHandler.getStackInSlot(slot);

                    if (current.isEmpty()) {
                        int toInsert = Math.min(remaining, output.getMaxStackSize());
                        ItemStack insertStack = output.copy();
                        insertStack.setCount(toInsert);
                        pEntity.itemHandler.setStackInSlot(slot, insertStack);
                        remaining -= toInsert;
                    } else if (ItemStack.isSameItemSameTags(current, output)) {
                        int freeSpace = current.getMaxStackSize() - current.getCount();
                        if (freeSpace > 0) {
                            int toInsert = Math.min(remaining, freeSpace);
                            current.grow(toInsert);
                            pEntity.itemHandler.setStackInSlot(slot, current);
                            remaining -= toInsert;
                        }
                    }
                }

                if (remaining > 0) {
                    System.err.println("craftItem: 空间不足，放不下全部产物！");
                }
            }
        }
    }

    private static boolean hasRecipe(LiquidFilterBlockEntity entity) {
        Level level = entity.level;
        if (level == null) return false;

        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        FluidStack fluid = entity.getFluidStack();
        ItemStack mesh = entity.itemHandler.getStackInSlot(2);

        Optional<LiquidFilterRecipe> recipeOpt = level.getRecipeManager()
                .getAllRecipesFor(LiquidFilterRecipe.Type.INSTANCE).stream()
                .filter(recipe -> recipe.matches(inventory, level, fluid))
                .findFirst();

        if (recipeOpt.isEmpty()) return false;

        LiquidFilterRecipe recipe = recipeOpt.get();

        if (!recipe.getMesh().isEmpty() &&
                !(mesh.is(ModTags.Items.EXTRACTION_MESH_BASIC) ||
                        mesh.is(ModTags.Items.EXTRACTION_MESH_ADVANCED) ||
                        mesh.is(ModTags.Items.EXTRACTION_MESH_ULTIMATE))) {
            return false;
        }

        List<ItemStack> outputs = LiquidFilterHandler.getFilteredOutputs(recipe, mesh);

        for (ItemStack output : outputs) {
            boolean inserted = false;
            for (int slot = 3; slot <= 6; slot++) {
                ItemStack current = inventory.getItem(slot);
                if (current.isEmpty() ||
                        (ItemStack.isSameItemSameTags(current, output) &&
                                current.getCount() + output.getCount() <= current.getMaxStackSize())) {
                    inserted = true;
                    break;
                }
            }
            if (!inserted) return false;
        }

        return true;
    }

    private static boolean hasCorrectFluidAmountInTank(LiquidFilterBlockEntity entity, Optional<LiquidFilterRecipe> recipe) {
        return entity.FLUID_TANK.getFluidAmount() >= recipe.get().getFluid().getAmount();
    }

    private static boolean hasCorrectFluidInTank(LiquidFilterBlockEntity entity, Optional<LiquidFilterRecipe> recipe) {
        return recipe.get().getFluid().equals(entity.FLUID_TANK.getFluid());
    }

    private static boolean canInsertAllOutputs(ItemStackHandler itemHandler, List<ItemStack> outputs) {
        for (ItemStack output : outputs) {
            int remaining = output.getCount();

            for (int slot = 3; slot <= 6 && remaining > 0; slot++) {
                ItemStack current = itemHandler.getStackInSlot(slot);

                if (current.isEmpty()) {
                    int maxInsert = output.getMaxStackSize();
                    remaining -= maxInsert;
                } else if (ItemStack.isSameItemSameTags(current, output)) {
                    int freeSpace = current.getMaxStackSize() - current.getCount();
                    remaining -= freeSpace;
                }
            }

            if (remaining > 0) return false;
        }
        return true;
    }


}