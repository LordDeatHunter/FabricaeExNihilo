package exnihiloadscensio;

import exnihiloadscensio.blocks.BlockFluidWitchwater;
import exnihiloadscensio.fluid.FluidWitchWater;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModFluids {

    public static FluidWitchWater fluidWitchwater;
    public static BlockFluidWitchwater blockWitchwater;

    public static void init() {
        fluidWitchwater = new FluidWitchWater();
        blockWitchwater = new BlockFluidWitchwater();
        FluidRegistry.addBucketForFluid(fluidWitchwater);
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        fluidWitchwater.initModel();
    }
}
