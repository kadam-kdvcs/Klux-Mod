package org.kdvcs.klux.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;

public class Humics extends Item {

    public Humics(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();

        if (level.getBlockState(pos).getBlock() instanceof BonemealableBlock growable) {

            if (growable.isValidBonemealTarget(level, pos, level.getBlockState(pos), level.isClientSide)) {
                if (!level.isClientSide) {
                    if (growable.isBonemealSuccess(level, level.random, pos, level.getBlockState(pos))) {
                        growable.performBonemeal((ServerLevel) level, level.random, pos, level.getBlockState(pos));
                    }
                    stack.shrink(1);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return super.useOn(context);
    }
}
