package org.kdvcs.klux.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
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
import net.minecraft.world.item.ItemStack;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.block.ModBlocks;

public class DehydratorFuelCategory implements IRecipeCategory<FuelRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Klux.MODID, "dehydrator_fuel");
    public static final RecipeType<FuelRecipe> TYPE = new RecipeType<>(UID, FuelRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public DehydratorFuelCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(150, 18);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.DEHYDRATOR.get()));
    }

    @Override
    public RecipeType<FuelRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("klux.dehydrator_fuel");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FuelRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 4, 1).addItemStack(recipe.fuelItem);
    }

    @Override
    public void draw(FuelRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;

        int ticks = recipe.getBurnTime();
        float seconds = ticks / 20f;

        Component timeText = Component.translatable("jei.klux.dehydrator.fuel_time", String.format("%.2f", seconds));

        float x = 60f;
        float y = 5f;

        guiGraphics.pose().pushPose();
        font.drawInBatch(
                timeText,
                x,
                y,
                0x404040,
                false,
                guiGraphics.pose().last().pose(),
                mc.renderBuffers().bufferSource(),
                Font.DisplayMode.NORMAL,
                0,
                15728880
        );
        guiGraphics.pose().popPose();

    }
}
