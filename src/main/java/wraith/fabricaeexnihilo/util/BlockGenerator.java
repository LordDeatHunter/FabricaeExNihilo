package wraith.fabricaeexnihilo.util;

import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;
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
    public void createInfestedLeavesBlock(LeavesBlock leaves) {
        if (FabricaeExNihilo.CONFIG.modules.silkworms.enabled) {
            var originalIdentifier = Registry.BLOCK.getId(leaves);
            var infestedIdentifier = !originalIdentifier.getNamespace().equals("minecraft")
                    ? FabricaeExNihilo.id("infested_" + originalIdentifier.getNamespace() + "_" + originalIdentifier.getPath())
                    : FabricaeExNihilo.id("infested_" + originalIdentifier.getPath());
            InfestedLeavesBlock block = new InfestedLeavesBlock(leaves, ModBlocks.INFESTED_LEAVES_SETTINGS);
            Registry.register(Registry.BLOCK, infestedIdentifier, block);
            ModBlocks.INFESTED_LEAVES.put(infestedIdentifier, block);
            Registry.register(Registry.ITEM, infestedIdentifier, new InfestedLeavesItem(block, ModItems.BASE_SETTINGS));
        }
    }
    
    public void initRegistryCallBack() {
        Registry.BLOCK.forEach(block -> processBlock(Registry.BLOCK.getId(block), block));
        RegistryEntryAddedCallback.event(Registry.BLOCK).register(((index, identifier, block) -> processBlock(identifier, block)));
    }

    public void processBlock(Identifier identifier, Block block) {
        if(MOD_BLACKLIST.contains(identifier.getNamespace()) || FabricaeExNihilo.CONFIG.modules.generator.blackList.contains(identifier.toString())) {
            return;
        }
        if (block instanceof LeavesBlock leavesBlock && !(block instanceof NonInfestableLeavesBlock)) {
            createInfestedLeavesBlock(leavesBlock);
        }
    }
}
