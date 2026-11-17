package wraith.fabricaeexnihilo.client.renderers;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import wraith.fabricaeexnihilo.modules.infested.InfestingLeavesBlockEntity;

public class InfestingLeavesBlockEntityRenderer implements BlockEntityRenderer<InfestingLeavesBlockEntity, InfestingLeavesBlockEntityRenderer.InfestingLeavesRenderState> {

    public InfestingLeavesBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public InfestingLeavesRenderState createRenderState() {
        return new InfestingLeavesRenderState();
    }

    @Override
    public void updateRenderState(InfestingLeavesBlockEntity infesting, InfestingLeavesRenderState state, float tickDelta, Vec3d cameraPos, ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlayCommand) {
        BlockEntityRenderState.updateBlockEntityRenderState(infesting, state, crumblingOverlayCommand);
        var color = infesting.getColor(0) | 0xff000000;
        BlockState targetBlockState = infesting.getTarget().getLeafBlock().getDefaultState();
        state.model = MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(targetBlockState);
        state.r = (color >> 16 & 255) / 255.0F;
        state.g = (color >> 8 & 255) / 255.0F;
        state.b = (color & 255) / 255.0F;
    }

    @Override
    public void render(InfestingLeavesRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        if (state.model == null) return;
        queue.submitBlockStateModel(matrices, TexturedRenderLayers.getEntityCutout(), state.model, state.r, state.g, state.b, state.lightmapCoordinates, OverlayTexture.DEFAULT_UV, 0);
    }

    public static class InfestingLeavesRenderState extends BlockEntityRenderState {
        public BlockStateModel model;
        public float r, g, b;
    }
}
