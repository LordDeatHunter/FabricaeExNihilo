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
import org.spongepowered.asm.mixin.injection.Redirect;
import wraith.fabricaeexnihilo.client.BlockModelRendererFlags;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(BlockModelRenderer.class)
public class FastBlockModelRendererMixin{
    @Redirect(
        method = "renderQuad(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumer;FFFLjava/util/List;II)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/VertexConsumer;quad(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/model/BakedQuad;FFFII)V"
        )
    )
    private static void renderQuad$VertexConsumer$quad(
        VertexConsumer consumer, MatrixStack.Entry entry, BakedQuad quad, float red, float green, float blue, int light, int overlay,
        MatrixStack.Entry originalEntry, VertexConsumer originalConsumer, float originalRed, float originalGreen, float originalBlue, List<BakedQuad> quads, int originalLight, int originalOverlay
    ){
        if(BlockModelRendererFlags.isColorOverriden()){
            consumer.quad(entry, quad, MathHelper.clamp(originalRed, 0, 1), MathHelper.clamp(originalGreen, 0, 1), MathHelper.clamp(originalBlue, 0, 1), light, overlay);
        }else{
            consumer.quad(entry, quad, red, green, blue, light, overlay);
        }
    }
}
