package exnihilofabrico.modules.base

import net.minecraft.enchantment.Enchantment
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

data class EnchantmentContainer(val enchantments: MutableMap<Identifier, Int> = mutableMapOf()): NBTSerializable {

    override fun toTag(): CompoundTag {
        val tag = CompoundTag()
        enchantments.forEach { k,v ->
            tag.putInt(k.toString(), v)
        }
        return tag
    }

    override fun fromTag(tag: CompoundTag) {
        tag.keys.forEach {
            enchantments[Identifier(it)] = tag.getInt(it)
        }
    }

    fun getEnchantmentLevel(enchantment: Enchantment): Int {
        return getEnchantmentLevel(Registry.ENCHANTMENT.getId(enchantment) ?: return 0)
    }

    fun getEnchantmentLevel(enchantment: Identifier): Int {
        return enchantments[enchantment] ?: 0
    }

    fun setEnchantmentLevel(enchantment: Enchantment, level: Int) {
        setEnchantmentLevel(Registry.ENCHANTMENT.getId(enchantment) ?: return, level)
    }

    fun setEnchantmentLevel(enchantment: Identifier, level: Int) {
        enchantments[enchantment] = level
    }

    fun setAllEnchantments(other: EnchantmentContainer) {
        enchantments.clear()
        other.enchantments.forEach { enchantment, level ->
            enchantments[enchantment] = level
        }
    }

}