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
import org.kdvcs.klux.block.custom.CompressorBlock;
import org.kdvcs.klux.recipe.CompressorRecipe;
import org.kdvcs.klux.screen.CompressorMenu;
import org.kdvcs.klux.sound.ModSounds;

import java.util.Optional;

public class CompressorBlockEntity extends BlockEntity implements MenuProvider {
        private final ItemStackHandler itemHandler = new ItemStackHandler(3);

        private static final int INPUT_SLOT = 0;
        private static final int SECOND_INPUT_SLOT = 1;
        private static final int OUTPUT_SLOT = 2;

        private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

        protected final ContainerData data;
        private int progress = 0;

        //  INITIALIZE THE SOUND PLAYER
        private long lastSoundTime = -1;

        public CompressorBlockEntity(BlockPos pPos, BlockState pBlockState) {
            super(ModBlockEntities.COMPRESSOR_BE.get(), pPos, pBlockState);
            this.data = new ContainerData() {
                @Override
                public int get(int pIndex) {
                    return switch (pIndex) {
                        case 0 -> CompressorBlockEntity.this.progress;
                        case 1 -> {
                            Optional<CompressorRecipe> recipe = getCurrentRecipe();
                            yield recipe.map(CompressorRecipe::getMaxProgress).orElse(78);
                        }
                        default -> 0;
                    };
                }

                @Override
                public void set(int pIndex, int pValue) {
                    if (pIndex == 0) CompressorBlockEntity.this.progress = pValue;
                }

                @Override
                public int getCount() {
                    return 2;
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
            return Component.translatable("block.klux.compressor");
        }

        @Nullable
        @Override
        public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
            return new CompressorMenu(pContainerId, pPlayerInventory, this, this.data);
        }

        @Override
        protected void saveAdditional(CompoundTag pTag) {
            pTag.put("inventory", itemHandler.serializeNBT());
            pTag.putInt("compressor.progress", progress);

            super.saveAdditional(pTag);
        }

        @Override
        public void load(CompoundTag pTag) {
            super.load(pTag);
            itemHandler.deserializeNBT(pTag.getCompound("inventory"));
            progress = pTag.getInt("compressor.progress");
        }

    public void tick(Level level, BlockPos pos, BlockState state) {
        boolean hasRecipe = hasRecipe();
        boolean wasWorking = state.getValue(CompressorBlock.WORKING);

        if (hasRecipe) {
            increaseCraftingProgress();

            //  PLAY SOUNDS
            if (level instanceof ServerLevel serverLevel) {
                long gameTime = level.getGameTime();
                if (lastSoundTime == -1 || gameTime - lastSoundTime >= 40) {
                    serverLevel.playSound(
                            null,
                            pos,
                            ModSounds.COMPRESSOR_WORKING.get(),
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

        if (wasWorking != hasRecipe) {
            level.setBlock(pos, state.setValue(CompressorBlock.WORKING, hasRecipe), 3);
        }

        setChanged(level, pos, state);
    }

        private void resetProgress() {
            progress = 0;
        }

        //  HERE USES A CUSTOM RECIPE TYPE
        private void craftItem() {
            Optional<CompressorRecipe> recipe = getCurrentRecipe();
            if (recipe.isEmpty()) return;

            CompressorRecipe compRecipe = recipe.get();
            ItemStack result = compRecipe.getResultItem(null);

            this.itemHandler.extractItem(INPUT_SLOT, compRecipe.getInputA().count, false);
            this.itemHandler.extractItem(SECOND_INPUT_SLOT, compRecipe.getInputB().count, false);

            this.itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(result.getItem(),
                    this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
        }

        private boolean hasRecipe() {
            Optional<CompressorRecipe> recipe = getCurrentRecipe();

            if(recipe.isEmpty()) {
                return false;
            }
            ItemStack result = recipe.get().getResultItem(null);

            return canInsertAmountIntoOutputSlot(result.getCount()) && canInsertItemIntoOutputSlot(result.getItem());
        }

    private Optional<CompressorRecipe> getCurrentRecipe() {
            SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                inventory.setItem(i, this.itemHandler.getStackInSlot(i));
            }

            return this.level.getRecipeManager().getRecipeFor(CompressorRecipe.Type.INSTANCE, inventory, level);
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
            return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
        }

        private boolean canInsertAmountIntoOutputSlot(int count) {
            return this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count <= this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
        }

        private boolean hasProgressFinished() {
            Optional<CompressorRecipe> recipe = getCurrentRecipe();
            return recipe.map(r -> progress >= r.getMaxProgress()).orElse(false);
        }

        private void increaseCraftingProgress() {
            Optional<CompressorRecipe> recipe = getCurrentRecipe();
            if (recipe.isPresent()) {
                progress = Math.min(progress + 1, recipe.get().getMaxProgress());
            }
        }
}