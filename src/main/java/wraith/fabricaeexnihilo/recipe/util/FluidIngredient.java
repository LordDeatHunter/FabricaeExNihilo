package wraith.fabricaeexnihilo.recipe.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtString;
import net.minecraft.tag.ServerTagManagerHolder;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class FluidIngredient extends AbstractIngredient<Fluid> {
    public static final Codec<FluidIngredient> CODEC = Codec.PASSTHROUGH
            .xmap(dynamic -> {
                var string = dynamic.asString().getOrThrow(false, FabricaeExNihilo.LOGGER::warn);
                if (string.startsWith("#")) {
                    return new FluidIngredient(TagFactory.FLUID.create(new Identifier(string.substring(1))));
                } else {
                    return new FluidIngredient(Registry.FLUID.get(new Identifier(string)));
                }
            }, itemIngredient -> {
                var string = itemIngredient.value.map(entry -> Registry.FLUID.getId(entry).toString(),
                        tag -> "#" + ServerTagManagerHolder.getTagManager()
                                .getOrCreateTagGroup(Registry.FLUID_KEY)
                                .getUncheckedTagId(tag));
                return new Dynamic<>(NbtOps.INSTANCE, NbtString.of(string));
            });
    
    public FluidIngredient(Fluid value) {
        super(value);
    }
    
    public FluidIngredient(Tag<Fluid> value) {
        super(value);
    }
    
    public FluidIngredient(Either<Fluid, Tag<Fluid>> value) {
        super(value);
    }
    
    public FluidIngredient(FluidVariant variant) {
        this(variant.getFluid());
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
        return flatten(fluid -> ItemUtils.asStack(fluid.getBucketItem())).stream().filter(fluid -> !fluid.isEmpty()).toList();
    }
    
    public List<EntryIngredient> asREIEntries() {
        return flattenListOfBuckets().stream().map(EntryIngredients::of).toList();
    }
    
    public static FluidIngredient EMPTY = new FluidIngredient((Fluid) null);
}