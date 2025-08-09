package org.kdvcs.klux.recipe;

import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import org.kdvcs.klux.item.ModItems;
import org.kdvcs.klux.potion.ModPotions;
import org.kdvcs.klux.util.ModBrewingRecipe;
import org.kdvcs.klux.util.ModBrewingRecipeRegistry;

public class ModBrewingRecipes {

    private static void add(Potion input, Item ingredient, Potion output) {
        ModBrewingRecipe recipe = new ModBrewingRecipe(input, ingredient, output);
        BrewingRecipeRegistry.addRecipe(recipe);
        ModBrewingRecipeRegistry.track(recipe);
    }

    public static void register() {

        //TO AWKWARD
        add(Potions.AWKWARD, ModItems.AROMATIC_POWDER.get(), Potions.WATER_BREATHING);
        add(Potions.AWKWARD, ModItems.HERBAL_ACTIVE_POWDER.get(), Potions.HEALING);
        add(Potions.AWKWARD, ModItems.WOODY_ESSENCE_POWDER.get(), Potions.STRENGTH);
        add(Potions.AWKWARD, ModItems.WILDFLOWER_POLLEN_POWDER.get(), Potions.SWIFTNESS);
        add(Potions.AWKWARD, ModItems.ACTIVATION_POWDER.get(), ModPotions.BASE_POTION.get());

        //TO OTHER POTIONS
        add(Potions.HEALING, ModItems.AROMATIC_POWDER.get(), Potions.REGENERATION);
        add(Potions.POISON, ModItems.AROMATIC_POWDER.get(), Potions.WEAKNESS);
        add(Potions.STRENGTH, ModItems.AROMATIC_POWDER.get(), Potions.FIRE_RESISTANCE);

        add(Potions.NIGHT_VISION, ModItems.HERBAL_ACTIVE_POWDER.get(), Potions.INVISIBILITY);
        add(Potions.WATER_BREATHING, ModItems.HERBAL_ACTIVE_POWDER.get(), Potions.NIGHT_VISION);
        add(Potions.WEAKNESS, ModItems.HERBAL_ACTIVE_POWDER.get(), Potions.STRENGTH);

        add(Potions.REGENERATION, ModItems.WOODY_ESSENCE_POWDER.get(), Potions.WATER_BREATHING);
        add(Potions.INVISIBILITY, ModItems.WOODY_ESSENCE_POWDER.get(), Potions.NIGHT_VISION);
        add(Potions.STRENGTH, ModItems.WOODY_ESSENCE_POWDER.get(), Potions.LEAPING);

        add(Potions.HEALING, ModItems.WILDFLOWER_POLLEN_POWDER.get(), Potions.HARMING);
        add(Potions.INVISIBILITY, ModItems.WILDFLOWER_POLLEN_POWDER.get(), Potions.SWIFTNESS);
        add(Potions.LEAPING, ModItems.WILDFLOWER_POLLEN_POWDER.get(), Potions.WATER_BREATHING);

        //TO MOD POTIONS


    }

}
