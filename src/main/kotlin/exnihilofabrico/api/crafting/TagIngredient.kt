package exnihilofabrico.api.crafting

import net.minecraft.tag.Tag

class TagIngredient<T>(val tag: Tag<T>) {
    fun test(input: T) {
        tag.contains(input)
    }
}