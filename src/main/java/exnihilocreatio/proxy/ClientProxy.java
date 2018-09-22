package exnihilocreatio.proxy;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.ModBlocks;
import exnihilocreatio.ModFluids;
import exnihilocreatio.ModItems;
import exnihilocreatio.client.models.InfestedLeavesBakedModel;
import exnihilocreatio.client.models.ModColorManager;
import exnihilocreatio.client.models.event.RenderEvent;
import exnihilocreatio.client.renderers.*;
import exnihilocreatio.entities.ProjectileStone;
import exnihilocreatio.items.ore.ItemOre;
import exnihilocreatio.modules.IExNihiloCreatioModule;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.tiles.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    public static void registerRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileBarrel.class, new RenderBarrel());

        ClientRegistry.bindTileEntitySpecialRenderer(TileCrucibleStone.class, new RenderCrucible());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCrucibleWood.class, new RenderCrucible());

        ClientRegistry.bindTileEntitySpecialRenderer(TileSieve.class, new RenderSieve());

        ClientRegistry.bindTileEntitySpecialRenderer(TileWaterwheel.class, new RenderWaterwheel());
        ClientRegistry.bindTileEntitySpecialRenderer(TileStoneAxle.class, new RenderStoneAxel());
        ClientRegistry.bindTileEntitySpecialRenderer(TileGrinder.class, new RenderGrinder());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAutoSifter.class, new RenderAutoSifter());

        RenderingRegistry.registerEntityRenderingHandler(ProjectileStone.class, new RenderProjectileStone.Factory());

        ClientRegistry.bindTileEntitySpecialRenderer(TileInfestingLeaves.class, new RenderInfestingLeaves());

        StateMapperBase ignoreState = new StateMapperBase() {
            @Override
            @Nonnull
            protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
                return InfestedLeavesBakedModel.variantTag;
            }
        };
        ModelLoader.setCustomStateMapper(ModBlocks.infestedLeaves, ignoreState);
    }

    @Override
    public void registerModels(ModelRegistryEvent event) {
        ModBlocks.initModels(event);
        ModItems.initModels(event);
        ModFluids.initModels();
        for(IExNihiloCreatioModule module : ExNihiloCreatio.loadedModules) {
            module.initBlockModels(event);
            module.initItemModels(event);
        }

        registerRenderers();
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        MinecraftForge.EVENT_BUS.register(new RenderEvent());
        OBJLoader.INSTANCE.addDomain(ExNihiloCreatio.MODID);
        for(IExNihiloCreatioModule module : ExNihiloCreatio.loadedModules)
            module.preInitClient(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        ExNihiloRegistryManager.ORE_REGISTRY.initModels();
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new RenderOrePiece(), ExNihiloRegistryManager.ORE_REGISTRY.getItemOreRegistry().toArray(new ItemOre[0]));
        ModColorManager.registerColorHandlers();

        for(IExNihiloCreatioModule module : ExNihiloCreatio.loadedModules)
            module.initClient(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        for(IExNihiloCreatioModule module : ExNihiloCreatio.loadedModules)
            module.postInitClient(event);
    }

    @Override
    public boolean runningOnServer() {
        return false;
    }
}
