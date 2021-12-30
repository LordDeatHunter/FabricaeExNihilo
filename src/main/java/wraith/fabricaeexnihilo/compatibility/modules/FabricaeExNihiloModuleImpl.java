package wraith.fabricaeexnihilo.compatibility.modules;

import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.api.compatibility.FabricaeExNihiloModule;
import wraith.fabricaeexnihilo.recipe.util.Loot;
import wraith.fabricaeexnihilo.api.registry.*;
import wraith.fabricaeexnihilo.modules.ModItems;
import wraith.fabricaeexnihilo.modules.ore.ChunkMaterial;
import wraith.fabricaeexnihilo.modules.ore.ChunkShape;
import wraith.fabricaeexnihilo.modules.ore.PieceShape;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterFluid;
import wraith.fabricaeexnihilo.util.Color;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.Arrays;
import java.util.stream.Stream;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public final class FabricaeExNihiloModuleImpl implements FabricaeExNihiloModule {

    private FabricaeExNihiloModuleImpl() {}

    public static final FabricaeExNihiloModuleImpl INSTANCE = new FabricaeExNihiloModuleImpl();
    
    @Override
    public void registerOres(OreRecipeRegistry registry) {
        // TODO: Implement tag checking to prevent creation of unnecessary ores
        // Vanilla Metals
        registry.register("iron", Color.IRON, PieceShape.NORMAL, ChunkShape.CHUNK, ChunkMaterial.GRANITE);
        registry.register("gold", Color.GOLD, PieceShape.FINE, ChunkShape.CHUNK, ChunkMaterial.STONE);

        // Modded Metals
        registry.register("aluminum",  Color.ALUMINUM,  PieceShape.FINE,   ChunkShape.CHUNK, ChunkMaterial.SAND);
        registry.register("ardite",    Color.ARDITE,    PieceShape.COARSE, ChunkShape.CHUNK, ChunkMaterial.NETHERRACK);
        registry.register("beryllium", Color.BERYLLIUM, PieceShape.NORMAL, ChunkShape.FLINT, ChunkMaterial.STONE);
        registry.register("boron",     Color.BORON,     PieceShape.COARSE, ChunkShape.CHUNK, ChunkMaterial.SAND);
        registry.register("cobalt",    Color.COBALT,    PieceShape.COARSE, ChunkShape.LUMP,  ChunkMaterial.NETHERRACK);
        registry.register("copper",    Color.COPPER,    PieceShape.NORMAL, ChunkShape.CHUNK, ChunkMaterial.STONE);
        registry.register("lead",      Color.LEAD,      PieceShape.COARSE, ChunkShape.LUMP,  ChunkMaterial.STONE);
        registry.register("lithium",   Color.LITHIUM,   PieceShape.FINE,   ChunkShape.FLINT, ChunkMaterial.SAND);
        registry.register("magnesium", Color.MAGNESIUM, PieceShape.COARSE, ChunkShape.LUMP,  ChunkMaterial.STONE);
        registry.register("nickel",    Color.NICKEL,    PieceShape.COARSE, ChunkShape.LUMP,  ChunkMaterial.STONE);
        registry.register("silver",    Color.SILVER,    PieceShape.NORMAL, ChunkShape.CHUNK, ChunkMaterial.STONE);
        registry.register("tin",       Color.TIN,       PieceShape.NORMAL, ChunkShape.LUMP,  ChunkMaterial.DIORITE);
        registry.register("titanium",  Color.TITANIUM,  PieceShape.COARSE, ChunkShape.CHUNK, ChunkMaterial.STONE);
        registry.register("thorium",   Color.THORIUM,   PieceShape.COARSE, ChunkShape.LUMP,  ChunkMaterial.STONE);
        registry.register("tungsten",  Color.TUNGSTEN,  PieceShape.COARSE, ChunkShape.CHUNK, ChunkMaterial.NETHERRACK);
        registry.register("uranium",   Color.URANIUM,   PieceShape.COARSE, ChunkShape.LUMP,  ChunkMaterial.STONE);
        registry.register("zinc",      Color.ZINC,      PieceShape.FINE,   ChunkShape.FLINT, ChunkMaterial.ANDESITE);
        registry.register("zirconium", Color.ZIRCONIUM, PieceShape.NORMAL, ChunkShape.FLINT, ChunkMaterial.ANDESITE);
    }

    @Override
    public void registerMesh(MeshRecipeRegistry registry) {
        registry.register(
                id("mesh_string"),
                ToolMaterials.WOOD.getEnchantability(),
                "item.minecraft.string",
                Color.WHITE,
                new Identifier("string")
        );
        registry.register(
                id("mesh_flint"),
                ToolMaterials.STONE.getEnchantability(),
                "item.minecraft.flint",
                Color.DARK_GRAY,
                new Identifier("flint")
        );
        registry.register(
                id("mesh_iron"),
                ToolMaterials.IRON.getEnchantability(),
                "Iron",
                new Color("777777"),
                new Identifier("iron_ingot")
        );
        registry.register(
                id("mesh_gold"),
                ToolMaterials.GOLD.getEnchantability(),
                "Gold",
                Color.GOLDEN,
                new Identifier("gold_ingot")
        );
        registry.register(
                id("mesh_diamond"),
                ToolMaterials.DIAMOND.getEnchantability(),
                "item.minecraft.diamond",
                Color.DARK_AQUA,
                new Identifier("diamond")
        );
    }
    
    @Override
    public void registerSieve(SieveRecipeRegistry registry) {
        var ironMesh = Registry.ITEM.get(id("mesh_iron"));
        var goldMesh = Registry.ITEM.get(id("mesh_gold"));
        var diamondMesh = Registry.ITEM.get(id("mesh_diamond"));

        var crushedNetherrack = ItemUtils.getExNihiloBlock("crushed_netherrack");
        var crushedPrismarine = ItemUtils.getExNihiloBlock("crushed_prismarine");
        var crushedEndstone = ItemUtils.getExNihiloBlock("crushed_endstone");

        var crushedAndesite = ItemUtils.getExNihiloBlock("crushed_andesite");
        var crushedDiorite = ItemUtils.getExNihiloBlock("crushed_diorite");
        var crushedGranite = ItemUtils.getExNihiloBlock("crushed_granite");
        
        for(var ore : FabricaeExNihiloRegistries.ORES.getAll()) {
            if(!Registry.ITEM.containsId(ore.getPieceID())) {
                continue;
            }
        
            switch(ore.getMaterial()) {
                case "iron", "gold" -> {

                }
                default -> {
                    var sieveable = switch (ore.getChunkMaterial()) {
                        case ANDESITE -> crushedAndesite;
                        case DIORITE -> crushedDiorite;
                        case ENDSTONE -> crushedEndstone;
                        case GRANITE -> crushedGranite;
                        case NETHERRACK -> crushedNetherrack;
                        case PRISMARINE -> crushedPrismarine;
                        case REDSAND -> Blocks.RED_SAND;
                        case SAND -> Blocks.SAND;
                        case SOULSAND -> Blocks.SOUL_SAND;
                        case STONE -> Blocks.GRAVEL;
                    };
                    registry.register(ironMesh, sieveable, new Loot(ore.getPieceID(), 0.05));
                    registry.register(goldMesh, sieveable, new Loot(ore.getPieceID(), 0.075));
                    registry.register(diamondMesh, sieveable, new Loot(ore.getPieceID(), 0.1));
                }
            }
        }
    }
}
