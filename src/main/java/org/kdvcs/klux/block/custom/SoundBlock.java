package org.kdvcs.klux.block.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.kdvcs.klux.sound.ModSounds;

import java.util.List;

public class SoundBlock extends Block {
    public SoundBlock(Properties p_49795_) {
        super(p_49795_);
    }

    //  public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;


    @Override
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_,
                                 Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {

        p_60504_.playSound(p_60506_, p_60505_, ModSounds.SOUND_BLOCK_TOUCH.get(), SoundSource.BLOCKS, 1.0F, 1.0F);

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack p_49816_, @Nullable BlockGetter p_49817_, List<Component> p_49818_, TooltipFlag p_49819_) {
        p_49818_.add(Component.translatable("tooltip.klux.sound_block.tooltip").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(p_49816_, p_49817_, p_49818_, p_49819_);
    }
}
