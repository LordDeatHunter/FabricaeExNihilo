package exnihilocreatio.client.renderers;

import exnihilocreatio.ModBlocks;
import exnihilocreatio.blocks.BlockWaterwheel;
import exnihilocreatio.tiles.TileWaterwheel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class RenderWaterwheel extends TileEntitySpecialRenderer<TileWaterwheel> {

    @Override
    public void render(TileWaterwheel tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        // Render the rotating handles
        renderWheel(tile);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    private void renderWheel(TileWaterwheel te) {
        GlStateManager.pushMatrix();

        GlStateManager.translate(.5, 0.5, .5);

        switch (te.facing) {

            case DOWN:
                break;
            case UP:
                break;
            case NORTH:
                break;
            case SOUTH:
                GlStateManager.rotate(180, 0, 1, 0);
                break;
            case WEST:
                GlStateManager.rotate(90, 0, 1, 0);
                break;
            case EAST:
                GlStateManager.rotate(-90, 0, 1, 0);
                break;
        }

        if (te.canTurn){
            te.rotationValue = (te.rotationValue + te.getEffectivePerTickRotation()) % 360;
        }

        GlStateManager.rotate(te.rotationValue, 0, 0, 1);


        RenderHelper.disableStandardItemLighting();
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }


        World world = te.getWorld();
        // Translate back to local view coordinates so that we can do the acual rendering here
        GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        IBlockState state = ModBlocks.watermill.getDefaultState().withProperty(BlockWaterwheel.IS_WHEEL, true);

        BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        IBakedModel model = dispatcher.getModelForState(state);
        dispatcher.getBlockModelRenderer().renderModel(world, model, state, te.getPos(), bufferBuilder, true);
        tessellator.draw();

        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}
