package exnihiloadscensio;

import exnihiloadscensio.blocks.*;
import exnihiloadscensio.tiles.TileBarrel;
import exnihiloadscensio.tiles.TileCrucible;
import exnihiloadscensio.tiles.TileInfestedLeaves;
import exnihiloadscensio.tiles.TileSieve;
import exnihiloadscensio.util.Data;
import exnihiloadscensio.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {


    public static BlockBaseFalling dust;
    public static BlockBaseFalling netherrackCrushed;
    public static BlockBaseFalling endstoneCrushed;
    public static BlockBarrel barrelWood;
    public static BlockBarrel barrelStone;
    public static BlockInfestedLeaves infestedLeaves;
    public static BlockCrucible crucible;
    public static BlockSieve sieve;


    public static void preInit() {
        dust = new BlockBaseFalling(SoundType.CLOTH, "block_dust");
        dust.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
        dust.setHardness(0.7F);

        netherrackCrushed = new BlockBaseFalling(SoundType.GROUND, "block_netherrack_crushed");
        netherrackCrushed.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
        netherrackCrushed.setHardness(0.7F);

        endstoneCrushed = new BlockBaseFalling(SoundType.GROUND, "block_endstone_crushed");
        endstoneCrushed.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
        endstoneCrushed.setHardness(0.7F);

        barrelWood = new BlockBarrel(0, Material.WOOD);
        barrelWood.setCreativeTab(ExNihiloAdscensio.tabExNihilo);

        barrelStone = new BlockBarrel(1, Material.ROCK);
        barrelStone.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
        GameRegistry.registerTileEntity(TileBarrel.class, "block_barrel");

        infestedLeaves = new BlockInfestedLeaves();
        GameRegistry.registerTileEntity(TileInfestedLeaves.class, "block_infested_leaves");
        infestedLeaves.setCreativeTab(ExNihiloAdscensio.tabExNihilo);

        crucible = new BlockCrucible();
        crucible.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
        GameRegistry.registerTileEntity(TileCrucible.class, "block_crucible");

        sieve = new BlockSieve();
        sieve.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
        GameRegistry.registerTileEntity(TileSieve.class, "block_sieve");
    }

    public static void registerBlocks(IForgeRegistry<Block> registry) {
        for (Block block : Data.BLOCKS) {
            registry.register(block);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void initModels(ModelRegistryEvent e) {
        for (Block block : Data.BLOCKS) {
            if (block instanceof IHasModel) {
                ((IHasModel) block).initModel(e);
            }
        }
    }
}
