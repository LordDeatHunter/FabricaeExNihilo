package wraith.fabricaeexnihilo.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.client.BlockModelRendererFlags;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.barrels.modes.*;
import wraith.fabricaeexnihilo.util.Color;

@SuppressWarnings("FieldCanBeLocal")
public class BarrelBlockEntityRenderer implements BlockEntityRenderer<BarrelBlockEntity> {
    
    private static final float XZ_SCALE = 12f / 16f;
    private static final float X_MIN = 2f / 16f;
    private static final float X_MAX = 14f / 16f;
    private static final float Z_MIN = 2f / 16f;
    private static final float Z_MAX = 14f / 16f;
    private static final float Y_MIN = 3f / 16f;
    private static final float Y_MAX = 15f / 16f;
    
    public BarrelBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }
    
    @Override
    public void render(@Nullable BarrelBlockEntity barrel, float tickDelta, @Nullable MatrixStack matrices, @Nullable VertexConsumerProvider vertexConsumers, int light, int overlays) {
        if (matrices == null || vertexConsumers == null || barrel == null || barrel.getMode() == null) {
            return;
        }
        
        renderMode(barrel.getMode(), barrel.getPos(), tickDelta, matrices, vertexConsumers, light, overlays);
    }
    
    private void renderMode(BarrelMode mode, BlockPos pos, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlays) {
        if (mode instanceof FluidMode fluidMode) {
            renderFluid(fluidMode, pos, tickDelta, matrices, vertexConsumers, light, overlays);
        } else if (mode instanceof ItemMode itemMode) {
            renderItem(itemMode, pos, tickDelta, matrices, vertexConsumers, light, overlays);
        } else if (mode instanceof AlchemyMode alchemyMode) {
            renderAlchemy(alchemyMode, pos, tickDelta, matrices, vertexConsumers, light, overlays);
        } else if (mode instanceof CompostMode compostMode) {
            renderCompost(compostMode, pos, tickDelta, matrices, vertexConsumers, light, overlays);
        }
    }
    
    @SuppressWarnings("UnstableApiUsage")
    private void renderFluid(FluidMode mode, BlockPos pos, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlays) {
        var sprite = FluidVariantRendering.getSprite(mode.getFluid());
        var color = FluidVariantRendering.getColor(mode.getFluid());
        var r = ((color >> 16) & 255) / 256f;
        var g = ((color >> 8) & 255) / 256f;
        var b = (color & 255) / 256f;
        
        if (sprite == null) return;
        
        RenderSystem.enableDepthTest();
        
        // Idea stolen from Modern Industrialisation
        
        var emitter = RendererAccess.INSTANCE.getRenderer().meshBuilder().getEmitter();
        emitter.square(Direction.UP, X_MIN, Z_MIN, X_MAX, Z_MAX, 1 - Y_MAX);
        emitter.spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV);
        vertexConsumers.getBuffer(RenderLayer.getTranslucent()).quad(matrices.peek(), emitter.toBakedQuad(0, sprite, false), r, g, b, light, overlays);
    }
    
    private void renderItem(ItemMode mode, BlockPos pos, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlays) {
        var yScale = Y_MAX - Y_MIN;
        
        matrices.push();
        matrices.translate(0.5, Y_MIN + yScale / 2, 0.5);
        matrices.scale(XZ_SCALE, yScale, XZ_SCALE);
        MinecraftClient.getInstance().getItemRenderer().renderItem(mode.getStack(), ModelTransformation.Mode.NONE, light, overlays, matrices, vertexConsumers, (int) pos.asLong());
        matrices.pop();
    }
    
    private void renderAlchemy(AlchemyMode mode, BlockPos pos, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlays) {
        renderMode(mode.getBefore(), pos, tickDelta, matrices, vertexConsumers, light, overlays);
        renderMode(mode.getAfter(), pos, tickDelta, matrices, vertexConsumers, light, overlays);
    }
    
    private void renderCompost(CompostMode mode, BlockPos pos, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlays) {
        var color = Color.average(Color.WHITE, mode.getColor(), Math.pow(mode.getProgress(), 4));
        var r = color.r;
        var g = color.g;
        var b = color.b;
        var a = color.a;
        
        matrices.push();
        
        var amount = mode.getAmount();
        var yScale = MathHelper.clamp((float) ((Y_MAX - Y_MIN) * Math.min(amount, 1.0f)), 0, 1);
        
        var result = mode.getResult();
        if (result.getItem() instanceof BlockItem blockItem) {
            var block = blockItem.getBlock().getDefaultState();
            var model = MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(block);
            var consumer = vertexConsumers.getBuffer(TexturedRenderLayers.getEntityCutout());
            matrices.translate(X_MIN, Y_MIN, X_MIN);
            matrices.scale(XZ_SCALE, yScale, XZ_SCALE);
            BlockModelRendererFlags.setColorOverride(true);
            MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(matrices.peek(), consumer, block, model, r, g, b, light, overlays);
            BlockModelRendererFlags.setColorOverride(false);
        } else {
            matrices.translate(X_MIN, Y_MIN, X_MIN);
            matrices.scale(XZ_SCALE, yScale, XZ_SCALE);
            MinecraftClient.getInstance().getItemRenderer().renderItem(mode.getResult(), ModelTransformation.Mode.NONE, light, overlays, matrices, vertexConsumers, (int) pos.asLong());
        }
        matrices.pop();
    }
}
