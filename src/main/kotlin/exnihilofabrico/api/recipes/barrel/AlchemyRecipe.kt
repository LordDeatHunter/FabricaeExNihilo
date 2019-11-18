package exnihilofabrico.api.recipes.barrel

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import exnihilofabrico.api.crafting.EntityStack
import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.crafting.ItemIngredient
import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.modules.barrels.modes.BarrelMode
import exnihilofabrico.modules.barrels.modes.EmptyMode
import net.minecraft.item.ItemStack

data class AlchemyRecipe(val reactant: FluidIngredient = FluidIngredient.EMPTY,
                         val catalyst: ItemIngredient = ItemIngredient.EMPTY,
                         val product: BarrelMode = EmptyMode(),
                         val byproduct: Lootable = Lootable.EMPTY,
                         val delay: Int = 0,
                         val toSpawn: EntityStack = EntityStack.EMPTY) {
    fun test(reactant: FluidVolume, catalyst: ItemStack) =
        reactant.amount >= FluidVolume.BUCKET && this.reactant.test(reactant) && this.catalyst.test(catalyst)
}

