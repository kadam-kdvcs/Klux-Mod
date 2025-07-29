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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import org.kdvcs.klux.block.custom.DehydratorBlock;
import org.kdvcs.klux.recipe.DehydratorRecipe;
import org.kdvcs.klux.screen.DehydratorMenu;
import org.kdvcs.klux.sound.ModSounds;

import java.util.Optional;
import java.util.Set;

public class DehydratorBlockEntity extends BlockEntity implements MenuProvider {
        private final ItemStackHandler itemHandler = new ItemStackHandler(3);

        private static final int INPUT_SLOT = 0;
        private static final int FUEL_SLOT = 1;
        private static final int OUTPUT_SLOT = 2;

        private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

        protected final ContainerData data;
        private int progress = 0;

        //  HERE USES A FUEL
        private int fuelTime = 0;
        private int maxFuelTime = 0;

        //  INITIALIZE THE SOUND PLAYER
        private long lastSoundTime = -1;

        public DehydratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
            super(ModBlockEntities.DEHYDRATOR_BE.get(), pPos, pBlockState);
            this.data = new ContainerData() {
                @Override
                public int get(int pIndex) {
                    return switch (pIndex) {
                        case 0 -> DehydratorBlockEntity.this.progress;
                        case 1 -> getCurrentRecipe().map(DehydratorRecipe::getMaxProgress).orElse(78);
                        case 2 -> DehydratorBlockEntity.this.fuelTime;
                        case 3 -> DehydratorBlockEntity.this.maxFuelTime;
                        default -> 0;
                    };
                }

                @Override
                public void set(int pIndex, int pValue) {
                    switch (pIndex) {
                        case 0 -> DehydratorBlockEntity.this.progress = pValue;
                        case 2 -> DehydratorBlockEntity.this.fuelTime = pValue;
                        case 3 -> DehydratorBlockEntity.this.maxFuelTime = pValue;
                    }
                }

                @Override
                public int getCount() {
                    return 4;
                }
            };
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            if(cap == ForgeCapabilities.ITEM_HANDLER) {
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
        public void invalidateCaps() {
            super.invalidateCaps();
            lazyItemHandler.invalidate();
        }

        public void drops() {
            SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
            for(int i = 0; i < itemHandler.getSlots(); i++) {
                inventory.setItem(i, itemHandler.getStackInSlot(i));
            }
            Containers.dropContents(this.level, this.worldPosition, inventory);
        }

        @Override
        public Component getDisplayName() {
            return Component.translatable("block.klux.dehydrator");
        }

        @Nullable
        @Override
        public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
            return new DehydratorMenu(pContainerId, pPlayerInventory, this, this.data);
        }

        @Override
        protected void saveAdditional(CompoundTag pTag) {
            pTag.put("inventory", itemHandler.serializeNBT());
            pTag.putInt("dehydrator.progress", progress);
            pTag.putInt("dehydrator.fuelTime", fuelTime);
            pTag.putInt("dehydrator.maxFuelTime", maxFuelTime);
            super.saveAdditional(pTag);
        }

        @Override
        public void load(CompoundTag pTag) {
            super.load(pTag);
            itemHandler.deserializeNBT(pTag.getCompound("inventory"));
            progress = pTag.getInt("dehydrator.progress");
            fuelTime = pTag.getInt("dehydrator.fuelTime");
            maxFuelTime = pTag.getInt("dehydrator.maxFuelTime");
        }

    public void tick(Level level, BlockPos pos, BlockState state) {
        boolean hasRecipe = hasRecipe();
        boolean wasWorking = state.getValue(DehydratorBlock.WORKING);

        boolean hasFuel = fuelTime > 0;
        if (!hasFuel && hasRecipe) {
            hasFuel = tryConsumeFuel();
        }

        if (hasFuel && hasRecipe) {
            fuelTime--;
        }

        if (hasRecipe && hasFuel) {
            increaseCraftingProgress();

            if (level instanceof ServerLevel serverLevel) {
                long gameTime = level.getGameTime();
                if (lastSoundTime == -1 || gameTime - lastSoundTime >= 66) {
                    serverLevel.playSound(
                            null,
                            pos,
                            ModSounds.DEHYDRATOR_WORKING.get(),
                            SoundSource.BLOCKS,
                            1.0f,
                            1.0f
                    );
                    lastSoundTime = gameTime;
                }
            }

            if (hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }

        boolean work = hasRecipe && hasFuel;
        if (wasWorking != work) {
            level.setBlock(pos, state.setValue(DehydratorBlock.WORKING, work), 3);
        }

        setChanged(level, pos, state);
    }

    //  FUEL LIST
    public static final Set<Item> ALLOWED_FUELS = Set.of(
            Items.COAL
    );

    private boolean tryConsumeFuel() {
        ItemStack fuelStack = this.itemHandler.getStackInSlot(FUEL_SLOT);
        if (fuelStack.isEmpty()) return false;

        if (!ALLOWED_FUELS.contains(fuelStack.getItem())) {
            return false;
        }

        int burnTime = getFuelBurnTime(fuelStack.getItem());
        if (burnTime <= 0) return false;

        this.fuelTime = burnTime;
        this.maxFuelTime = burnTime;

        fuelStack.shrink(1);
        return true;

    }

    private int getFuelBurnTime(Item item) {
        if (item == Items.COAL) return 400;
        return 0;
    }


    private void resetProgress() {
            progress = 0;
        }

        //  HERE USES A CUSTOM RECIPE TYPE
        private void craftItem() {

            Optional<DehydratorRecipe> recipe = getCurrentRecipe();
            if (recipe.isEmpty()) return;

            ItemStack result = recipe.get().getResultItem(null);
            int requiredCount = recipe.get().ingredientCount;

            this.itemHandler.extractItem(INPUT_SLOT, requiredCount, false);

            this.itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(result.getItem(),
                    this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
        }

        private boolean hasRecipe() {
            Optional<DehydratorRecipe> recipe = getCurrentRecipe();

            if(recipe.isEmpty()) {
                return false;
            }
            ItemStack result = recipe.get().getResultItem(null);
            int requiredCount = recipe.get().ingredientCount;
            ItemStack inputStack = itemHandler.getStackInSlot(INPUT_SLOT);

            return inputStack.getCount() >= requiredCount && canInsertAmountIntoOutputSlot(result.getCount()) && canInsertItemIntoOutputSlot(result.getItem());
        }

    private Optional<DehydratorRecipe> getCurrentRecipe() {
            SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                inventory.setItem(i, this.itemHandler.getStackInSlot(i));
            }

            return this.level.getRecipeManager().getRecipeFor(DehydratorRecipe.Type.INSTANCE, inventory, level);
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
            return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
        }

        private boolean canInsertAmountIntoOutputSlot(int count) {
            return this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count <= this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
        }

        private boolean hasProgressFinished() {
            Optional<DehydratorRecipe> recipe = getCurrentRecipe();
            return recipe.map(r -> progress >= r.getMaxProgress()).orElse(false);
        }

        private void increaseCraftingProgress() {
            Optional<DehydratorRecipe> recipe = getCurrentRecipe();
            if (recipe.isPresent()) {
                progress = Math.min(progress + 1, recipe.get().getMaxProgress());
            }
        }
}
