package wraith.fabricaeexnihilo.api.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.util.CodecUtils;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public record Loot(ItemStack stack, List<Double> chances) {
    public static final Loot EMPTY = new Loot(ItemStack.EMPTY);
    public static final Codec<Loot> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CodecUtils.ITEM_STACK
                    .fieldOf("stack")
                    .forGetter(Loot::stack),
            Codec.DOUBLE
                    .listOf()
                    .fieldOf("chances")
                    .forGetter(Loot::chances)
    ).apply(instance, Loot::new));
    
    public Loot(ItemStack stack, double... chances) {
        this(stack, new ArrayList<>());
        for (var chance : chances) {
            this.chances.add(chance);
        }
    }

    public Loot(ItemConvertible result, double... chances) {
        this(ItemUtils.asStack(result), chances);
    }

    public Loot(ItemConvertible result, List<Double> chances) {
        this(ItemUtils.asStack(result), chances);
    }

    public Loot(Identifier result, List<Double> chances) {
        this(ItemUtils.asStack(Registry.ITEM.get(result)), chances);
    }

    public Loot(Identifier result, double... chances) {
        this(ItemUtils.asStack(Registry.ITEM.get(result)), chances);
    }

    public boolean isEmpty() {
        return stack.isEmpty() || chances.isEmpty();
    }

    public ItemStack getStack() {
        return stack;
    }

    public List<Double> getChances() {
        return chances;
    }
    
    public ItemStack createStack(Random random) {
        var stack = this.stack.copy();
        stack.setCount((int)chances.stream()
                .filter(chance -> chance > random.nextDouble())
                .count());
        return stack;
    }
}
