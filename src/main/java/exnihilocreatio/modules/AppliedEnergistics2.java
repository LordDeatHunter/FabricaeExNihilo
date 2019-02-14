package exnihilocreatio.modules;

import exnihilocreatio.ModBlocks;
import exnihilocreatio.blocks.BlockBaseFalling;
import exnihilocreatio.blocks.BlockSieve;
import exnihilocreatio.recipes.defaults.IRecipeDefaults;
import exnihilocreatio.registries.registries.FluidBlockTransformerRegistry;
import exnihilocreatio.registries.registries.HammerRegistry;
import exnihilocreatio.registries.registries.SieveRegistry;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class AppliedEnergistics2 implements IExNihiloCreatioModule, IRecipeDefaults {
    @Getter
    public final String MODID = "appliedenergistics2";
    // BlockBaseFalling adds the block to Data.Blocks
    public static final BlockBaseFalling skystoneCrushed = (BlockBaseFalling) new BlockBaseFalling(SoundType.GROUND, "block_skystone_crushed").setHardness(0.7F);


    @GameRegistry.ObjectHolder("appliedenergistics2:material")
    // Certus = 0, Charged Certus = 1, Sky stone dust = 45
    private static final Item AE_MATERIAL = null;
    @GameRegistry.ObjectHolder("appliedenergistics2:crystal_seed")
    // Pure Certus = 0
    private static final Item AE_SEEDS = null;
    @GameRegistry.ObjectHolder("appliedenergistics2:sky_stone_block")
    private static final Block SKY_STONE = null;

    public void registerSieve(SieveRegistry registry) {
        // Sky Stone Dust
        registry.register(new BlockInfo(ModBlocks.dust), new ItemInfo(AE_MATERIAL, 45), 0.1f, BlockSieve.MeshType.FLINT.getID());
        registry.register(new BlockInfo(ModBlocks.dust), new ItemInfo(AE_MATERIAL, 45), 0.2f, BlockSieve.MeshType.IRON.getID());
        registry.register(new BlockInfo(ModBlocks.dust), new ItemInfo(AE_MATERIAL, 45), 0.3f, BlockSieve.MeshType.DIAMOND.getID());

        // Certus Quartz
        ItemInfo stack = new ItemInfo(AE_MATERIAL);
        registry.register(new BlockInfo(skystoneCrushed), stack, 0.01f, BlockSieve.MeshType.IRON.getID());
        registry.register(new BlockInfo(skystoneCrushed), stack, 0.02f, BlockSieve.MeshType.DIAMOND.getID());

        // Pure Certus Quartz Seed
        stack = new ItemInfo(AE_SEEDS);
        registry.register(new BlockInfo(skystoneCrushed), stack, 0.01f, BlockSieve.MeshType.STRING.getID());
        registry.register(new BlockInfo(skystoneCrushed), stack, 0.01f, BlockSieve.MeshType.FLINT.getID());
        registry.register(new BlockInfo(skystoneCrushed), stack, 0.02f, BlockSieve.MeshType.IRON.getID());
        registry.register(new BlockInfo(skystoneCrushed), stack, 0.02f, BlockSieve.MeshType.DIAMOND.getID());

        // Charged Certus Quartz
        stack = new ItemInfo(AE_MATERIAL, 1);
        registry.register(new BlockInfo(skystoneCrushed), stack, 0.001f, BlockSieve.MeshType.DIAMOND.getID());
    }

    public void registerHammer(HammerRegistry registry) {
        registry.register(new BlockInfo(SKY_STONE), new ItemStack(skystoneCrushed, 1), 3, 1.0F, 0.0F);
    }

    public void registerFluidBlockTransform(FluidBlockTransformerRegistry registry) {
        registry.register(FluidRegistry.LAVA, new ItemInfo(AE_MATERIAL, 45), new BlockInfo(SKY_STONE.getDefaultState()));
    }
}
