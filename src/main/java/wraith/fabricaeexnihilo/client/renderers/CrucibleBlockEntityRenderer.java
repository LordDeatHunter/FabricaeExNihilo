package wraith.fabricaeexnihilo.client.renderers;

import alexiil.mc.lib.attributes.fluid.render.FluidRenderFace;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.modules.crucibles.CrucibleBlockEntity;

import java.util.List;

public class CrucibleBlockEntityRenderer implements BlockEntityRenderer<CrucibleBlockEntity> {

    private static final double xzScale = 12.0 / 16.0;
    private static final double xMin = 2.0 / 16.0;
    private static final double xMax = 14.0 / 16.0;
    private static final double zMin = 2.0 / 16.0;
    private static final double zMax = 14.0 / 16.0;
    private static final double yMin = 5.0 / 16.0;
    private static final double yMax = 15.0 / 16.0;

    public CrucibleBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(CrucibleBlockEntity crucible, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (crucible == null) {
            return;
        }
        var contents = crucible.getContents();
        var queued = crucible.getQueued();

        if (!contents.isEmpty()) {
            renderFluidVolume(crucible, contents, (double)contents.amount().as1620() / crucible.getMaxCapacity(), matrices, vertexConsumers);
        }

        var render = crucible.getRender();

        if (!queued.isEmpty() && !render.isEmpty()) {
            renderQueued(render, (double)queued.amount().as1620() / crucible.getMaxCapacity(), matrices, vertexConsumers, light, overlay);
        }

    }

    public void renderFluidVolume(BlockEntity blockEntity, FluidVolume volume, double level, @Nullable MatrixStack matrices, @Nullable VertexConsumerProvider vertexConsumer) {
        var yRender = (yMax - yMin) * level + yMin;

        //RenderSystem.disableLighting();
        volume.render(List.of(FluidRenderFace.createFlatFace(xMin, yMin, zMin, xMax, yRender, zMax, 16.0, Direction.UP)), vertexConsumer, matrices);
        //RenderSystem.enableLighting();
    }

    public void renderQueued(ItemStack renderStack, double level, @Nullable MatrixStack matrices, @Nullable VertexConsumerProvider vertexConsumer, int light, int overlay) {
        var yScale = (yMax - yMin) * level;

//        GlStateManager.pushMatrix()
//        GlStateManager.translated(x+0.5,y+yMin+yScale/2, z+0.5)
//        GlStateManager.scaled(xzScale,yScale,xzScale)
//        MinecraftClient.getInstance().itemRenderer.renderItem(renderStack, ModelTransformation.Mode.NONE, light, overlay, matrices, vertexConsumer)
//        GlStateManager.popMatrix()
    }

}