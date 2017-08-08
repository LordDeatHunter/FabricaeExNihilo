package exnihilocreatio.blocks;

import exnihilocreatio.util.Data;
import exnihilocreatio.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockBase extends Block implements IHasModel {
    public BlockBase(Material mat, String name) {
        super(mat);
        setUnlocalizedName(name);
        setRegistryName(name);
        Data.BLOCKS.add(this);
    }
}
