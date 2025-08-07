package org.kdvcs.klux.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModBrewingRecipeRegistry {
    private static final List<ModBrewingRecipe> REGISTERED = new ArrayList<>();

    public static void track(ModBrewingRecipe recipe) {
        REGISTERED.add(recipe);
    }

    public static List<ModBrewingRecipe> getAll() {
        return Collections.unmodifiableList(REGISTERED);
    }
}
