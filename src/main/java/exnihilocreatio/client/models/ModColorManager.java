package exnihilocreatio.client.models;

import exnihilocreatio.ModBlocks;
import exnihilocreatio.tiles.TileInfestedLeaves;
import exnihilocreatio.util.Util;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

public class ModColorManager {
    private static Minecraft MINECRAFT = Minecraft.getMinecraft();

    public static void registerColorHandlers(){
        registerBlockColorHandlers(MINECRAFT.getBlockColors());
    }

    public static void registerBlockColorHandlers(BlockColors blockColors){

        IBlockColor leafColorHandler = new IBlockColor() {
            @Override
            public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
                if (state != null && pos != null && worldIn != null)
                    return ((TileInfestedLeaves)worldIn.getTileEntity(pos)).getColor();
                return Util.whiteColor.toInt();
            }
        };

        blockColors.registerBlockColorHandler(leafColorHandler, ModBlocks.infestedLeaves);
    }
}
