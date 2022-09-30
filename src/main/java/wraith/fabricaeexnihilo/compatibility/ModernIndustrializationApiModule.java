package wraith.fabricaeexnihilo.compatibility;

import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.api.FENApiModule;
import wraith.fabricaeexnihilo.api.FENRegistries;

public class ModernIndustrializationApiModule implements FENApiModule {

    @Override
    public void onInit(FENRegistries registries) {
        registries.registerOrePiece("antimony", registries.defaultItemSettings());
        registries.registerOrePiece("tin", registries.defaultItemSettings());
        registries.registerOrePiece("nickel", registries.defaultItemSettings());
        registries.registerOrePiece("lead", registries.defaultItemSettings());
    }

    @Override
    public boolean shouldLoad() {
        return FabricLoader.getInstance().isModLoaded("modern_industrialization");
    }

    @Override
    @Nullable
    public ConditionJsonProvider getResourceCondition() {
        return DefaultResourceConditions.allModsLoaded("modern_industrialization");
    }
}
