package org.kdvcs.klux.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AdhesivePaste extends Item {

    public AdhesivePaste(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return new ItemStack(Items.GLASS_BOTTLE);
    }

    @Override
    public boolean hasCraftingRemainingItem() {
        return true;
    }
}
