package org.kdvcs.klux.compat.jei;

import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * JEI tooltip callback that adds a "Chance: XX%" line to the tooltip.
 */

public class ChanceTooltipCallback implements IRecipeSlotTooltipCallback {
    private final float chance;

    public ChanceTooltipCallback(float chance) {
        this.chance = chance;
    }

    @Override
    public void onTooltip(IRecipeSlotView slotView, List<Component> tooltip) {
        tooltip.add(Component.translatable("jei.klux.tooltip.chance", String.format("%.0f%%", chance * 100)));
    }

}
