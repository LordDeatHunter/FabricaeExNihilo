package exnihilofabrico.api

import net.minecraft.block.LeavesBlock
import net.minecraft.util.Identifier

/**
 * Several functions to generate exnihilofabrico blocks based on other blocks
 *
 * These functions if called before Ex Nihilo Fabrico registers its blocks will cause Ex Nihilo Fabrico to register them.
 *
 */
interface IBlockGenerator {
    /**
     * Generated Identifier = exnihilofabric:infested_<blockID.namespace>_<blockID.path>
     */
    fun createInfestedLeavesBlock(block: LeavesBlock)

    /**
     * plankID and slabID are used to generate a crafting recipe
     * Default assumption is the texture shares a name with the planks and is under modid:block
     *
     * Generated Identifier = exnihilofabrico:<plankID.namespace>_<plankID.path> with "planks" replaced by "sieve"
     */
    fun createSieveBlock(plankID: Identifier, slabID: Identifier, tex: Identifier = Identifier(plankID.namespace, "block/${plankID.path}"))

    /**
     * plankID and slabID are used to generate a crafting recipe
     * Default assumption is the texture shares a name with the planks and is under modid:block
     *
     * Generated Identifier = exnihilofabrico:<plankID.namespace>_<plankID.path> with "planks" replaced by "barrel"
     */
    fun createWoodBarrelBlock(plankID: Identifier, slabID: Identifier, tex: Identifier = Identifier(plankID.namespace, "block/${plankID.path}"))

    /**
     * Provide the log you want to be used as the crafting ingredient
     * Default assumption is the texture shares a name and is under modid:block
     *
     * Generated Identifier = exnihilofabrico:<logID.namespace>_<logID.path> with "log" replaced by "crucible"
     */
    fun createWoodCrucibleBlock(logID: Identifier, logTex: Identifier = Identifier(logID.namespace, "block/${logID.path}"))
}