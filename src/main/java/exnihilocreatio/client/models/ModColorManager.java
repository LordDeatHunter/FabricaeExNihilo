package exnihilocreatio.client.models;

import exnihilocreatio.ModBlocks;
import exnihilocreatio.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;

public class ModColorManager {
    private static final Minecraft MINECRAFT = Minecraft.getMinecraft();

    public static void registerColorHandlers() {
        registerBlockColorHandlers(MINECRAFT.getBlockColors());
    }

    private static void registerBlockColorHandlers(BlockColors blockColors) {
        IBlockColor leafColorHandler = (state, worldIn, pos, tintIndex) -> Util.whiteColor.toInt();

        blockColors.registerBlockColorHandler(leafColorHandler, ModBlocks.infestedLeaves);
    }
}
