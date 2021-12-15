package wraith.fabricaeexnihilo.util;

import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.ModItems;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlock;
import wraith.fabricaeexnihilo.modules.crucibles.CrucibleBlock;
import wraith.fabricaeexnihilo.modules.infested.InfestedLeavesBlock;
import wraith.fabricaeexnihilo.modules.infested.InfestedLeavesItem;
import wraith.fabricaeexnihilo.modules.infested.NonInfestableLeavesBlock;
import wraith.fabricaeexnihilo.modules.sieves.SieveBlock;

import java.util.HashSet;

public class BlockGenerator implements wraith.fabricaeexnihilo.api.BlockGenerator {

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
                    ? FabricaeExNihilo.id("infested_" + originalIdentifier.getNamespace() + "_" + originalIdentifier.getPath())
                    : FabricaeExNihilo.id("infested_" + originalIdentifier.getPath());
            registerBlockAndItem(new InfestedLeavesBlock(block, ModBlocks.INFESTED_LEAVES_SETTINGS), infestedIdentifier);
        }
    }

    @Override
    public void createSieveBlock(Identifier plankID, Identifier slabID, Identifier tex) {
        var sieveID = !plankID.getNamespace().equals("minecraft")
                ? FabricaeExNihilo.id(plankID.getNamespace() + "_" + plankID.getPath().replace("planks", "sieve"))
                : FabricaeExNihilo.id(plankID.getPath().replace("planks", "sieve"));
        registerBlockAndItem(new SieveBlock(tex, plankID, slabID, ModBlocks.WOOD_SETTINGS), sieveID);
    }

    @Override
    public void createWoodBarrelBlock(Identifier plankID, Identifier slabID, Identifier tex) {
        var barrelID = !plankID.getNamespace().equals("minecraft")
                ? FabricaeExNihilo.id(plankID.getNamespace() + "_" + plankID.getPath().replace("planks", "barrel"))
                : FabricaeExNihilo.id(plankID.getPath().replace("planks", "barrel"));
        registerBlockAndItem(new BarrelBlock(tex, plankID, slabID, ModBlocks.WOOD_SETTINGS), barrelID);
    }

    @Override
    public void createWoodCrucibleBlock(Identifier logID, Identifier logTex) {
        var crucibleID = !logID.getNamespace().equals("minecraft")
                ? FabricaeExNihilo.id(logID.getNamespace() + "_" + logID.getPath().replace("log", "crucible"))
                : FabricaeExNihilo.id(logID.getPath().replace("log", "crucible"));
        registerBlockAndItem(new CrucibleBlock(logTex, logID, ModBlocks.WOOD_SETTINGS), crucibleID);
    }

    private void registerBlockAndItem(Block block, Identifier identifier) {
        if (block instanceof CrucibleBlock crucibleBlock) {
            Registry.register(Registry.BLOCK, identifier, crucibleBlock);
            Registry.register(Registry.ITEM, identifier, new BlockItem(crucibleBlock, ModItems.BASE_SETTINGS));
            ModBlocks.CRUCIBLES.put(identifier, crucibleBlock);
        } else if (block instanceof BarrelBlock barrelBlock) {
            Registry.register(Registry.BLOCK, identifier, barrelBlock);
            Registry.register(Registry.ITEM, identifier, new BlockItem(barrelBlock, ModItems.BASE_SETTINGS));
            ModBlocks.BARRELS.put(identifier, barrelBlock);
        } else if (block instanceof SieveBlock sieveBlock) {
            Registry.register(Registry.BLOCK, identifier, sieveBlock);
            ModBlocks.SIEVES.put(identifier, sieveBlock);
            Registry.register(Registry.ITEM, identifier, new BlockItem(sieveBlock, ModItems.BASE_SETTINGS));
        } else if (block instanceof InfestedLeavesBlock infestedLeavesBlock) {
            Registry.register(Registry.BLOCK, identifier, infestedLeavesBlock);
            ModBlocks.INFESTED_LEAVES.put(identifier, infestedLeavesBlock);
            Registry.register(Registry.ITEM, identifier, new InfestedLeavesItem(infestedLeavesBlock, ModItems.BASE_SETTINGS));
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
