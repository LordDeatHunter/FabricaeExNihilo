package exnihilofabrico.api.crafting

import net.minecraft.block.Block
import java.util.*

data class WeightedList(val values: MutableMap<Block, Int> = mutableMapOf()){
    private var totalWeight: Int = values.values.sum()

    constructor(keys: Iterable<Block>, weights: Iterable<Int>) : this(keys.zip(weights).toMap().toMutableMap())

    fun choose(rand: Random): Block {
        var rem = rand.nextInt(totalWeight)
        values.forEach { rem -= it.value; if(rem <= 0) return it.key }
        return values.entries.last().key
    }

    /**
     * Takes another weighted list and adds all its entries to this WeightedList.
     */
    fun amend(other: WeightedList) {
        totalWeight += other.totalWeight
        other.values.forEach { (t, u) -> values[t] = u + (values[t] ?: 0) }
    }
}