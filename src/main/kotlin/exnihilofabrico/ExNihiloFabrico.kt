package exnihilofabrico

import exnihilofabrico.api.registry.ExNihiloRegistries.CROOK
import exnihilofabrico.api.registry.ExNihiloRegistries.CRUCIBLE_HEAT
import exnihilofabrico.api.registry.ExNihiloRegistries.CRUCIBLE_STONE
import exnihilofabrico.api.registry.ExNihiloRegistries.CRUCIBLE_WOOD
import exnihilofabrico.api.registry.ExNihiloRegistries.HAMMER
import exnihilofabrico.api.registry.ExNihiloRegistries.SIEVE
import exnihilofabrico.api.registry.ExNihiloRegistries.WITCHWATER_ENTITY
import exnihilofabrico.api.registry.ExNihiloRegistries.WITCHWATER_WORLD
import exnihilofabrico.client.renderers.CrucibleBlockEntityRenderer
import exnihilofabrico.client.renderers.SieveBlockEntityRenderer
import exnihilofabrico.common.ModBlocks
import exnihilofabrico.common.ModItems
import exnihilofabrico.content.crucibles.CrucibleBlockEntity
import exnihilofabrico.content.sieves.SieveBlockEntity
import exnihilofabrico.modules.MetaModule
import io.github.cottonmc.cotton.logging.ModLogger
import net.fabricmc.api.*
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

val MODID: String = "exnihilofabrico"
val VERSION: String = "0.0a"

// Small helper functions
fun id(name: String) = Identifier(MODID, name)

@EnvironmentInterface(itf=ClientModInitializer::class, value=EnvType.CLIENT)
object ExNihiloFabrico: ModInitializer, ClientModInitializer {
    val ITEM_GROUP = FabricItemGroupBuilder.build(Identifier(MODID, "general")) { ItemStack(Items.WOODEN_PICKAXE) }
    val LOGGER = ModLogger(MODID, "ExNihiloFabrico")

    override fun onInitialize() {
        loadConfigs()

        /* Register Blocks */
        LOGGER.info("Registering Blocks")
        ModBlocks.registerBlocks(Registry.BLOCK)
        LOGGER.info("Registered Blocks")
        /* Register Items */
        LOGGER.info("Registering Items")
        ModBlocks.registerBlockItems(Registry.ITEM)
        ModItems.registerItems(Registry.ITEM)
        LOGGER.info("Registered Items")

        /* Register Block Entities */
        LOGGER.info("Registering Block Entities")
        ModBlocks.registerBlockEntities(Registry.BLOCK_ENTITY)
        LOGGER.info("Registered Block Entities")

        /* Load Ex Nihilo Fabrico Registries */
        // Barrel Recipes
        // MetaModule.registerBarrelAlchemy(BARREL_ALCHEMY)
        // MetaModule.registerBarrelMilking(BARREL_MILKING)
        // Crucible Recipes
        MetaModule.registerCrucibleHeat(CRUCIBLE_HEAT)
        MetaModule.registerCrucibleStone(CRUCIBLE_STONE)
        MetaModule.registerCrucibleWood(CRUCIBLE_WOOD)
        // Sieve Recipes
        MetaModule.registerSieve(SIEVE)
        // Tool Recipes
        MetaModule.registerHammer(HAMMER)
        MetaModule.registerCrook(CROOK)
        // Witch Water Recipes
        MetaModule.registerWitchWaterEntity(WITCHWATER_ENTITY)
        MetaModule.registerWitchWaterFluid(WITCHWATER_WORLD)
        // Misc. Files
    }

    @Environment(EnvType.CLIENT)
    override fun onInitializeClient() {
        // Register "BE"SRs
        BlockEntityRendererRegistry.INSTANCE.register(SieveBlockEntity::class.java, SieveBlockEntityRenderer())
        BlockEntityRendererRegistry.INSTANCE.register(CrucibleBlockEntity::class.java, CrucibleBlockEntityRenderer())
        LOGGER.info("Registered BESR for Sieve")
    }

    fun loadConfigs() {
        
    }


}