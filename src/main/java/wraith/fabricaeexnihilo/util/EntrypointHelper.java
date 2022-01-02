package wraith.fabricaeexnihilo.util;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.api.FabricaeExNihiloApiModule;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlock;
import wraith.fabricaeexnihilo.modules.crucibles.CrucibleBlock;
import wraith.fabricaeexnihilo.modules.infested.InfestedLeavesBlock;
import wraith.fabricaeexnihilo.modules.infested.InfestingLeavesBlock;
import wraith.fabricaeexnihilo.modules.sieves.SieveBlock;

import java.util.List;
import java.util.function.BiConsumer;

public class EntrypointHelper {
    public static void callEntrypoints() {
        var entrypoints = FabricLoader.getInstance().getEntrypoints("fabricaeexnihilo:api", FabricaeExNihiloApiModule.class).stream().filter(FabricaeExNihiloApiModule::shouldLoad).toList();
        
        handle(entrypoints, FabricaeExNihiloApiModule::registerOres, FabricaeExNihilo.ORES::putIfAbsent);
        handle(entrypoints, FabricaeExNihiloApiModule::registerMeshes, FabricaeExNihilo.MESHES::putIfAbsent);
        handle(entrypoints, FabricaeExNihiloApiModule::registerSieves, id -> ModBlocks.SIEVES.put(id, new SieveBlock()));
        handle(entrypoints, FabricaeExNihiloApiModule::registerCrucibles, id -> ModBlocks.CRUCIBLES.put(id, new CrucibleBlock(ModBlocks.WOOD_SETTINGS)));
        handle(entrypoints, FabricaeExNihiloApiModule::registerBarrels, id -> ModBlocks.BARRELS.put(id, new BarrelBlock(ModBlocks.WOOD_SETTINGS)));
        handle(entrypoints, FabricaeExNihiloApiModule::registerInfestedLeaves, (base, id) -> {
            var infested = new InfestedLeavesBlock(base, ModBlocks.INFESTED_LEAVES_SETTINGS);
            ModBlocks.INFESTED_LEAVES.put(new Identifier(id.getNamespace(), "infested_" + id.getPath()), infested);
            ModBlocks.INFESTING_LEAVES.put(new Identifier(id.getNamespace(), "infesting_" + id.getPath()), new InfestingLeavesBlock(ModBlocks.INFESTED_LEAVES_SETTINGS, infested));
        });
    }
    
    private static <R> void handle(List<FabricaeExNihiloApiModule> entrypoints, BiConsumer<FabricaeExNihiloApiModule, R> function, R registry) {
        entrypoints.forEach(entrypoint -> function.accept(entrypoint, registry));
    }
}
