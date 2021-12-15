package wraith.fabricaeexnihilo.api.registry;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import wraith.fabricaeexnihilo.api.crafting.EntityTypeIngredient;
import wraith.fabricaeexnihilo.api.recipes.barrel.MilkingRecipe;

import java.util.Collection;

public interface MilkingRegistry extends Registry<MilkingRecipe> {

    default boolean register(EntityTypeIngredient entity, FluidVolume fluid, int cooldown) {
        return register(new MilkingRecipe(entity, fluid, cooldown));
    }

    default boolean register(EntityType<?> entity, FluidVolume fluid, int cooldown) {
        return register(new EntityTypeIngredient(entity), fluid, cooldown);
    }

    default boolean register(EntityType<?> entity, Fluid fluid, int amount, int cooldown) {
        return register(new EntityTypeIngredient(entity), FluidVolume.create(fluid, amount), cooldown);
    }

    default boolean register(Tag.Identified<EntityType<?>> entity, Fluid fluid, int amount, int cooldown) {
        return register(new EntityTypeIngredient(entity), FluidVolume.create(fluid, amount), cooldown);
    }

    default boolean register(Tag.Identified<EntityType<?>> entity, FluidVolume fluid, int cooldown) {
        return register(new EntityTypeIngredient(entity), fluid, cooldown);
    }

    default boolean register(EntityType<?> entity, Identifier fluid, int amount, int cooldown) {
        return register(new EntityTypeIngredient(entity), FluidVolume.create(net.minecraft.util.registry.Registry.FLUID.get(fluid), amount), cooldown);
    }

    default boolean register(Tag.Identified<EntityType<?>> entity, Identifier fluid, int amount, int cooldown) {
        return register(new EntityTypeIngredient(entity), FluidVolume.create(net.minecraft.util.registry.Registry.FLUID.get(fluid), amount), cooldown);
    }

    Pair<FluidVolume, Integer> getResult(Entity entity);

    boolean hasResult(Entity entity);

    // All recipes, chunked/broken up for pagination
    Collection<MilkingRecipe> getREIRecipes();

}