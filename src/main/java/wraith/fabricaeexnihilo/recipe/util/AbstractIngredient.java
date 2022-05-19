package wraith.fabricaeexnihilo.recipe.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import dev.latvian.mods.rhino.mod.util.TagUtils;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

import static com.ibm.icu.impl.locale.KeyTypeData.ValueType.single;

public abstract class AbstractIngredient<T> implements Predicate<T> {
    protected final Either<T, TagKey<T>> value;
    
    public AbstractIngredient(T value) {
        this(Either.left(value));
    }
    
    public AbstractIngredient(TagKey<T> value) {
        this(Either.right(value));
    }
    
    public AbstractIngredient(Either<T, TagKey<T>> value) {
        this.value = value;
    }
    
    public boolean test(T value) {
        return this.value.map(single -> single.equals(value), tag -> Iterables.contains(getRegistry().iterateEntries(tag), value));
    }
    
    public boolean isEmpty() {
        return this.value.map(Objects::isNull, tag -> Iterables.isEmpty(getRegistry().iterateEntries(tag)));
    }
    
    public List<T> flatten() {
        return this.value.map(ImmutableList::of, (tag) -> StreamSupport.stream(getRegistry().iterateEntries(tag).spliterator(), false)
                .map(RegistryEntry::value).toList());
    }
    
    public <U> List<U> flatten(Function<T, U> func) {
        return flatten().stream().map(func).toList();
    }
    
    public abstract Registry<T> getRegistry();
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractIngredient other) {
            return this.getClass() == other.getClass() && this.value.equals(other.value);
        }
        return false;
    }
    
    public Either<T, TagKey<T>> getValue() {
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
