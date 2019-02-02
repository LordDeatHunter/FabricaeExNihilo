package exnihilocreatio.modules.tconstruct;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.config.ModConfig;
import exnihilocreatio.modules.TinkersConstruct;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.registries.IForgeRegistry;
import slimeknights.tconstruct.common.ModelRegisterUtil;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.TinkerRegistryClient;
import slimeknights.tconstruct.library.client.ToolBuildGuiInfo;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.tools.TinkerModifiers;

public class Helper {
    @SidedProxy(serverSide = "exnihilocreatio.modules.tconstruct.Helper$Proxy", clientSide = "exnihilocreatio.modules.tconstruct.Helper$ProxyClient")
    public static Proxy proxy;
    public static class Proxy {
        @Optional.Method(modid="tconstruct")
        public void registerItems(IForgeRegistry<Item> registry) {
            if (ModConfig.compatibility.tinkers_construct_compat.addExNihiloHammer &&
                    !(Loader.isModLoaded("tcomplement") && ModConfig.compatibility.tinkers_construct_compat.respectTinkersComplement)) {
                // Register Sledge Hammer
                registry.register(TinkersConstruct.SLEDGE_HAMMER);
                TinkerRegistry.registerToolCrafting(TinkersConstruct.SLEDGE_HAMMER);
            }
            if (ModConfig.compatibility.tinkers_construct_compat.addExNihiloCrook) {
                // Register Crook
                registry.register(TinkersConstruct.TINKERS_CROOK);
                TinkerRegistry.registerToolCrafting(TinkersConstruct.TINKERS_CROOK);
            }
        }
    }
    public static class ProxyClient extends Proxy {
        @Optional.Method(modid="tconstruct")
        public void registerItems(IForgeRegistry<Item> registry) {
            super.registerItems(registry);
            registerToolModels();
            registerModifierModels();
            registerTableModels();
        }
        @Optional.Method(modid="tconstruct")
        public static void registerToolModels(){
            if (ModConfig.compatibility.tinkers_construct_compat.addExNihiloHammer &&
                    !(Loader.isModLoaded("tcomplement") && ModConfig.compatibility.tinkers_construct_compat.respectTinkersComplement)) {
                ModelRegisterUtil.registerToolModel(TinkersConstruct.SLEDGE_HAMMER);
            }
            if (ModConfig.compatibility.tinkers_construct_compat.addExNihiloCrook) {
                ModelRegisterUtil.registerToolModel(TinkersConstruct.TINKERS_CROOK);
            }
        }
        @Optional.Method(modid="tconstruct")
        public static void registerTableModels(){
            ToolBuildGuiInfo info;
            if (ModConfig.compatibility.tinkers_construct_compat.addExNihiloHammer &&
                    !(Loader.isModLoaded("tcomplement") && ModConfig.compatibility.tinkers_construct_compat.respectTinkersComplement)) {
                // Sledge Hammer Crafting
                info = new ToolBuildGuiInfo(TinkersConstruct.SLEDGE_HAMMER);
                info.addSlotPosition(33 - 18, 42 + 18); // Handle
                info.addSlotPosition(33 + 20, 42 - 20); // Head
                info.addSlotPosition(33, 42); // Binding
                TinkerRegistryClient.addToolBuilding(info);
            }
            if (ModConfig.compatibility.tinkers_construct_compat.addExNihiloCrook) {
                // Crook Crafting
                info = new ToolBuildGuiInfo(TinkersConstruct.TINKERS_CROOK);
                info.addSlotPosition(33 - 20, 42 + 18); // handle
                info.addSlotPosition(33 - 2, 42 - 22);  // head
                info.addSlotPosition(33 + 16, 42 - 10); // extra
                info.addSlotPosition(33 - 2, 42 + 2); // handle2
                TinkerRegistryClient.addToolBuilding(info);
            }
        }
        @Optional.Method(modid="tconstruct")
        public static void registerModifierModels(){
            for (IModifier modifier : new IModifier[]{
                    TinkerModifiers.modDiamond,
                    TinkerModifiers.modEmerald,
                    TinkerModifiers.modGlowing,
                    TinkerModifiers.modHaste,
                    TinkerModifiers.modKnockback,
                    TinkerModifiers.modLuck,
                    TinkerModifiers.modMendingMoss,
                    TinkerModifiers.modReinforced,
                    TinkerModifiers.modSoulbound
            }) {
                ModelRegisterUtil.registerModifierModel(modifier, new ResourceLocation(ExNihiloCreatio.MODID, "models/item/tools/modifiers/" + modifier.getIdentifier()));
            }
        }
    }
}
