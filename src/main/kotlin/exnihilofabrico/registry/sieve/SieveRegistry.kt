package exnihilofabrico.registry.sieve

import com.google.gson.reflect.TypeToken
import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.recipes.SieveRecipe
import exnihilofabrico.api.registry.ISieveRegistry
import exnihilofabrico.compatibility.modules.MetaModule
import exnihilofabrico.compatibility.rei.SieveCategory
import exnihilofabrico.registry.AbstractRegistry
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack
import java.io.File
import java.io.FileReader
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

data class SieveRegistry(val registry: MutableList<SieveRecipe> = mutableListOf()): AbstractRegistry<MutableList<SieveRecipe>>(), ISieveRegistry {

    override fun clear() = registry.clear()

    override fun getResult(mesh: ItemStack, fluid: Fluid?, sievable: ItemStack, player: PlayerEntity?, rand: Random): List<ItemStack> {
        val allResults = getAllResults(mesh, fluid, sievable)
        val tries = 1 + (player?.luck?.toInt() ?: 0) + // Player's luck
                if(ExNihiloFabrico.config.modules.sieves.fortune) // Fortune tries
                    (EnchantmentHelper.getEnchantments(mesh)[Enchantments.FORTUNE] ?: 0)
                else
                    0


        val results: MutableList<ItemStack> = ArrayList()
        for(i in 0 until tries)
            allResults.forEach { loot ->
                val num = loot.chance.count { rand.nextDouble() < it }
                if(num > 0) {
                    val stack = loot.stack.copy()
                    stack.count = num
                    results.add(stack)
                }
            }
        return results
    }

    override fun getAllResults(mesh: ItemStack, fluid: Fluid?, sievable: ItemStack) =
        registry.filter { it.test(mesh, fluid, sievable) }.map { it.loot }.flatten()

    override fun isValidRecipe(mesh: ItemStack, fluid: Fluid?, sievable: ItemStack) =
        registry.any {it.test(mesh, fluid, sievable)}

    override fun isValidMesh(mesh: ItemStack) = registry.any { it.mesh.test(mesh)}

    override fun register(recipe: SieveRecipe): Boolean {
        registry.forEach {
            if(it.test(recipe)){
                it.loot.addAll(recipe.loot)
                return true
            }
        }
        return registry.add(recipe)
    }

    override fun getAllRecipes() = registry
    override fun getREIRecipes() =
        registry.map { recipe ->
            recipe.loot.chunked(SieveCategory.MAX_OUTPUTS) {
                SieveRecipe(recipe.mesh, recipe.fluid, recipe.sievable, it.toMutableList())
            }
        }.flatten()

    override fun serializable() = registry
    override fun registerJson(file: File) {
        if(file.exists()){
            val json: MutableList<SieveRecipe> = gson.fromJson(FileReader(file),
                SERIALIZATION_TYPE
            )
            json.forEach { register(it) }
        }
    }

    companion object {
        val SERIALIZATION_TYPE: Type = object : TypeToken<MutableList<SieveRecipe>>() {}.type
        fun fromJson(file: File) = fromJson(
            file,
            { SieveRegistry() },
            MetaModule::registerSieve
        )
    }
}
