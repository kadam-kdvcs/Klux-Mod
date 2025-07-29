package org.kdvcs.klux.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.kdvcs.klux.config.KluxCommonConfigs;

import java.util.List;
import java.util.Optional;

public class ReturnScepter extends Item {
    private static final String NBT_KEY = "ReturnScepterCooldown";

    @Override
    public boolean canBeHurtBy(DamageSource p_41387_) {
        return !p_41387_.is(DamageTypes.CACTUS);
    }

    public ReturnScepter(Properties properties) {
        super(properties.fireResistant());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }

        if (isOnCooldown(serverPlayer)) {
            long secondsLeft = (getCooldownTicksLeft(serverPlayer) + 19) / 20;
            serverPlayer.displayClientMessage(
                    Component.translatable("returnscepter.cooldown.seconds", secondsLeft).withStyle(ChatFormatting.RED),
                    true
            );
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }

        BlockPos spawnPos = serverPlayer.getRespawnPosition();
        ResourceKey<Level> respawnDimensionKey = serverPlayer.getRespawnDimension();

        if (spawnPos == null) {
            sendFailMessage(serverPlayer);
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }

        ServerLevel respawnDimension = serverPlayer.server.getLevel(respawnDimensionKey);
        if (respawnDimension == null) {
            sendFailMessage(serverPlayer);
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }

        BlockState state = respawnDimension.getBlockState(spawnPos);
        boolean isValidRespawn = false;

        if (state.getBlock() instanceof BedBlock) {
            isValidRespawn = BedBlock.canSetSpawn(respawnDimension);
        } else if (state.getBlock() instanceof RespawnAnchorBlock) {
            isValidRespawn = RespawnAnchorBlock.canSetSpawn(respawnDimension);
        } else if (serverPlayer.isRespawnForced()) {
            isValidRespawn = true;
        }

        if (!isValidRespawn) {
            sendFailMessage(serverPlayer);
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }

        Optional<Vec3> respawnLocation = ServerPlayer.findRespawnPositionAndUseSpawnBlock(
                respawnDimension,
                spawnPos,
                serverPlayer.getRespawnAngle(),
                serverPlayer.isRespawnForced(),
                true
        );

        if (respawnLocation.isEmpty()) {
            ServerLevel overworld = serverPlayer.server.getLevel(Level.OVERWORLD);
            BlockPos fallbackPos = overworld.getSharedSpawnPos();

            respawnLocation = Optional.of(new Vec3(
                    fallbackPos.getX() + 0.5D,
                    fallbackPos.getY(),
                    fallbackPos.getZ() + 0.5D
            ));
            respawnDimension = overworld;

            serverPlayer.sendSystemMessage(Component.translatable("returnscepter.fallback_spawn"));
        }

        Vec3 target = respawnLocation.get();
        serverPlayer.teleportTo(
                respawnDimension,
                target.x(),
                target.y(),
                target.z(),
                serverPlayer.getRespawnAngle(),
                0.0F
        );

        ((ServerLevel) respawnDimension).sendParticles(
                ParticleTypes.POOF,
                target.x, target.y + 1, target.z,
                50, 0.5, 0.5, 0.5, 0.5
        );

            serverPlayer.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 80, 4));
            serverPlayer.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 80, 2));
            serverPlayer.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 80, 1));
            serverPlayer.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 400, 0));


        respawnDimension.playSound(null, target.x, target.y, target.z,
                SoundEvents.ENDERMAN_TELEPORT,
                SoundSource.PLAYERS,
                1.0F, 1.0F);

        serverPlayer.sendSystemMessage(Component.translatable("returnscepter.teleported"));
        player.getItemInHand(hand).hurtAndBreak(1, serverPlayer, p -> p.broadcastBreakEvent(hand));
        setCooldown(serverPlayer);
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    private void sendFailMessage(ServerPlayer player) {
        player.sendSystemMessage(Component.translatable("returnscepter.no_respawn"));
        player.level().playSound(null, player.blockPosition(),
                SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    private boolean isOnCooldown(ServerPlayer player) {
        return getCooldownTicksLeft(player) > 0;
    }

    private long getCooldownTicksLeft(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        long cooldownUntil = data.getLong(NBT_KEY);
        long now = player.serverLevel().getGameTime();
        return Math.max(0, cooldownUntil - now);
    }

    private void setCooldown(ServerPlayer player) {
        long now = player.serverLevel().getGameTime();
        int configuredCooldown = KluxCommonConfigs.RETURNSCEPTER_COOLDOWN.get();
        player.getPersistentData().putLong(NBT_KEY, now + configuredCooldown);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.klux.return_scepter.tooltip").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("return_scepter.use").withStyle(ChatFormatting.GREEN));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
