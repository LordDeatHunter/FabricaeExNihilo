package exnihiloadscensio.blocks;

import exnihiloadscensio.util.Data;
import exnihiloadscensio.util.IHasModel;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBaseFalling extends BlockFalling implements IHasModel {
    public BlockBaseFalling(SoundType sound, String name) {
        super(Material.SAND);
        setUnlocalizedName(name);
        setRegistryName(name);
        setSoundType(sound);
        Data.BLOCKS.add(this);
    }
}
