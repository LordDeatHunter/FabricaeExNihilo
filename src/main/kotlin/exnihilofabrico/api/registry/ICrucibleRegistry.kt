package exnihilofabrico.api.registry

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import exnihilofabrico.api.crafting.ItemIngredient
import exnihilofabrico.api.recipes.crucible.CrucibleRecipe
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.tag.Tag

interface ICrucibleRegistry: IRegistry<CrucibleRecipe> {
    fun register(input: ItemIngredient, output: FluidVolume) = register(CrucibleRecipe(input, output))
    fun register(input: ItemConvertible, output: FluidVolume) = register(ItemIngredient(input), output)
    fun register(input: Tag<Item>, output: FluidVolume) = register(ItemIngredient(input), output)
    fun register(input: ItemIngredient, fluid: Fluid, amount: Int) = register(input, FluidVolume.create(fluid, amount))
    fun register(input: ItemConvertible, fluid: Fluid, amount: Int) = register(ItemIngredient(input), FluidVolume.create(fluid, amount))
    fun register(input: Tag<Item>, fluid: Fluid, amount: Int) = register(ItemIngredient(input), FluidVolume.create(fluid, amount))

    fun getResult(item: Item): FluidVolume?

    // All recipes, chunked/broken up for pagination
    fun getREIRecipes(): Collection<CrucibleRecipe>
}