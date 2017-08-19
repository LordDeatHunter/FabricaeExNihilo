package exnihilocreatio.compatibility.tconstruct;

import exnihilocreatio.ModItems;
import exnihilocreatio.items.ore.EnumOreSubtype;
import exnihilocreatio.items.ore.ItemOre;
import exnihilocreatio.registries.OreRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.modifiers.Modifier;

public class CompatTConstruct {
    public static void postInit() {
        Modifier smashingModifier = new ModifierSmashing();

        TinkerRegistry.registerModifier(smashingModifier);
        smashingModifier.addItem(ModItems.hammerDiamond);

        registerMelting();
    }

    public static void registerMelting() {
        for (ItemOre ore : OreRegistry.getItemOreRegistry()) {
            if (FluidRegistry.isFluidRegistered(ore.getOre().getName())) {
                Fluid fluid = FluidRegistry.getFluid(ore.getOre().getName());
                TinkerRegistry.registerMelting(new ItemStack(ore, 1, EnumOreSubtype.CHUNK.getMeta()), fluid, 2 * Material.VALUE_Ingot);
            }
        }
    }
}
