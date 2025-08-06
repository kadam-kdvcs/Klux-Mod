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
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.block.ModBlocks;
import org.kdvcs.klux.recipe.FluxSynthesizerRecipe;
import org.kdvcs.klux.recipe.LiquidReactorRecipe;

import java.util.List;

public class LiquidReactorCategory implements IRecipeCategory<LiquidReactorRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Klux.MODID, "liquid_reactor");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Klux.MODID,
            "textures/jei/gui_liquid_reactor.png");

    public static final RecipeType<LiquidReactorRecipe> LIQUID_REACTOR_TYPE =
            new RecipeType<>(UID, LiquidReactorRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    //  DRAW PROCESSING TIME
    private LiquidReactorRecipe currentRecipe;

    public LiquidReactorCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 94);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.LIQUID_REACTOR.get()));
    }

    @Override
    public RecipeType<LiquidReactorRecipe> getRecipeType() {
        return LIQUID_REACTOR_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.klux.liquid_reactor");
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
    public void setRecipe(IRecipeLayoutBuilder builder, LiquidReactorRecipe recipe, IFocusGroup focuses) {
        this.currentRecipe = recipe;

        builder.addSlot(RecipeIngredientRole.INPUT, 12, 16)
                .addIngredients(ForgeTypes.FLUID_STACK,
                        List.of(new FluidStack(recipe.getFluidInput1(), recipe.getFluidInput1Amount())))
                .setFluidRenderer(2000, false, 24, 62);

        builder.addSlot(RecipeIngredientRole.INPUT, 140, 16)
                .addIngredients(ForgeTypes.FLUID_STACK,
                        List.of(new FluidStack(recipe.getFluidInput2(), recipe.getFluidInput2Amount())))
                .setFluidRenderer(2000, false, 24, 62);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 53, 16)
                .addIngredients(ForgeTypes.FLUID_STACK,
                        List.of(recipe.getOutputFluid()))
                .setFluidRenderer(4000, false, 70, 62);

    }

    //  HERE DRAWS A PROCESSING TIME
    @Override
    public void draw(LiquidReactorRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;

        int ticks = recipe.getMaxProgress();
        float seconds = ticks / 20f;

        Component timeText = Component.translatable("jei.klux.liquid_reactor.time", String.format("%.2f", seconds));

        Component unsortedText = Component.translatable("jei.klux.liquid_reactor.notice");

        float timeX = 148f;
        float timeY = 82f;

        float unsortedX = timeX - 77f;
        float unsortedY = timeY - 77f;

        guiGraphics.pose().pushPose();

        font.drawInBatch(
                unsortedText,
                unsortedX,
                unsortedY,
                0x404040,
                false,
                guiGraphics.pose().last().pose(),
                mc.renderBuffers().bufferSource(),
                Font.DisplayMode.NORMAL,
                0,
                15728880
        );

        font.drawInBatch(
                timeText,
                timeX,
                timeY,
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