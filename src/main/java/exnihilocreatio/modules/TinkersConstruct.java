package exnihilocreatio.modules;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.ModItems;
import exnihilocreatio.config.ModConfig;
import exnihilocreatio.items.ore.EnumOreSubtype;
import exnihilocreatio.items.ore.ItemOre;
import exnihilocreatio.modules.tconstruct.modifiers.ModifierSmashing;
import exnihilocreatio.modules.tconstruct.tools.SledgeHammer;
import exnihilocreatio.modules.tconstruct.tools.TiCrook;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import lombok.Getter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.registries.IForgeRegistry;
import slimeknights.tconstruct.common.ModelRegisterUtil;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.TinkerRegistryClient;
import slimeknights.tconstruct.library.client.ToolBuildGuiInfo;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.tools.TinkerModifiers;

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

    public void registerItems(IForgeRegistry<Item> registry) {
        SLEDGE_HAMMER = new SledgeHammer();
        TINKERS_CROOK = new TiCrook();
        ToolBuildGuiInfo info;
        if (ModConfig.compatibility.tinkers_construct_compat.addExNihiloHammer &&
                !(Loader.isModLoaded("tcomplement") && ModConfig.compatibility.tinkers_construct_compat.respectTinkersComplement)) {
            // Register Sledge Hammer
            registry.register(SLEDGE_HAMMER);
            TinkerRegistry.registerToolCrafting(SLEDGE_HAMMER);
            ModelRegisterUtil.registerToolModel(SLEDGE_HAMMER);
        }
        if (ModConfig.compatibility.tinkers_construct_compat.addExNihiloCrook) {
            // Register Crook
            registry.register(TINKERS_CROOK);
            TinkerRegistry.registerToolCrafting(TINKERS_CROOK);
            ModelRegisterUtil.registerToolModel(TINKERS_CROOK);
        }
        // Register Modifiers
        for (IModifier modifier : new IModifier[]{
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
            ModelRegisterUtil.registerModifierModel(modifier, new ResourceLocation(ExNihiloCreatio.MODID, "models/item/tools/modifiers/" + modifier.getIdentifier()));
        }
    }

    @Override
    public void initClient(FMLInitializationEvent event) {
        ToolBuildGuiInfo info;
        if (ModConfig.compatibility.tinkers_construct_compat.addExNihiloHammer &&
                !(Loader.isModLoaded("tcomplement") && ModConfig.compatibility.tinkers_construct_compat.respectTinkersComplement)) {
            // Sledge Hammer Crafting
            info = new ToolBuildGuiInfo(SLEDGE_HAMMER);
            info.addSlotPosition(33 - 18, 42 + 18); // Handle
            info.addSlotPosition(33 + 20, 42 - 20); // Head
            info.addSlotPosition(33, 42); // Binding
            TinkerRegistryClient.addToolBuilding(info);
        }
        if (ModConfig.compatibility.tinkers_construct_compat.addExNihiloCrook) {
            // Crook Crafting
            info = new ToolBuildGuiInfo(TINKERS_CROOK);
            info.addSlotPosition(33 - 20, 42 + 18); // handle
            info.addSlotPosition(33 - 2, 42 - 22);  // head
            info.addSlotPosition(33 + 16, 42 - 10); // extra
            info.addSlotPosition(33 - 2, 42 + 2); // handle2
            TinkerRegistryClient.addToolBuilding(info);
        }
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
}
