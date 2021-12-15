package wraith.fabricaeexnihilo.api.registry;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemConvertible;
import net.minecraft.tag.Tag;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.api.recipes.barrel.FluidOnTopRecipe;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.FluidMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.ItemMode;
import wraith.fabricaeexnihilo.util.FluidUtils;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.Collection;

public interface FluidOnTopRegistry extends Registry<FluidOnTopRecipe> {

        default boolean register(FluidIngredient contents, FluidIngredient onTop, BarrelMode result) {
            return register(new FluidOnTopRecipe(contents, onTop, result));
        }

        default boolean register(FluidIngredient contents, FluidIngredient onTop, ItemConvertible result) {
            return register(new FluidOnTopRecipe(contents, onTop, new ItemMode(ItemUtils.asStack(result))));
        }

        default boolean register(Fluid contents, Fluid onTop, ItemConvertible result) {
            return register(new FluidIngredient(contents), new FluidIngredient(onTop), result);
        }

        default boolean register(FluidIngredient contents, FluidIngredient onTop, Fluid result) {
            return register(new FluidOnTopRecipe(contents, onTop, new FluidMode(FluidUtils.asVolume(result))));
        }

        default boolean register(Fluid contents, Tag.Identified<Fluid> onTop, Fluid result) {
            return register(new FluidOnTopRecipe(new FluidIngredient(contents), new FluidIngredient(onTop), new FluidMode(FluidUtils.asVolume(result))));
        }

        default boolean register(Fluid contents, Tag.Identified<Fluid> onTop, ItemConvertible result) {
            return register(new FluidOnTopRecipe(new FluidIngredient(contents), new FluidIngredient(onTop), new ItemMode(ItemUtils.asStack(result))));
        }

        BarrelMode getResult(Fluid contents, Fluid onTop);

        default BarrelMode getResult(FluidVolume contents, Fluid onTop) {
            return getResult(contents.getRawFluid(), onTop);
        }

        // All recipes, chunked/broken up for pagination
        Collection<FluidOnTopRecipe> getREIRecipes();

}