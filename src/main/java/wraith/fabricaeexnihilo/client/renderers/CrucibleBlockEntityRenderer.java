package wraith.fabricaeexnihilo.client.renderers;

import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import wraith.fabricaeexnihilo.modules.crucibles.CrucibleBlockEntity;

public class CrucibleBlockEntityRenderer implements BlockEntityRenderer<CrucibleBlockEntity, CrucibleBlockEntityRenderer.CrucibleRenderState> {

    private static final float XZ_SCALE = 12.0F / 16.0F;
    private static final float X_MIN = 2.0F / 16.0F;
    private static final float X_MAX = 14.0F / 16.0F;
    private static final float Z_MIN = 2.0F / 16.0F;
    private static final float Z_MAX = 14.0F / 16.0F;
    private static final float Y_MIN = 5.0F / 16.0F;
    private static final float Y_MAX = 15.0F / 16.0F;

    private final ItemModelManager itemModelManager;

    public CrucibleBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemModelManager = ctx.itemModelManager();
    }

    @Override
    public CrucibleRenderState createRenderState() {
        return new CrucibleRenderState();
    }

    @Override
    public void updateRenderState(CrucibleBlockEntity crucible, CrucibleRenderState state, float tickDelta, Vec3d cameraPos, ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlayCommand) {
        BlockEntityRenderState.updateBlockEntityRenderState(crucible, state, crumblingOverlayCommand);
        var fluid = crucible.getFluid();
        var contained = crucible.getContained();
        var queued = crucible.getQueued();
        var stack = crucible.getRenderStack();

        state.hasFluid = contained > 0 && !fluid.isBlank();
        if (state.hasFluid) {
            state.fluidLevel = contained / (float) crucible.getMaxCapacity();
            state.fluidSprite = FluidVariantRendering.getSprite(fluid);
            int color = FluidVariantRendering.getColor(fluid);
            state.fluidR = ((color >> 16) & 255) / 256f;
            state.fluidG = ((color >> 8) & 255) / 256f;
            state.fluidB = (color & 255) / 256f;
        } else {
            state.fluidSprite = null;
        }

        state.hasQueued = queued > 0 && !stack.isEmpty();
        if (state.hasQueued) {
            state.queuedLevel = queued / (float) crucible.getMaxCapacity();
            this.itemModelManager.clearAndUpdate(state.queuedItemState, stack, ItemDisplayContext.NONE, crucible.getWorld(), null, (int) crucible.getPos().asLong());
        } else {
            state.queuedItemState.clear();
        }
    }

    @Override
    public void render(CrucibleRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        if (state.hasFluid && state.fluidSprite != null) {
            float y = MathHelper.lerp(state.fluidLevel, Y_MIN, Y_MAX);
            Sprite sprite = state.fluidSprite;
            float u0 = sprite.getMinU();
            float u1 = sprite.getMaxU();
            float v0 = sprite.getMinV();
            float v1 = sprite.getMaxV();
            float r = state.fluidR;
            float g = state.fluidG;
            float b = state.fluidB;
            int light = state.lightmapCoordinates;
            int overlay = OverlayTexture.DEFAULT_UV;

            queue.submitCustom(matrices, TexturedRenderLayers.getBlockTranslucentCull(), (entry, consumer) -> {
                consumer.vertex(entry, X_MIN, y, Z_MIN).color(r, g, b, 1.0f).texture(u0, v0).overlay(overlay).light(light).normal(entry, 0, 1, 0);
                consumer.vertex(entry, X_MIN, y, Z_MAX).color(r, g, b, 1.0f).texture(u0, v1).overlay(overlay).light(light).normal(entry, 0, 1, 0);
                consumer.vertex(entry, X_MAX, y, Z_MAX).color(r, g, b, 1.0f).texture(u1, v1).overlay(overlay).light(light).normal(entry, 0, 1, 0);
                consumer.vertex(entry, X_MAX, y, Z_MIN).color(r, g, b, 1.0f).texture(u1, v0).overlay(overlay).light(light).normal(entry, 0, 1, 0);
            });
        }

        if (state.hasQueued) {
            var yScale = MathHelper.clampedLerp(0, Y_MAX - Y_MIN, state.queuedLevel);
            matrices.push();
            matrices.translate(0.5, Y_MIN + yScale / 2, 0.5);
            matrices.scale(XZ_SCALE, yScale, XZ_SCALE);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
            state.queuedItemState.render(matrices, queue, state.lightmapCoordinates, OverlayTexture.DEFAULT_UV, 0);
            matrices.pop();
        }
    }

    public static class CrucibleRenderState extends BlockEntityRenderState {
        public boolean hasFluid;
        public float fluidLevel;
        public Sprite fluidSprite;
        public float fluidR, fluidG, fluidB;

        public boolean hasQueued;
        public float queuedLevel;
        public final ItemRenderState queuedItemState = new ItemRenderState();
    }
}
