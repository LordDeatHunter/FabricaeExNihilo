package exnihilocreatio.client.renderers;

import exnihilocreatio.ModBlocks;
import exnihilocreatio.blocks.AutoSieverPart;
import exnihilocreatio.blocks.BlockAutoSiever;
import exnihilocreatio.blocks.BlockStoneAxle;
import exnihilocreatio.tiles.TileAutoSiever;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class RenderAutoSiever extends TileEntitySpecialRenderer<TileAutoSiever> {
    private static List<BakedQuad> quadsBox;
    private static List<BakedQuad> quadsGear;
    private static List<BakedQuad> quadsShaft;

    @Override
    public void render(TileAutoSiever te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        // GlStateManager.pushAttrib();
        // GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        // GlStateManager.translate(x, y, z);

        renderGear(te, x, y, z);
        renderRod(te, x, y, z);
        renderBox(te, x, y, z);

        // GlStateManager.popMatrix();
        // GlStateManager.popAttrib();

    }

    void renderGear(TileAutoSiever tile, double x, double y, double z){
        if (quadsGear==null)
        {
            final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
            IBlockState state = tile.getBlockType().getDefaultState().withProperty(BlockAutoSiever.PART_TYPE, AutoSieverPart.GEAR);

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

        GlStateManager.rotate(System.currentTimeMillis() % 360, 0, 1, 0);

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

    void renderRod(TileAutoSiever tile, double x, double y, double z){
        if (quadsShaft==null)
        {
            final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
            IBlockState state = tile.getBlockType().getDefaultState().withProperty(BlockAutoSiever.PART_TYPE, AutoSieverPart.ROD);

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

    void renderBox(TileAutoSiever tile, double x, double y, double z){
        if (quadsBox==null)
        {
            final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
            IBlockState state = tile.getBlockType().getDefaultState().withProperty(BlockAutoSiever.PART_TYPE, AutoSieverPart.BOX);

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
