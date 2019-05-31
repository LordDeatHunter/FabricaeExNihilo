package exnihilofabrico.content.base

import exnihilofabrico.ExNihiloFabrico
import net.minecraft.item.Item

abstract class BaseItem(settings: Settings): Item(settings.itemGroup(ExNihiloFabrico.ITEM_GROUP)) {

}