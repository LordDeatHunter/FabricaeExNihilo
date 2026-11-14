package wraith.fabricaeexnihilo.client.renderers;

import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.math.Vec3d;
import wraith.fabricaeexnihilo.modules.sieves.SieveBlockEntity;

public class SieveBlockEntityRenderer implements BlockEntityRenderer<SieveBlockEntity, SieveBlockEntityRenderer.SieveRenderState> {

    private static final float XZ_SCALE = 0.875F;
    private static final float Y_MIN = 0.0625F;
    private static final float Y_MAX = 0.3750F;

    private final ItemModelManager itemModelManager;

    public SieveBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemModelManager = ctx.itemModelManager();
    }

    @Override
    public SieveRenderState createRenderState() {
        return new SieveRenderState();
    }

    @Override
    public void updateRenderState(SieveBlockEntity sieve, SieveRenderState state, float tickDelta, Vec3d cameraPos, ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlayCommand) {
        BlockEntityRenderState.updateBlockEntityRenderState(sieve, state, crumblingOverlayCommand);
        var mesh = sieve.getMesh();
        var contents = sieve.getContents();
        state.progress = (float) sieve.getProgress();

        if (!mesh.isEmpty()) {
            state.hasMesh = true;
            this.itemModelManager.clearAndUpdate(state.meshState, mesh, ItemDisplayContext.NONE, sieve.getWorld(), null, (int) sieve.getPos().asLong());
        } else {
            state.hasMesh = false;
            state.meshState.clear();
        }

        if (!contents.isEmpty()) {
            state.hasContents = true;
            this.itemModelManager.clearAndUpdate(state.contentsState, contents, ItemDisplayContext.NONE, sieve.getWorld(), null, (int) sieve.getPos().asLong());
        } else {
            state.hasContents = false;
            state.contentsState.clear();
        }
    }

    @Override
    public void render(SieveRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        if (state.hasMesh) {
            matrices.push();
            matrices.translate(0.5, 0.5, 0.5);
            state.meshState.render(matrices, queue, state.lightmapCoordinates, OverlayTexture.DEFAULT_UV, 0);
            matrices.pop();
        }

        if (state.hasContents) {
            var yScale = Y_MAX - (Y_MAX - Y_MIN) * state.progress;
            matrices.push();
            matrices.translate(0.5, 0.625 + yScale / 2, 0.5);
            matrices.scale(XZ_SCALE, yScale, XZ_SCALE);
            state.contentsState.render(matrices, queue, state.lightmapCoordinates, OverlayTexture.DEFAULT_UV, 0);
            matrices.pop();
        }
    }

    public static class SieveRenderState extends BlockEntityRenderState {
        public final ItemRenderState meshState = new ItemRenderState();
        public final ItemRenderState contentsState = new ItemRenderState();
        public boolean hasMesh;
        public boolean hasContents;
        public float progress;
    }
}
