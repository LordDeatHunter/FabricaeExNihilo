package exnihilofabrico.api.recipes

import exnihilofabrico.api.crafting.*
import exnihilofabrico.modules.barrels.modes.BarrelMode
import exnihilofabrico.modules.barrels.modes.EmptyMode
import net.minecraft.item.ItemStack

data class AlchemyRecipe(val reactant: FluidIngredient = FluidIngredient.EMPTY,
                         val catalyst: ItemIngredient = ItemIngredient.EMPTY,
                         val product: BarrelMode = EmptyMode(),
                         val byproduct: Lootable = Lootable.EMPTY,
                         val delay: Int = 0,
                         val toSpawn: EntityStack = EntityStack.EMPTY) {
    fun test(reactant: FluidStack, catalyst: ItemStack) =
        reactant.amount >= FluidStack.BUCKET_AMOUNT && this.reactant.test(reactant) && this.catalyst.test(catalyst)
}

