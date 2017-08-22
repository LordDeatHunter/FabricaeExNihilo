package exnihilocreatio.compatibility.tconstruct;

import exnihilocreatio.ModItems;
import exnihilocreatio.config.ModConfig;
import exnihilocreatio.items.ore.EnumOreSubtype;
import exnihilocreatio.items.ore.ItemOre;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.modifiers.Modifier;

public class CompatTConstruct {
    public static void postInit() {
        if (ModConfig.compatibility.tinkers_construct_compat.addModifer) {
            Modifier smashingModifier = new ModifierSmashing();
            TinkerRegistry.registerModifier(smashingModifier);
            smashingModifier.addItem(ModItems.hammerDiamond);
        }

        if (ModConfig.compatibility.tinkers_construct_compat.addMeltingOfChunks) {
            registerMeltingChunks();
        }
        if (ModConfig.compatibility.tinkers_construct_compat.addMeltingOfDust) {
            registerMeltingDust();
        }
    }

    private static void registerMeltingChunks() {
        for (ItemOre ore : ExNihiloRegistryManager.ORE_REGISTRY.getItemOreRegistry()) {
            if (FluidRegistry.isFluidRegistered(ore.getOre().getName())) {
                Fluid fluid = FluidRegistry.getFluid(ore.getOre().getName());
                TinkerRegistry.registerMelting(new ItemStack(ore, 1, EnumOreSubtype.CHUNK.getMeta()), fluid,
                        (int) (ModConfig.compatibility.tinkers_construct_compat.ingotsPerChunkWhenMelting * Material.VALUE_Ingot));
            }
        }
    }

    private static void registerMeltingDust() {
        for (ItemOre ore : ExNihiloRegistryManager.ORE_REGISTRY.getItemOreRegistry()) {
            if (FluidRegistry.isFluidRegistered(ore.getOre().getName())) {
                Fluid fluid = FluidRegistry.getFluid(ore.getOre().getName());
                TinkerRegistry.registerMelting(new ItemStack(ore, 1, EnumOreSubtype.DUST.getMeta()), fluid,
                        (int) (ModConfig.compatibility.tinkers_construct_compat.ingotsPerDustWhenMelting * Material.VALUE_Ingot));
            }
        }
    }
}
