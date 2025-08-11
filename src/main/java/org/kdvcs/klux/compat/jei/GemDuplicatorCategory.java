package org.kdvcs.klux.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.block.ModBlocks;
import org.kdvcs.klux.recipe.GemDuplicatorRecipe;


import java.util.List;

public class GemDuplicatorCategory implements IRecipeCategory<GemDuplicatorRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Klux.MODID, "gem_duplicator");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Klux.MODID,
            "textures/jei/gui_gem_duplicator.png");
    public static final ResourceLocation ARROW = new ResourceLocation(Klux.MODID,
            "textures/jei/arrow_6.png");

    public static final RecipeType<GemDuplicatorRecipe> GEM_DUPLICATOR_TYPE =
            new RecipeType<>(UID, GemDuplicatorRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final IDrawableStatic meter;

    //  DRAW PROCESSING TIME
    private GemDuplicatorRecipe currentRecipe;

    public GemDuplicatorCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 95);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.GEM_DUPLICATOR.get()));

        //DRAW PROCESSING ARROW
        IDrawableStatic staticArrow = helper.drawableBuilder(ARROW, 0, 0, 48, 8)
                .setTextureSize(48, 8)
                .build();
        this.arrow = helper.createAnimatedDrawable(staticArrow, 20, IDrawableAnimated.StartDirection.LEFT, false);

        this.meter = helper.drawableBuilder(TEXTURE, 176, 0, 36, 61)
                .setTextureSize(256, 256)
                .build();

    }

    @Override
    public RecipeType<GemDuplicatorRecipe> getRecipeType() {
        return GEM_DUPLICATOR_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.klux.gem_duplicator");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GemDuplicatorRecipe recipe, IFocusGroup focuses) {
        this.currentRecipe = recipe;

        Ingredient gemIngredient = Ingredient.of(ItemTags.create(new ResourceLocation("forge", "gems")));
        builder.addSlot(RecipeIngredientRole.INPUT, 73, 38)
                .addItemStacks(KadamJeiUtil.expandWithCount(gemIngredient, 1));

        //FLUID TANK
        builder.addSlot(RecipeIngredientRole.INPUT, 12, 17)
                .addIngredients(ForgeTypes.FLUID_STACK, List.of(recipe.getFluid()))
                .setFluidRenderer(2000, false, 36, 61);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 148, 38)
                .addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(GemDuplicatorRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {

        arrow.draw(guiGraphics, 95, 42);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 300);
        meter.draw(guiGraphics, 12, 17);
        guiGraphics.pose().popPose();

    }

}
