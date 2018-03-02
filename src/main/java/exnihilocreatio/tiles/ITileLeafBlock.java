package exnihilocreatio.tiles;

import net.minecraft.block.state.IBlockState;

/**
 * Bad coding, ye I know, should be mutual parent class but meh for now.
 */
public interface ITileLeafBlock {
    IBlockState getLeafBlock();

    void setLeafBlock(IBlockState block);
}
