package org.kdvcs.klux.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
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
import org.jetbrains.annotations.Nullable;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.block.ModBlocks;
import org.kdvcs.klux.recipe.LiquidFilterRecipe;
import org.kdvcs.klux.recipe.WeightedOutput;

import java.util.List;

public class LiquidFilterCategory implements IRecipeCategory<LiquidFilterRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Klux.MODID, "liquid_filter");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Klux.MODID,
            "textures/jei/gui_liquid_filter.png");

    public static final RecipeType<LiquidFilterRecipe> LIQUID_FILTER_TYPE =
            new RecipeType<>(UID, LiquidFilterRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    private LiquidFilterRecipe currentRecipe;

    public LiquidFilterCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 95);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.LIQUID_FILTER.get()));
    }

    @Override
    public RecipeType<LiquidFilterRecipe> getRecipeType() {
        return LIQUID_FILTER_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.klux.liquid_filter");
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
    public void setRecipe(IRecipeLayoutBuilder builder, LiquidFilterRecipe recipe, IFocusGroup focuses) {
        this.currentRecipe = recipe;

        builder.addSlot(RecipeIngredientRole.INPUT, 12, 20)
                .addIngredients(recipe.getMesh())
                .addTooltipCallback(new NotConsumedTooltipCallback());

        builder.addSlot(RecipeIngredientRole.INPUT, 12, 56)
                .addItemStacks(KadamJeiUtil.expandWithCount(recipe.ingredient.ingredient, recipe.ingredient.count));

        builder.addSlot(RecipeIngredientRole.INPUT, 52, 17)
                .addIngredients(ForgeTypes.FLUID_STACK, List.of(recipe.getFluid()))
                .setFluidRenderer(2000, false, 43, 61);

        //  GET THE POSITION
        int[] xs = {130, 148};
        int[] ys = {29, 47};
        List<WeightedOutput> outputs = recipe.getResults();
        for (int i = 0; i < outputs.size() && i < 4; i++) {
            WeightedOutput output = outputs.get(i);
            int x = xs[i % 2];
            int y = ys[i / 2];
            builder.addSlot(RecipeIngredientRole.OUTPUT, x, y)
                    .addItemStack(output.stack)
                    .addTooltipCallback(new ChanceTooltipCallback(output.chance));
        }
    }

    @Override
    public void draw(LiquidFilterRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;

        int ticks = recipe.getMaxProgress();
        float seconds = ticks / 20f;

        Component timeText = Component.translatable("jei.klux.liquid_filter.time", String.format("%.2f", seconds));

        float x = 148f;
        float y = 81f;

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
