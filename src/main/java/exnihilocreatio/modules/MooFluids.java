package exnihilocreatio.modules;

import exnihilocreatio.config.ModConfig;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import java.util.HashSet;
import java.util.Set;

public class MooFluids implements IExNihiloCreatioModule {
    private static Set<Fluid> fluidSet = new HashSet<>();

    @Override
    public String getMODID() {
        return "moofluids";
    }

    @Override
    public void postInitServer(FMLPostInitializationEvent event){
        for(String s : ModConfig.compatibility.moofluids_compat.fluidList){
            if(FluidRegistry.isFluidRegistered(s))
                fluidSet.add(FluidRegistry.getFluid(s));
        }
    }

    public static boolean fluidIsAllowed(Fluid fluid){
        return fluidSet.contains(fluid) != ModConfig.compatibility.moofluids_compat.fluidListIsBlackList;
    }
}
