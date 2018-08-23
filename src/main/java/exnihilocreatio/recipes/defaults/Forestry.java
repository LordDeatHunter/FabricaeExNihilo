package exnihilocreatio.recipes.defaults;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.ModBlocks;
import exnihilocreatio.ModFluids;
import exnihilocreatio.ModItems;
import exnihilocreatio.blocks.BlockSieve.MeshType;
import exnihilocreatio.config.ModConfig;
import exnihilocreatio.items.ItemResource;
import exnihilocreatio.items.ore.ItemOre;
import exnihilocreatio.items.seeds.ItemSeedBase;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.*;
import exnihilocreatio.registries.types.Meltable;
import exnihilocreatio.texturing.Color;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import exnihilocreatio.util.Util;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.LinkedHashMap;
import java.util.Map;

public class Forestry implements IRecipeDefaults {
    @Getter
    public String MODID = "forestry";


    @Override
    public void registerCompost(CompostRegistry registry) {
        BlockInfo dirtState = new BlockInfo(Blocks.DIRT);

        // Compost Drones
        // Compost Peat
        // Compost Compost (yo dawg)
        registry.register("forestry:fertilizerBio", 0.5f, dirtState);
        // Compost Mouldy Wheat, Decaying Wheat, Mulch
        registry.register("forestry:mouldyWheat", 0.0625f, dirtState);
        registry.register("forestry:decayingWheat", 0.125f, dirtState);
        registry.register("forestry:mulch", 0.25f, dirtState);
        // Compost Wood Pulp
        // Compost Pollens
    }

    @Override
    public void registerCrook(CrookRegistry registry) {
        // All Leaves for Forest Bees
        // Jungle Leaves for Tropical
    }

    @Override
    public void registerSieve(SieveRegistry registry) {
        // Sand for Apatite
        // Sand for Modest Bees
        // Snow for Wintry Bees
        // Humus for Marshy Bees
        // Crushed End Stone for Ender Bees
        // Dirt for Meadows
    }

    @Override
    public void registerHammer(HammerRegistry registry) {

    }

    @Override
    public void registerHeat(HeatRegistry registry) {

    }

    @Override
    public void registerBarrelLiquidBlacklist(BarrelLiquidBlacklistRegistry registry) {

    }

    @Override
    public void registerFluidOnTop(FluidOnTopRegistry registry) {

    }

    @Override
    public void registerOreChunks(OreRegistry registry) {

    }

    @Override
    public void registerFluidTransform(FluidTransformRegistry registry) {

    }

    @Override
    public void registerFluidBlockTransform(FluidBlockTransformerRegistry registry) {

    }

    @Override
    public void registerFluidItemFluid(FluidItemFluidRegistry registry) {

    }

    @Override
    public void registerCrucibleStone(CrucibleRegistry registry) {
        // Melt down honey drops
        registry.register("dropHoney", FluidRegistry.getFluid("for.honey"), 10);
    }

    @Override
    public void registerCrucibleWood(CrucibleRegistry registry) {

    }

    @Override
    public void registerMilk(MilkEntityRegistry registry) {

    }

}
