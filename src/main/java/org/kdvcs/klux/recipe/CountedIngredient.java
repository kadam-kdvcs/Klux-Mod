package org.kdvcs.klux.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;

/**
 *
 *
 * CountedIngredient represents a standard Minecraft Ingredient with an additional count value.
 *
 * This is useful in custom crafting or machine recipes where multiple units of a single ingredient are needed.
 * It supports JSON deserialization (from data pack recipes), and network serialization/deserialization for syncing.
 *
 * Example JSON usage:
 * {
 *   "item": "minecraft:iron_ingot",
 *   "count": 3
 * }
 *
 * This class is used to extend vanilla crafting logic without altering Ingredient's base behavior.
 *
 *
 *
 * @Kadam
 *
 *
 */

public class CountedIngredient {
    public final Ingredient ingredient;
    public final int count;

    public CountedIngredient(Ingredient ingredient, int count) {
        this.ingredient = ingredient;
        this.count = count;
    }

    public static CountedIngredient fromJson(JsonObject json) {

        Ingredient ingredient = Ingredient.fromJson(json);
        int count = GsonHelper.getAsInt(json, "count", 1);
        return new CountedIngredient(ingredient, count);

    }

    public void toNetwork(FriendlyByteBuf buf) {
        ingredient.toNetwork(buf);
        buf.writeVarInt(count);
    }

    public static CountedIngredient fromNetwork(FriendlyByteBuf buf) {
        Ingredient ingredient = Ingredient.fromNetwork(buf);
        int count = buf.readVarInt();
        return new CountedIngredient(ingredient, count);
    }

}
