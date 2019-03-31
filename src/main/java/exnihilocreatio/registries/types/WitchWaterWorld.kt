package exnihilocreatio.registries.types

import exnihilocreatio.util.BlockInfo

data class WitchWaterWorld(val results: MutableList<BlockInfo>, val weights: MutableList<Int>){
    private var totalWeight: Int = weights.sum()

    fun toMap(): Map<BlockInfo, Int> {
        val mapping = HashMap<BlockInfo, Int>()
        for(idx in 0 until results.size)
            mapping[results[idx]] = weights[idx]
        return mapping
    }

    constructor(results: MutableList<BlockInfo>): this(results, results.map { 1 }.toMutableList())

    fun add(result: BlockInfo, weight: Int){
        results.add(result)
        weights.add(weight)
        totalWeight += weight
    }

    fun getResult(chance: Int): BlockInfo {
        if(chance > totalWeight || chance < 1)
            return results.last()
        var remainer = chance
        var idx = -1
        while(remainer > 0){
            idx++
            remainer -= weights[idx]
        }
        return results[idx]
    }

    fun getResult(chance: Float): BlockInfo {
        return getResult((chance * this.totalWeight).toInt() + 1)
    }

    companion object {
        @JvmStatic
        fun fromMap(mapping: Map<BlockInfo, Int>): WitchWaterWorld {
            val keys = mapping.keys.toMutableList()
            val vals = keys.map { mapping[it] ?: 0 }.toMutableList()
            return WitchWaterWorld(keys, vals)
        }
    }
}