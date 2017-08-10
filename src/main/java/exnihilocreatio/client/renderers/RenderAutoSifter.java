package exnihilocreatio.client.renderers;

import exnihilocreatio.blocks.BlockAutoSifter;
import exnihilocreatio.blocks.EnumAutoSifterParts;
import exnihilocreatio.tiles.TileAutoSifter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class RenderAutoSifter extends TileEntitySpecialRenderer<TileAutoSifter> {
    private static List<BakedQuad> quadsBox;
    private static List<BakedQuad> quadsGear;
    private static List<BakedQuad> quadsShaft;
    private static List<BakedQuad> quadsConnection;

    @Override
    public void render(TileAutoSifter te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        for (BlockPos blockPos : te.toSift) {
            renderPiston(te, blockPos.getX() - te.getPos().getX() + x, blockPos.getY() - te.getPos().getY() + y - 1, blockPos.getZ() - te.getPos().getZ() + z);
        }
        renderRod(te, x, y, z);
        renderBox(te, x, y, z);
    }

    void renderPiston(TileAutoSifter tile, double x, double y, double z) {
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


        // float rotFacing = tile.facing == EnumFacing.SOUTH ? 180 : tile.facing == EnumFacing.WEST ? 90 : tile.facing == EnumFacing.EAST ? -90 : 0;

        // GlStateManager.rotate(rotFacing, 0, 1, 0);

        // if (tile.canTurn){
        //     tile.rotationValue = (tile.rotationValue + tile.perTickEffective) % 360;
        // }

        // GlStateManager.rotate(System.currentTimeMillis() % 360, 0, 1, 0);
        GlStateManager.translate(0, (float)(System.currentTimeMillis() % 200) / 200F, 0);


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

    void renderConnection(TileAutoSifter tile, double x, double y, double z) {
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


        // float rotFacing = tile.facing == EnumFacing.SOUTH ? 180 : tile.facing == EnumFacing.WEST ? 90 : tile.facing == EnumFacing.EAST ? -90 : 0;

        // GlStateManager.rotate(rotFacing, 0, 1, 0);

        // if (tile.canTurn){
        //     tile.rotationValue = (tile.rotationValue + tile.perTickEffective) % 360;
        // }

        // GlStateManager.rotate(System.currentTimeMillis() % 360, 0, 1, 0);
        GlStateManager.translate(0, (float)(System.currentTimeMillis() % 200) / 200F, 0);


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

    void renderRod(TileAutoSifter tile, double x, double y, double z) {
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


        // float rotFacing = tile.facing == EnumFacing.SOUTH ? 180 : tile.facing == EnumFacing.WEST ? 90 : tile.facing == EnumFacing.EAST ? -90 : 0;

        // GlStateManager.rotate(rotFacing, 0, 1, 0);

        // if (tile.canTurn){
        //     tile.rotationValue = (tile.rotationValue + tile.perTickEffective) % 360;
        // }

        GlStateManager.rotate(System.currentTimeMillis() % 360, 0, 0, 1);

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

    void renderBox(TileAutoSifter tile, double x, double y, double z) {
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
}
