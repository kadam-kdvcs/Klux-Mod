package org.kdvcs.klux.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.jetbrains.annotations.Nullable;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.util.ModBrewingRecipe;

public class ModBrewingCategory implements IRecipeCategory<ModBrewingRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(Klux.MODID, "mod_brewing");
    public static final RecipeType<ModBrewingRecipe> TYPE = new RecipeType<>(UID, ModBrewingRecipe.class);

    public static final ResourceLocation TEXTURE = new ResourceLocation(Klux.MODID, "textures/jei/brewing_stand_background.png");

    private final IDrawable background;
    private final IDrawable icon;

    public ModBrewingCategory(IGuiHelper helper) {

        this.background = helper.createDrawable(TEXTURE, 0, 0, 114, 61);

        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.BREWING_STAND));
    }

    @Override
    public RecipeType<ModBrewingRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.klux.mod_brewing");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ModBrewingRecipe recipe, IFocusGroup focuses) {

        builder.addSlot(RecipeIngredientRole.INPUT, 1, 37)
                .addItemStack(PotionUtils.setPotion(new ItemStack(Items.POTION), recipe.input));
        builder.addSlot(RecipeIngredientRole.INPUT, 24, 44)
                .addItemStack(PotionUtils.setPotion(new ItemStack(Items.POTION), recipe.input));
        builder.addSlot(RecipeIngredientRole.INPUT, 47, 37)
                .addItemStack(PotionUtils.setPotion(new ItemStack(Items.POTION), recipe.input));

        builder.addSlot(RecipeIngredientRole.CATALYST, 24, 3)
                .addItemStack(new ItemStack(recipe.ingredient));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 81, 3)
                .addItemStack(PotionUtils.setPotion(new ItemStack(Items.POTION), recipe.output));
    }

}
