package exnihilocreatio.modules;

import exnihilocreatio.modules.forestry.blocks.BlockHive;
import exnihilocreatio.modules.forestry.items.ItemBlockHive;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class Forestry implements IExNihiloCreatioModule {
    @Getter
    private final String MODID = "forestry";

    public static final BlockHive EXNIHILO_HIVE = new BlockHive(Material.WOOD, "hive");

    @Override
    public void registerBlocks(IForgeRegistry<Block> registry) {
        registry.register(EXNIHILO_HIVE);
    }

    @Override
    public void registerItems(IForgeRegistry<Item> registry) {
        registry.register(new ItemBlockHive(EXNIHILO_HIVE).setRegistryName(EXNIHILO_HIVE.getRegistryName()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void initBlockModels(ModelRegistryEvent e){
        EXNIHILO_HIVE.initModel(e);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public  void initItemModels(ModelRegistryEvent e){

    }
}
