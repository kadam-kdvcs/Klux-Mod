package org.kdvcs.klux.compat.jei;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KadamJeiUtil {

    /**
     *
     *
     * Expands all matching ItemStacks from the given Ingredient and sets their count.
     * @param ingredient The Ingredient object, which may be a tag or a specific item.
     * @param count The quantity to set for each matching ItemStack.
     * @return A list of ItemStack copies with the specified count set.
     *
     * @Kadam
     *
     *
     */

    public static List<ItemStack> expandWithCount(Ingredient ingredient, int count) {
        return Arrays.stream(ingredient.getItems())
                .map(stack -> {
                    ItemStack copy = stack.copy();
                    copy.setCount(count);
                    return copy;
                })
                .collect(Collectors.toList());

    }
}
