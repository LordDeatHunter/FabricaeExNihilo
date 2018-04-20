package exnihilocreatio;

import exnihilocreatio.blocks.BlockFluidWitchwater;
import exnihilocreatio.fluid.FluidWitchWater;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModFluids {

    public static final FluidWitchWater fluidWitchwater = new FluidWitchWater();
    public static BlockFluidWitchwater blockWitchwater = new BlockFluidWitchwater();

    public static void init() {
        FluidRegistry.addBucketForFluid(fluidWitchwater);
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        fluidWitchwater.initModel();
    }
}
