package org.kdvcs.klux.compat.jei;

import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * JEI tooltip callback that adds a "Not consumed" line to the tooltip.
 */

public class NotConsumedTooltipCallback implements IRecipeSlotTooltipCallback {

    @Override
    public void onTooltip(IRecipeSlotView slotView, List<Component> tooltip) {
        tooltip.add(Component.translatable("jei.klux.require").withStyle(ChatFormatting.RED));
        tooltip.add(Component.translatable("jei.klux.tooltip.not_consumed").withStyle(ChatFormatting.GRAY));
    }
}
