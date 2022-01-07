package wraith.fabricaeexnihilo.util;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerProfession;

// TODO: Should probably be inlined
public class RegistryUtils {
    
    public static Identifier getId(Item item) {
        return Registry.ITEM.getId(item);
    }
    
    public static Identifier getId(Fluid fluid) {
        return Registry.FLUID.getId(fluid);
    }
    
    public static Identifier getId(Block block) {
        return Registry.BLOCK.getId(block);
    }
    
    public static Identifier getId(StatusEffect statusEffect) {
        return Registry.STATUS_EFFECT.getId(statusEffect);
    }
    
    public static Identifier getId(EntityType<?> entityType) {
        return EntityType.getId(entityType);
    }
    
    public static Identifier getId(Enchantment enchantment) {
        return Registry.ENCHANTMENT.getId(enchantment);
    }
    
    public static Identifier getId(VillagerProfession villagerProfession) {
        return Registry.VILLAGER_PROFESSION.getId(villagerProfession);
    }
    
}
