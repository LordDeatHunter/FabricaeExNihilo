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
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import wraith.fabricaeexnihilo.modules.strainer.StrainerBlockEntity;

import java.util.ArrayList;
import java.util.List;

public class StrainerBlockEntityRenderer implements BlockEntityRenderer<StrainerBlockEntity, StrainerBlockEntityRenderer.StrainerRenderState> {

    private final ItemModelManager itemModelManager;

    public StrainerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemModelManager = ctx.itemModelManager();
    }

    @Override
    public StrainerRenderState createRenderState() {
        return new StrainerRenderState();
    }

    @Override
    public void updateRenderState(StrainerBlockEntity entity, StrainerRenderState state, float tickDelta, Vec3d cameraPos, ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlayCommand) {
        BlockEntityRenderState.updateBlockEntityRenderState(entity, state, crumblingOverlayCommand);
        state.itemStates.clear();
        var world = entity.getWorld();
        state.rotationAngle = world != null ? (world.getTime() + tickDelta) * 4 : 0;

        List<ItemStack> items = entity.getInventory().stream().filter(stack -> !stack.isEmpty()).toList();
        for (ItemStack item : items) {
            ItemRenderState itemState = new ItemRenderState();
            this.itemModelManager.clearAndUpdate(itemState, item, ItemDisplayContext.GROUND, world, null, entity.getPos().hashCode());
            state.itemStates.add(itemState);
        }
    }

    @Override
    public void render(StrainerRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        var items = state.itemStates;
        var count = items.size();
        if (items.isEmpty()) return;

        matrices.push();
        matrices.translate(0.5, -0.1, 0.5);

        block:
        {
            // First layer
            matrices.translate(count == 1 ? 0 : 0.25, count < 5 ? 0.5 : 0.25, count == 1 ? 0 : 0.25);
            renderItem(items.get(0), state, matrices, queue);

            if (count < 2) break block;
            matrices.translate(-0.5, 0, -0.5);
            renderItem(items.get(1), state, matrices, queue);

            if (count < 3) break block;
            matrices.translate(0, 0, 0.5);
            renderItem(items.get(2), state, matrices, queue);

            if (count < 4) break block;
            matrices.translate(0.5, 0, -0.5);
            renderItem(items.get(3), state, matrices, queue);

            // Second layer
            if (count < 5) break block;
            matrices.translate(count == 5 ? -0.25 : 0, 0.5, count == 5 ? 0.25 : 0.5);
            renderItem(items.get(4), state, matrices, queue);

            if (count < 6) break block;
            matrices.translate(-0.5, 0, -0.5);
            renderItem(items.get(5), state, matrices, queue);

            if (count < 7) break block;
            matrices.translate(0, 0, 0.5);
            renderItem(items.get(6), state, matrices, queue);

            if (count < 8) break block;
            matrices.translate(0.5, 0, -0.5);
            renderItem(items.get(7), state, matrices, queue);
        }
        matrices.pop();
    }

    private void renderItem(ItemRenderState itemState, StrainerRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue) {
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.rotationAngle));
        itemState.render(matrices, queue, state.lightmapCoordinates, OverlayTexture.DEFAULT_UV, 0);
        matrices.pop();
    }

    public static class StrainerRenderState extends BlockEntityRenderState {
        public final List<ItemRenderState> itemStates = new ArrayList<>();
        public float rotationAngle;
    }
}
