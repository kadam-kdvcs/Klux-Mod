package org.kdvcs.klux.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class AddItemModifier extends LootModifier {

    public static final Supplier<Codec<AddItemModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(inst -> codecStart(inst)
                    .and(ForgeRegistries.ITEMS.getCodec()
                            .fieldOf("item").forGetter(m -> m.item))
                    .and(Codec.FLOAT.optionalFieldOf("chance", 1.0f).forGetter(m -> m.chance))
                    .and(Codec.INT.optionalFieldOf("minCount", 1).forGetter(m -> m.minCount))
                    .and(Codec.INT.optionalFieldOf("maxCount", 1).forGetter(m -> m.maxCount))
                    .and(Codec.FLOAT.optionalFieldOf("lootingBonusPerLevel", 1.0f).forGetter(m -> m.lootingBonusPerLevel))
                    .apply(inst, AddItemModifier::new)
            )
    );

    private final Item item;
    private final float chance;
    private final int minCount;
    private final int maxCount;
    private final float lootingBonusPerLevel;

    public AddItemModifier(LootItemCondition[] conditionsIn, Item item) {
        this(conditionsIn, item, 1.0f, 1, 1, 1.0f);
    }

    public AddItemModifier(LootItemCondition[] conditionsIn, Item item, float chance, int minCount, int maxCount) {
        this(conditionsIn, item, chance, minCount, maxCount, 1.0f);
    }

    public AddItemModifier(LootItemCondition[] conditionsIn, Item item, float chance, int minCount, int maxCount, float lootingBonusPerLevel) {
        super(conditionsIn);
        this.item = item;
        this.chance = chance;
        this.minCount = minCount;
        this.maxCount = maxCount;
        this.lootingBonusPerLevel = lootingBonusPerLevel;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        RandomSource random = context.getRandom();

        for (LootItemCondition condition : this.conditions) {
            if (!condition.test(context)) {
                return generatedLoot;
            }
        }

        if (random.nextFloat() > this.chance) {
            return generatedLoot;
        }

        int lootingLevel = context.getLootingModifier();

        //BASE
        int baseCount = (minCount >= maxCount)
                ? minCount
                : random.nextInt(maxCount - minCount + 1) + minCount;

        int finalCount = baseCount + Math.round(lootingLevel * lootingBonusPerLevel);
        generatedLoot.add(new ItemStack(this.item, finalCount));
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
