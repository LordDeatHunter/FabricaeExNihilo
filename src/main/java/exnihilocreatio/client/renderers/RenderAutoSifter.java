package exnihilocreatio.client.renderers;

import exnihilocreatio.blocks.BlockAutoSifter;
import exnihilocreatio.blocks.EnumAutoSifterParts;
import exnihilocreatio.config.ModConfig;
import exnihilocreatio.tiles.TileAutoSifter;
import exnihilocreatio.tiles.TileSieve;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Point3f;
import java.util.List;

public class RenderAutoSifter extends TileEntitySpecialRenderer<TileAutoSifter> {
    private static List<BakedQuad> quadsBox;
    private static List<BakedQuad> quadsGear;
    private static List<BakedQuad> quadsShaft;
    private static List<BakedQuad> quadsConnection;

    @Override
    public void render(TileAutoSifter te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te.toSift != null && ModConfig.client.clientFancyAutoSieveAnimations) {
            for (TileSieve[] tileSieves : te.toSift) {
                for (TileSieve tileSieve : tileSieves) {
                    if (tileSieve != null) {
                        BlockPos blockPos = tileSieve.getPos();
                        renderPiston(te, blockPos.getX() - te.getPos().getX() + x, blockPos.getY() - te.getPos().getY() + y - 1, blockPos.getZ() - te.getPos().getZ() + z);
                    }
                }
            }
        }

        if (te.connectionPieces != null && ModConfig.client.clientFancyAutoSieveAnimations) {
            for (Tuple<Point3f, EnumFacing.Axis> connectionPiece : te.connectionPieces) {
                renderConnection(te, x + connectionPiece.getFirst().x, y + connectionPiece.getFirst().y, z + connectionPiece.getFirst().z, connectionPiece.getSecond());
            }
        }

        renderRod(te, x, y, z, partialTicks);
        renderBox(te, x, y, z);

