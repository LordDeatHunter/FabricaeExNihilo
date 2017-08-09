package exnihilocreatio.client.renderers;

import exnihilocreatio.blocks.AutoSieverPart;
import exnihilocreatio.blocks.BlockAutoSiever;
import exnihilocreatio.tiles.TileAutoSiever;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class RenderAutoSiever extends TileEntitySpecialRenderer<TileAutoSiever> {
    @Override
    public void render(TileAutoSiever te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        // Render the rotating handles
        renderGear(te);
        renderBox(te);
        renderRod(te);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

    }

    void renderGear(TileAutoSiever te){
        GlStateManager.pushMatrix();

        GlStateManager.translate(.5, 0.5, .5);

        /*
        if (te.canTurn){
            te.rotationValue = (te.rotationValue + te.perTickEffective) % 360;
        }

        GlStateManager.rotate(te.rotationValue, 0, 0, 1);
        */
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.rotate(System.currentTimeMillis() % 360, 0, 1, 0);

        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        World world = te.getWorld();
        // Translate back to local view coordinates so that we can do the acual rendering here
        GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        IBlockState state = te.getBlockType().getDefaultState().withProperty(BlockAutoSiever.PART_TYPE, AutoSieverPart.GEAR);

        BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        IBakedModel model = dispatcher.getModelForState(state);
        dispatcher.getBlockModelRenderer().renderModel(world, model, state, te.getPos(), bufferBuilder, true);
        tessellator.draw();

        GlStateManager.popMatrix();
    }

    void renderRod(TileAutoSiever te){
        GlStateManager.pushMatrix();

        GlStateManager.translate(.5, 0.5, .5);

        /*
        if (te.canTurn){
            te.rotationValue = (te.rotationValue + te.perTickEffective) % 360;
        }

        GlStateManager.rotate(te.rotationValue, 0, 0, 1);
        */
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.rotate(System.currentTimeMillis() % 360, 0, 0, 1);

        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);


        World world = te.getWorld();
        // Translate back to local view coordinates so that we can do the acual rendering here
        GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        IBlockState state = te.getBlockType().getDefaultState().withProperty(BlockAutoSiever.PART_TYPE, AutoSieverPart.ROD);

        BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        IBakedModel model = dispatcher.getModelForState(state);
        dispatcher.getBlockModelRenderer().renderModel(world, model, state, te.getPos(), bufferBuilder, true);
        tessellator.draw();

        GlStateManager.popMatrix();
    }

    void renderBox(TileAutoSiever te){
        GlStateManager.pushMatrix();

        GlStateManager.translate(.5, 0.5, .5);
        GlStateManager.scale(0.5, 0.5, 0.5);

        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        World world = te.getWorld();
        // Translate back to local view coordinates so that we can do the acual rendering here
        GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        IBlockState state = te.getBlockType().getDefaultState().withProperty(BlockAutoSiever.PART_TYPE, AutoSieverPart.BOX);

        BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        IBakedModel model = dispatcher.getModelForState(state);
        dispatcher.getBlockModelRenderer().renderModel(world, model, state, te.getPos(), bufferBuilder, true);
        tessellator.draw();

        GlStateManager.popMatrix();
    }
}
