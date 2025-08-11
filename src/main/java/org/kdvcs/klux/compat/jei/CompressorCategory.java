package org.kdvcs.klux.compat.jei;

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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.block.ModBlocks;
import org.kdvcs.klux.recipe.CompressorRecipe;

public class CompressorCategory implements IRecipeCategory<CompressorRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Klux.MODID, "compressor");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Klux.MODID,
            "textures/jei/gui_compressor.png");
    public static final ResourceLocation ARROW = new ResourceLocation(Klux.MODID,
            "textures/jei/arrow_0.png");

    public static final RecipeType<CompressorRecipe> COMPRESSOR_TYPE =
            new RecipeType<>(UID, CompressorRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    //  DRAW PROCESSING TIME
    private CompressorRecipe currentRecipe;

    public CompressorCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 86);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.COMPRESSOR.get()));

        //DRAW PROCESSING ARROW
        IDrawableStatic staticArrow = helper.drawableBuilder(ARROW, 0, 0, 34, 14)
                .setTextureSize(34, 14)
                .build();
        this.arrow = helper.createAnimatedDrawable(staticArrow, 20, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public RecipeType<CompressorRecipe> getRecipeType() {
        return COMPRESSOR_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.klux.compressor");
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
    public void setRecipe(IRecipeLayoutBuilder builder, CompressorRecipe recipe, IFocusGroup focuses) {
        this.currentRecipe = recipe;

        builder.addSlot(RecipeIngredientRole.INPUT, 62, 17)
                .addItemStacks(KadamJeiUtil.expandWithCount(recipe.getInputA().ingredient, recipe.getInputA().count));

        builder.addSlot(RecipeIngredientRole.INPUT, 62, 53)
                .addItemStacks(KadamJeiUtil.expandWithCount(recipe.getInputB().ingredient, recipe.getInputB().count));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 112, 35)
                .addItemStack(recipe.getResultItem(null));
    }

    //  HERE DRAWS A PROCESSING TIME
    @Override
    public void draw(CompressorRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {

        arrow.draw(guiGraphics, 67, 36);

        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;

        int ticks = recipe.getMaxProgress();
        float seconds = ticks / 20f;

        Component timeText = Component.translatable("jei.klux.compressor.time", String.format("%.2f", seconds));

        //  POSITION OF THE TEXT
        float x = 148f;
        float y = 72f;

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

