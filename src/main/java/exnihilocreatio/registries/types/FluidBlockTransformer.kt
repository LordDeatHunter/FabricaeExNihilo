package exnihilocreatio.registries.types

import exnihilocreatio.util.BlockInfo
import exnihilocreatio.util.EntityInfo
import net.minecraft.item.crafting.Ingredient

// TODO: MOVE TO API PACKAGE ON BREAKING VERSION CHANGE
data class FluidBlockTransformer(val fluidName: String,
                                 val input: Ingredient,
                                 val output: BlockInfo,
                                 val toSpawn: EntityInfo = EntityInfo.EMPTY,
                                 val spawnCount: Int = 4,
                                 val spawnRange: Int = 4) {

    @JvmOverloads
    constructor(fluidName: String, input: Ingredient, output: BlockInfo, entityName: String? = null, spawnCount: Int = 4, spawnRange: Int = 4)
            : this(fluidName, input, output, if (entityName == null) EntityInfo.EMPTY else EntityInfo(entityName), spawnCount, spawnRange)

}
