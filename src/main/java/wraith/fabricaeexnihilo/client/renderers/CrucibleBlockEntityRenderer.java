package wraith.fabricaeexnihilo.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import wraith.fabricaeexnihilo.modules.crucibles.CrucibleBlockEntity;

@SuppressWarnings("UnstableApiUsage")
public class CrucibleBlockEntityRenderer implements BlockEntityRenderer<CrucibleBlockEntity> {
    private static final float XZ_SCALE = 12.0F / 16.0F;
    private static final float X_MIN = 2.0F / 16.0F;
    private static final float X_MAX = 14.0F / 16.0F;
    private static final float Z_MIN = 2.0F / 16.0F;
    private static final float Z_MAX = 14.0F / 16.0F;
    private static final float Y_MIN = 5.0F / 16.0F;
    private static final float Y_MAX = 15.0F / 16.0F;
    
    public CrucibleBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }
    
    @Override
    public void render(CrucibleBlockEntity crucible, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (crucible == null) {
            return;
        }
        var fluid = crucible.getFluid();
        var contained = crucible.getContained();
        var queued = crucible.getQueued();
        var stack = crucible.getRenderStack();
        
        if (contained > 0) {
            renderFluidVolume(fluid, contained / (float) crucible.getMaxCapacity(), matrices, vertexConsumers, light, overlay);
        }
        
        if (queued > 0 && !stack.isEmpty()) {
            renderQueued(stack, queued / (float) crucible.getMaxCapacity(), matrices, vertexConsumers, light, overlay, (int) crucible.getPos().asLong());
        }
        
    }
    
    public void renderFluidVolume(FluidVariant fluid, float level, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        var sprite = FluidVariantRendering.getSprite(fluid);
        var color = FluidVariantRendering.getColor(fluid);
        var r = ((color >> 16) & 255) / 256f;
        var g = ((color >> 8) & 255) / 256f;
        var b = (color & 255) / 256f;
        
        if (sprite == null) return;
        
        RenderSystem.enableDepthTest();
        
        // Idea stolen from Modern Industrialisation

        var emitter = RendererAccess.INSTANCE.getRenderer().meshBuilder().getEmitter();
        emitter.square(Direction.UP, X_MIN, Z_MIN, X_MAX, Z_MAX, 1 - MathHelper.lerp(level, Y_MIN, Y_MAX));
        emitter.spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV);
        vertexConsumers.getBuffer(RenderLayer.getTranslucent()).quad(matrices.peek(), emitter.toBakedQuad(0, sprite, false), r, g, b, light, overlay);
    }
    
    public void renderQueued(ItemStack renderStack, float level, MatrixStack matrices, VertexConsumerProvider vertexConsumer, int light, int overlay, int seed) {
        // Some magic. Could probably be simplified
        var yScale = MathHelper.clamp((Y_MAX - Y_MIN) * level, 0, Y_MAX - Y_MIN);
        
        matrices.push();
        matrices.translate(0.5, Y_MIN + yScale / 2, 0.5);
        matrices.scale(XZ_SCALE, yScale, XZ_SCALE);
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
        MinecraftClient.getInstance().getItemRenderer().renderItem(renderStack, ModelTransformation.Mode.NONE, light, overlay, matrices, vertexConsumer, seed);
        matrices.pop();
    }
}