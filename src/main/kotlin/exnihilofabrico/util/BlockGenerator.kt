package exnihilofabrico.util

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.MODID
import exnihilofabrico.api.IBlockGenerator
import exnihilofabrico.id
import exnihilofabrico.modules.ModBlocks
import exnihilofabrico.modules.barrels.BarrelBlock
import exnihilofabrico.modules.crucibles.CrucibleBlock
import exnihilofabrico.modules.infested.InfestedLeavesBlock
import exnihilofabrico.modules.sieves.SieveBlock
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback
import net.minecraft.block.Block
import net.minecraft.block.LeavesBlock
import net.minecraft.block.Material
import net.minecraft.item.BlockItem
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object BlockGenerator: IBlockGenerator {
    val MOD_BLACKLIST = mutableSetOf(MODID)

    override fun blacklistMod(modid: String) {
        MOD_BLACKLIST.add(modid)
    }

    override fun createInfestedLeavesBlock(block: LeavesBlock) {
        if(ExNihiloFabrico.config.modules.silkworms.enabled) {
            val originalIdentifier = Registry.BLOCK.getId(block)
            val infestedIdentifier =
                if(originalIdentifier.namespace != "minecraft")
                    id("infested_${originalIdentifier.namespace}_${originalIdentifier.path}")
                else
                    id("infested_${originalIdentifier.path}")
            registerBlockAndItem(InfestedLeavesBlock(block, ModBlocks.infestedLeavesSettings), infestedIdentifier)
        }
    }
    override fun createSieveBlock(plankID: Identifier, slabID: Identifier, tex: Identifier) {
        val sieveID =
            if(plankID.namespace != "minecraft")
                id("${plankID.namespace}_"+plankID.path.replace("planks","sieve"))
            else
                id(plankID.path.replace("planks","sieve"))
        registerBlockAndItem(SieveBlock(tex, plankID, slabID, ModBlocks.woodSettings), sieveID)
    }
    override fun createWoodBarrelBlock(plankID: Identifier, slabID: Identifier, tex: Identifier) {
        val barrelID =
            if(plankID.namespace != "minecraft")
                id("${plankID.namespace}_"+plankID.path.replace("planks","barrel"))
            else
                id(plankID.path.replace("planks","barrel"))
        registerBlockAndItem(BarrelBlock(tex, plankID, slabID, ModBlocks.woodSettings), barrelID)
    }
    override fun createWoodCrucibleBlock(logID: Identifier, logTex: Identifier) {
        val crucibleID =
            if(logID.namespace != "minecraft")
                id("${logID.namespace}_"+logID.path.replace("log","crucible"))
            else
                id(logID.path.replace("log","crucible"))
        registerBlockAndItem(CrucibleBlock(logTex, logID, ModBlocks.woodSettings), crucibleID)
    }

    private fun registerBlockAndItem(block: Block, identifier: Identifier) {
        Registry.register(Registry.BLOCK, identifier, block)
        Registry.register(Registry.ITEM, identifier, BlockItem(block, ModBlocks.itemSettings))
        when(block) {
            is CrucibleBlock -> ModBlocks.CRUCIBLES[identifier] = block
            is BarrelBlock -> ModBlocks.BARRELS[identifier] = block
            is SieveBlock -> ModBlocks.SIEVES[identifier] = block
            is InfestedLeavesBlock -> ModBlocks.INFESTED_LEAVES[identifier] = block
        }
    }

    fun initRegistryCallBack() {
        Registry.BLOCK.forEach {
            processBlock(Registry.BLOCK.getId(it), it)
        }
        RegistryEntryAddedCallback.event(Registry.BLOCK).register(
            RegistryEntryAddedCallback<Block>{ _, identifier, block -> processBlock(identifier, block)})
    }

    fun processBlock(identifier: Identifier, block: Block) {
        if(MOD_BLACKLIST.contains(identifier.namespace)) return
        if(ExNihiloFabrico.config.modules.generator.blackList.contains(identifier)) return
        if(block.defaultState.material == Material.WOOD && identifier.path.contains("planks")) {
            val slabID = Identifier(identifier.namespace, identifier.path.replace("planks", "slab"))
            createSieveBlock(identifier, slabID)
            createWoodBarrelBlock(identifier, slabID)
        }
        if(block.defaultState.material == Material.WOOD &&
            identifier.path.contains("log") &&
            !identifier.path.contains("stripped") &&
            !identifier.path.contains("quarter") // Suppress Terrestria's quarter blocks
        ) {
            createWoodCrucibleBlock(identifier)
        }
        else if(block is LeavesBlock) {
            createInfestedLeavesBlock(block)
        }
    }
}