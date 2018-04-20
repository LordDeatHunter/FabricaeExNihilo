package exnihilocreatio.client.renderers;

import exnihilocreatio.client.models.ModelVertex;
import exnihilocreatio.texturing.Color;
import exnihilocreatio.texturing.SpriteColor;
import exnihilocreatio.tiles.TileBarrel;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.animation.FastTESR;

import javax.annotation.Nullable;

public class RenderBarrel extends FastTESR<TileBarrel> {
    private static final ModelVertex[] model = new ModelVertex[4];

    static {
        model[0] = new ModelVertex(EnumFacing.UP, 0.125, 0.875, 0.125, 0, 0);
        model[1] = new ModelVertex(EnumFacing.UP, 0.875, 0.875, 0.125, 1, 0);
        model[2] = new ModelVertex(EnumFacing.UP, 0.875, 0.875, 0.875, 1, 1);
        model[3] = new ModelVertex(EnumFacing.UP, 0.125, 0.875, 0.875, 0, 1);
    }

    @Override
    public void renderTileEntityFast(@Nullable TileBarrel te, double x, double y, double z, float partialTicks, int destroyStage, float partial, @Nullable BufferBuilder buffer) {
        if (te == null || te.getMode() == null || buffer == null) return;

        // Fill Level
        float fill = te.getMode().getFilledLevelForRender(te);

        if (fill == 0) return;

        final SpriteColor sprite = te.getMode().getSpriteColor(te);
        buffer.setTranslation(x, y, z);
        addSpriteColor(te, sprite, buffer, te.getMode().getFilledLevelForRender(te));
        buffer.setTranslation(0, 0, 0);

    }

    private void addSpriteColor(TileBarrel te, SpriteColor sprite, BufferBuilder buffer, float fill) {
        if (sprite == null) return;

        final BlockPos pos = te.getPos();
        // Light levels
        final int mixedBrightness = te.getWorld().getBlockState(pos).getPackedLightmapCoords(te.getWorld(), te.getPos());
        final int skyLight = mixedBrightness >> 16 & 0xFFFF;
        final int blockLight = mixedBrightness & 0xFFFF;
        // Texturing
        TextureAtlasSprite icon = sprite.getSprite();
        Color color = sprite.getColor();

        // Draw
        for (final ModelVertex vert : model) {
            for (final VertexFormatElement e : buffer.getVertexFormat().getElements()) {
                switch (e.getUsage()) {
                    case COLOR:
                        buffer.color(color.r, color.g, color.b, color.a);
                        break;

                    case NORMAL:
                        buffer.normal(vert.face.getFrontOffsetX(), vert.face.getFrontOffsetY(), vert.face.getFrontOffsetZ());
                        break;

                    case POSITION:
                        final double vertX = vert.x;
                        final double vertZ = vert.z;

                        buffer.pos(vertX, (double) fill, vertZ);
                        break;

                    case UV:
                        if (e.getIndex() == 1) {
                            buffer.lightmap(skyLight, blockLight);
                        } else {
                            buffer.tex(icon.getInterpolatedU(vert.u), icon.getInterpolatedV(16.0 - vert.v));
                        }
                        break;

                    default:
                        break;
                }
            }
            buffer.endVertex();
        }
    }
}