package exnihilocreatio.client.renderers;

import exnihilocreatio.blocks.BlockInfestedLeaves;
import exnihilocreatio.blocks.BlockInfestingLeaves;
import exnihilocreatio.client.models.ModelVertex;
import exnihilocreatio.tiles.TileInfestingLeaves;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.animation.FastTESR;

import static java.lang.Math.round;

public class RenderInfestingLeaves extends FastTESR<TileInfestingLeaves> {

    private static ModelVertex[] model = new ModelVertex[24];
    static {
        model[0] = new ModelVertex( EnumFacing.UP, 0, 1, 0, 0, 0, 0, 0 );
        model[1] = new ModelVertex( EnumFacing.UP, 1, 1, 0, 1, 0, 0, 0 );
        model[2] = new ModelVertex( EnumFacing.UP, 1, 1, 1, 1, 1, 0, 0 );
        model[3] = new ModelVertex( EnumFacing.UP, 0, 1, 1, 0, 1, 0, 0 );

        model[4] = new ModelVertex( EnumFacing.DOWN, 0, 0, 0, 0, 0, 0, 0 );
        model[5] = new ModelVertex( EnumFacing.DOWN, 1, 0, 0, 1, 0, 0, 0 );
        model[6] = new ModelVertex( EnumFacing.DOWN, 1, 0, 1, 1, 1, 0, 0 );
        model[7] = new ModelVertex( EnumFacing.DOWN, 0, 0, 1, 0, 1, 0, 0 );

        model[8] = new ModelVertex( EnumFacing.NORTH, 0, 0, 0, 0, 0, 0, 0 );
        model[9] = new ModelVertex( EnumFacing.NORTH, 1, 0, 0, 1, 0, 0, 0 );
        model[10] = new ModelVertex( EnumFacing.NORTH, 1, 1, 0, 1, 0, 0, 1 );
        model[11] = new ModelVertex( EnumFacing.NORTH, 0, 1, 0, 0, 0, 0, 1 );

        model[12] = new ModelVertex( EnumFacing.SOUTH, 0, 0, 1, 0, 0, 0, 0 );
        model[13] = new ModelVertex( EnumFacing.SOUTH, 1, 0, 1, 1, 0, 0, 0 );
        model[14] = new ModelVertex( EnumFacing.SOUTH, 1, 1, 1, 1, 0, 0, 1 );
        model[15] = new ModelVertex( EnumFacing.SOUTH, 0, 1, 1, 0, 0, 0, 1 );

        model[16] = new ModelVertex( EnumFacing.EAST, 1, 0, 0, 0, 0, 0, 0 );
        model[17] = new ModelVertex( EnumFacing.EAST, 1, 0, 1, 1, 0, 0, 0 );
        model[18] = new ModelVertex( EnumFacing.EAST, 1, 1, 1, 1, 0, 0, 1 );
        model[19] = new ModelVertex( EnumFacing.EAST, 1, 1, 0, 0, 0, 0, 1 );

        model[20] = new ModelVertex( EnumFacing.WEST, 0, 0, 0, 0, 0, 0, 0 );
        model[21] = new ModelVertex( EnumFacing.WEST, 0, 0, 1, 1, 0, 0, 0 );
        model[22] = new ModelVertex( EnumFacing.WEST, 0, 1, 1, 1, 0, 0, 1 );
        model[23] = new ModelVertex( EnumFacing.WEST, 0, 1, 0, 0, 0, 0, 1 );
    }
    @Override
    public void renderTileEntityFast(TileInfestingLeaves te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer){
        final BlockPos pos = te.getPos();
        final Block block = getWorld().getBlockState(pos).getBlock();

        // Check because sometimes the renderer gets called while the block is breaking/decaying
        if(!(block instanceof BlockInfestingLeaves) || block instanceof BlockInfestedLeaves) return;

        // Light levels
        final int mixedBrightness = te.getWorld().getBlockState(pos).getPackedLightmapCoords(te.getWorld(), te.getPos());
        final int skyLight = mixedBrightness >> 16 & 0xFFFF;
        final int blockLight = mixedBrightness & 0xFFFF;
        //
        final IBlockState state = te.getLeafBlock();

        // Color
        final int color = te.getColor();
        int rColor = color >> 16 & 0xff;
        int gColor = color >> 8 & 0xff;
        int bColor = color & 0xff;
        int aColor = 255;
        rColor += round((255 - rColor)*te.getProgress());
        gColor += round((255 - gColor)*te.getProgress());
        bColor += round((255 - bColor)*te.getProgress());

        final TextureAtlasSprite sprite = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
        buffer.setTranslation(x-pos.getX(), y-pos.getY(), z-pos.getZ());

        for ( final ModelVertex vert : model ) {
            final EnumFacing face = vert.face;
            for (final VertexFormatElement e : buffer.getVertexFormat().getElements()) {
                switch (e.getUsage()) {
                    case COLOR:
                        buffer.color(rColor, gColor, bColor, aColor);
                        break;

                    case NORMAL:
                        buffer.normal(face.getFrontOffsetX(), face.getFrontOffsetY(), face.getFrontOffsetZ());
                        break;

                    case POSITION:
                        final double vertX = pos.getX() + vert.x;
                        final double vertY = pos.getY() + vert.yMultiplier;
                        final double vertZ = pos.getZ() + vert.z;

                        buffer.pos(vertX, vertY, vertZ);
                        break;

                    case UV:
                        if (e.getIndex() == 1) {
                            buffer.lightmap(skyLight, blockLight);
                        } else {
                            buffer.tex(sprite.getInterpolatedU(vert.u + vert.uMultiplier), sprite.getInterpolatedV(16.0 - (vert.v + vert.vMultiplier)));
                        }
                        break;

                    default:
                        break;
                }
            }
            buffer.endVertex();
        }


        // Don't screw everyone else up
        buffer.setTranslation(0, 0, 0);
    }
}