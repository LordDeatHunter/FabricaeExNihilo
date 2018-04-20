package exnihilocreatio.compatibility.crafttweaker;

import crafttweaker.api.item.IItemStack;
import exnihilocreatio.compatibility.crafttweaker.prefab.ENCBaseAdd;
import exnihilocreatio.compatibility.crafttweaker.prefab.ENCBaseRemove;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.exnihilocreatio.Sieve")
public class Hammer {

    @ZenMethod
    public static void removeAll() {
        CrTIntegration.removeActions.add(new RemoveAll());
    }

    @ZenMethod
    public static void addRecipe(IItemStack block, IItemStack drop, int miningLevel, float chance, float fortuneChance) {
        CrTIntegration.addActions.add(new AddRecipe(block, drop, miningLevel, chance, fortuneChance));
    }

    private static class RemoveAll extends ENCBaseRemove {
        @Override
        public void apply() {
            ExNihiloRegistryManager.HAMMER_REGISTRY.clearRegistry();
        }

        @Override
        public String describe() {
            return "Removing all recipes for the Ex Nihilo Hammer.";
        }
    }

    private static class AddRecipe extends ENCBaseAdd {
        private final IBlockState state;
        private final IItemStack blockIn;
        private final IItemStack drop;
        private final int miningLevel;
        private final float chance;
        private final float fortuneChance;

        AddRecipe(IItemStack block, IItemStack drop, int miningLevel, float chance, float fortuneChance) {
            Block hammerBlock = Block.getBlockFromItem(((ItemStack) block.getInternal()).getItem());

            if (hammerBlock != Blocks.AIR) {
                state = hammerBlock.getStateFromMeta(block.getMetadata());
            } else {
                state = null;
            }

            this.blockIn = block;
            this.miningLevel = miningLevel;
            this.fortuneChance = fortuneChance;
            this.drop = drop;
            this.chance = chance;
        }

        @Override
        public void apply() {
            if (state == null) return;

            ExNihiloRegistryManager.HAMMER_REGISTRY.register(state, (ItemStack) drop.getInternal(), miningLevel, chance, fortuneChance);
        }

        @Override
        public String describe() {
            if (state == null) {
                return "Can't add Hammer recipe for " + blockIn + " as it has no Block";
            }
            return "Adding Hammer recipe for " + state + " with drop " + drop + " at a chance of " + chance + " with mining level " + miningLevel;
        }
    }
}
