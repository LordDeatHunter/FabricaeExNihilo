package wraith.fabricaeexnihilo.recipe.util;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class WeightedList {

    public static final Codec<WeightedList> CODEC = Codec.unboundedMap(Registry.BLOCK.getCodec(), Codec.INT)
        .xmap(WeightedList::new, WeightedList::getValues);

    private final Map<Block, Integer> values;
    private int totalWeight;

    public WeightedList(Map<Block, Integer> values) {
        this.values = values;
        setTotalWeight();
    }

    public WeightedList(List<Pair<Block, Integer>> values) {
        this.values = new HashMap<>();
        for (var value : values) {
            this.values.put(value.getLeft(), value.getRight());
        }
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

    public <U> List<U> flatten(Function<Block, U> func) {
        return values.keySet().stream().map(func).toList();
    }

    /**
     * Takes another weighted list and adds all its entries to this WeightedList.
     */
    public void amend(WeightedList other) {
        totalWeight += other.totalWeight;
        other.values.forEach((key, value) -> values.put(key, value + values.getOrDefault(key, 0)));
    }
}