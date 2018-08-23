package exnihilocreatio.recipes.defaults;

import exnihilocreatio.registries.registries.*;
import exnihilocreatio.util.BlockInfo;
import lombok.Getter;
import net.minecraft.init.Blocks;

public class MoreBees implements IRecipeDefaults {
    @Getter
    public String MODID = "morebees";


    @Override
    public void registerCompost(CompostRegistry registry) {
        BlockInfo dirtState = new BlockInfo(Blocks.DIRT);


    }

    @Override
    public void registerCrook(CrookRegistry registry) {

    }

    @Override
    public void registerSieve(SieveRegistry registry) {
        // Gravel for Rocky Bees
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

    }

    @Override
    public void registerCrucibleWood(CrucibleRegistry registry) {

    }

    @Override
    public void registerMilk(MilkEntityRegistry registry) {

    }

}
