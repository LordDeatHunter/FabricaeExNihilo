package wraith.fabricaeexnihilo.recipe.util;

import com.google.common.collect.Iterables;
import com.mojang.datafixers.util.Either;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractIngredient other) {
            return this.getClass() == other.getClass() && this.value.equals(other.value);
        }
        return false;
    }
    
    public abstract Registry<T> getRegistry();

    public Either<T, TagKey<T>> getValue() {
        return value;
    }

    public int hashCode() {
        return value.hashCode();
    }

    public boolean isEmpty() {
        return this.value.map(Objects::isNull, tag -> Iterables.isEmpty(getRegistry().iterateEntries(tag)));
    }

    public boolean test(T value) {
        return streamEntries().anyMatch(value1 -> value == value1);
    }
    
    public Stream<T> streamEntries() {
        return value.map(Stream::of, tag -> StreamSupport.stream(getRegistry().iterateEntries(tag).spliterator(), false)
                .map(RegistryEntry::value));
    }

    @Override
    public String toString() {
        return value.map(Object::toString, tag -> "#" + tag.toString());
    }
}
