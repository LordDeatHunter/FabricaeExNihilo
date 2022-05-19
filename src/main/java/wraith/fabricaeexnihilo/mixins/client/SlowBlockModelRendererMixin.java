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
@SuppressWarnings("all") // TODO: fix this and re-enable
public class SlowBlockModelRendererMixin {
    @ModifyVariable(
            method = "renderQuad(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumer;FFFLjava/util/List;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/VertexConsumer;quad(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/model/BakedQuad;FFFII)V",
                    shift = At.Shift.BY,
                    by = -8
            ),
            index = 10
    )
    private static float renderQuad$red(float original, MatrixStack.Entry entry, VertexConsumer consumer, float red, float green, float blue, List<BakedQuad> quads, int lighting, int overlay) {
        return BlockModelRendererFlags.isColorOverriden() ? MathHelper.clamp(red, 0, 1) : original;
    }
    
    @ModifyVariable(
            method = "renderQuad(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumer;FFFLjava/util/List;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/VertexConsumer;quad(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/model/BakedQuad;FFFII)V",
                    shift = At.Shift.BY,
                    by = -8
            ),
            index = 11
    )
    private static float renderQuad$green(float original, MatrixStack.Entry entry, VertexConsumer consumer, float red, float green, float blue, List<BakedQuad> quads, int lighting, int overlay) {
        return BlockModelRendererFlags.isColorOverriden() ? MathHelper.clamp(green, 0, 1) : original;
    }
    
    @ModifyVariable(
            method = "renderQuad(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumer;FFFLjava/util/List;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/VertexConsumer;quad(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/model/BakedQuad;FFFII)V",
                    shift = At.Shift.BY,
                    by = -8
            ),
            index = 12
    )
    private static float renderQuad$blue(float original, MatrixStack.Entry entry, VertexConsumer consumer, float red, float green, float blue, List<BakedQuad> quads, int lighting, int overlay) {
        return BlockModelRendererFlags.isColorOverriden() ? MathHelper.clamp(blue, 0, 1) : original;
    }
}
