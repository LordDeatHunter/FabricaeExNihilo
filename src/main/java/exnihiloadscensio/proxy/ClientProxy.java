package exnihiloadscensio.proxy;

import exnihiloadscensio.ModBlocks;
import exnihiloadscensio.ModItems;
import exnihiloadscensio.client.renderers.*;
import exnihiloadscensio.entities.ProjectileStone;
import exnihiloadscensio.items.ore.ItemOre;
import exnihiloadscensio.registries.CompostRegistry;
import exnihiloadscensio.registries.OreRegistry;
import exnihiloadscensio.tiles.TileBarrel;
import exnihiloadscensio.tiles.TileCrucible;
import exnihiloadscensio.tiles.TileInfestedLeaves;
import exnihiloadscensio.tiles.TileSieve;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

@Mod.EventBusSubscriber
public class ClientProxy extends CommonProxy {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModBlocks.initModels(event);
        ModItems.initModels(event);

        registerRenderers();
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Override
    public void initOreModels() {
        OreRegistry.initModels();
    }

    @Override
    public boolean runningOnServer() {
        return false;
    }

    public static void registerRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileBarrel.class, new RenderBarrel());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCrucible.class, new RenderCrucible());
        ClientRegistry.bindTileEntitySpecialRenderer(TileSieve.class, new RenderSieve());
        ClientRegistry.bindTileEntitySpecialRenderer(TileInfestedLeaves.class, new RenderInfestedLeaves());
        RenderingRegistry.registerEntityRenderingHandler(ProjectileStone.class, new RenderProjectileStone.Factory());
    }

    @Override
    public void registerColorHandlers() {
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new RenderOrePiece(), OreRegistry.getItemOreRegistry().toArray(new ItemOre[0]));
    }

    @Override
    public void registerConfigs(File configDirectory) {
        CompostRegistry.recommendAllFood(new File(configDirectory, "RecommendedFoodRegistry.json"));
    }
}
