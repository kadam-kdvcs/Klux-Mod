package org.kdvcs.klux.screen;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import org.kdvcs.klux.block.ModBlocks;
import net.minecraftforge.fluids.FluidStack;
import org.kdvcs.klux.block.entity.LiquidReactorBlockEntity;

public class LiquidReactorMenu extends AbstractContainerMenu {
    public final LiquidReactorBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    private FluidStack inputFluid1 = FluidStack.EMPTY;
    private FluidStack inputFluid2 = FluidStack.EMPTY;
    private FluidStack outputFluid = FluidStack.EMPTY;

    public LiquidReactorMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    public LiquidReactorMenu(int id, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.LIQUID_REACTOR_MENU.get(), id);
        checkContainerSize(inv, 3);
        blockEntity = (LiquidReactorBlockEntity) entity;
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, 0, 16, 16));
            this.addSlot(new SlotItemHandler(handler, 1, 144, 16));
            this.addSlot(new SlotItemHandler(handler, 2, 80, 16));
        });

        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public FluidStack getInputFluid1() {
        return inputFluid1.isEmpty() ? blockEntity.getInputFluidStack1() : inputFluid1;
    }

    public FluidStack getInputFluid2() {
        return inputFluid2.isEmpty() ? blockEntity.getInputFluidStack2() : inputFluid2;
    }

    public FluidStack getOutputFluid() {
        return outputFluid.isEmpty() ? blockEntity.getOutputFluidStack() : outputFluid;
    }

    public LiquidReactorBlockEntity getBlockEntity() {
        return this.blockEntity;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);  // Max Progress
        int progressArrowSize = 9;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    private static final int TE_INVENTORY_SLOT_COUNT = 3;

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, ModBlocks.LIQUID_REACTOR.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 88 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 146));
        }
    }

    // 这里的三个 set 方法用来接收网络同步的流体数据
    public void setInputFluid1(FluidStack fluid) {
        this.inputFluid1 = fluid;
        this.blockEntity.setFluids(fluid, this.inputFluid2, this.outputFluid);
    }

    public void setInputFluid2(FluidStack fluid) {
        this.inputFluid2 = fluid;
        this.blockEntity.setFluids(this.inputFluid1, fluid, this.outputFluid);
    }

    public void setOutputFluid(FluidStack fluid) {
        this.outputFluid = fluid;
        this.blockEntity.setFluids(this.inputFluid1, this.inputFluid2, fluid);
    }
}
