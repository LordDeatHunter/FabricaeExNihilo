package exnihilocreatio.recipes.defaults;

import exnihilocreatio.ModBlocks;
import exnihilocreatio.blocks.BlockSieve;
import exnihilocreatio.items.ore.ItemOre;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.OreRegistry;
import exnihilocreatio.registries.registries.SieveRegistry;
import exnihilocreatio.texturing.Color;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import lombok.Getter;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class TinkersConstruct implements IRecipeDefaults {
    @Getter
    private final String MODID = "tconstruct";

    public void registerSieve(SieveRegistry registry) {
        OreRegistry oreRegistry = ExNihiloRegistryManager.ORE_REGISTRY;
        ItemOre ardite = oreRegistry.getOreItem("ardite");
        if (ardite != null) {
            registry.register(new BlockInfo(ModBlocks.netherrackCrushed), new ItemInfo(ardite), 0.1f, BlockSieve.MeshType.FLINT.getID());
            registry.register(new BlockInfo(ModBlocks.netherrackCrushed), new ItemInfo(ardite), 0.2f, BlockSieve.MeshType.IRON.getID());
            registry.register(new BlockInfo(ModBlocks.netherrackCrushed), new ItemInfo(ardite), 0.3f, BlockSieve.MeshType.DIAMOND.getID());
        }

        ItemOre cobalt = oreRegistry.getOreItem("cobalt");
        if (cobalt != null) {
            registry.register(new BlockInfo(ModBlocks.netherrackCrushed), new ItemInfo(cobalt), 0.1f, BlockSieve.MeshType.FLINT.getID());
            registry.register(new BlockInfo(ModBlocks.netherrackCrushed), new ItemInfo(cobalt), 0.2f, BlockSieve.MeshType.IRON.getID());
            registry.register(new BlockInfo(ModBlocks.netherrackCrushed), new ItemInfo(cobalt), 0.3f, BlockSieve.MeshType.DIAMOND.getID());
        }
        registry.register(new BlockInfo(ModBlocks.netherrackCrushed), new ItemInfo(Items.BLAZE_POWDER), 0.05f, BlockSieve.MeshType.IRON.getID());
    }

    public void registerOreChunks(OreRegistry registry) {
        Item tconstructIngots = Item.getByNameOrId("tconstruct:ingots");
        if (tconstructIngots != null) {
            registry.register("ardite", new Color("FF751A"), new ItemInfo(tconstructIngots, 1));
            registry.getSieveBlackList().add(ExNihiloRegistryManager.ORE_REGISTRY.getOreItem("ardite")); //Disables the default sieve recipes
            registry.register("cobalt", new Color("3333FF"), new ItemInfo(tconstructIngots));
            registry.getSieveBlackList().add(ExNihiloRegistryManager.ORE_REGISTRY.getOreItem("cobalt")); //Disables the default sieve recipes
        }
    }
}
