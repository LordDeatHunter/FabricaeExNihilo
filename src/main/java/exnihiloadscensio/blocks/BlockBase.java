package exnihiloadscensio.blocks;

import exnihiloadscensio.util.Data;
import exnihiloadscensio.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBase extends Block implements IHasModel {
    public BlockBase(Material mat, String name) {
        super(mat);
        setUnlocalizedName(name);
        setRegistryName(name);
        Data.BLOCKS.add(this);
    }
}
