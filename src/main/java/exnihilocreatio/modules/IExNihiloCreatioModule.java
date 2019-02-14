package exnihilocreatio.modules;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public interface IExNihiloCreatioModule {

    String getMODID();

    default void registerBlocks(IForgeRegistry<Block> registry){}
    default void registerItems(IForgeRegistry<Item> registry){}
    default void registerOredicts(){}

    // Called inside CommonProxy
    default void preInit(FMLPreInitializationEvent event){}
    default void init(FMLInitializationEvent event){}
    default void postInit(FMLPostInitializationEvent event){}

    // Called inside ClientProxy
    @SideOnly(Side.CLIENT)
    default void preInitClient(FMLPreInitializationEvent event){}
    @SideOnly(Side.CLIENT)
    default void initClient(FMLInitializationEvent event){}
    @SideOnly(Side.CLIENT)
    default void postInitClient(FMLPostInitializationEvent event){}

    @SideOnly(Side.CLIENT)
    default void initBlockModels(ModelRegistryEvent e){}
    @SideOnly(Side.CLIENT)
    default void initItemModels(ModelRegistryEvent e){}

    // Called inside ServerProxy ... unlikely to be used
    @SideOnly(Side.SERVER)
    default void preInitServer(FMLPreInitializationEvent event){}
    @SideOnly(Side.SERVER)
    default void initServer(FMLInitializationEvent event){}
    @SideOnly(Side.SERVER)
    default void postInitServer(FMLPostInitializationEvent event){}

}
