package exnihilocreatio.modules.forestry.registry

import exnihilocreatio.util.BlockInfo
import net.minecraft.block.state.IBlockState
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import java.util.*

data class HiveRequirements(
        var hive: BlockInfo = BlockInfo.EMPTY,
        var dimension: Int? = null,
        var allowedBiomes: Set<Int>? = null,
        var minTemperature: Float? = null,
        var maxTemperature: Float? = null,
        var minLight: Int? = null,
        var maxLight: Int? = null,
        var minElevation: Int? = null,
        var maxElevation: Int? = null,
        var adjacentBlocks: Map<Ingredient, Int> = HashMap(),
        var nearbyBlocks: Map<Ingredient, Int> = HashMap()
) {
    // Kotlin doesn't like Lombok @Getter
    constructor(hive: BlockInfo, dim: Int?, biomes: Set<Int>?, adjacent: Map<Ingredient, Int>?, nearby: Map<Ingredient, Int>?)
            : this(hive, dim, biomes, adjacentBlocks = adjacent ?: mapOf(), nearbyBlocks = nearby ?: mapOf())

    fun check(world: World, pos: BlockPos): Boolean {
        // Check dimension
        if (dimension != null && dimension != world.provider.dimension)
            return false
        // Check Biome
        if (allowedBiomes != null && !allowedBiomes!!.contains(Biome.getIdForBiome(world.getBiome(pos))))
            return false

        // Check Temperature
        val temp = world.getBiome(pos).defaultTemperature
        if (!rangeCheck(temp, minTemperature, maxTemperature))
            return false

        // Check Light
        val light = world.getLight(pos).toFloat()
        if (!rangeCheck(light, minLight, maxLight))
            return false

        // Check Elevation
        if (!rangeCheck(pos.y, minElevation, maxElevation))
            return false

        // Check Adjacent Blocks --- could probably be done smarter
        val adjacentStates = getAdjacentBlockStates(world, pos)
        for (ingredient in adjacentBlocks.keys) {
            var req = adjacentBlocks[ingredient] ?: 0
            for (state in adjacentStates) {
                if (ingredient.test(BlockInfo(state).itemStack)) {
                    req -= 1
                }
            }
            if (req > 0) {
                return false
            }
        }
        // Check Nearby Blocks
        val nearby = getNearbyStates(world, pos)
        for (ingredient in nearbyBlocks.keys) {
            var req = nearbyBlocks[ingredient] ?: 0
            // If the same req is in the adjacent block set bump up the req due to double checking
            if (adjacentBlocks.containsKey(ingredient))
                req += adjacentBlocks[ingredient] ?: 0
            for (state in nearby) {
                if (ingredient.test(BlockInfo(state).itemStack)) {
                    req -= 1
                }
            }
            if (req > 0) {
                return false
            }
        }

        return true
    }

    private fun rangeCheck(toTest: Number, min: Number?, max: Number?): Boolean {
        if (min != null && min.toFloat() > toTest.toFloat())
            return false
        return !(max != null && max.toFloat() < toTest.toFloat())
    }

    private fun getAdjacentBlockStates(world: World, pos: BlockPos): Array<IBlockState> {
        return arrayOf(world.getBlockState(pos.up()), world.getBlockState(pos.down()), world.getBlockState(pos.north()), world.getBlockState(pos.south()), world.getBlockState(pos.east()), world.getBlockState(pos.west()))
    }

    private fun getNearbyStates(world: World, pos: BlockPos): List<IBlockState> {
        val nearby = ArrayList<IBlockState>()
        for (i in -3..2)
            for (j in -3..2)
                for (k in -1..0)
                    if (i != 0 || j != 0 || k != 0)
                    // Ignore Center Block, aka the hive
                        if (rangeCheck(pos.y + j, 0, 255))
                        // Ignore blocks above/below build limit
                            nearby.add(world.getBlockState(pos.add(i, j, k)))
        return nearby
    }
}
