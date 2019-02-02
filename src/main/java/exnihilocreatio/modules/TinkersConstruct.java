package exnihilocreatio.modules;

import exnihilocreatio.ModItems;
import exnihilocreatio.config.ModConfig;
import exnihilocreatio.items.ore.EnumOreSubtype;
import exnihilocreatio.items.ore.ItemOre;
import exnihilocreatio.modules.tconstruct.Helper;
import exnihilocreatio.modules.tconstruct.modifiers.ModifierSmashing;
import exnihilocreatio.modules.tconstruct.tools.SledgeHammer;
import exnihilocreatio.modules.tconstruct.tools.TiCrook;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import lombok.Getter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.registries.IForgeRegistry;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.modifiers.Modifier;

public class TinkersConstruct implements IExNihiloCreatioModule {
    public static SledgeHammer SLEDGE_HAMMER;
    public static TiCrook TINKERS_CROOK;
    @Getter
    private final String MODID = "tconstruct";

    private static void registerMeltingChunks() {
        for (ItemOre ore : ExNihiloRegistryManager.ORE_REGISTRY.getItemOreRegistry()) {
            if (FluidRegistry.isFluidRegistered(ore.getOre().getName())) {
                Fluid fluid = FluidRegistry.getFluid(ore.getOre().getName());
                TinkerRegistry.registerMelting(new ItemStack(ore, 1, EnumOreSubtype.CHUNK.getMeta()), fluid,
                        (int) (ModConfig.compatibility.tinkers_construct_compat.ingotsPerChunkWhenMelting * Material.VALUE_Ingot));
            } else if (ore.getOre().getName().equals("aluminium") && FluidRegistry.isFluidRegistered("aluminum")) {
                Fluid fluid = FluidRegistry.getFluid("aluminum");
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
            } else if (ore.getOre().getName().equals("aluminium") && FluidRegistry.isFluidRegistered("aluminum")) {
                Fluid fluid = FluidRegistry.getFluid("aluminum");
                TinkerRegistry.registerMelting(new ItemStack(ore, 1, EnumOreSubtype.DUST.getMeta()), fluid,
                        (int) (ModConfig.compatibility.tinkers_construct_compat.ingotsPerDustWhenMelting * Material.VALUE_Ingot));
            }
        }
    }



    @Override
    public void initClient(FMLInitializationEvent event) {
    }

    @Override
    public void preInitClient(FMLPreInitializationEvent event) {
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        if (ModConfig.compatibility.tinkers_construct_compat.addModifer) {
            Modifier smashingModifier = new ModifierSmashing();
            smashingModifier.addItem(ModItems.hammerDiamond);
        }

        if (ModConfig.compatibility.tinkers_construct_compat.addMeltingOfChunks) {
            registerMeltingChunks();
        }
        if (ModConfig.compatibility.tinkers_construct_compat.addMeltingOfDust) {
            registerMeltingDust();
        }
    }


    @Override
    public void registerItems(IForgeRegistry<Item> registry) {
        Helper.proxy.registerItems(registry);
    }
}
