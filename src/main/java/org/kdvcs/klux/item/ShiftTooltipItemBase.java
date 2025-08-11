package org.kdvcs.klux.item;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.ChatFormatting;

import javax.annotation.Nullable;
import java.util.List;

/**
 *
 *
 * A generic base Item class that supports:
 *
 * Displaying multiple tooltip lines when the player holds Shift
 * Each tooltip line can have its own text color
 * If no color is specified, GRAY is used by default
 * A fallback hint is shown when Shift is not held ("Hold Shift to view details")
 *
 * Examples:
 *
 * // Register an item with default (gray) tooltip colors
 * public static final RegistryObject<Item> BASIC_ITEM = ITEMS.register("basic_item",
 *     () -> new ShiftTooltipItemBase(new Item.Properties(),
 *         new ShiftTooltipItemBase.TooltipLine("tooltip.klux.basic.line1"),
 *         new ShiftTooltipItemBase.TooltipLine("tooltip.klux.basic.line2")));
 *
 * // Register an item with mixed custom and default colors
 * public static final RegistryObject<Item> COLORED_ITEM = ITEMS.register("colored_item",
 *     () -> new ShiftTooltipItemBase(new Item.Properties(),
 *         new ShiftTooltipItemBase.TooltipLine("tooltip.klux.colored.line1", ChatFormatting.GOLD),
 *         new ShiftTooltipItemBase.TooltipLine("tooltip.klux.colored.line2"))); // GRAY
 *
 * // Register a BlockItem using tooltips (block itself registered separately)
 * public static final RegistryObject<Item> BLOCK_ITEM = ITEMS.register("some_block",
 *     () -> new ShiftTooltipItemBase(new BlockItem.Properties(),
 *         new ShiftTooltipItemBase.TooltipLine("tooltip.klux.block.line")));
 *
 * 💡 Notes:
 * - This class is meant for Item and BlockItem registration only.
 * - Blocks (Block) do not use tooltips and must be handled separately.
 * - Shift is required to reveal full tooltip content. A default message will appear if Shift is not held.
 *
 *
 * @Kadam
 *
 *
 */

public class ShiftTooltipItemBase extends Item {

    public static class TooltipLine {
        public final String key;
        public final ChatFormatting color;

        public TooltipLine(String key) {
            this(key, ChatFormatting.GRAY);
        }

        public TooltipLine(String key, ChatFormatting color) {
            this.key = key;
            this.color = color;
        }
    }

    private final TooltipLine[] tooltipLines;

    public ShiftTooltipItemBase(Properties properties, TooltipLine... tooltipLines) {
        super(properties);
        this.tooltipLines = tooltipLines;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            for (TooltipLine line : tooltipLines) {
                tooltip.add(Component.translatable(line.key).withStyle(line.color));
            }
        } else {
            tooltip.add(Component.translatable("tooltip.press")
                    .append(Component.keybind("key.shift"))
                    .append(Component.translatable("tooltip.look"))
                    .withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
