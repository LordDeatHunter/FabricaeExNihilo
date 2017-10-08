package exnihilocreatio.compatibility.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import exnihilocreatio.blocks.BlockSieve;
import exnihilocreatio.compatibility.crafttweaker.prefab.ENCBaseAdd;
import exnihilocreatio.compatibility.crafttweaker.prefab.ENCBaseRemove;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.exnihilocreatio.Sieve")
@ZenRegister
public class Sieve {

    @ZenMethod
    public static void removeAll(){
        CrTIntegration.removeActions.add(new RemoveAll());
    }

    @ZenMethod
    public static void addStringMeshRecipe(IItemStack block, IItemStack drop, float chance){
        CrTIntegration.addActions.add(new AddRecipe(block, drop, chance, BlockSieve.MeshType.STRING));
    }

    @ZenMethod
    public static void addFlintMeshRecipe(IItemStack block, IItemStack drop, float chance){
        CrTIntegration.addActions.add(new AddRecipe(block, drop, chance, BlockSieve.MeshType.FLINT));
    }

    @ZenMethod
    public static void addIronMeshRecipe(IItemStack block, IItemStack drop, float chance){
        CrTIntegration.addActions.add(new AddRecipe(block, drop, chance, BlockSieve.MeshType.IRON));
    }

    @ZenMethod
    public static void addDiamondMeshRecipe(IItemStack block, IItemStack drop, float chance){
        CrTIntegration.addActions.add(new AddRecipe(block, drop, chance, BlockSieve.MeshType.DIAMOND));
    }

    private static class RemoveAll extends ENCBaseRemove {
        @Override
        public void apply() {
            ExNihiloRegistryManager.SIEVE_REGISTRY.clearRegistry();
        }

        @Override
        public String describe() {
            return "Removing all recipes for the Ex Nihilo Sieve.";
        }
    }

    private static class AddRecipe extends ENCBaseAdd {
        private final IItemStack itemStackIn;
        private final Block block;
        private final IItemStack drop;
        private final float chance;
        private final BlockSieve.MeshType meshType;

        AddRecipe(IItemStack block, IItemStack drop, float chance, BlockSieve.MeshType meshType) {

            Block siftBlock = Block.getBlockFromItem(((ItemStack) block.getInternal()).getItem());
            if (siftBlock != Blocks.AIR){
                this.block = siftBlock;
            }else {
                this.block = null;
            }

            this.drop = drop;
            this.chance = chance;
            this.meshType = meshType;
            itemStackIn = block;
        }

        @Override
        public void apply() {
            ExNihiloRegistryManager.SIEVE_REGISTRY.register(new BlockInfo(block, itemStackIn.getDamage()), new ItemInfo((ItemStack) drop.getInternal()), chance, meshType.getID());
        }

        @Override
        public String describe() {
            if (block == null){
                return "Can't add Sieve recipe for " + itemStackIn.toString() + " as it has no Block";
            }
            return "Adding Sieve recipe for " + itemStackIn.toString() + " for Mesh " + meshType.getName();
        }
    }
}
