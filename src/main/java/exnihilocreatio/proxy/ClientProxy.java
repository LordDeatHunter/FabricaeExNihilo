package exnihilocreatio.proxy;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.ModBlocks;
import exnihilocreatio.ModFluids;
import exnihilocreatio.ModItems;
import exnihilocreatio.client.renderers.*;
import exnihilocreatio.entities.ProjectileStone;
import exnihilocreatio.items.ore.ItemOre;
import exnihilocreatio.registries.OreRegistry;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.tiles.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    public static void registerRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileBarrel.class, new RenderBarrel());

        ClientRegistry.bindTileEntitySpecialRenderer(TileCrucibleStone.class, new RenderCrucible());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCrucibleWood.class, new RenderCrucible());

        ClientRegistry.bindTileEntitySpecialRenderer(TileSieve.class, new RenderSieve());
        ClientRegistry.bindTileEntitySpecialRenderer(TileInfestedLeaves.class, new RenderInfestedLeaves());

        ClientRegistry.bindTileEntitySpecialRenderer(TileWaterwheel.class, new RenderWaterwheel());
        ClientRegistry.bindTileEntitySpecialRenderer(TileStoneAxle.class, new RenderStoneAxel());
        ClientRegistry.bindTileEntitySpecialRenderer(TileGrinder.class, new RenderGrinder());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAutoSifter.class, new RenderAutoSifter());

        RenderingRegistry.registerEntityRenderingHandler(ProjectileStone.class, new RenderProjectileStone.Factory());
    }

    @Override
    public void registerModels(ModelRegistryEvent event) {
        ModBlocks.initModels(event);
        ModItems.initModels(event);
        ModFluids.initModels();

        registerRenderers();
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        OBJLoader.INSTANCE.addDomain(ExNihiloCreatio.MODID);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        OreRegistry.initModels();
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new RenderOrePiece(), OreRegistry.getItemOreRegistry().toArray(new ItemOre[0]));

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Override
    public boolean runningOnServer() {
        return false;
    }

    @Override
    public void registerConfigs(File configDirectory) {
        ExNihiloRegistryManager.COMPOST_REGISTRY.recommendAllFood(new File(configDirectory, "RecommendedFoodRegistry.json"));
    }
}
