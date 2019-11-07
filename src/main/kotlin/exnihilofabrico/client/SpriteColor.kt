package exnihilofabrico.client

import exnihilofabrico.util.Color
import net.minecraft.client.texture.Sprite

data class SpriteColor(val sprite: Sprite?, val color: Int) {
    constructor(sprite: Sprite?, color: Color): this(sprite, color.toInt())
}