package wraith.fabricaeexnihilo.compatibility;

import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.api.FENApiModule;
import wraith.fabricaeexnihilo.api.FENRegistries;
import wraith.fabricaeexnihilo.util.Color;

public class MythicMetalsApiModule implements FENApiModule {
    @Override
    public void onInit(FENRegistries registries) {
        registries.registerOrePiece("adamantite", registries.defaultItemSettings());
        registries.registerOrePiece("aquarium", registries.defaultItemSettings());
        registries.registerOrePiece("banglum", registries.defaultItemSettings());
        registries.registerOrePiece("carmot", registries.defaultItemSettings());
        registries.registerOrePiece("kyber", registries.defaultItemSettings());
        registries.registerOrePiece("manganese", registries.defaultItemSettings());
        registries.registerOrePiece("midas_gold", registries.defaultItemSettings());
        registries.registerOrePiece("mythril", registries.defaultItemSettings());
        registries.registerOrePiece("orichalcum", registries.defaultItemSettings());
        registries.registerOrePiece("osmium", registries.defaultItemSettings());
        registries.registerOrePiece("palladium", registries.defaultItemSettings());
        registries.registerOrePiece("platinum", registries.defaultItemSettings());
        registries.registerOrePiece("prometheum", registries.defaultItemSettings());
        registries.registerOrePiece("quadrillum", registries.defaultItemSettings());
        registries.registerOrePiece("runite", registries.defaultItemSettings());
        registries.registerOrePiece("silver", registries.defaultItemSettings());
        registries.registerOrePiece("stormyx", registries.defaultItemSettings());
        registries.registerOrePiece("tin", registries.defaultItemSettings());

        registries.registerCrushedBlock("crushed_blackstone", registries.gravelyBlockSettings());
        registries.registerCrushedBlock("crushed_deepslate", registries.gravelyBlockSettings());

        registries.registerMesh("adamantite", Color.DARK_RED, 20, registries.defaultItemSettings());
    }

    @Override
    public boolean shouldLoad() {
        return FabricLoader.getInstance().isModLoaded("mythicmetals");
    }

    @Override
    @Nullable
    public ConditionJsonProvider getResourceCondition() {
        return DefaultResourceConditions.allModsLoaded("mythicmetals");
    }
}
