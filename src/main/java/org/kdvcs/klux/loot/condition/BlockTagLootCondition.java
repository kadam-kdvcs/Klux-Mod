package org.kdvcs.klux.loot.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.kdvcs.klux.loot.ModLootConditions;

public class BlockTagLootCondition implements LootItemCondition {
    public static final Codec<BlockTagLootCondition> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("tag").forGetter(cond -> cond.tag.location())
            ).apply(instance, tagRL -> new BlockTagLootCondition(TagKey.create(Registries.BLOCK, tagRL)))
    );

    private final TagKey<Block> tag;

    public BlockTagLootCondition(TagKey<Block> tag) {
        this.tag = tag;
    }

    @Override
    public boolean test(LootContext context) {
        BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        return state != null && state.is(tag);
    }

    @Override
    public LootItemConditionType getType() {
        return ModLootConditions.BLOCK_TAG.get();
    }
}
