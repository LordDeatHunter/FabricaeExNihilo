package exnihilofabrico.api.registry

import java.util.*

class WeightedList<V>(val values: MutableList<Pair<V, Int>>){
    private var totalWeight: Int = values.map { it.second }.sum()
    fun choose(rand: Random): V {
        var rem = rand.nextInt(totalWeight)
        values.forEach { rem -= it.second; if(rem <= 0) return it.first }
        return values.last().first
    }

    /**
     * Takes another weighted list and adds all its entries to this WeightedList.
     */
    fun amend(other: WeightedList<V>) {
        totalWeight += other.totalWeight
        values.addAll(other.values)
    }
}