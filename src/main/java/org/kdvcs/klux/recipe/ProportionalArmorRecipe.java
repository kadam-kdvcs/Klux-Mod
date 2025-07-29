package org.kdvcs.klux.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * ProportionalArmorRecipe class defines a custom crafting recipe for Minecraft.
 *
 * This recipe allows crafting a new armor item from an input armor, preserving
 * the durability proportionally between input and output items.
 *
 * It implements the CraftingRecipe interface and provides JSON parsing,
 * network serialization, and matching logic for the recipe.
 *
 *
 * @Kadam
 *
 *
 */

public class ProportionalArmorRecipe implements CraftingRecipe {
    private final ResourceLocation id;
    private final int width;
    private final int height;
    private final NonNullList<Ingredient> ingredients; // size = width * height
    private final Item inputArmor;
    private final Item resultArmor;

    public ProportionalArmorRecipe(ResourceLocation id, int width, int height,
                                   NonNullList<Ingredient> ingredients, Item inputArmor, Item resultArmor) {
        super();
        this.id = id;
        this.width = width;
        this.height = height;
        this.ingredients = ingredients;
        this.inputArmor = inputArmor;
        this.resultArmor = resultArmor;
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }


    @Override
    public boolean matches(CraftingContainer inv, Level level) {
        for (int startX = 0; startX <= inv.getWidth() - width; startX++) {
            for (int startY = 0; startY <= inv.getHeight() - height; startY++) {
                if (checkMatch(inv, startX, startY)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkMatch(CraftingContainer inv, int startX, int startY) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Ingredient expected = ingredients.get(y * width + x);
                ItemStack actual = inv.getItem((startX + x) + (startY + y) * inv.getWidth());
                if (!expected.test(actual)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess access) {
        ItemStack inputStack = ItemStack.EMPTY;

        //INPUT ARMOR
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && stack.getItem() == inputArmor) {
                inputStack = stack;
                break;
            }
        }

        ItemStack result = new ItemStack(resultArmor);

        //RATIO OF THE DURABILITY
        if (!inputStack.isEmpty() && inputStack.isDamaged()) {
            int maxDamage = inputStack.getMaxDamage();
            int remaining = maxDamage - inputStack.getDamageValue();
            float ratio = (float) remaining / maxDamage;

            int resultMax = result.getMaxDamage();
            int damage = resultMax - Math.round(ratio * resultMax);
            result.setDamageValue(damage);
        }

        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= this.width && height >= this.height;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return new ItemStack(resultArmor);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.PROPORTIONAL_ARMOR_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    public static class Serializer implements RecipeSerializer<ProportionalArmorRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {}

        @Override
        public ProportionalArmorRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            JsonArray patternJson = GsonHelper.getAsJsonArray(json, "pattern");
            String[] pattern = new String[patternJson.size()];
            for (int i = 0; i < patternJson.size(); i++) {
                pattern[i] = GsonHelper.convertToString(patternJson.get(i), "pattern[" + i + "]");
            }
            int height = pattern.length;
            int width = pattern[0].length();

            JsonObject key = GsonHelper.getAsJsonObject(json, "key");
            Map<String, Ingredient> keyMap = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : key.entrySet()) {
                keyMap.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
            }

            NonNullList<Ingredient> ingredients = NonNullList.withSize(width * height, Ingredient.EMPTY);
            for (int y = 0; y < height; y++) {
                String row = pattern[y];
                for (int x = 0; x < width; x++) {
                    String c = String.valueOf(row.charAt(x));
                    ingredients.set(y * width + x, keyMap.getOrDefault(c, Ingredient.EMPTY));
                }
            }

            JsonObject resultObj = GsonHelper.getAsJsonObject(json, "result");
            Item resultItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(resultObj, "item")));
            if (resultItem == null) throw new JsonSyntaxException("Result item missing or invalid");

            Ingredient inputIngredient = keyMap.get("I");
            if (inputIngredient == null || inputIngredient.getItems().length == 0) {
                throw new JsonSyntaxException("Input armor 'I' not found in key");
            }
            Item inputArmor = inputIngredient.getItems()[0].getItem();

            return new ProportionalArmorRecipe(recipeId, width, height, ingredients, inputArmor, resultItem);
        }

        @Override
        public ProportionalArmorRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int width = buffer.readVarInt();
            int height = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(width * height, Ingredient.EMPTY);
            for (int i = 0; i < ingredients.size(); i++) {
                ingredients.set(i, Ingredient.fromNetwork(buffer));
            }
            Item inputArmor = buffer.readItem().getItem();
            Item resultItem = buffer.readItem().getItem();
            return new ProportionalArmorRecipe(recipeId, width, height, ingredients, inputArmor, resultItem);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ProportionalArmorRecipe recipe) {
            buffer.writeVarInt(recipe.width);
            buffer.writeVarInt(recipe.height);
            for (Ingredient ingredient : recipe.ingredients) {
                ingredient.toNetwork(buffer);
            }
            buffer.writeItem(new ItemStack(recipe.inputArmor));
            buffer.writeItem(new ItemStack(recipe.resultArmor));
        }
    }
}
