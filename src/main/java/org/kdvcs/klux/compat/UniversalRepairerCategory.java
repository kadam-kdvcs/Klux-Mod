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
import org.kdvcs.klux.item.ModItems;
import org.kdvcs.klux.recipe.UniversalRepairerRecipe;

import java.util.List;

public class UniversalRepairerCategory implements IRecipeCategory<UniversalRepairerRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Klux.MODID, "universal_repairer");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Klux.MODID,
            "textures/jei/gui_universal_repairer.png");

    public static final RecipeType<UniversalRepairerRecipe> UNIVERSAL_REPAIRER_TYPE =
            new RecipeType<>(UID, UniversalRepairerRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    private UniversalRepairerRecipe currentRecipe;

    public UniversalRepairerCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 143, 95);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.UNIVERSAL_REPAIRER.get()));
    }

    @Override
    public RecipeType<UniversalRepairerRecipe> getRecipeType() {
        return UNIVERSAL_REPAIRER_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.klux.universal_repairer");
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
    public void setRecipe(IRecipeLayoutBuilder builder, UniversalRepairerRecipe recipe, IFocusGroup focuses) {
        this.currentRecipe = recipe;

        builder.addSlot(RecipeIngredientRole.INPUT, 107, 38)
                .addItemStack(new ItemStack(ModItems.EXAMPLE_TOOL.get()))
                .addTooltipCallback(new ExampleTooltipCallBack());

        builder.addSlot(RecipeIngredientRole.INPUT, 20, 17)
                .addIngredients(ForgeTypes.FLUID_STACK, List.of(recipe.getRequiredFluid()))
                .setFluidRenderer(2000, false, 54, 61);

    }

    @Override
    public void draw(UniversalRepairerRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;

        int repairAmount = recipe.getRepairAmount();

        Component repairText = Component.translatable("jei.klux.universal_repairer.repair", repairAmount);

        float x = 62f;
        float y = 82f;

        guiGraphics.pose().pushPose();
        font.drawInBatch(
                repairText,
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
