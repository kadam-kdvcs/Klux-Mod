package org.kdvcs.klux.block.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import org.kdvcs.klux.block.entity.LiquidFilterBlockEntity;
import org.kdvcs.klux.block.entity.ModBlockEntities;

import java.util.List;

public class LiquidFilterBlock extends BaseEntityBlock {

    public static final BooleanProperty WORKING = BooleanProperty.create("working");
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public LiquidFilterBlock() {
        super (Properties.of()
                .lightLevel(state -> state.getValue(WORKING) ? 10 : 0)
                .strength(4.2f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.METAL)
        );
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(WORKING, false)
                .setValue(FACING, Direction.EAST));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(WORKING, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WORKING, FACING);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof LiquidFilterBlockEntity) {
                ((LiquidFilterBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof LiquidFilterBlockEntity) {
                NetworkHooks.openScreen(((ServerPlayer)pPlayer), (LiquidFilterBlockEntity)entity, pPos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LiquidFilterBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                  BlockEntityType<T> type) {

        return createTickerHelper(type, ModBlockEntities.LIQUID_FILTER_BE.get(),
                LiquidFilterBlockEntity::tick);
    }

    @Override
    public void appendHoverText(ItemStack p_49816_, @Nullable BlockGetter p_49817_, List<Component> p_49818_, TooltipFlag p_49819_) {
        if (Screen.hasShiftDown()) {
            p_49818_.add(Component.translatable("tooltip.klux.liquid_filter.tooltip").withStyle(ChatFormatting.GREEN));
        } else {
            p_49818_.add(Component.translatable("tooltip.press").withStyle(ChatFormatting.GRAY).
                    append(Component.keybind("key.shift").withStyle(ChatFormatting.GRAY)
                            .append(Component.translatable("tooltip.look").withStyle(ChatFormatting.GRAY))));
        }
        super.appendHoverText(p_49816_, p_49817_, p_49818_, p_49819_);
    }
}
