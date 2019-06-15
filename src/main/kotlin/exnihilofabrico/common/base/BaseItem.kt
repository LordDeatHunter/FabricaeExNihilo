package exnihilofabrico.common.base

import exnihilofabrico.ExNihiloFabrico
import net.minecraft.item.Item

open class BaseItem(settings: Settings): Item(settings.itemGroup(ExNihiloFabrico.ITEM_GROUP)) {

}