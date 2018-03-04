package exnihilocreatio.client.renderers;

import exnihilocreatio.blocks.BlockInfestingLeaves;
import exnihilocreatio.client.models.ModelVertex;
import exnihilocreatio.texturing.Color;
import exnihilocreatio.tiles.TileInfestingLeaves;
import exnihilocreatio.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.model.animation.FastTESR;

import javax.annotation.Nullable;


public class RenderInfestingLeaves extends FastTESR<TileInfestingLeaves> {

    private static final ModelVertex[] model = new ModelVertex[24];

    static {
        model[0] = new ModelVertex(EnumFacing.UP, 0, 1, 0, 0, 1);
        model[1] = new ModelVertex(EnumFacing.UP, 1, 1, 0, 1, 1);
        model[2] = new ModelVertex(EnumFacing.UP, 1, 1, 1, 1, 0);
        model[3] = new ModelVertex(EnumFacing.UP, 0, 1, 1, 0, 0);

        model[4] = new ModelVertex(EnumFacing.DOWN, 0, 0, 0, 0, 0);
        model[5] = new ModelVertex(EnumFacing.DOWN, 1, 0, 0, 1, 0);
        model[6] = new ModelVertex(EnumFacing.DOWN, 1, 0, 1, 1, 1);
        model[7] = new ModelVertex(EnumFacing.DOWN, 0, 0, 1, 0, 1);

        model[8] = new ModelVertex(EnumFacing.NORTH, 0, 0, 0, 1, 0);
        model[9] = new ModelVertex(EnumFacing.NORTH, 1, 0, 0, 0, 0);
        model[10] = new ModelVertex(EnumFacing.NORTH, 1, 1, 0, 0, 1);
        model[11] = new ModelVertex(EnumFacing.NORTH, 0, 1, 0, 1, 1);

        model[12] = new ModelVertex(EnumFacing.SOUTH, 0, 0, 1, 0, 0);
        model[13] = new ModelVertex(EnumFacing.SOUTH, 1, 0, 1, 1, 0);
        model[14] = new ModelVertex(EnumFacing.SOUTH, 1, 1, 1, 1, 1);
        model[15] = new ModelVertex(EnumFacing.SOUTH, 0, 1, 1, 0, 1);

        model[16] = new ModelVertex(EnumFacing.EAST, 1, 0, 0, 1, 0);
        model[17] = new ModelVertex(EnumFacing.EAST, 1, 0, 1, 0, 0);
        model[18] = new ModelVertex(EnumFacing.EAST, 1, 1, 1, 0, 1);
        model[19] = new ModelVertex(EnumFacing.EAST, 1, 1, 0, 1, 1);

        model[20] = new ModelVertex(EnumFacing.WEST, 0, 0, 0, 0, 0);
        model[21] = new ModelVertex(EnumFacing.WEST, 0, 0, 1, 1, 0);
        model[22] = new ModelVertex(EnumFacing.WEST, 0, 1, 1, 1, 1);
        model[23] = new ModelVertex(EnumFacing.WEST, 0, 1, 0, 0, 1);

    }

    @Override
    public void renderTileEntityFast(@Nullable TileInfestingLeaves te, double x, double y, double z, float partialTicks, int destroyStage, float partial, @Nullable BufferBuilder buffer) {
        if (te == null || buffer == null) return;
        final BlockPos pos = te.getPos();
        final Block block = getWorld().getBlockState(pos).getBlock();

        // Light levels
        final int mixedBrightness = getWorld().getBlockState(pos).getPackedLightmapCoords(te.getWorld(), pos);
        final int skyLight = mixedBrightness >> 16 & 0xFFFF;
        final int blockLight = mixedBrightness & 0xFFFF;
        //
        final IBlockState state = te.getLeafBlock();

        // Color
        final Color color;
        if (block instanceof BlockInfestingLeaves)
            color = Color.average(new Color(BiomeColorHelper.getFoliageColorAtPos(getWorld(), pos)), Util.whiteColor, (float) Math.pow((te.getProgress() / 100f), 2.0));
        else
            color = Util.whiteColor;

        final TextureAtlasSprite sprite = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
        buffer.setTranslation(x - pos.getX(), y - pos.getY(), z - pos.getZ());
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, skyLight * 16.0F, blockLight * 16.0F);

        Vec3d view = Minecraft.getMinecraft().getRenderViewEntity().getPositionEyes(partialTicks);
        for (final ModelVertex vert : model) {
            final EnumFacing face = vert.face;

            // Dirty face culling
            double dot = 0;
            switch (face) {
                case DOWN:
                    dot = -(view.y - (pos.getY()));
                    break;
                case UP:
                    dot = (view.y - (pos.getY() + 1));
                    break;
                case NORTH:
                    dot = -(view.z - (pos.getZ()));
                    break;
                case SOUTH:
                    dot = (view.z - (pos.getZ() + 1));
                    break;
                case WEST:
                    dot = -(view.x - (pos.getX()));
                    break;
                case EAST:
                    dot = (view.x - (pos.getX() + 1));
                    break;
            }
            if (dot > 0 && block.shouldSideBeRendered(state, getWorld(), pos, vert.face)) {
                for (final VertexFormatElement e : buffer.getVertexFormat().getElements()) {
                    switch (e.getUsage()) {
                        case COLOR:
                            buffer.color(color.r, color.g, color.b, color.a);
                            break;

                        case NORMAL:
                            buffer.normal(face.getFrontOffsetX(), face.getFrontOffsetY(), face.getFrontOffsetZ());
                            break;

                        case POSITION:
                            final double vertX = pos.getX() + vert.x;
                            final double vertY = pos.getY() + vert.y;
                            final double vertZ = pos.getZ() + vert.z;

                            buffer.pos(vertX, vertY, vertZ);
                            break;

                        case UV:
                            if (e.getIndex() == 1) {
                                buffer.lightmap(skyLight, blockLight);
                            } else {
                                buffer.tex(sprite.getInterpolatedU(vert.u), sprite.getInterpolatedV(16.0 - vert.v));
                            }
                            break;

                        default:
                            break;
                    }
                }
                buffer.endVertex();
            }
        }
        // Don't screw everyone else up
        buffer.setTranslation(0, 0, 0);
    }
}