package exnihilofabrico.api.registry

import exnihilofabrico.api.crafting.TagIngredient
import exnihilofabrico.api.recipes.CrucibleHeatRecipe
import net.minecraft.block.Block
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.tag.Tag

interface ICrucibleHeatRegistry {
    fun clear()
    fun register(recipe: CrucibleHeatRecipe): Boolean

    fun register(blocks: TagIngredient<Item>, fluids: TagIngredient<Fluid>, heat: Int) = register(CrucibleHeatRecipe(blocks, fluids, heat))

    fun registerFluidTag(tag: Tag<Fluid>, heat: Int) = register(TagIngredient(), TagIngredient(tag), heat)

    fun register(block: Block, heat: Int) = register(TagIngredient.of(block), TagIngredient(), heat)
    fun register(fluid: Fluid, heat: Int) = register(TagIngredient(), TagIngredient.of(fluid), heat)

    fun getHeat(block: Block): Int
    fun getHeat(fluid: Fluid): Int
    fun getHeat(item: Item): Int
    fun getHeat(stack: ItemStack): Int
}