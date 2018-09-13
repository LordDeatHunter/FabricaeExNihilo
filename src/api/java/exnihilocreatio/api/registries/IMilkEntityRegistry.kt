package exnihilocreatio.api.registries

import exnihilocreatio.registries.types.Milkable
import net.minecraft.entity.Entity
import net.minecraftforge.fluids.Fluid

interface IMilkEntityRegistry : IRegistryList<Milkable> {

    fun register(entityOnTop: Entity, result: Fluid, amount: Int, coolDown: Int)
    fun register(entityOnTop: String, result: String, amount: Int, coolDown: Int)

    fun isValidRecipe(entityOnTop: Entity?): Boolean
    fun isValidRecipe(entityOnTop: String?): Boolean

    fun getMilkable(entityOnTop: Entity?): Milkable?

    fun getResult(entityOnTop: Entity): String?

    fun getAmount(entityOnTop: Entity): Int

    fun getCoolDown(entityOnTop: Entity): Int
}
