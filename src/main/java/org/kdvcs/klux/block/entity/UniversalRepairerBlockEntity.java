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
import net.minecraft.world.item.crafting.RecipeManager;
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
import org.kdvcs.klux.block.custom.FluidAssemblerBlock;
import org.kdvcs.klux.block.custom.UniversalRepairerBlock;
import org.kdvcs.klux.networking.ModMessages;
import org.kdvcs.klux.networking.packet.UniversalRepairerSyncS2CPacket;
import org.kdvcs.klux.recipe.UniversalRepairerRecipe;
import org.kdvcs.klux.screen.UniversalRepairerMenu;

import java.util.Map;

public class UniversalRepairerBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0 -> stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
                case 1 -> stack.isDamageableItem();
                default -> false;
            };
        }
    };

    private final FluidTank FLUID_TANK = new FluidTank(64000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (!level.isClientSide()) {
                ModMessages.sendToClients(new UniversalRepairerSyncS2CPacket(this.fluid, worldPosition));
            }
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return !stack.isEmpty();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(
                    Direction.UP, LazyOptional.of(() -> new WrappedHandler(itemHandler,
                            (i) -> i == 1,                                                          //EXTRACT
                            (index, stack) -> itemHandler.isItemValid(1, stack))),             //IMPORT

                    Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemHandler,
                            (i) -> i == 1,
                            (index, stack) -> itemHandler.isItemValid(1, stack))),

//                    Direction.NORTH, LazyOptional.of(() -> new WrappedHandler(itemHandler,
//                            (index) -> index == 1,
//                            (index, stack) -> itemHandler.isItemValid(1, stack))),

                    Direction.SOUTH, LazyOptional.of(() -> new WrappedHandler(itemHandler,
                            (i) -> i == 1,
                            (i, s) -> itemHandler.isItemValid(1, s))),

                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemHandler,
                            (i) -> i == 1,
                            (index, stack) -> itemHandler.isItemValid(1, stack))),

                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemHandler,
                            (index) -> index == 1,
                            (index, stack) -> false)
                    ));

    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 100;

    private long lastSoundTime = -1;

    public UniversalRepairerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.UNIVERSAL_REPAIRER_BE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> UniversalRepairerBlockEntity.this.progress;
                    case 1 -> UniversalRepairerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> UniversalRepairerBlockEntity.this.progress = value;
                    case 1 -> UniversalRepairerBlockEntity.this.maxProgress = value;
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
        return Component.translatable("block.klux.universal_repairer");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        ModMessages.sendToClients(new UniversalRepairerSyncS2CPacket(this.getFluidStack(), worldPosition));
        return new UniversalRepairerMenu(id, inventory, this, this.data);
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
        nbt.putInt("progress", progress);
        nbt.putInt("max_progress", maxProgress);
        FLUID_TANK.writeToNBT(nbt);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("progress");
        maxProgress = nbt.getInt("max_progress");
        FLUID_TANK.readFromNBT(nbt);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public void setFluid(FluidStack stack) {
        this.FLUID_TANK.setFluid(stack);
    }

    public FluidStack getFluidStack() {
        return this.FLUID_TANK.getFluid();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, UniversalRepairerBlockEntity entity) {
        if (level.isClientSide()) return;

        ItemStack itemToRepair = entity.itemHandler.getStackInSlot(1);
        FluidStack fluidInTank = entity.getFluidStack();

        SimpleContainer container = new SimpleContainer(1);
        container.setItem(0, itemToRepair);

        RecipeManager recipeManager = level.getRecipeManager();
        UniversalRepairerRecipe matchingRecipe = recipeManager
                .getAllRecipesFor(UniversalRepairerRecipe.Type.INSTANCE).stream()
                .filter(recipe -> recipe.matches(container, fluidInTank, level))
                .findFirst()
                .orElse(null);

        boolean isWorking = false;

        if (matchingRecipe != null) {
            entity.maxProgress = matchingRecipe.getMaxProgress();
            entity.progress++;

            if (level instanceof ServerLevel serverLevel) {
                long gameTime = level.getGameTime();
                if (entity.lastSoundTime == -1 || gameTime - entity.lastSoundTime >= 46) {
                    serverLevel.playSound(null, pos, SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
                    entity.lastSoundTime = gameTime;
                }
            }

            if (entity.progress >= entity.maxProgress) {
                int currentDamage = itemToRepair.getDamageValue();
                int repairAmount = matchingRecipe.getRepairAmount();
                itemToRepair.setDamageValue(Math.max(currentDamage - repairAmount, 0));

                entity.FLUID_TANK.drain(matchingRecipe.getRequiredFluid(), IFluidHandler.FluidAction.EXECUTE);

                entity.progress = 0;
                setChanged(level, pos, state);
            }

            isWorking = true;
        } else {
            entity.progress = 0;
        }

        if (hasFluidItemInSourceSlot(entity)) {
            transferItemFluidToFluidTank(entity);
        }

        if (state.getValue(UniversalRepairerBlock.WORKING) != isWorking) {
            level.setBlock(pos, state.setValue(UniversalRepairerBlock.WORKING, isWorking), 3);
        }
    }

    private static boolean hasFluidItemInSourceSlot(UniversalRepairerBlockEntity entity) {
        return !entity.itemHandler.getStackInSlot(0).isEmpty();
    }

    private static void transferItemFluidToFluidTank(UniversalRepairerBlockEntity entity) {
        entity.itemHandler.getStackInSlot(0).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {
            int drainAmount = Math.min(entity.FLUID_TANK.getSpace(), 1000);
            FluidStack stack = handler.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
            if (entity.FLUID_TANK.isFluidValid(stack)) {
                stack = handler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
                int filled = entity.FLUID_TANK.fill(stack, IFluidHandler.FluidAction.EXECUTE);

                ItemStack inputStack = entity.itemHandler.getStackInSlot(0);

                entity.itemHandler.extractItem(0, 1, false);
                entity.itemHandler.insertItem(0, handler.getContainer(), false);
            }
        });
    }
}
