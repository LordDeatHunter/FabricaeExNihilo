package exnihilocreatio.blocks;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.util.Data;
import exnihilocreatio.util.IHasModel;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockBaseFalling extends BlockFalling implements IHasModel {
    public BlockBaseFalling(SoundType sound, String name) {
        super(Material.SAND);
        this.setTranslationKey(name);
        this.setRegistryName(name);
        this.setSoundType(sound);
        this.setCreativeTab(ExNihiloCreatio.tabExNihilo);
        Data.BLOCKS.add(this);
    }
}
