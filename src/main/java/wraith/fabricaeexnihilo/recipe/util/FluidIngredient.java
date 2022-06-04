package wraith.fabricaeexnihilo.recipe.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtString;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.List;
import java.util.function.Function;

@SuppressWarnings("UnstableApiUsage")
public class FluidIngredient extends AbstractIngredient<Fluid> {

    public static final Codec<FluidIngredient> CODEC = Codec.PASSTHROUGH
        .xmap(dynamic -> {
            var string = dynamic.asString().getOrThrow(false, FabricaeExNihilo.LOGGER::warn);
            if (string.startsWith("#")) {
                return new FluidIngredient(TagKey.of(Registry.FLUID_KEY, new Identifier(string.substring(1))));
            } else {
                return new FluidIngredient(Registry.FLUID.get(new Identifier(string)));
            }
        }, itemIngredient -> {
            var string = itemIngredient.value.map(entry -> Registry.FLUID.getId(entry).toString(), tag -> "#" + tag.id());
            return new Dynamic<>(NbtOps.INSTANCE, NbtString.of(string));
        });
    public static FluidIngredient EMPTY = new FluidIngredient((Fluid) null);

    public FluidIngredient(Fluid value) {
        super(value);
    }

    public FluidIngredient(TagKey<Fluid> value) {
        super(value);
    }

    public FluidIngredient(Either<Fluid, TagKey<Fluid>> value) {
        super(value);
    }

    public FluidIngredient(FluidVariant variant) {
        this(variant.getFluid());
    }

    @Override
    public Registry<Fluid> getRegistry() {
        return Registry.FLUID;
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

    public boolean test(FluidVariant fluid) {
        return test(fluid.getFluid());
    }

    public List<ItemStack> flattenListOfBuckets() {
        return streamEntries().map(fluid1 -> ItemUtils.asStack(fluid1.getBucketItem())).toList().stream().filter(fluid -> !fluid.isEmpty()).toList();
    }

    public <U> List<U> flattenListOfBuckets(Function<ItemStack, U> func) {
        return flattenListOfBuckets().stream().map(func).toList();
    }

}