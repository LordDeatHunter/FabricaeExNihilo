package exnihilocreatio.recipes.defaults;

import exnihilocreatio.registries.registries.*;
import exnihilocreatio.util.BlockInfo;
import lombok.Getter;
import net.minecraft.init.Blocks;

public class MagicBees implements IRecipeDefaults {
    @Getter
    public String MODID = "magicbees";


    @Override
    public void registerCompost(CompostRegistry registry) {
        BlockInfo dirtState = new BlockInfo(Blocks.DIRT);


    }

    @Override
    public void registerCrook(CrookRegistry registry) {

    }

    @Override
    public void registerSieve(SieveRegistry registry) {
        // Crushed Netherrack for Infernal Bees
        // Crushed Endstone for Oblivion Bees
        // Dirt for Unusual  Bees
        // Grass for Mystical Bees
        // Sand for Sorcerous Bees
        // Crushed Andesite for Attuned Bees
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
