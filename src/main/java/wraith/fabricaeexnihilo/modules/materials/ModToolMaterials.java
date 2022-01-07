package wraith.fabricaeexnihilo.modules.materials;

import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;

import java.util.function.Supplier;

public enum ModToolMaterials implements ToolMaterial {
    WOODEN(ToolMaterials.WOOD),
    ANDESITE(ToolMaterials.STONE),
    DIORITE(ToolMaterials.STONE),
    GRANITE(ToolMaterials.STONE),
    DEEPSLATE(ToolMaterials.STONE),
    BLACKSTONE(ToolMaterials.STONE),
    BASALT(ToolMaterials.STONE),
    STONE(ToolMaterials.STONE),
    BONE(1, 131, 4.0f, 1.0f, 5, () -> Ingredient.ofItems(Items.BONE)),
    BLAZE(3, 1561, 6.0f, 2.0f, 22, () -> Ingredient.ofItems(Items.BLAZE_ROD)),
    CLAY(1, 250, 6.0f, 2.0f, 14, () -> Ingredient.ofItems(Items.BRICK)),
    UNCOOKED_CLAY(2, 1, 1.0f, 0.1f, 0, () -> Ingredient.ofItems(Items.CLAY_BALL)),
    PURPUR(3, 1561, 8.0f, 3.0f, 10, () -> Ingredient.ofItems(Items.POPPED_CHORUS_FRUIT, Items.CHORUS_FRUIT)),
    PRISMARINE(1, 131, 4.0f, 1.0f, 5, () -> Ingredient.ofItems(Items.PRISMARINE, Items.PRISMARINE_SHARD));
    
    private final int miningLevel;
    private final int durability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;
    
    ModToolMaterials(int miningLevel, int durability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.miningLevel = miningLevel;
        this.durability = durability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
    }
    
    ModToolMaterials(ToolMaterials material) {
        this(material.getMiningLevel(),
                material.getDurability(),
                material.getMiningSpeedMultiplier(),
                material.getAttackDamage(),
                material.getEnchantability(),
                material::getRepairIngredient
        );
    }
    
    @Override
    public int getMiningLevel() {
        return this.miningLevel;
    }
    
    @Override
    public int getDurability() {
        return this.durability;
    }
    
    @Override
    public float getMiningSpeedMultiplier() {
        return this.miningSpeed;
    }
    
    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }
    
    @Override
    public int getEnchantability() {
        return this.enchantability;
    }
    
    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
    
}