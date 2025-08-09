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
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.block.ModBlocks;
import org.kdvcs.klux.recipe.DehydratorRecipe;

import java.util.Arrays;
import java.util.List;

public class DehydratorCategory implements IRecipeCategory<DehydratorRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Klux.MODID, "dehydrator");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Klux.MODID,
            "textures/jei/gui_dehydrator.png");

    public static final RecipeType<DehydratorRecipe> DEHYDRATOR_TYPE =
            new RecipeType<>(UID, DehydratorRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    //  DRAW PROCESSING TIME
    private DehydratorRecipe currentRecipe;

    public DehydratorCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 86);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.DEHYDRATOR.get()));
    }

    @Override
    public RecipeType<DehydratorRecipe> getRecipeType() {
        return DEHYDRATOR_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.klux.dehydrator");
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
    public void setRecipe(IRecipeLayoutBuilder builder, DehydratorRecipe recipe, IFocusGroup focuses) {

        //  SAVE THE RECIPE
        this.currentRecipe = recipe;

        Ingredient ingredient = recipe.getIngredients().get(0);
        int count = recipe.ingredientCount;

        List<ItemStack> stacksWithCount = Arrays.stream(ingredient.getItems())
                .map(stack -> {
                    ItemStack copy = stack.copy();
                    copy.setCount(count);
                    return copy;
                })
                .toList();

        if (!stacksWithCount.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 56, 36)
                    .addItemStacks(stacksWithCount)
                    .setSlotName("input");
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 114, 36)
                .addItemStack(recipe.getResultItem(null));
    }

    //  HERE DRAWS A PROCESSING TIME
    @Override
    public void draw(DehydratorRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;

        int ticks = recipe.getMaxProgress();
        float seconds = ticks / 20f;

        Component timeText = Component.translatable("jei.klux.dehydrator.time", String.format("%.2f", seconds));

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

