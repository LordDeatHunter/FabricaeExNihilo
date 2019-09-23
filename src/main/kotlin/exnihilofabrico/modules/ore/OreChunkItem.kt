package exnihilofabrico.modules.ore

import exnihilofabrico.modules.base.BaseItem
import exnihilofabrico.modules.base.IHasColor
import exnihilofabrico.util.Color

class OreChunkItem(val properties: OreProperties, settings: Settings): BaseItem(settings), IHasColor {
    override fun getColor(index: Int) =  if(index==1) properties.color.toInt() else Color.WHITE.toInt()
}