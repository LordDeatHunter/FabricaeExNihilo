package wraith.fabricaeexnihilo.modules.base;

import com.mojang.serialization.Codec;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class EnchantmentContainer {

    private static final Codec<Map<Identifier, Integer>> CODEC = Codec.unboundedMap(Identifier.CODEC, Codec.INT);

    private final Map<Identifier, Integer> enchantments = new HashMap<>();

    public static void addEnchantments(ItemStack itemStack, EnchantmentContainer container, RegistryWrapper.WrapperLookup registries) {
        if (registries == null) return;
        var enchantmentRegistry = registries.getOrThrow(RegistryKeys.ENCHANTMENT);
        container.enchantments.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .forEach(entry -> {
                    var key = RegistryKey.of(RegistryKeys.ENCHANTMENT, entry.getKey());
                    enchantmentRegistry.getOptional(key).ifPresent(ref -> itemStack.addEnchantment(ref, entry.getValue()));
                });
    }

    public Map<Identifier, Integer> getEnchantments() {
        return enchantments;
    }

    public void writeData(WriteView view) {
        view.put("enchantments", CODEC, enchantments);
    }

    public void readData(ReadView view) {
        enchantments.clear();
        view.read("enchantments", CODEC).ifPresent(enchantments::putAll);
    }

    public int getEnchantmentLevel(RegistryKey<Enchantment> key) {
        return getEnchantmentLevel(key.getValue());
    }

    public int getEnchantmentLevel(Identifier enchantment) {
        return enchantments.getOrDefault(enchantment, 0);
    }

    public void setEnchantmentLevel(RegistryKey<Enchantment> key, int level) {
        setEnchantmentLevel(key.getValue(), level);
    }

    public void setEnchantmentLevel(Identifier enchantment, int level) {
        enchantments.put(enchantment, level);
    }
}
