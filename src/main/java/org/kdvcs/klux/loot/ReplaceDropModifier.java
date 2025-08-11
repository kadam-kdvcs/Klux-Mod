package org.kdvcs.klux.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class ReplaceDropModifier extends LootModifier {
    private final Block targetBlock;
    private final Item replacementItem;
    private final float chance;

    public ReplaceDropModifier(LootItemCondition[] conditions, Block targetBlock, Item replacementItem, float chance) {
        super(conditions);
        this.targetBlock = targetBlock;
        this.replacementItem = replacementItem;
        this.chance = chance;
    }

    public static final Codec<ReplaceDropModifier> CODEC = RecordCodecBuilder.create(instance ->
            codecStart(instance).and(
                    instance.group(
                            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("target_block").forGetter(m -> m.targetBlock),
                            BuiltInRegistries.ITEM.byNameCodec().fieldOf("replacement_item").forGetter(m -> m.replacementItem),
                            Codec.FLOAT.fieldOf("chance").forGetter(m -> m.chance)
                    )
            ).apply(instance, (conditions, block, item, chance) -> new ReplaceDropModifier(conditions, block, item, chance))
    );

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        var blockState = context.getParamOrNull(net.minecraft.world.level.storage.loot.parameters.LootContextParams.BLOCK_STATE);
        if (blockState != null) {
            var block = blockState.getBlock();
            if (block == targetBlock && context.getRandom().nextFloat() < chance) {
                generatedLoot.clear();
                generatedLoot.add(new ItemStack(replacementItem));
            }
        }
        return generatedLoot;
    }
}
