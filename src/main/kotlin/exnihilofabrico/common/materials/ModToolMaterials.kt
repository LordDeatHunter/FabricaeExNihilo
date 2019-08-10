package exnihilofabrico.common.materials

import net.minecraft.item.Items
import net.minecraft.item.ToolMaterial
import net.minecraft.item.ToolMaterials
import net.minecraft.recipe.Ingredient

enum class ModToolMaterials(
    private val miningLevel: Int,
    private val durability: Int,
    private val miningSpeed: Float,
    private val attackDamage: Float,
    private val enchantability: Int,
    private val repairIngredient: Ingredient
): ToolMaterial {
    WOOD(ToolMaterials.WOOD),
    ANDESITE(ToolMaterials.STONE),
    DIORITE(ToolMaterials.STONE),
    GRANITE(ToolMaterials.STONE),
    STONE(ToolMaterials.STONE),
    BONE(1, 131, 4.0f, 1.0f, 5, Ingredient.ofItems(Items.BONE)),
    BLAZE(3, 1561, 6.0f, 2.0f, 22, Ingredient.ofItems(Items.BLAZE_ROD)),
    CLAY(1, 250, 6.0f, 2.0f, 14, Ingredient.ofItems(Items.BRICK)),
    CLAY_UNCOOKED(2, 1, 1.0f, 0.1f, 0, Ingredient.ofItems(Items.CLAY_BALL)),
    PURPUR(3, 1561, 8.0f, 3.0f, 10, Ingredient.ofItems(Items.POPPED_CHORUS_FRUIT, Items.CHORUS_FRUIT)),
    PRISMARINE(1, 131, 4.0f, 1.0f, 5, Ingredient.ofItems(Items.PRISMARINE, Items.PRISMARINE_SHARD));

    override fun getMiningLevel() = miningLevel
    override fun getEnchantability() = enchantability
    override fun getAttackDamage() = attackDamage
    override fun getDurability() = durability
    override fun getMiningSpeed(): Float = miningSpeed
    override fun getRepairIngredient() = repairIngredient

    constructor(material: ToolMaterial):
            this(material.miningLevel,
                material.durability,
                material.miningSpeed,
                material.attackDamage,
                material.enchantability,
                material.repairIngredient)
}