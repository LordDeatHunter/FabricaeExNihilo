package wraith.fabricaeexnihilo.mixins;

import com.google.common.collect.BiMap;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LootContextTypes.class)
public interface LootContextTypesAccess {
    @Accessor("MAP")
    static BiMap<Identifier, LootContextType> fen$getMap() {
        throw new AssertionError();
    }
}
