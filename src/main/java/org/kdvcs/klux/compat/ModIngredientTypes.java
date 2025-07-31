package org.kdvcs.klux.compat;

import mezz.jei.api.ingredients.IIngredientType;
import net.minecraftforge.fluids.FluidStack;

@Deprecated
public class ModIngredientTypes {
    public static final IIngredientType<FluidStack> FLUID_STACK = new IIngredientType<FluidStack>() {

        @Override
        public Class<? extends FluidStack> getIngredientClass() {
            return FluidStack.class;
        }

    };
}
