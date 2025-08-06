package org.kdvcs.klux.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class WeightedOutput {
    public final ItemStack stack;
    public final float chance;

    public WeightedOutput(ItemStack stack, float chance) {
        this.stack = stack;
        this.chance = chance;
    }

    public static WeightedOutput fromJson(JsonObject json) {
        ItemStack stack = ShapedRecipe.itemStackFromJson(json);
        float chance = GsonHelper.getAsFloat(json, "chance", 1.0f);
        return new WeightedOutput(stack, chance);
    }

    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeItem(stack);
        buf.writeFloat(chance);
    }

    public static WeightedOutput fromNetwork(FriendlyByteBuf buf) {
        ItemStack stack = buf.readItem();
        float chance = buf.readFloat();
        return new WeightedOutput(stack, chance);
    }
}
