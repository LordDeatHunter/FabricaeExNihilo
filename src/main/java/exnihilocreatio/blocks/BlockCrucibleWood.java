package exnihilocreatio.blocks;

import exnihilocreatio.tiles.TileCrucibleWood;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockCrucibleWood extends BlockCrucibleBase {

    public BlockCrucibleWood() {
        super("block_crucible_wood", Material.WOOD);

        this.setHardness(2.0f);
    }

    @Override
    public TileEntity createTileEntity(@Nonnull World worldIn, @Nonnull IBlockState state) {
        return new TileCrucibleWood();
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }
}
