package wraith.fabricaeexnihilo.api.crafting;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.util.ItemUtils;
import wraith.fabricaeexnihilo.util.RegistryUtils;

import java.util.*;
import java.util.stream.Collectors;

public class FluidIngredient extends AbstractIngredient<Fluid> {

    public FluidIngredient(Collection<Tag.Identified<Fluid>> tags, Set<Fluid> matches) {
        super(tags, matches);
    }

    @SafeVarargs
    public FluidIngredient(Tag.Identified<Fluid>... tags) {
        this(Arrays.asList(tags), new HashSet<>());
    }

    public FluidIngredient(Fluid... matches) {
        this(new ArrayList<>(), new HashSet<>(Arrays.asList(matches)));
    }

    public FluidIngredient(FluidVolume... matches) {
        this(new ArrayList<>(), Arrays.stream(matches).map(FluidVolume::getRawFluid).collect(Collectors.toSet()));
    }

    public FluidIngredient() {
        this(new ArrayList<>(), new HashSet<>());
    }

    public boolean test(BlockState state) {
        return state.getBlock() instanceof FluidBlock fluidBlock && test(fluidBlock);
    }

    public boolean test(Block block) {
        return block instanceof FluidBlock fluidBlock && test(fluidBlock);
    }

    public boolean test(FluidBlock block) {
        return test(block.getFluidState(block.getDefaultState()).getFluid());
    }

    public boolean test(FluidState state) {
        return test(state.getFluid());
    }

    public boolean test(FluidVolume stack) {
        var fluid = stack.getRawFluid();
        return fluid != null && test(fluid);
    }

    public List<ItemStack> flattenListOfBuckets() {
        return flatten(fluid -> ItemUtils.asStack(fluid.getBucketItem())).stream().filter(fluid -> !fluid.isEmpty()).toList();
    }

    public List<EntryIngredient> asREIEntries() {
        return flattenListOfBuckets().stream().map(EntryIngredients::of).toList();
    }

    @Override
    public JsonElement serializeElement(Fluid fluid, JsonSerializationContext context) {
        return new JsonPrimitive(RegistryUtils.getId(fluid).toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FluidIngredient other) {
            return this.tags.size() == other.tags.size() &&
                    this.matches.size() == other.matches.size() &&
                    this.tags.containsAll(other.tags) &&
                    this.matches.containsAll(other.matches);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return tags.hashCode() ^ matches.hashCode();
    }

    public static FluidIngredient EMPTY = new FluidIngredient();

    public static FluidIngredient fromJson(JsonElement json, JsonDeserializationContext context) {
        return fromJson(json, context, val -> deserializeTag(val, context), val -> deserializeMatch(val, context), FluidIngredient::new);
    }

    public static Tag.Identified<Fluid> deserializeTag(JsonElement json, JsonDeserializationContext context) {
        return TagFactory.FLUID.create(new Identifier(json.getAsString().substring(1)));
    }

    public static Fluid deserializeMatch(JsonElement json, JsonDeserializationContext context) {
        return Registry.FLUID.get(new Identifier(json.getAsString()));
    }

}