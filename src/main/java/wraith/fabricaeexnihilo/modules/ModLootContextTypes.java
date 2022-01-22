package wraith.fabricaeexnihilo.modules;

import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextType;
import wraith.fabricaeexnihilo.mixins.LootContextTypesAccess;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class ModLootContextTypes {
    public static final LootContextType STRAINER = new LootContextType.Builder()
            .require(LootContextParameters.BLOCK_ENTITY)
            .require(LootContextParameters.BLOCK_STATE)
            .require(LootContextParameters.ORIGIN)
            .build();
    
    public static void register() {
        LootContextTypesAccess.fen$getMap().put(id("strainer"), STRAINER);
    }
}
