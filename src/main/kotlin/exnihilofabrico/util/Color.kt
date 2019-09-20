package exnihilofabrico.util

import kotlin.math.sqrt

data class Color(val r: Float, val g: Float, val b: Float, val a: Float) {
    constructor(color: Int, ignoreAlpha: Boolean=true): this(
        (color shr 16 and 255).toFloat() / 255f,
        (color shr 8 and 255).toFloat() / 255f,
        (color and 255).toFloat() / 255f,
        if(ignoreAlpha) 1.0f else (color shr 24 and 255) / 255f
    )
    constructor(hex: String): this(hex.toInt(16), hex.length <= 6)

    fun toInt(): Int {
        return toIntIgnoreAlpha() or ((a*255).toInt() shl 24)
    }

    fun toIntAlphaOne(): Int {
        return toIntIgnoreAlpha() or -16777216
    }

    fun toIntIgnoreAlpha(): Int {
        return ((r*255).toInt() shl 16) or ((g*255).toInt() shl 8) or ((b*255).toInt())
    }

    fun toHexNoAlpha(): String {
        return toIntIgnoreAlpha().toString(16)
    }

    companion object {
        fun average(left: Color, right: Color, leftWeight: Float = 0.5f): Color {
            val rightWeight = 1.0f - leftWeight
            val r = sqrt(left.r*left.r*leftWeight + right.r*right.r*rightWeight)
            val g = sqrt(left.g*left.g*leftWeight + right.g*right.g*rightWeight)
            val b = sqrt(left.b*left.b*leftWeight + right.b*right.b*rightWeight)
            val a = left.a*leftWeight + right.a*rightWeight
            return Color(r,g,b,a)
        }
        @JvmStatic
        val DARK_RED = Color("AA0000")
        @JvmStatic
        val RED = Color("FF5555")
        @JvmStatic
        val GOLD = Color("FFAA00")
        @JvmStatic
        val YELLOW = Color("FFFF55")
        @JvmStatic
        val DARK_GREEN = Color("00AA00")
        @JvmStatic
        val GREEN = Color("55FF55")
        @JvmStatic
        val AQUA = Color("55FFFF")
        @JvmStatic
        val DARK_AQUA = Color("00AAAA")
        @JvmStatic
        val DARK_BLUE = Color("0000AA")
        @JvmStatic
        val BLUE = Color("5555FF")
        @JvmStatic
        val LIGHT_PURPLE = Color("FF55FF")
        @JvmStatic
        val DARK_PURPLE = Color("AA00AA")
        @JvmStatic
        val WHITE = Color("FFFFFF")
        @JvmStatic
        val GRAY = Color("AAAAAA")
        @JvmStatic
        val DARK_GRAY = Color("555555")
        @JvmStatic
        val BLACK = Color("000000")
    }
}

object MetalColors {
    val IRON = Color("BF8040")
    val GOLD = Color("FFFF00")

    val ALUMINUM = Color("BFBFBF")
    val ARDITE = Color("FF751A")
    val BERYLLIUM = Color("E5ECDD")
    val BORON = Color("939393")
    val COBALT = Color("3333FF")
    val COPPER = Color("FF9933")
    val LEAD = Color("330066")
    val LITHIUM = Color("EDEDED")
    val MAGNESIUM = Color("F8DEF8")
    val NICKEL = Color("FFFFCC")
    val SILVER = Color("F2F2F2")
    val TIN = Color("E6FFF2")
    val TITANIUM = Color("BD87CA")
    val THORIUM = Color("333333")
    val TUNGSTEN = Color("1C1C1C")
    val URANIUM = Color("4E5B43")
    val ZINC = Color("A59C74")
    val ZIRCONIUM = Color("E6E8C7")
}