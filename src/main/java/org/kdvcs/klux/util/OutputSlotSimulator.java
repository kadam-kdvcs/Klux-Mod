package org.kdvcs.klux.util;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

// maybe I should take GregTech for a more accurate method to make this...

@Deprecated
public class OutputSlotSimulator {

    private final IItemHandler itemHandler;
    private final int startSlot;
    private final int endSlot;
    private final Consumer<String> debugLogger;

    public OutputSlotSimulator(IItemHandler itemHandler, int startSlot, int endSlot, Consumer<String> debugLogger) {
        this.itemHandler = itemHandler;
        this.startSlot = startSlot;
        this.endSlot = endSlot;
        this.debugLogger = debugLogger;
    }

    public boolean canInsertAllOutputs(List<ItemStack> outputs) {

        List<ItemStack> slots = new ArrayList<>();
        for (int slot = startSlot; slot <= endSlot; slot++) {
            slots.add(itemHandler.getStackInSlot(slot).copy());
        }

        log("=== SIMULATION START ===");

        for (ItemStack output : outputs) {
            int remaining = output.getCount();
            log(String.format("try to insert %s x%d", output.getHoverName().getString(), remaining));

            for (int i = 0; i < slots.size(); i++) {
                ItemStack slotStack = slots.get(i);
                if (!slotStack.isEmpty() && ItemStack.isSameItemSameTags(slotStack, output)) {
                    int freeSpace = slotStack.getMaxStackSize() - slotStack.getCount();
                    if (freeSpace > 0) {
                        int toInsert = Math.min(freeSpace, remaining);
                        slotStack.grow(toInsert);
                        remaining -= toInsert;
                        log(String.format("to slot %d, added %d, the rest %d", startSlot + i, toInsert, remaining));
                        if (remaining <= 0) break;
                    }
                }
            }

            if (remaining > 0) {
                for (int i = 0; i < slots.size(); i++) {
                    ItemStack slotStack = slots.get(i);
                    if (slotStack.isEmpty()) {
                        int toInsert = Math.min(remaining, output.getMaxStackSize());
                        ItemStack newStack = output.copy();
                        newStack.setCount(toInsert);
                        slots.set(i, newStack);
                        remaining -= toInsert;
                        log(String.format("to empty slot %d, added %d, rest %d", startSlot + i, toInsert, remaining));
                        if (remaining <= 0) break;
                    }
                }
            }

            if (remaining > 0) {
                log(String.format("can't insert %s", output.getHoverName().getString()));
                return false;
            }
        }

        log("successfully inserted!");
        return true;
    }

    private void log(String msg) {
        if (debugLogger != null) debugLogger.accept(msg);
    }
}
