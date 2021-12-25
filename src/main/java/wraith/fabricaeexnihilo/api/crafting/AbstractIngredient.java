package wraith.fabricaeexnihilo.api.crafting;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.tag.Tag;
import net.minecraft.util.ActionResult;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

public abstract class AbstractIngredient<T> implements Predicate<T> {
    protected final Either<T, Tag<T>> value;
    
    public AbstractIngredient(T value) {
        this(Either.left(value));
    }
    
    public AbstractIngredient(Tag<T> value) {
        this(Either.right(value));
    }
    
    public AbstractIngredient(Either<T, Tag<T>> value) {
        this.value = value;
    }
    
    public boolean test(T value) {
        return this.value.map(single -> single.equals(value), tag -> tag.contains(value));
    }
    
    public abstract JsonElement toJson();
    
    public abstract void toPacket(PacketByteBuf buf);

    public boolean isEmpty() {
        return this.value.map(Objects::isNull, tag -> tag.values().isEmpty());
    }

    public List<T> flatten() {
        return this.value.map(ImmutableList::of, Tag::values);
    }

    public <U> List<U> flatten(Function<T, U> func) {
        return flatten().stream().map(func).toList();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractIngredient other) {
            return this.getClass() == other.getClass() && this.value.equals(other.value);
        }
        return false;
    }
    
    public int hashCode() {
        return value.hashCode();
    }
}
