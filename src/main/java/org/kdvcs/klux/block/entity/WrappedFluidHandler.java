package org.kdvcs.klux.block.entity;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class WrappedFluidHandler implements IFluidHandler {

    private final List<FluidTank> tanks;
    private final Predicate<Integer> canDrain;
    private final BiPredicate<Integer, FluidStack> canFill;

    public WrappedFluidHandler(List<FluidTank> tanks, Predicate<Integer> canDrain,
                               BiPredicate<Integer, FluidStack> canFill) {
        this.tanks = tanks;
        this.canDrain = canDrain;
        this.canFill = canFill;
    }

    @Override
    public int getTanks() {
        return tanks.size();
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        if (tank < 0 || tank >= tanks.size()) return FluidStack.EMPTY;
        return tanks.get(tank).getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        if (tank < 0 || tank >= tanks.size()) return 0;
        return tanks.get(tank).getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        if (tank < 0 || tank >= tanks.size()) return false;
        return canFill.test(tank, stack) && tanks.get(tank).isFluidValid(stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        for (int i = 0; i < tanks.size(); i++) {
            if (canFill.test(i, resource)) {
                return tanks.get(i).fill(resource, action);
            }
        }
        return 0;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        for (int i = 0; i < tanks.size(); i++) {
            if (canDrain.test(i)) {
                FluidStack drained = tanks.get(i).drain(resource, action);
                if (!drained.isEmpty()) {
                    return drained;
                }
            }
        }
        return FluidStack.EMPTY;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        for (int i = 0; i < tanks.size(); i++) {
            if (canDrain.test(i)) {
                FluidStack drained = tanks.get(i).drain(maxDrain, action);
                if (!drained.isEmpty()) {
                    return drained;
                }
            }
        }
        return FluidStack.EMPTY;
    }
}
