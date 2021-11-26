package wraith.fabricaeexnihilo.api.recipes.barrel;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;

public record FluidOnTopRecipe(FluidIngredient inBarrel, FluidIngredient onTop, BarrelMode result) {

    public boolean test(FluidVolume contents, FluidBlock block) {
        return inBarrel.test(contents) && onTop.test(block);
    }

    public boolean test(FluidVolume contents, Block block) {
        return inBarrel.test(contents) && onTop.test(block);
    }

    public boolean test(FluidVolume contents, BlockState block) {
        return inBarrel.test(contents) && onTop.test(block);
    }

}
