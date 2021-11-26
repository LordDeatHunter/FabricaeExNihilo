package wraith.fabricaeexnihilo.api.recipes.barrel;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import wraith.fabricaeexnihilo.api.crafting.EntityTypeIngredient;

public record MilkingRecipe(EntityTypeIngredient entity, FluidVolume result, int cooldown) {

    public EntityTypeIngredient getEntity() {
        return entity;
    }

    public FluidVolume getResult() {
        return result;
    }

    public int getCooldown() {
        return cooldown;
    }

}