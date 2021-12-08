package wraith.fabricaeexnihilo.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
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

        MinecraftClient.getInstance().getTextureManager().bindTexture(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
        MinecraftClient.getInstance().getTextureManager().getTexture(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).setFilter(false, false);
        matrices.push();
        matrices.translate(0.5, 0.5, 0.5);

        var r = (color >> 16 & 255) / 255.0F;
        var g = (color >> 8 & 255) / 255.0F;
        var b = (color & 255) / 255.0F;

        RenderSystem.setShaderColor(r, g, b, 1F);
        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.NONE, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, (int) infesting.getPos().asLong());
        RenderSystem.enableCull();
        matrices.pop();
    }
}
