package wraith.fabricaeexnihilo.util;

import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.api.IBlockGenerator;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlock;
import wraith.fabricaeexnihilo.modules.crucibles.CrucibleBlock;
import wraith.fabricaeexnihilo.modules.infested.InfestedLeavesBlock;
import wraith.fabricaeexnihilo.modules.infested.NonInfestableLeavesBlock;
import wraith.fabricaeexnihilo.modules.sieves.SieveBlock;

import java.util.HashSet;

public class BlockGenerator implements IBlockGenerator {

    public static final BlockGenerator INSTANCE = new BlockGenerator();

    private BlockGenerator() {}
    
    public final HashSet<String> MOD_BLACKLIST = new HashSet<>();

    public void blacklistMod(String modid) {
        MOD_BLACKLIST.add(modid);
    }

    @Override
    public void createInfestedLeavesBlock(LeavesBlock block) {
        if (FabricaeExNihilo.CONFIG.modules.silkworms.enabled) {
            var originalIdentifier = Registry.BLOCK.getId(block);
            var infestedIdentifier = !originalIdentifier.getNamespace().equals("minecraft")
                    ? FabricaeExNihilo.ID("infested_" + originalIdentifier.getNamespace() + "_" + originalIdentifier.getPath())
                    : FabricaeExNihilo.ID("infested_" + originalIdentifier.getPath());
            registerBlockAndItem(new InfestedLeavesBlock(block, ModBlocks.INFESTED_LEAVES_SETTINGS), infestedIdentifier);
        }
    }

    @Override
    public void createSieveBlock(Identifier plankID, Identifier slabID, Identifier tex) {
        var sieveID = !plankID.getNamespace().equals("minecraft")
                ? FabricaeExNihilo.ID(plankID.getNamespace() + "_" + plankID.getPath().replace("planks", "sieve"))
                : FabricaeExNihilo.ID(plankID.getPath().replace("planks", "sieve"));
        registerBlockAndItem(new SieveBlock(tex, plankID, slabID, ModBlocks.WOOD_SETTINGS), sieveID);
    }

    @Override
    public void createWoodBarrelBlock(Identifier plankID, Identifier slabID, Identifier tex) {
        var barrelID = !plankID.getNamespace().equals("minecraft")
                ? FabricaeExNihilo.ID(plankID.getNamespace() + "_" + plankID.getPath().replace("planks", "barrel"))
                : FabricaeExNihilo.ID(plankID.getPath().replace("planks", "barrel"));
        registerBlockAndItem(new BarrelBlock(tex, plankID, slabID, ModBlocks.WOOD_SETTINGS), barrelID);
    }

    @Override
    public void createWoodCrucibleBlock(Identifier logID, Identifier logTex) {
        var crucibleID = !logID.getNamespace().equals("minecraft")
                ? FabricaeExNihilo.ID(logID.getNamespace() + "_" + logID.getPath().replace("log", "crucible"))
                : FabricaeExNihilo.ID(logID.getPath().replace("log", "crucible"));
        registerBlockAndItem(new CrucibleBlock(logTex, logID, ModBlocks.WOOD_SETTINGS), crucibleID);
    }

    private void registerBlockAndItem(Block block, Identifier identifier) {
        Registry.register(Registry.BLOCK, identifier, block);
        Registry.register(Registry.ITEM, identifier, new BlockItem(block, ModBlocks.ITEM_SETTINGS));
        if (block instanceof CrucibleBlock crucibleBlock) {
            ModBlocks.CRUCIBLES.put(identifier, crucibleBlock);
        }
        if (block instanceof BarrelBlock barrelBlock) {
            ModBlocks.BARRELS.put(identifier, barrelBlock);
        }
        if (block instanceof SieveBlock sieveBlock) {
            ModBlocks.SIEVES.put(identifier, sieveBlock);
        }
        if (block instanceof InfestedLeavesBlock infestedLeavesBlock) {
            ModBlocks.INFESTED_LEAVES.put(identifier, infestedLeavesBlock);
        }
    }

    public void initRegistryCallBack() {
        Registry.BLOCK.forEach(block -> processBlock(Registry.BLOCK.getId(block), block));
        RegistryEntryAddedCallback.event(Registry.BLOCK).register(((index, identifier, block) -> processBlock(identifier, block)));
    }

    public void processBlock(Identifier identifier, Block block) {
        if(MOD_BLACKLIST.contains(identifier.getNamespace()) || FabricaeExNihilo.CONFIG.modules.generator.blackList.contains(identifier)) {
            return;
        }
        if(block.getDefaultState().getMaterial() == Material.WOOD && identifier.getPath().contains("planks")) {
            var slabID = new Identifier(identifier.getNamespace(), identifier.getPath().replace("planks", "slab"));
            createSieveBlock(identifier, slabID);
            createWoodBarrelBlock(identifier, slabID);
        }
        if(block.getDefaultState().getMaterial() == Material.WOOD &&
                identifier.getPath().contains("log") &&
                !identifier.getPath().contains("stripped") &&
                !identifier.getPath().contains("quarter") // Suppress Terrestria's quarter blocks
        ) {
            createWoodCrucibleBlock(identifier);
        }
        else if(block instanceof LeavesBlock leavesBlock && !(block instanceof NonInfestableLeavesBlock)) {
            createInfestedLeavesBlock(leavesBlock);
        }
    }

}
