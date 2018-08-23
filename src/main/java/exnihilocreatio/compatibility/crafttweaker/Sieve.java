package exnihilocreatio.compatibility.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import exnihilocreatio.blocks.BlockSieve;
import exnihilocreatio.compatibility.crafttweaker.prefab.ENCBaseAdd;
import exnihilocreatio.compatibility.crafttweaker.prefab.ENCBaseRemove;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.types.Siftable;
import exnihilocreatio.util.ItemInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.exnihilocreatio.Sieve")
@ZenRegister
public class Sieve {

    @ZenMethod
    public static void removeAll() {
        CrTIntegration.removeActions.add(new RemoveAll());
    }

    @ZenMethod
    public static void addStringMeshRecipe(IIngredient block, IItemStack drop, float chance) {
        CrTIntegration.addActions.add(new AddRecipe(block, drop, chance, BlockSieve.MeshType.STRING));
    }

    @ZenMethod
    public static void addFlintMeshRecipe(IIngredient block, IItemStack drop, float chance) {
        CrTIntegration.addActions.add(new AddRecipe(block, drop, chance, BlockSieve.MeshType.FLINT));
    }

    @ZenMethod
    public static void addIronMeshRecipe(IIngredient block, IItemStack drop, float chance) {
        CrTIntegration.addActions.add(new AddRecipe(block, drop, chance, BlockSieve.MeshType.IRON));
    }

    @ZenMethod
    public static void addDiamondMeshRecipe(IIngredient block, IItemStack drop, float chance) {
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
        private final Ingredient input;
        private final IItemStack drop;
        private final float chance;
        private final BlockSieve.MeshType meshType;

        AddRecipe(IIngredient block, IItemStack drop, float chance, BlockSieve.MeshType meshType) {
            this.input = CraftTweakerMC.getIngredient(block);
            this.drop = drop;
            this.chance = chance;
            this.meshType = meshType;
        }

        @Override
        public void apply() {
            ExNihiloRegistryManager.SIEVE_REGISTRY.register(input, new Siftable(new ItemInfo((ItemStack) drop.getInternal()), chance, meshType.getID()));
        }

        @Override
        public String describe() {
            return "Adding Sieve recipe for " + input + " for Mesh " + meshType.getName();
        }
    }
}
