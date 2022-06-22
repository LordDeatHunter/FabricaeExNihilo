package wraith.fabricaeexnihilo.mixins.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import wraith.fabricaeexnihilo.client.BlockModelRendererFlags;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(BlockModelRenderer.class)
public class BlockModelRendererMixin {
    @ModifyVariable(method = "renderQuads", at = @At("LOAD"), index = 10)
    private static float fabricaeexnihilo$modifyRed(float original, MatrixStack.Entry entry, VertexConsumer consumer, float red, float green, float blue, List<BakedQuad> quads, int lighting, int overlay) {
        return BlockModelRendererFlags.isColorOverridden() ? MathHelper.clamp(red, 0, 1) : original;
    }
    
    @ModifyVariable(method = "renderQuads", at = @At("LOAD"), index = 11)
    private static float fabricaeexnihilo$modifyGreen(float original, MatrixStack.Entry entry, VertexConsumer consumer, float red, float green, float blue, List<BakedQuad> quads, int lighting, int overlay) {
        return BlockModelRendererFlags.isColorOverridden() ? MathHelper.clamp(green, 0, 1) : original;
    }
    
    @ModifyVariable(method = "renderQuads", at = @At("LOAD"), index = 12)
    private static float fabricaeexnihilo$modifyBlue(float original, MatrixStack.Entry entry, VertexConsumer consumer, float red, float green, float blue, List<BakedQuad> quads, int lighting, int overlay) {
        return BlockModelRendererFlags.isColorOverridden() ? MathHelper.clamp(blue, 0, 1) : original;
    }
}
