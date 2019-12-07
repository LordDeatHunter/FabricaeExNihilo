package exnihilofabrico.compatibility.modules.TechReborn

import exnihilofabrico.api.compatibility.IExNihiloFabricoModule
import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.api.registry.*
import exnihilofabrico.id
import exnihilofabrico.util.Color
import exnihilofabrico.util.getExNihiloBlock
import exnihilofabrico.util.getExNihiloItem
import net.minecraft.block.Blocks
import net.minecraft.fluid.Fluids
import net.minecraft.item.ToolMaterials
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object TechReborn: IExNihiloFabricoModule {
    override fun registerAlchemy(registry: IAlchemyRegistry) {
    }

    override fun registerCompost(registry: ICompostRegistry) {
    }

    override fun registerLeaking(registry: ILeakingRegistry) {
    }

    override fun registerFluidOnTop(registry: IFluidOnTopRegistry) {
    }

    override fun registerFluidTransform(registry: IFluidTransformRegistry) {
    }

    override fun registerMilking(registry: IMilkingRegistry) {

    }

    override fun registerCrucibleHeat(registry: ICrucibleHeatRegistry) {
        registry.register(Registry.FLUID.get(Identifier("techreborn:nitro_diesel")), 16)
    }

    override fun registerCrucibleStone(registry: ICrucibleRegistry) {
    }

    override fun registerCrucibleWood(registry: ICrucibleRegistry) {
    }

    override fun registerMesh(registry: IMeshRegistry) {
        registry.register(
            id("carbon_mesh"), ToolMaterials.IRON.enchantability, "item.techreborn.carbon_fiber", Color.BLACK, Identifier("techreborn:carbon_fiber")
        )
    }

    override fun registerSieve(registry: ISieveRegistry) {
        val carbonMesh = getExNihiloItem("carbon_mesh")
        val goldMesh = getExNihiloItem("mesh_gold")
        val diamondMesh = getExNihiloItem("mesh_diamond")

        val crushedNetherrack = getExNihiloBlock("crushed_netherrack")
        val crushedEndstone = getExNihiloBlock("crushed_endstone")

        Registry.ITEM[Identifier("techreborn:ruby_gem")].let { gem ->
            registry.register(goldMesh, Blocks.GRAVEL, Lootable(gem, 0.01))
            registry.register(carbonMesh, Blocks.GRAVEL, Lootable(gem, 0.05))
        }
        Registry.ITEM[Identifier("techreborn:red_garnet_gem")].let { gem ->
            registry.register(diamondMesh, Blocks.GRAVEL, Lootable(gem, 0.01))
            registry.register(carbonMesh, Blocks.GRAVEL, Lootable(gem, 0.05))
        }
        Registry.ITEM[Identifier("techreborn:sapphire_gem")].let { gem ->
            registry.register(diamondMesh, Fluids.WATER, Blocks.GRAVEL, Lootable(gem, 0.01))
            registry.register(carbonMesh, Fluids.WATER, Blocks.GRAVEL, Lootable(gem, 0.05))
        }
        Registry.ITEM[Identifier("techreborn:yellow_garnet_gem")].let { gem ->
            registry.register(diamondMesh, crushedNetherrack, Lootable(gem, 0.01))
            registry.register(carbonMesh, Registry.FLUID[Identifier("techreborn:sodium_persulfate")], crushedNetherrack, Lootable(gem, 0.05))
        }
        Registry.ITEM[Identifier("techreborn:peridot_gem")].let { peridot ->
            registry.register(goldMesh, crushedEndstone, Lootable(peridot, 0.01))
            registry.register(carbonMesh, crushedEndstone, Lootable(peridot, 0.05))
        }
    }

    override fun registerCrook(registry: IToolRegistry) {
    }

    override fun registerHammer(registry: IToolRegistry) {
    }

    override fun registerWitchWaterWorld(registry: IWitchWaterWorldRegistry) {
    }

    override fun registerWitchWaterEntity(registry: IWitchWaterEntityRegistry) {
    }

    override fun registerOres(registry: IOreRegistry) {
        // Stone Materials
        // Bauxite
        // Galena
        // Iridium
        //
        // Nether Materials
        // Cinnabar
        // Pyrite
        //
        // End Materials
        // Sheldonite
        // Sodalite
    }

}