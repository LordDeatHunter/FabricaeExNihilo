package exnihilocreatio;

import exnihilocreatio.capabilities.ENCapabilities;
import exnihilocreatio.command.CommandReloadConfig;
import exnihilocreatio.config.Config;
import exnihilocreatio.entities.ENEntities;
import exnihilocreatio.handlers.HandlerCrook;
import exnihilocreatio.handlers.HandlerHammer;
import exnihilocreatio.networking.PacketHandler;
import exnihilocreatio.proxy.CommonProxy;
import exnihilocreatio.registries.*;
import exnihilocreatio.registries.manager.ExNihiloDefaultRecipes;
import exnihilocreatio.util.LogUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;

@Mod(modid = ExNihiloCreatio.MODID, name = "Ex Nihilo Creatio", version = ExNihiloCreatio.VERSION, acceptedMinecraftVersions = "[1.12, 1.13)")
@Mod.EventBusSubscriber
public class ExNihiloCreatio {

    public static final String MODID = "exnihilocreatio";
    public static final String VERSION = "0.1.1";

    @SidedProxy(serverSide = "exnihilocreatio.proxy.ServerProxy", clientSide = "exnihilocreatio.proxy.ClientProxy")
    public static CommonProxy proxy;

    @Instance(MODID)
    public static ExNihiloCreatio instance;

    public static File configDirectory;

    public static boolean configsLoaded = false;

    public static ExNihiloDefaultRecipes defaultRecipes;
    public static CreativeTabs tabExNihilo = new CreativeTabs("exNihilo") {
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack getTabIconItem() {
            return new ItemStack(Item.getItemFromBlock(ModBlocks.sieve), 1);
        }
    };

    static {
        FluidRegistry.enableUniversalBucket();
    }

    @EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);

        LogUtil.setup();

        configDirectory = new File(event.getModConfigurationDirectory(), "exnihilocreatio");
        configDirectory.mkdirs();

        Config.doNormalConfig(new File(configDirectory, "ExNihiloCreatio.cfg"));

        proxy.registerConfigs(configDirectory);


        ENCapabilities.init();
        ENEntities.init();

        PacketHandler.initPackets();

        defaultRecipes = new ExNihiloDefaultRecipes();

        MinecraftForge.EVENT_BUS.register(new HandlerHammer());

        MinecraftForge.EVENT_BUS.register(new HandlerCrook());

        if (Config.enableBarrels) {
            BarrelModeRegistry.registerDefaults();
        }
    }

    @EventHandler
    public static void init(FMLInitializationEvent event) {
        proxy.init(event);

        loadConfigs();

        Recipes.init();
        // OreRegistry.doRecipes();

    }

    @EventHandler
    public static void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);

        if (Loader.isModLoaded("tconstruct") && Config.doTICCompat) {
            // CompatTConstruct.postInit();
        }
        if (Loader.isModLoaded("EnderIO") && Config.doEnderIOCompat) {
            // CompatEIO.postInit();
        }
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        proxy.registerModels(event);
    }

    public static void loadConfigs() {
        configsLoaded = true;

        CompostRegistry.loadJson(new File(configDirectory, "CompostRegistry.json"));
        HammerRegistry.loadJson(new File(configDirectory, "HammerRegistry.json"));
        FluidBlockTransformerRegistry.loadJson(new File(configDirectory, "FluidBlockTransformerRegistry.json"));
        FluidOnTopRegistry.loadJson(new File(configDirectory, "FluidOnTopRegistry.json"));
        HeatRegistry.loadJson(new File(configDirectory, "HeatRegistry.json"));
        CrucibleRegistry.loadJson(new File(configDirectory, "CrucibleRegistry.json"));
        SieveRegistry.loadJson(new File(configDirectory, "SieveRegistry.json"));
        CrookRegistry.loadJson(new File(configDirectory, "CrookRegistry.json"));
        FluidTransformRegistry.loadJson(new File(configDirectory, "FluidTransformRegistry.json"));
        BarrelLiquidBlacklistRegistry.loadJson(new File(configDirectory, "BarrelLiquidBlacklistRegistry.json"));

        MinecraftForge.EVENT_BUS.post(new RegistryReloadedEvent());
    }

    @EventHandler
    public static void serverStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandReloadConfig());
    }

    @EventHandler
    public static void modMapping(FMLModIdMappingEvent event) {

    }

}
