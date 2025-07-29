package org.kdvcs.klux.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;

public class CountedIngredient {
    public final Ingredient ingredient;
    public final int count;

    public CountedIngredient(Ingredient ingredient, int count) {
        this.ingredient = ingredient;
        this.count = count;
    }

    public static CountedIngredient fromJson(JsonObject json) {
        // 这里传入整个json对象，而不是json.get("ingredients")
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
