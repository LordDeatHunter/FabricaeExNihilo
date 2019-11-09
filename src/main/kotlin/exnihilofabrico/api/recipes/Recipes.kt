package exnihilofabrico.api.recipes

import exnihilofabrico.api.crafting.EntityStack
import exnihilofabrico.api.crafting.FluidStack
import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.api.crafting.TagIngredient
import exnihilofabrico.api.registry.WeightedList
import exnihilofabrico.modules.barrels.BarrelMode
import net.minecraft.entity.EntityType
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.recipe.Ingredient

data class BarrelAlchemyRecipe(val reactant: TagIngredient<Item> = TagIngredient(),
                               val catalyst: TagIngredient<Item>? = null,
                               val product: BarrelMode,
                               val byproduct: Lootable = Lootable.EMPTY,
                               val delay: Int = 0,
                               val toSpawn: EntityStack = EntityStack.EMPTY)
data class BarrelMilkingRecipe(val entity: TagIngredient<EntityType<*>>,
                               val result: FluidStack,
                               val coolDown: Int)

data class CrucibleRecipe(val input: Ingredient,
                          val output: FluidStack,
                          val stone: Boolean = true)

data class ToolRecipe(val ingredient:TagIngredient<Item> = TagIngredient(),
                      val lootables: MutableList<Lootable> = mutableListOf())

data class WitchWaterWorldRecipe(val fluid: TagIngredient<Fluid>,
                                 val results: WeightedList)