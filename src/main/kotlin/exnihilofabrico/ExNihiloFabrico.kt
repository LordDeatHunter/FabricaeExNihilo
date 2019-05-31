package exnihilofabrico

import exnihilofabrico.client.renderers.SieveBlockEntityRenderer
import exnihilofabrico.common.ModBlocks
import exnihilofabrico.common.ModItems
import exnihilofabrico.content.sieves.SieveBlockEntity
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
    }

    @Environment(EnvType.CLIENT)
    override fun onInitializeClient() {
        // Register "BE"SRs
        BlockEntityRendererRegistry.INSTANCE.register(SieveBlockEntity::class.java, SieveBlockEntityRenderer())
        LOGGER.info("Registered BESR for Sieve")
    }

    fun loadConfigs() {
        
    }


}