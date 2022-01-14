package wraith.fabricaeexnihilo.recipe.util;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import net.minecraft.tag.Tag;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.ibm.icu.impl.locale.KeyTypeData.ValueType.single;

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
    
    public Either<T, Tag<T>> getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return value.map(Object::toString, tag -> "#" + tag.toString());
    }
    
    public int hashCode() {
        return value.hashCode();
    }
}
