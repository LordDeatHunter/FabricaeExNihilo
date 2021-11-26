package wraith.fabricaeexnihilo.api.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import net.minecraft.tag.Tag;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

public abstract class AbstractIngredient<T> implements Predicate<T> {

    protected final Set<T> matches;
    protected final Collection<Tag.Identified<T>> tags;

    public AbstractIngredient(Collection<Tag.Identified<T>> tags, Set<T> matches) {
        this.tags = tags;
        this.matches = matches;
    }

    public AbstractIngredient(T... matches) {
        this(new ArrayList<>(), new HashSet<>(Arrays.asList(matches)));
    }

    public boolean test(T value) {
        return tags.stream().anyMatch(tag -> tag.contains(value)) || matches.contains(value);
    }

    abstract JsonElement serializeElement(T value, JsonSerializationContext context);

    public JsonElement toJson(JsonSerializationContext context) {
        var arr = new JsonArray();
        tags.forEach(tag -> arr.add("#" + tag.getId().toString()));
        matches.forEach(value -> arr.add(serializeElement(value, context)));
        return arr.size() > 1 ? arr : arr.get(0);
    }

    public boolean isEmpty() {
        return tags.isEmpty() && matches.isEmpty();
    }

    public List<T> flatten() {
        var ret = tags.stream().map(Tag::values).flatMap(Collection::stream).toList();
        ret.addAll(matches);
        return ret;
    }

    public <U> List<U> flatten(Function<T, U> func) {
        return flatten().stream().map(func).toList();
    }

    public static <T, U> U fromJson(JsonElement json, JsonDeserializationContext context, Function<JsonElement, Tag.Identified<T>> deserializeTag, Function<JsonElement, T> deserializeMatch, BiFunction<Collection<Tag.Identified<T>>, Set<T>, U> factory) {
        var tags = new ArrayList<Tag.Identified<T>>();
        var matches = new HashSet<T>();

        StreamSupport.stream((json.isJsonArray() ? json.getAsJsonArray() : List.of(json)).spliterator(), false)
                .filter(JsonElement::isJsonPrimitive)
                .forEach(jsonElement -> {
                    if (jsonElement.getAsString().startsWith("#"))
                        tags.add(deserializeTag.apply(jsonElement));
                    else
                        matches.add(deserializeMatch.apply(jsonElement));
                });
        return factory.apply(tags, matches);
    }
}
