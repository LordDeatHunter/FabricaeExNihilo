package exnihilofabrico.registry.sieve

import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.recipes.SieveRecipe
import exnihilofabrico.api.registry.ISieveRegistry
import exnihilofabrico.compatibility.modules.MetaModule
import exnihilofabrico.registry.AbstractRegistry
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack
import java.io.File
import java.io.FileReader
import java.util.*
import kotlin.collections.ArrayList

data class SieveRegistry(val registry: MutableList<SieveRecipe> = mutableListOf()): AbstractRegistry<MutableList<SieveRecipe>>(), ISieveRegistry {

    override fun clear() = registry.clear()

    override fun getResult(mesh: ItemStack, fluid: Fluid?, sievable: ItemStack, player: PlayerEntity?, rand: Random): List<ItemStack> {
        val allResults = getAllResults(mesh, fluid, sievable)
        val tries = 1 +
                (EnchantmentHelper.getEnchantments(mesh)[Enchantments.FORTUNE] ?: 0) + // Fortune tries
                (player?.luck?.toInt() ?: 0) // Player's luck

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

    override fun register(sieveRecipe: SieveRecipe): Boolean {
        registry.forEach {
            if(it.test(sieveRecipe)){
                it.loot.addAll(sieveRecipe.loot)
                return true
            }
        }
        return registry.add(sieveRecipe)
    }

    override fun getAllRecipes() = registry

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
        val SERIALIZATION_TYPE = object : TypeToken<MutableList<SieveRecipe>>() {}.type
        fun fromJson(file: File) = fromJson(
            file,
            { SieveRegistry() },
            MetaModule::registerSieve
        )
    }
}
