package wraith.fabricaeexnihilo.mixins;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import wraith.fabricaeexnihilo.modules.ModLootContextTypes;

@Mixin(LootContextTypes.class)
public interface LootContextTypesAccess {
    @Accessor("MAP")
    static BiMap<Identifier, LootContextType> fen$getMap() {
        throw new AssertionError();
    }
}
