package org.kdvcs.klux.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.function.Supplier;

public class AddItemModifier extends LootModifier {
    private static final Random RANDOM = new Random();

    public static final Supplier<Codec<AddItemModifier>> CODEC = Suppliers.memoize(()
            -> RecordCodecBuilder.create(inst -> codecStart(inst).and(ForgeRegistries.ITEMS.getCodec()
                    .fieldOf("item").forGetter(m -> m.item))
            .and(Codec.FLOAT.optionalFieldOf("chance", 1.0f).forGetter(m -> m.chance))
            .and(Codec.INT.optionalFieldOf("minCount", 1).forGetter(m -> m.minCount))
            .and(Codec.INT.optionalFieldOf("maxCount", 1).forGetter(m -> m.maxCount))
            .apply(inst, AddItemModifier::new)));

    private final Item item;
    private final float chance;
    private final int minCount;
    private final int maxCount;

    public AddItemModifier(LootItemCondition[] conditionsIn, Item item) {
        this(conditionsIn, item, 1.0f, 1, 1);
    }

    public AddItemModifier(LootItemCondition[] conditionsIn, Item item, float chance, int minCount, int maxCount) {
        super(conditionsIn);
        this.item = item;
        this.chance = chance;
        this.minCount = minCount;
        this.maxCount = maxCount;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {

        for(LootItemCondition condition : this.conditions) {
            if(!condition.test(context)) {
                return generatedLoot;
            }
        }

        if(RANDOM.nextFloat() > chance) {
            return generatedLoot;
        }

        int count = minCount >= maxCount ? minCount : RANDOM.nextInt(maxCount - minCount + 1) + minCount;
        generatedLoot.add(new ItemStack(this.item, count));

        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}