package exnihilocreatio.compatibility.tconstruct;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.ModItems;
import exnihilocreatio.config.ModConfig;
import exnihilocreatio.items.ore.EnumOreSubtype;
import exnihilocreatio.items.ore.ItemOre;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.registries.IForgeRegistry;
import slimeknights.tconstruct.common.ModelRegisterUtil;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.TinkerRegistryClient;
import slimeknights.tconstruct.library.client.ToolBuildGuiInfo;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.tools.TinkerModifiers;

public class CompatTConstruct {
    public static SledgeHammer sledgeHammer = new SledgeHammer();
    public static TiCrook crook = new TiCrook();

    public static void registerItems(IForgeRegistry<Item> registry) {
        ToolBuildGuiInfo info;
        if(ModConfig.compatibility.tinkers_construct_compat.addExNihiloHammer){
            // Register Sledge Hammer
            registry.register(sledgeHammer);
            TinkerRegistry.registerToolCrafting(sledgeHammer);
            ModelRegisterUtil.registerToolModel(sledgeHammer);
        }
        if(ModConfig.compatibility.tinkers_construct_compat.addExNihiloCrook){
            // Register Crook
            registry.register(crook);
            TinkerRegistry.registerToolCrafting(crook);
            ModelRegisterUtil.registerToolModel(crook);
        }
        // Register Modifiers
        for (IModifier modifier: new IModifier[] {
                TinkerModifiers.modDiamond,
                TinkerModifiers.modEmerald,
                TinkerModifiers.modGlowing,
                TinkerModifiers.modHaste,
                TinkerModifiers.modKnockback,
                TinkerModifiers.modLuck,
                TinkerModifiers.modMendingMoss,
                TinkerModifiers.modReinforced,
                TinkerModifiers.modSoulbound,
        }) {
            ModelRegisterUtil.registerModifierModel(modifier, new ResourceLocation(ExNihiloCreatio.MODID, "models/item/tools/modifiers/"+modifier.getIdentifier()));
        }
    }

    public static void initClient(FMLInitializationEvent event) {
        ToolBuildGuiInfo info;
        if(ModConfig.compatibility.tinkers_construct_compat.addExNihiloHammer){
            // Sledge Hammer Crafting
            info = new ToolBuildGuiInfo(sledgeHammer);
            info.addSlotPosition(33 - 18, 42 + 18); // Handle
            info.addSlotPosition(33 + 20, 42 - 20); // Head
            info.addSlotPosition(33, 42); // Binding
            TinkerRegistryClient.addToolBuilding(info);
        }
        if(ModConfig.compatibility.tinkers_construct_compat.addExNihiloCrook){
            // Crook Crafting
            info = new ToolBuildGuiInfo(crook);
            info.addSlotPosition(33 - 11, 42 + 11); // handle
            info.addSlotPosition(33 - 2, 42 - 20); // head
            info.addSlotPosition(33 + 18, 42 - 8); // binding
            TinkerRegistryClient.addToolBuilding(info);
        }
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
