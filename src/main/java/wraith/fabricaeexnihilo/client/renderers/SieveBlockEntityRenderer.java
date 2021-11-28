package wraith.fabricaeexnihilo.client.renderers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.modules.sieves.SieveBlockEntity;

public class SieveBlockEntityRenderer implements BlockEntityRenderer<SieveBlockEntity> {

    private final float xzScale = 0.875F;
    private final float yMin = 0.0625F;
    private final float yMax = 0.3750F;

    public SieveBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    public void render(@Nullable SieveBlockEntity sieve, float partialTicks, MatrixStack matrixStack, @Nullable VertexConsumerProvider vertexConsumerProvider, int light, int overlays) {
        if (sieve == null) {
            return;
        }
        var mesh = sieve.getMesh();
        var contents = sieve.getContents();
        var progress = sieve.getProgress();

        // Render the Mesh
        var pos = sieve.getPos();
        int lightAbove = WorldRenderer.getLightmapCoordinates(sieve.getWorld(), pos.up());
        renderMesh(matrixStack, pos, mesh, lightAbove, overlays, vertexConsumerProvider);
        renderContents(matrixStack, pos, contents, (float) progress, lightAbove, overlays, vertexConsumerProvider);
    }

    public void renderMesh(MatrixStack matrixStack, BlockPos pos, ItemStack mesh, int light, int overlays, @Nullable VertexConsumerProvider vertexConsumerProvider) {
        if (mesh.isEmpty()) {
            return;
        }
        var x = pos.getX();
        var y = pos.getY();
        var z = pos.getZ();

        matrixStack.push();
        matrixStack.translate(0.5, 0.5, 0.5);
        MinecraftClient.getInstance().getItemRenderer().renderItem(mesh, ModelTransformation.Mode.NONE, light, overlays, matrixStack, vertexConsumerProvider, (int) pos.asLong());
        matrixStack.pop();
    }

    public void renderContents(MatrixStack matrixStack, BlockPos pos, ItemStack contents, float progress, int light, int overlays, @Nullable VertexConsumerProvider vertexConsumerProvider) {
        if (contents.isEmpty()) {
            return;
        }
        var yScale = yMax - (yMax - yMin) * progress;

        var x = pos.getX();
        var y = pos.getY();
        var z = pos.getZ();

        matrixStack.push();
        matrixStack.translate(0.5, 0.625 + yScale / 2,0.5);
        matrixStack.scale(xzScale, yScale, xzScale);
        MinecraftClient.getInstance().getItemRenderer().renderItem(contents, ModelTransformation.Mode.NONE, light, overlays, matrixStack, vertexConsumerProvider, (int) pos.asLong());
        matrixStack.pop();
    }
}