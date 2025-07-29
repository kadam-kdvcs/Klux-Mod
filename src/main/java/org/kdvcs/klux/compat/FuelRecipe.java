package org.kdvcs.klux.compat;

import net.minecraft.world.item.ItemStack;

public class FuelRecipe {
    public final ItemStack fuelItem;
    public final int burnTime;

    public FuelRecipe(ItemStack fuelItem, int burnTime) {
        this.fuelItem = fuelItem;
        this.burnTime = burnTime;
    }

    public int getBurnTime() {
        return burnTime;
    }
}
