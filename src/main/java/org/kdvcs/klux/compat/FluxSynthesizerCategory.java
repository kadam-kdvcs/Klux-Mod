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
import org.kdvcs.klux.recipe.FluidAssemblerRecipe;
import org.kdvcs.klux.recipe.FluxSynthesizerRecipe;

import java.util.List;

public class FluxSynthesizerCategory implements IRecipeCategory<FluxSynthesizerRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Klux.MODID, "flux_synthesizer");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Klux.MODID,
            "textures/jei/gui_flux_synthesizer.png");

    public static final RecipeType<FluxSynthesizerRecipe> FLUX_SYNTHESIZER_TYPE =
            new RecipeType<>(UID, FluxSynthesizerRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    //  DRAW PROCESSING TIME
    private FluxSynthesizerRecipe currentRecipe;

    public FluxSynthesizerCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 90);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.FLUX_SYNTHESIZER.get()));
    }

    @Override
    public RecipeType<FluxSynthesizerRecipe> getRecipeType() {
        return FLUX_SYNTHESIZER_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.klux.flux_synthesizer");
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
    public void setRecipe(IRecipeLayoutBuilder builder, FluxSynthesizerRecipe recipe, IFocusGroup focuses) {
        this.currentRecipe = recipe;

        builder.addSlot(RecipeIngredientRole.INPUT, 59, 19)
                .addItemStacks(KadamJeiUtil.expandWithCount(recipe.getIngredients().get(0), recipe.ingredient1.count));

        builder.addSlot(RecipeIngredientRole.INPUT, 59, 55)
                .addItemStacks(KadamJeiUtil.expandWithCount(recipe.getIngredients().get(1), recipe.ingredient2.count));

        //FLUID TANK
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 14)
                .addIngredients(ForgeTypes.FLUID_STACK, List.of(recipe.getFluid()))
                .setFluidRenderer(2000, false, 16, 62);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 109, 37)
                .addItemStack(recipe.getResultItem(null));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 144, 14)
                .addIngredients(ForgeTypes.FLUID_STACK, List.of(recipe.getResultFluid()))
                .setFluidRenderer(2000, false, 16, 62);
    }

    //  HERE DRAWS A PROCESSING TIME
    @Override
    public void draw(FluxSynthesizerRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;

        int ticks = recipe.getMaxProgress();
        float seconds = ticks / 20f;

        Component timeText = Component.translatable("jei.klux.flux_synthesizer.time", String.format("%.2f", seconds));

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