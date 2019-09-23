package exnihilofabrico.util

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
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object BlockGenerator: IBlockGenerator {
    val MOD_BLACKLIST = mutableSetOf<String>(MODID)

    override fun blacklistMod(modid: String) {
        MOD_BLACKLIST.add(modid)
    }

    override fun createInfestedLeavesBlock(block: LeavesBlock) {
        val originalIdentifier = Registry.BLOCK.getId(block)
        val infestedIdentifier =
            if(originalIdentifier.namespace != "minecraft")
                id("infested_${originalIdentifier.namespace}_${originalIdentifier.path}")
            else
                id("infested_${originalIdentifier.path}")
        ModBlocks.INFESTED_LEAVES[infestedIdentifier] = InfestedLeavesBlock(block, ModBlocks.infestedLeavesSettings)
    }
    override fun createSieveBlock(plankID: Identifier, slabID: Identifier, tex: Identifier) {
        val sieveID =
            if(plankID.namespace != "minecraft")
                id("${plankID.namespace}_"+plankID.path.replace("planks","sieve"))
            else
                id(plankID.path.replace("planks","sieve"))
        ModBlocks.SIEVES[sieveID] = SieveBlock(tex, plankID, slabID, ModBlocks.woodSettings)
    }
    override fun createWoodBarrelBlock(plankID: Identifier, slabID: Identifier, tex: Identifier) {
        val barrelID =
            if(plankID.namespace != "minecraft")
                id("${plankID.namespace}_"+plankID.path.replace("planks","barrel"))
            else
                id(plankID.path.replace("planks","barrel"))
        ModBlocks.BARRELS[barrelID] = BarrelBlock(tex, plankID, slabID, ModBlocks.woodSettings)
    }
    override fun createWoodCrucibleBlock(logID: Identifier, logTex: Identifier) {
        val crucibleID =
            if(logID.namespace != "minecraft")
                id("${logID.namespace}_"+logID.path.replace("log","crucible"))
            else
                id(logID.path.replace("log","crucible"))
        ModBlocks.CRUCIBLES[crucibleID] = CrucibleBlock(logTex, logID, ModBlocks.woodSettings)
    }

    fun initRegistryCallBack() {
        Registry.BLOCK.forEach {
            processBlock(Registry.BLOCK.getId(it), it)
        }
        RegistryEntryAddedCallback.event(Registry.BLOCK).register(
            RegistryEntryAddedCallback<Block>{i, identifier, block -> processBlock(identifier, block)})
    }

    fun processBlock(identifier: Identifier, block: Block) {
        if(MOD_BLACKLIST.contains(identifier.namespace)) return
        if(block.defaultState.material == Material.WOOD && identifier.path.contains("planks")) {
            val planksID = identifier
            val slabID = Identifier(planksID.namespace, planksID.path.replace("planks", "slab"))
            createSieveBlock(planksID, slabID)
            createWoodBarrelBlock(planksID, slabID)
        }
        if(block.defaultState.material == Material.WOOD && identifier.path.contains("log") && !identifier.path.contains("stripped")) {
            createWoodCrucibleBlock(identifier)
        }
        else if(block is LeavesBlock) {
            createInfestedLeavesBlock(block)
        }
    }
}