package exnihilocreatio.tiles;

import net.minecraft.block.state.IBlockState;

public interface ITileLeafBlock {
    IBlockState getLeafBlock();

    void setLeafBlock(IBlockState block);
}
