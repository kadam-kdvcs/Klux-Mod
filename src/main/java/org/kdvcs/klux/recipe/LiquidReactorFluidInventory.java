package org.kdvcs.klux.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class LiquidReactorFluidInventory implements Container {
    private final FluidStack input1;
    private final FluidStack input2;

    public LiquidReactorFluidInventory(FluidStack input1, FluidStack input2) {
        this.input1 = input1;
        this.input2 = input2;
    }

    public FluidStack getInput1() {
        return input1;
    }

    public FluidStack getInput2() {
        return input2;
    }

    @Override public int getContainerSize() {
        return 0;
    }

    @Override public boolean isEmpty() {
        return false;
    }

    @Override public ItemStack getItem(int slot) {
        return ItemStack.EMPTY;
    }

    @Override public void setItem(int slot, ItemStack stack) {}

    @Override public ItemStack removeItem(int slot, int amount) {
        return ItemStack.EMPTY;
    }

    @Override public ItemStack removeItemNoUpdate(int slot) {
        return ItemStack.EMPTY;
    }

    @Override public void setChanged() {}

    @Override public boolean stillValid(Player player) {
        return true;
    }

    @Override public void clearContent() {}

}
