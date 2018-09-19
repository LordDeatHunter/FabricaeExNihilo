package exnihilocreatio.compatibility.tconstruct;

import exnihilocreatio.ModItems;
import exnihilocreatio.config.ModConfig;
import exnihilocreatio.items.ore.EnumOreSubtype;
import exnihilocreatio.items.ore.ItemOre;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.registries.IForgeRegistry;
import slimeknights.tconstruct.common.ModelRegisterUtil;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.TinkerRegistryClient;
import slimeknights.tconstruct.library.client.ToolBuildGuiInfo;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.modifiers.Modifier;

public class CompatTConstruct {
    public static SledgeHammer sledgeHammer = new SledgeHammer();

    public static void registerItems(IForgeRegistry<Item> registry) {
        // Register Sledge Hammer
        registry.register(sledgeHammer);
        TinkerRegistry.registerToolCrafting(sledgeHammer);
        ModelRegisterUtil.registerToolModel(sledgeHammer);
    }

    public static void initClient(FMLInitializationEvent event) {
        // Sledge Hammer Crafting
        ToolBuildGuiInfo sledgeHammerInfo = new ToolBuildGuiInfo(sledgeHammer);
        sledgeHammerInfo.addSlotPosition(33 - 18, 42 + 18); // Handle
        sledgeHammerInfo.addSlotPosition(33 + 20, 42 - 20); // Head
        sledgeHammerInfo.addSlotPosition(33, 42); // Binding
        TinkerRegistryClient.addToolBuilding(sledgeHammerInfo);
    }

    public static void postInit() {
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
}
