package exnihilofabrico.api

import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

interface IWoodDefinition {
    fun getTexturePlanks(): Identifier
    fun getTextureLog(): Identifier
    fun getTextureLogTop(): Identifier

    fun getLeafID(): Identifier
    fun getLogID(): Identifier
    fun getPlanksID(): Identifier
    fun getSlabID(): Identifier
    fun getSaplingID(): Identifier

    fun getLeafBlock() = Registry.BLOCK[getLeafID()]
    fun getLogBlock() = Registry.BLOCK[getLogID()]
    fun getPlanksBlock() = Registry.BLOCK[getPlanksID()]
    fun getSlabBlock() = Registry.BLOCK[getSlabID()]
    fun getSaplingBlock() = Registry.BLOCK[getSaplingID()]

    // Used for Ex Nihilo Fabrico tree seeds
    fun getSeedID(): Identifier
    fun getSeedItem() = Registry.ITEM[getSeedID()]
}