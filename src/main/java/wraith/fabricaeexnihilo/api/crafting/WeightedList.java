package wraith.fabricaeexnihilo.api.crafting;

import com.google.common.collect.Streams;
import com.mojang.serialization.Codec;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.*;

public class WeightedList {
    public static final Codec<WeightedList> CODEC = Codec.unboundedMap(Registry.BLOCK.getCodec(), Codec.INT)
            .xmap(WeightedList::new, WeightedList::getValues);

    private final Map<Block, Integer> values;
    private int totalWeight;

    public WeightedList(Iterable<Block> keys, Iterable<Integer> weights) {
        this.values = new HashMap<>();
        Streams.zip(Streams.stream(keys), Streams.stream(weights), Pair::new).forEach(pair -> this.values.put(pair.getLeft(), pair.getRight()));
        setTotalWeight();
    }

    public WeightedList(Map<Block, Integer> values) {
        this.values = values;
        setTotalWeight();
    }

    @SafeVarargs
    public WeightedList(Pair<Block, Integer>... values) {
        this.values = new HashMap<>();
        for (var value : values) {
            this.values.put(value.getLeft(), value.getRight());
        }
        setTotalWeight();
    }

    public WeightedList() {
        this.values = new HashMap<>();
        setTotalWeight();
    }

    public Map<Block, Integer> getValues() {
        return values;
    }

    private void setTotalWeight() {
        this.totalWeight = this.values.values().stream().mapToInt(Integer::intValue).sum();
    }

    public Block choose(Random random) {
        var rem = random.nextInt(totalWeight);
        Block block = null;
        for (var entry : values.entrySet()) {
            rem -= entry.getValue();
            block = entry.getKey();
            if (rem <= 0) {
                break;
            }
        }
        return block;
    }

    public List<ItemStack> asListOfStacks() {
        return values.entrySet().stream().map(item -> ItemUtils.asStack(item.getKey(), item.getValue())).toList();
    }

    public List<EntryIngredient> asEntryList() {
        return values.keySet().stream().map(EntryIngredients::of).toList();
    }

    /**
     * Takes another weighted list and adds all its entries to this WeightedList.
     */
    public void amend(WeightedList other) {
        totalWeight += other.totalWeight;
        other.values.forEach((key, value) -> values.put(key, value + values.getOrDefault(key, 0)));
    }

}