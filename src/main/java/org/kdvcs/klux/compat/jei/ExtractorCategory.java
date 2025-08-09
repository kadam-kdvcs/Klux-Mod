package org.kdvcs.klux.compat.jei;

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
import org.jetbrains.annotations.Nullable;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.block.ModBlocks;
import org.kdvcs.klux.recipe.ExtractorRecipe;

public class ExtractorCategory implements IRecipeCategory<ExtractorRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Klux.MODID, "extractor");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Klux.MODID, "textures/jei/gui_extractor.png");

    public static final RecipeType<ExtractorRecipe> EXTRACTOR_TYPE = new RecipeType<>(UID, ExtractorRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    private ExtractorRecipe currentRecipe;

    public ExtractorCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 88);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.EXTRACTOR.get()));
    }

    @Override
    public RecipeType<ExtractorRecipe> getRecipeType() {
        return EXTRACTOR_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.klux.extractor");
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
    public void setRecipe(IRecipeLayoutBuilder builder, ExtractorRecipe recipe, IFocusGroup focuses) {
        this.currentRecipe = recipe;

        builder.addSlot(RecipeIngredientRole.INPUT, 47, 36)
                .addItemStacks(KadamJeiUtil.expandWithCount(recipe.getInput().ingredient, recipe.getInput().count));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 109, 36)
                .addItemStack(recipe.getResultItem(null));

    }

    @Override
    public void draw(ExtractorRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;

        int ticks = recipe.getMaxProgress();
        float seconds = ticks / 20f;

        Component timeText = Component.translatable("jei.klux.extractor.time", String.format("%.2f", seconds));

        float x = 148;
        float y = 72;

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
