package exnihilofabrico.api.recipes

import exnihilofabrico.api.crafting.*
import exnihilofabrico.api.registry.WeightedList
import exnihilofabrico.modules.barrels.BarrelMode
import exnihilofabrico.modules.barrels.EmptyMode
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.recipe.Ingredient
import net.minecraft.village.VillagerProfession
import java.util.function.Predicate

data class BarrelAlchemyRecipe(val reactant: TagIngredient<Item> = TagIngredient(),
                               val catalyst: TagIngredient<Item>? = null,
                               val product: BarrelMode = EmptyMode(),
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

data class WitchWaterEntityRecipe(val target: TagIngredient<EntityType<*>>,
                                  val profession: VillagerProfession?,
                                  val tospawn: EntityType<*>): Predicate<Entity?> {
    override fun test(entity: Entity?): Boolean {
        if(entity == null || entity !is LivingEntity)
            return false
        if(target.test(entity.type)) {
            if(entity is VillagerEntity) {
                return entity.villagerData.profession == profession
            }
            else
                return profession == null

        }
        return false
    }
}