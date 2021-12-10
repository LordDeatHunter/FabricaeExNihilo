package wraith.fabricaeexnihilo.client.renderers;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.render.FluidRenderFace;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import wraith.fabricaeexnihilo.modules.crucibles.CrucibleBlockEntity;

import java.util.List;

public class CrucibleBlockEntityRenderer implements BlockEntityRenderer<CrucibleBlockEntity> {

    private final float xzScale = 12.0F / 16.0F;
    private final float xMin = 2.0F / 16.0F;
    private final float xMax = 14.0F / 16.0F;
    private final float zMin = 2.0F / 16.0F;
    private final float zMax = 14.0F / 16.0F;
    private final float yMin = 5.0F / 16.0F;
    private final float yMax = 15.0F / 16.0F;

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
            renderFluidVolume(contents, contents.copy().amount().div(crucible.getMaxCapacity()), matrices, vertexConsumers);
        }

        var render = crucible.getRender();

        if (!queued.isEmpty() && !render.isEmpty()) {
            renderQueued(render, queued.copy().amount().div(crucible.getMaxCapacity()), matrices, vertexConsumers, light, overlay, (int) crucible.getPos().asLong());
        }

    }

    public void renderFluidVolume(FluidVolume volume, FluidAmount level, MatrixStack matrices, VertexConsumerProvider vertexConsumer) {
        double dLevel = (double)level.as1620() / FluidAmount.BUCKET.as1620();
        var yRender = MathHelper.clamp((yMax - yMin) * dLevel + yMin, 0, 1);
        volume.render(List.of(FluidRenderFace.createFlatFace(xMin, yMin, zMin, xMax, yRender, zMax, 1.0, Direction.UP)), vertexConsumer, matrices);
    }

    public void renderQueued(ItemStack renderStack, FluidAmount level, MatrixStack matrices, VertexConsumerProvider vertexConsumer, int light, int overlay, int seed) {
        float dLevel = (float)level.as1620() / FluidAmount.BUCKET.as1620();
        var yScale = MathHelper.clamp((yMax - yMin) * dLevel, 0, 1);

        matrices.push();
        matrices.translate(0.5, yMin + yScale / 2, 0.5);
        matrices.scale(xzScale, yScale, xzScale);
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
        MinecraftClient.getInstance().getItemRenderer().renderItem(renderStack, ModelTransformation.Mode.NONE, light, overlay, matrices, vertexConsumer, seed);
        matrices.pop();
    }

}