        renderIngredients(te, x, y, z, partialTicks);
    }

    private void renderPiston(TileAutoSifter tile, double x, double y, double z) {
        if (quadsGear == null) {
            final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
            IBlockState state = tile.getBlockType().getDefaultState().withProperty(BlockAutoSifter.PART_TYPE, EnumAutoSifterParts.PISTON);

            quadsGear = blockRenderer.getModelForState(state).getQuads(state, null, 0);
        }

        Tessellator tessellator = Tessellator.getInstance();
        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);
        GlStateManager.translate(0.5, 0.5, 0.5);
        GlStateManager.scale(0.5, 0.5, 0.5);

        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();


        GlStateManager.translate(tile.offsetX, 0.8F, tile.offsetZ);


        RenderHelper.disableStandardItemLighting();

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        BufferBuilder worldRendererBuffer = tessellator.getBuffer();

        worldRendererBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        // worldRendererBuffer.setTranslation(-.5, -.5, -.5);

        RenderUtils.renderModelTESRFast(quadsGear, worldRendererBuffer, tile.getWorld(), tile.getPos());

        // worldRendererBuffer.setTranslation(0, 0, 0);
        tessellator.draw();

        GlStateManager.popMatrix();
        RenderHelper.enableStandardItemLighting();

        GlStateManager.disableBlend();
        GlStateManager.enableCull();
    }

    private void renderConnection(TileAutoSifter tile, double x, double y, double z, EnumFacing.Axis axis) {
        if (quadsConnection == null) {
            final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
            IBlockState state = tile.getBlockType().getDefaultState().withProperty(BlockAutoSifter.PART_TYPE, EnumAutoSifterParts.CONNECTION);

            quadsConnection = blockRenderer.getModelForState(state).getQuads(state, null, 0);
        }

        /*
        int xRel = blockPos.getX() - te.getPos().getX() + x;
        int yRel = blockPos.getY() - te.getPos().getY() + y - 1;
        int zRel = blockPos.getZ() - te.getPos().getZ() + z;*/

        Tessellator tessellator = Tessellator.getInstance();
        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);
        GlStateManager.translate(0.5, 0.5, 0.5);
        GlStateManager.scale(0.5, 0.5, 0.5);

        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();

        GlStateManager.translate(tile.offsetX, 0.0F, tile.offsetZ);

        // float rotFacing = tile.facing == EnumFacing.SOUTH ? 180 : tile.facing == EnumFacing.WEST ? 90 : tile.facing == EnumFacing.EAST ? -90 : 0;

        if (axis == EnumFacing.Axis.Z)
            GlStateManager.rotate(90, 0, 1, 0);

        // if (tile.canTurn){
        //     tile.rotationValue = (tile.rotationValue + tile.perTickEffective) % 360;
        // }

        // GlStateManager.rotate(System.currentTimeMillis() % 360, 0, 1, 0);
        // GlStateManager.translate(0, (float)(System.currentTimeMillis() % 200) / 200F, 0);


        RenderHelper.disableStandardItemLighting();

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        BufferBuilder worldRendererBuffer = tessellator.getBuffer();

        worldRendererBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        // worldRendererBuffer.setTranslation(-.5, -.5, -.5);

        RenderUtils.renderModelTESRFast(quadsConnection, worldRendererBuffer, tile.getWorld(), tile.getPos());

        // worldRendererBuffer.setTranslation(0, 0, 0);
        tessellator.draw();

        GlStateManager.popMatrix();
        RenderHelper.enableStandardItemLighting();

        GlStateManager.disableBlend();
        GlStateManager.enableCull();
    }

    private void renderRod(TileAutoSifter tile, double x, double y, double z, float partialTicks) {
        if (quadsShaft == null) {
            final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
            IBlockState state = tile.getBlockType().getDefaultState().withProperty(BlockAutoSifter.PART_TYPE, EnumAutoSifterParts.ROD);

            quadsShaft = blockRenderer.getModelForState(state).getQuads(state, null, 0);
        }

        Tessellator tessellator = Tessellator.getInstance();
        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);
        GlStateManager.translate(0.5, 0.5, 0.5);
        GlStateManager.scale(0.5, 0.5, 0.5);


        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();


        float rotFacing = tile.facing == EnumFacing.SOUTH ? 180 : tile.facing == EnumFacing.WEST ? 90 : tile.facing == EnumFacing.EAST ? -90 : 0;
        GlStateManager.rotate(rotFacing, 0, 1, 0);

        float rot = (tile.rotationValue + tile.perTickRotation * partialTicks) % 360;
        GlStateManager.rotate(rot, 0, 0, 1);


        RenderHelper.disableStandardItemLighting();

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        BufferBuilder worldRendererBuffer = tessellator.getBuffer();

        worldRendererBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        // worldRendererBuffer.setTranslation(-.5, -.5, -.5);

        RenderUtils.renderModelTESRFast(quadsShaft, worldRendererBuffer, tile.getWorld(), tile.getPos());

        // worldRendererBuffer.setTranslation(0, 0, 0);
        tessellator.draw();

        GlStateManager.popMatrix();
        RenderHelper.enableStandardItemLighting();

        GlStateManager.disableBlend();
        GlStateManager.enableCull();
    }

    private void renderBox(TileAutoSifter tile, double x, double y, double z) {
        if (quadsBox == null) {
            final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
            IBlockState state = tile.getBlockType().getDefaultState().withProperty(BlockAutoSifter.PART_TYPE, EnumAutoSifterParts.BOX);

            quadsBox = blockRenderer.getModelForState(state).getQuads(state, null, 0);
        }

        Tessellator tessellator = Tessellator.getInstance();
        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);
        GlStateManager.translate(0.5, 0.5, 0.5);
        GlStateManager.scale(0.5, 0.5, 0.5);


        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();


        RenderHelper.disableStandardItemLighting();

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        BufferBuilder worldRendererBuffer = tessellator.getBuffer();

        worldRendererBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        // worldRendererBuffer.setTranslation(-.5, -.5, -.5);

        RenderUtils.renderModelTESRFast(quadsBox, worldRendererBuffer, tile.getWorld(), tile.getPos());

        // worldRendererBuffer.setTranslation(0, 0, 0);
        tessellator.draw();

        GlStateManager.popMatrix();
        RenderHelper.enableStandardItemLighting();

        GlStateManager.disableBlend();
        GlStateManager.enableCull();
    }


    private void renderIngredients(TileAutoSifter tile, double x, double y, double z, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRendererBuffer = tessellator.getBuffer();

        // GlStateManager.translate(0, 1, 0);

        if (tile.getTexture() != null) {
            TextureAtlasSprite icon = tile.getTexture();
            double minU = (double) icon.getMinU();
            double maxU = (double) icon.getMaxU();
            double minV = (double) icon.getMinV();
            double maxV = (double) icon.getMaxV();

            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            worldRendererBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);

            double height = (float) tile.itemHandlerAutoSifter.getStackInSlot(0).getCount() / (float) tile.itemHandlerAutoSifter.getSlotLimit(0);

            // float fillAmount = (float) (height);
            float fillAmount = (float) (0.5 * height + 0.0625);

            worldRendererBuffer.pos(0.0625f, fillAmount, 0.0625f).tex(minU, minV).normal(0, 1, 0).endVertex();
            worldRendererBuffer.pos(0.0625f, fillAmount, 0.9375f).tex(minU, maxV).normal(0, 1, 0).endVertex();
            worldRendererBuffer.pos(0.9375f, fillAmount, 0.9375f).tex(maxU, maxV).normal(0, 1, 0).endVertex();
            worldRendererBuffer.pos(0.9375f, fillAmount, 0.0625f).tex(maxU, minV).normal(0, 1, 0).endVertex();

            tessellator.draw();
        }

        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }
}
