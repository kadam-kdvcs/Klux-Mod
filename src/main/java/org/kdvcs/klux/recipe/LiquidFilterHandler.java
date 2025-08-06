package org.kdvcs.klux.recipe;

import net.minecraft.world.item.ItemStack;
import org.kdvcs.klux.util.MeshUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LiquidFilterHandler {
    private static final Random RANDOM = new Random();

    public static List<ItemStack> getFilteredOutputs(LiquidFilterRecipe recipe, ItemStack mesh) {
        List<ItemStack> result = new ArrayList<>();
        float multiplier = MeshUtil.getChanceMultiplier(mesh);

        for (WeightedOutput output : recipe.getResults()) {
            float effectiveChance = output.chance * multiplier;
            if (RANDOM.nextFloat() <= effectiveChance) {
                result.add(output.stack.copy());
            }
        }
        return result;
    }

}
