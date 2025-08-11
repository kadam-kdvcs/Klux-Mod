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
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.block.ModBlocks;
import org.kdvcs.klux.recipe.FluidExtractorRecipe;

import java.util.List;

public class FluidExtractorCategory implements IRecipeCategory<FluidExtractorRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Klux.MODID, "fluid_extractor");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Klux.MODID,
            "textures/jei/gui_fluid_extractor.png");
    public static final ResourceLocation ARROW = new ResourceLocation(Klux.MODID,
            "textures/jei/arrow_2.png");

    public static final RecipeType<FluidExtractorRecipe> FLUID_EXTRACTOR_TYPE =
            new RecipeType<>(UID, FluidExtractorRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final IDrawableStatic meter;

    //  DRAW PROCESSING TIME
    private FluidExtractorRecipe currentRecipe;

    public FluidExtractorCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 93);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.FLUID_EXTRACTOR.get()));

        //DRAW PROCESSING ARROW
        IDrawableStatic staticArrow = helper.drawableBuilder(ARROW, 0, 0, 22, 15)
                .setTextureSize(22, 15)
                .build();
        this.arrow = helper.createAnimatedDrawable(staticArrow, 20, IDrawableAnimated.StartDirection.LEFT, false);

        this.meter = helper.drawableBuilder(TEXTURE, 176, 0, 16, 61)
                .setTextureSize(256, 256)
                .build();
    }

    @Override
    public RecipeType<FluidExtractorRecipe> getRecipeType() {
        return FLUID_EXTRACTOR_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.klux.fluid_extractor");
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
    public void setRecipe(IRecipeLayoutBuilder builder, FluidExtractorRecipe recipe, IFocusGroup focuses) {
        this.currentRecipe = recipe;

        builder.addSlot(RecipeIngredientRole.INPUT, 45, 36)
                .addItemStacks(KadamJeiUtil.expandWithCount(recipe.getIngredients().get(0), recipe.ingredient.count));

        //FLUID TANK
        builder.addSlot(RecipeIngredientRole.OUTPUT, 118, 15)
                .addIngredients(ForgeTypes.FLUID_STACK, List.of(recipe.getFluid()))
                .setFluidRenderer(2000, false, 16, 61);

    }

    //  HERE DRAWS A PROCESSING TIME
    @Override
    public void draw(FluidExtractorRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {

        arrow.draw(guiGraphics, 81, 36);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 300);
        meter.draw(guiGraphics, 118, 15);
        guiGraphics.pose().popPose();

        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;

        int ticks = recipe.getMaxProgress();
        float seconds = ticks / 20f;

        Component timeText = Component.translatable("jei.klux.fluid_extractor.time", String.format("%.2f", seconds));

        //  POSITION OF THE TEXT
        float x = 148f;
        float y = 77f;

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
