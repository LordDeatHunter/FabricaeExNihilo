package wraith.fabricaeexnihilo.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import wraith.fabricaeexnihilo.modules.infested.InfestingLeavesBlockEntity;
import wraith.fabricaeexnihilo.util.ItemUtils;

public class InfestingLeavesBlockEntityRenderer implements BlockEntityRenderer<InfestingLeavesBlockEntity> {

    public InfestingLeavesBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(InfestingLeavesBlockEntity infesting, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (infesting == null) {
            return;
        }
        var color = infesting.getColor(0) | -16777216;
        var stack = ItemUtils.asStack(infesting.getInfestedBlock().asItem());

        // TODO: Check if this is valid
        //MinecraftClient.getInstance().getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
        //var texture = MinecraftClient.getInstance().getTextureManager().getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
        //if (texture != null) {
        //    texture.setFilter(false, false);
        //}
        matrices.push();

        var r = (color >> 16 & 255) / 255.0F;
        var g = (color >> 8 & 255) / 255.0F;
        var b = (color & 255) / 255.0F;

        RenderSystem.setShaderColor(r, g, b, 1F);
        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.NONE, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, (int) infesting.getPos().asLong());

        RenderSystem.enableCull();
        matrices.pop();
    }
}
