package exnihilofabrico.api.registry

import exnihilofabrico.modules.ore.EnumChunkMaterial
import exnihilofabrico.modules.ore.EnumChunkShape
import exnihilofabrico.modules.ore.EnumPieceShape
import exnihilofabrico.modules.ore.OreProperties
import exnihilofabrico.util.Color
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry

interface IOreRegistry: IRegistry<OreProperties> {
    fun getAll(): List<OreProperties>

    fun register(vararg properties: OreProperties): Boolean
    fun register(material: String, color: Color, pieceShape: EnumPieceShape, chunkShape: EnumChunkShape, chunkMaterial: EnumChunkMaterial): Boolean =
        register(OreProperties(material, color, pieceShape, chunkShape, chunkMaterial))

    fun registerPieceItems(itemRegistry: Registry<Item>)
    fun registerChunkItems(itemRegistry: Registry<Item>)


    fun getPropertiesForModel(identifier: ModelIdentifier): OreProperties?
}