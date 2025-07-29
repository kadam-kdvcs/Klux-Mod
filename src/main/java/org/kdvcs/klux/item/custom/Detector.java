package org.kdvcs.klux.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.kdvcs.klux.sound.ModSounds;
import org.kdvcs.klux.util.ModTags;

import java.util.List;

public class Detector extends Item {
    public Detector(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext p_41427_) {

        if(!p_41427_.getLevel().isClientSide()) {
                BlockPos positionClicked = p_41427_.getClickedPos();
                Player player = p_41427_.getPlayer();
                boolean foundBlock = false;
                for(int i = 0; i <= positionClicked.getY() + 64; i++) {
                    BlockState state = p_41427_.getLevel().getBlockState(positionClicked.below(i));
                    if (isValuableBlock(state)) {
                        outputValuableCoordinates(positionClicked.below(i), player, state.getBlock());
                        foundBlock = true;

                        p_41427_.getLevel().playSeededSound(null, positionClicked.getX(), positionClicked.getY(), positionClicked.getZ(),
                                ModSounds.DETECTOR_FOUND_ORE.get(), SoundSource.BLOCKS,1f,1f,0);

                        break;
                    }
                }
                if(!foundBlock) {
                    player.sendSystemMessage(Component.literal("No Valuables Found!"));
                }
        }

        //damage
        p_41427_.getItemInHand().hurtAndBreak(1,p_41427_.getPlayer(),
                player -> player.broadcastBreakEvent(player.getUsedItemHand()));

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        p_41423_.add(Component.translatable("tooltip.klux.detector.tooltip").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
    }

    private void outputValuableCoordinates(BlockPos blockPos, Player player, Block block) {
        player.sendSystemMessage(Component.literal("Found" + I18n.get(block.getDescriptionId()) + "at" +
                "(" + blockPos.getX() + "," + blockPos.getY() + "," + blockPos.getZ() + ")"));
    }

    private boolean isValuableBlock(BlockState state) {
        return state.is(ModTags.Blocks.DETECTOR_VALUABLES);
    }
}
