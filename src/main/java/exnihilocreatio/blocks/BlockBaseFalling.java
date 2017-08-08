package exnihilocreatio.blocks;

import exnihilocreatio.util.Data;
import exnihilocreatio.util.IHasModel;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockBaseFalling extends BlockFalling implements IHasModel {
    public BlockBaseFalling(SoundType sound, String name) {
        super(Material.SAND);
        setUnlocalizedName(name);
        setRegistryName(name);
        setSoundType(sound);
        Data.BLOCKS.add(this);
    }
}
