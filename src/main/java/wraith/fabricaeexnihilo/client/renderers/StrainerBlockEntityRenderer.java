package wraith.fabricaeexnihilo.client.renderers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3f;
import wraith.fabricaeexnihilo.modules.strainer.StrainerBlockEntity;

public class StrainerBlockEntityRenderer implements BlockEntityRenderer<StrainerBlockEntity> {
    public StrainerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }
    
    @Override
    public void render(StrainerBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        var itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        var items = entity.getInventory().stream().filter(stack -> !stack.isEmpty()).toList();
        var count = items.size();
        if (items.isEmpty()) return;
        
        // TODO: Clean up, maybe move the translation to renderItem and use coordinates relative to the block
        
        matrices.push();
        matrices.translate(0.5, -0.1, 0.5);
        // First layer
        matrices.translate(count == 1 ? 0 : 0.25, count < 5 ? 0.5 : 0.25, count == 1 ? 0 : 0.25);
        renderItem(items.get(0), entity, tickDelta, matrices, vertexConsumers, light, overlay, itemRenderer);
        if (count > 1) {
            matrices.translate(-0.5, 0, -0.5);
            renderItem(items.get(1), entity, tickDelta, matrices, vertexConsumers, light, overlay, itemRenderer);
        }
        if (count > 2) {
            matrices.translate(0, 0, 0.5);
            renderItem(items.get(2), entity, tickDelta, matrices, vertexConsumers, light, overlay, itemRenderer);
        }
        if (count > 3) {
            matrices.translate(0.5, 0, -0.5);
            renderItem(items.get(3), entity, tickDelta, matrices, vertexConsumers, light, overlay, itemRenderer);
        }
        // Second layer
        if (count > 4) {
            matrices.translate(count == 5 ? -0.25 : 0, 0.5, count == 5 ? 0.25 : 0.5);
            renderItem(items.get(4), entity, tickDelta, matrices, vertexConsumers, light, overlay, itemRenderer);
        }
        if (count > 5) {
            matrices.translate(-0.5, 0, -0.5);
            renderItem(items.get(5), entity, tickDelta, matrices, vertexConsumers, light, overlay, itemRenderer);
        }
        if (count > 6) {
            matrices.translate(0, 0, 0.5);
            renderItem(items.get(6), entity, tickDelta, matrices, vertexConsumers, light, overlay, itemRenderer);
        }
        if (count == 8) {
            matrices.translate(0.5, 0, -0.5);
            renderItem(items.get(7), entity, tickDelta, matrices, vertexConsumers, light, overlay, itemRenderer);
        }
        matrices.pop();
    }
    
    private void renderItem(ItemStack stack, StrainerBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ItemRenderer itemRenderer) {
        matrices.push();
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((entity.getWorld().getTime() + tickDelta) * 4));
        itemRenderer.renderItem(stack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, entity.getPos().hashCode());
        matrices.pop();
    }
    
}
