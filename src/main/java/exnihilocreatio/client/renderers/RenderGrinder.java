package exnihilocreatio.client.renderers;

import exnihilocreatio.blocks.EnumGrinderParts;
import exnihilocreatio.blocks.BlockGrinder;
import exnihilocreatio.tiles.TileGrinder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class RenderGrinder extends TileEntitySpecialRenderer<TileGrinder> {
    private static List<BakedQuad> quadsBox;
    private static List<BakedQuad> quadsGear;
    private static List<BakedQuad> quadsShaft;

    @Override
    public void render(TileGrinder te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        // GlStateManager.pushAttrib();
        // GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        // GlStateManager.translate(x, y, z);

        renderGear(te, x, y, z, partialTicks);
        renderRod(te, x, y, z, partialTicks);
        renderBox(te, x, y, z);

        // GlStateManager.popMatrix();
        // GlStateManager.popAttrib();

    }

    void renderGear(TileGrinder tile, double x, double y, double z, float partialTicks) {
        if (quadsGear == null) {
            final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
            IBlockState state = tile.getBlockType().getDefaultState().withProperty(BlockGrinder.PART_TYPE, EnumGrinderParts.GEAR);

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

        float rot = 360F - ((tile.rotationValue + tile.perTickRotation * partialTicks) % 360F);
        GlStateManager.rotate(rot, 0, 1, 0);

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

    void renderRod(TileGrinder tile, double x, double y, double z, float partialTicks) {
        if (quadsShaft == null) {
            final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
            IBlockState state = tile.getBlockType().getDefaultState().withProperty(BlockGrinder.PART_TYPE, EnumGrinderParts.ROD);

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

    void renderBox(TileGrinder tile, double x, double y, double z) {
        if (quadsBox == null) {
            final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
            IBlockState state = tile.getBlockType().getDefaultState().withProperty(BlockGrinder.PART_TYPE, EnumGrinderParts.BOX);

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
