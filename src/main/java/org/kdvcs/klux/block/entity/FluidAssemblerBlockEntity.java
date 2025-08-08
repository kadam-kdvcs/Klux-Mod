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
import org.kdvcs.klux.block.custom.ExtractorBlock;
import org.kdvcs.klux.block.custom.FluidAssemblerBlock;
import org.kdvcs.klux.item.ModItems;
import org.kdvcs.klux.networking.ModMessages;
import org.kdvcs.klux.networking.packet.FluidAssemblerSyncS2CPacket;
import org.kdvcs.klux.recipe.FluidAssemblerRecipe;
import org.kdvcs.klux.screen.FluidAssemblerMenu;
import org.kdvcs.klux.sound.ModSounds;

import java.util.Map;
import java.util.Optional;

public class FluidAssemblerBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0 -> stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
                case 1 -> true;
                case 2 -> false;
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    private final FluidTank FLUID_TANK = new FluidTank(64000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if(!level.isClientSide()) {
                ModMessages.sendToClients(new FluidAssemblerSyncS2CPacket(this.fluid, worldPosition));
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

    //IN THIS CASE, EAST STANDS FOR LEFT SIDE, WEST STANDS FOR RIGHT SIDE.
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(
                    Direction.UP, LazyOptional.of(() -> new WrappedHandler(itemHandler,
                            (i) -> i == 1,
                            (index, stack) -> itemHandler.isItemValid(1, stack))),

                    Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemHandler,
                            (i) -> i == 2,
                            (index, stack) -> false)),

//                    Direction.NORTH, LazyOptional.of(() -> new WrappedHandler(itemHandler,
//                            (index) -> index == 1,
//                            (index, stack) -> itemHandler.isItemValid(1, stack))),

                    Direction.SOUTH, LazyOptional.of(() -> new WrappedHandler(itemHandler,
                            (i) -> i == 2,
                            (i, s) -> false)),

                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemHandler,
                            (i) -> i == 1,
                            (index, stack) -> itemHandler.isItemValid(1, stack))),

                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemHandler,
                            (index) -> index == 2,
                            (index, stack) -> false)
                    ));

    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;

    //  INITIALIZE THE SOUND PLAYER
    private long lastSoundTime = -1;

    public FluidAssemblerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FLUID_ASSEMBLER_BE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> FluidAssemblerBlockEntity.this.progress;
                    case 1 -> FluidAssemblerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> FluidAssemblerBlockEntity.this.progress = value;
                    case 1 -> FluidAssemblerBlockEntity.this.maxProgress = value;
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
        return Component.translatable("block.klux.fluid_assembler");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        ModMessages.sendToClients(new FluidAssemblerSyncS2CPacket(this.getFluidStack(), worldPosition));
        return new FluidAssemblerMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {

            //MAIN LOGIC FOR DIRECTIONS OF THE MACHINE
            if (side == null) {
                return lazyItemHandler.cast();
            }

            Direction localDir = this.getBlockState().getValue(FluidAssemblerBlock.FACING);
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

            LazyOptional<WrappedHandler> handler = directionWrappedHandlerMap.get(actualDirection);
            if (handler != null) {
                return handler.cast();
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
        nbt.putInt("fluid_assembler.progress", this.progress);
        nbt.putInt("fluid_assembler.max_progress", this.maxProgress);
        nbt = FLUID_TANK.writeToNBT(nbt);

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("fluid_assembler.progress");
        maxProgress = nbt.contains("fluid_assembler.max_progress") ? nbt.getInt("fluid_assembler.max_progress") : 78;
        FLUID_TANK.readFromNBT(nbt);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FluidAssemblerBlockEntity pEntity) {

        boolean wasWorking = state.getValue(ExtractorBlock.WORKING);

        if(level.isClientSide()) {
            return;
        }

        SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());
        for (int i = 0; i < pEntity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, pEntity.itemHandler.getStackInSlot(i));
        }

        Optional<FluidAssemblerRecipe> recipeOpt = level.getRecipeManager()
                .getAllRecipesFor(FluidAssemblerRecipe.Type.INSTANCE).stream()
                .filter(r -> r.matches(inventory, pEntity.getFluidStack(), level))
                .findFirst();

        if(recipeOpt.isPresent() && hasRecipe(pEntity)) {

            //  PLAY SOUNDS
            if (level instanceof ServerLevel serverLevel) {
                long gameTime = level.getGameTime();
                if (pEntity.lastSoundTime == -1 || gameTime - pEntity.lastSoundTime >= 46) {
                    serverLevel.playSound(
                            null,
                            pos,
                            ModSounds.FLUID_ASSEMBLER_WORKING.get(),
                            SoundSource.BLOCKS,
                            0.8f,
                            1.0f
                    );
                    pEntity.lastSoundTime = gameTime;
                }
            }

            FluidAssemblerRecipe recipe = recipeOpt.get();
            pEntity.maxProgress = recipe.getMaxProgress();
            pEntity.progress++;

            setChanged(level, pos, state);

            if(pEntity.progress >= pEntity.maxProgress) {
                craftItem(pEntity);
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
            level.setBlock(pos, state.setValue(FluidAssemblerBlock.WORKING, isWorkingNow), 3);
        }
    }

    private static void transferItemFluidToFluidTank(FluidAssemblerBlockEntity pEntity) {
        pEntity.itemHandler.getStackInSlot(0).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {
            int drainAmount = Math.min(pEntity.FLUID_TANK.getSpace(), 1000);

            FluidStack stack = handler.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
            if(pEntity.FLUID_TANK.isFluidValid(stack)) {
                stack = handler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
                fillTankWithFluid(pEntity, stack, handler.getContainer());
            }

        });
    }

    private static void fillTankWithFluid(FluidAssemblerBlockEntity pEntity, FluidStack stack, ItemStack container) {
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

    private static boolean hasFluidItemInSourceSlot(FluidAssemblerBlockEntity pEntity) {
        return pEntity.itemHandler.getStackInSlot(0).getCount() > 0;
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static void craftItem(FluidAssemblerBlockEntity pEntity) {
        Level level = pEntity.level;
        SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());
        for (int i = 0; i < pEntity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, pEntity.itemHandler.getStackInSlot(i));
        }

        Optional<FluidAssemblerRecipe> recipeOpt = level.getRecipeManager()
                .getAllRecipesFor(FluidAssemblerRecipe.Type.INSTANCE).stream()
                .filter(r -> r.matches(inventory, pEntity.getFluidStack(), level))
                .findFirst();

        if (recipeOpt.isPresent()) {
            FluidAssemblerRecipe recipe = recipeOpt.get();

            pEntity.FLUID_TANK.drain(recipe.getFluid().getAmount(), IFluidHandler.FluidAction.EXECUTE);
            pEntity.itemHandler.extractItem(1, recipe.ingredient.count, false);

            ItemStack output = recipe.getResultItem(null);
            ItemStack currentOutput = pEntity.itemHandler.getStackInSlot(2);

            if (currentOutput.isEmpty()) {
                pEntity.itemHandler.setStackInSlot(2, output.copy());
            } else if (currentOutput.getItem() == output.getItem()) {
                currentOutput.grow(output.getCount());
                pEntity.itemHandler.setStackInSlot(2, currentOutput);
            }

            pEntity.resetProgress();
        }
    }


    private static boolean hasRecipe(FluidAssemblerBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        Optional<FluidAssemblerRecipe> recipe = level.getRecipeManager()
                .getAllRecipesFor(FluidAssemblerRecipe.Type.INSTANCE).stream()
                .filter(r -> r.matches(inventory, entity.getFluidStack(), level))
                .findFirst();

        return recipe.isPresent()
                && canInsertAmountIntoOutputSlot(inventory)
                && canInsertItemIntoOutputSlot(inventory, recipe.get().getResultItem(null));
    }


    private static boolean hasCorrectFluidAmountInTank(FluidAssemblerBlockEntity entity, Optional<FluidAssemblerRecipe> recipe) {
        return entity.FLUID_TANK.getFluidAmount() >= recipe.get().getFluid().getAmount();
    }

    private static boolean hasCorrectFluidInTank(FluidAssemblerBlockEntity entity, Optional<FluidAssemblerRecipe> recipe) {
        return recipe.get().getFluid().equals(entity.FLUID_TANK.getFluid());
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack stack) {
        return inventory.getItem(2).getItem() == stack.getItem() || inventory.getItem(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(2).getMaxStackSize() > inventory.getItem(2).getCount();
    }
}
