package exnihilofabrico.impl

import com.swordglowsblue.artifice.api.ArtificeResourcePack
import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.registry.ExNihiloRegistries
import exnihilofabrico.id
import exnihilofabrico.modules.ModBlocks
import net.fabricmc.fabric.api.tag.TagRegistry
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.enchantment.InfoEnchantment
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object EnchantmentTagManager {
    fun getHighestApplicableEnchantmentsAtPower(power: Int, stack: ItemStack, hasTreasure: Boolean): List<InfoEnchantment> {
        val enchantments = getApplicableEnchantments(stack, hasTreasure)

        return enchantments.map { it.getHighestLevelAtPower(power) }
    }


    fun getTagIDForEnchantment(enchantment: Enchantment): Identifier {
        val enchID = Registry.ENCHANTMENT.getId(enchantment) ?: Identifier("empty")
        return id("enchantments/${enchID.namespace}/${enchID.path}")
    }

    fun itemIsAcceptableForEnchantment(stack: ItemStack, enchantment: Enchantment): Boolean {
        return stack.item.isIn(TagRegistry.item(
            getTagIDForEnchantment(
                enchantment
            )
        ))
    }

    /**
     * @return  A list of enchantments for which this item is tagged with the item tag
     *          exnihilofabrico:enchantments/modid/enchantment_id
     */
    fun getApplicableEnchantments(stack: ItemStack, hasTreasure: Boolean): List<Enchantment> =
        Registry.ENCHANTMENT
            .filter { hasTreasure || !it.isTreasure }
            .filter { stack.item.isIn(TagRegistry.item(
                getTagIDForEnchantment(
                    it
                )
            )) }


    fun generateDefaultTags(builder: ArtificeResourcePack.ServerResourcePackBuilder) {
        builder.addItemTag(getTagIDForEnchantment(Enchantments.EFFICIENCY)) { tag ->
            if(ExNihiloFabrico.config.modules.crucibles.efficiency)
                tag.values(*ModBlocks.CRUCIBLES.keys.toTypedArray())
            if(ExNihiloFabrico.config.modules.barrels.efficiency)
                tag.values(*ModBlocks.BARRELS.keys.toTypedArray())
            if(ExNihiloFabrico.config.modules.sieves.efficiency)
                tag.values(*ExNihiloRegistries.MESH.getAll().map { it.identifier }.toTypedArray())
        }
        builder.addItemTag(getTagIDForEnchantment(Enchantments.FIRE_ASPECT)) { tag ->
            if(ExNihiloFabrico.config.modules.crucibles.fireAspect)
                tag.values(*ModBlocks.CRUCIBLES.keys.toTypedArray())
        }
        builder.addItemTag(getTagIDForEnchantment(Enchantments.FORTUNE)) { tag ->
            if(ExNihiloFabrico.config.modules.sieves.fortune)
                tag.values(*ExNihiloRegistries.MESH.getAll().map { it.identifier }.toTypedArray())
        }
    }

    fun mergeInfoLists(first: List<InfoEnchantment>, second: List<InfoEnchantment>): List<InfoEnchantment> {
        val mapping = mutableMapOf<Enchantment, Int>()
        for(set in listOf(first, second))
            set.forEach {
                mapping[it.enchantment] = maxOf(mapping.getOrDefault(it.enchantment, 0), it.level)
            }
        return mapping.map { (k,v) -> InfoEnchantment(k,v) }
    }
}

fun Enchantment.getHighestLevelAtPower(power: Int): InfoEnchantment {
    return (this.maximumLevel downTo this.minimumLevel-1)
        .filter { power >= this.getMinimumPower(it) && power <= this.getMaximumPower(it) }
        .map { InfoEnchantment(this, it) }.first()
}