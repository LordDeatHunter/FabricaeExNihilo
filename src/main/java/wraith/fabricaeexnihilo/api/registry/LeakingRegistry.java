package wraith.fabricaeexnihilo.api.registry;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.tag.Tag;
import net.minecraft.util.Pair;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;
import wraith.fabricaeexnihilo.api.recipes.barrel.LeakingRecipe;

import java.util.Collection;

public interface LeakingRegistry extends Registry<LeakingRecipe> {

    default boolean register(ItemIngredient target, FluidIngredient fluid, int loss, Block result) {
        return register(LeakingRecipe.builder()
                .withTarget(target)
                .withFluid(fluid)
                .withLoss(FluidAmount.of1620(loss))
                .withResult(result)
                .build());
    }

    default boolean register(ItemIngredient target, FluidVolume fluid, Block result) {
        return register(LeakingRecipe.builder()
                .withTarget(target)
                .withFluid(new FluidIngredient(fluid))
                .withLoss(fluid.amount())
                .withResult(result)
                .build());
    }

    default boolean register(ItemConvertible target, Fluid fluid, int amount, Block result) {
        return register(LeakingRecipe.builder()
                .withTarget(new ItemIngredient(target))
                .withFluid(new FluidIngredient(fluid))
                .withLoss(FluidAmount.of1620(amount))
                .withResult(result)
                .build());
    }

    default boolean register(Tag.Identified<Item> target, Fluid fluid, int amount, Block result) {
        return register(LeakingRecipe.builder()
                .withTarget(new ItemIngredient(target))
                .withFluid(new FluidIngredient(fluid))
                .withLoss(FluidAmount.of1620(amount))
                .withResult(result)
                .build());
    }

    default boolean register(Tag.Identified<Item> target, Tag.Identified<Fluid> fluid, int amount, Block result) {
        return register(LeakingRecipe.builder()
                .withTarget(new ItemIngredient(target))
                .withFluid(new FluidIngredient(fluid))
                .withLoss(FluidAmount.of1620(amount))
                .withResult(result)
                .build());
    }

    default boolean register(ItemConvertible target, Tag.Identified<Fluid> fluid, int amount, Block result) {
        return register(LeakingRecipe.builder()
                .withTarget(new ItemIngredient(target))
                .withFluid(new FluidIngredient(fluid))
                .withLoss(FluidAmount.of1620(amount))
                .withResult(result)
                .build());
    }

    /**
     * Returns the block to transform the input block into, and the amount to drain.
     */
    Pair<Block, FluidAmount> getResult(Block block, FluidVolume fluid);

    // All recipes, chunked/broken up for pagination
    Collection<LeakingRecipe> getREIRecipes();

}
