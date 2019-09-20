package exnihilofabrico.util

import exnihilofabrico.api.IBlockGenerator
import exnihilofabrico.common.ModBlocks
import exnihilofabrico.common.barrels.BarrelBlock
import exnihilofabrico.common.crucibles.CrucibleBlock
import exnihilofabrico.common.infested.InfestedLeavesBlock
import exnihilofabrico.common.sieves.SieveBlock
import exnihilofabrico.id
import net.minecraft.block.LeavesBlock
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object BlockGenerator: IBlockGenerator {
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
}