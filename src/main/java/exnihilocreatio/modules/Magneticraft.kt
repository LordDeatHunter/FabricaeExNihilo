package exnihilocreatio.modules

import com.cout970.magneticraft.api.MagneticraftApi
import exnihilocreatio.items.tools.IHammer
import exnihilocreatio.util.Data
import exnihilocreatio.util.isHammer
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.event.FMLInitializationEvent

object Magneticraft: IExNihiloCreatioModule {
    override fun getMODID(): String {
        return "magneticraft"
    }

    override fun init(event: FMLInitializationEvent?) {
        registerExNihiloHammers()
    }

    fun registerExNihiloHammers() {
        for(item in Data.ITEMS){
            if(!isHammer(item))
                continue
            val exHammer = ItemStack(item)

            val level = when(item) {
                is IHammer -> (item as IHammer).getMiningLevel(exHammer)
                else -> item.getHarvestLevel(exHammer, "crushing", null, null)
            }
            val speed = level*2 + 6
            val magneticraftHammer = MagneticraftApi.getHammerRegistry().createHammer(level, speed, 1)
            MagneticraftApi.getHammerRegistry().registerHammer(exHammer, magneticraftHammer)
        }
    }
}