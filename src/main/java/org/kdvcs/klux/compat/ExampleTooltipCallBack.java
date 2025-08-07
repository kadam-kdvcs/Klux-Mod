package org.kdvcs.klux.compat;

import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * JEI tooltip callback that adds a "Chance: XX%" line to the tooltip.
 */

public class ExampleTooltipCallBack implements IRecipeSlotTooltipCallback {
    @Override
    public void onTooltip(IRecipeSlotView slotView, List<Component> tooltip) {
        tooltip.add(Component.translatable("jei.klux.example_tool.tooltip").withStyle(ChatFormatting.GRAY));
    }
}
