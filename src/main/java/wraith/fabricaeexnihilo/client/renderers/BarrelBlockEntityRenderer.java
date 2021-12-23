package wraith.fabricaeexnihilo.client.renderers;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.render.FluidRenderFace;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.client.BlockModelRendererFlags;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.barrels.modes.AlchemyMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.CompostMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.FluidMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.ItemMode;
import wraith.fabricaeexnihilo.util.Color;

import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class BarrelBlockEntityRenderer implements BlockEntityRenderer<BarrelBlockEntity> {

    private final float xzScale = 12.0F / 16.0F;
    private final float xMin = 2.0F / 16.0F;
    private final float xMax = 14.0F / 16.0F;
    private final float zMin = 2.0F / 16.0F;
    private final float zMax = 14.0F / 16.0F;
    private final float yMin = 0.1875F;
    private final float yMax = 0.9375F;

    public BarrelBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(@Nullable BarrelBlockEntity barrel, float tickDelta, @Nullable MatrixStack matrices, @Nullable VertexConsumerProvider vertexConsumers, int light, int overlays) {
        if (matrices == null || vertexConsumers == null || barrel == null || barrel.getMode() == null) {
            return;
        }
        var mode = barrel.getMode();

        if (mode instanceof FluidMode fluidMode) {
            renderFluid(fluidMode, barrel.getPos(), tickDelta, matrices, vertexConsumers, light, overlays);
        } else if (mode instanceof ItemMode itemMode) {
            renderItemMode(itemMode, barrel.getPos(), tickDelta, matrices, vertexConsumers, light, overlays);
        } else if (mode instanceof AlchemyMode alchemyMode) {
            renderAlchemy(alchemyMode, barrel.getPos(), tickDelta, matrices, vertexConsumers, light, overlays);
        } else if (mode instanceof CompostMode compostMode) {
            renderCompost(compostMode, barrel.getPos(), tickDelta, matrices, vertexConsumers, light, overlays);
        }

    }

    public void renderFluid(FluidMode mode, BlockPos pos, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlays) {
        renderFluidMode(mode, vertexConsumers, matrices);
    }

    public void renderItemMode(ItemMode mode, BlockPos pos, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlays) {
        renderItem(mode.getStack(), pos, tickDelta, matrices, vertexConsumers, light, overlays, 1.0f);
    }

    public void renderAlchemy(AlchemyMode mode, BlockPos pos, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlays) {
        renderAlchemyMode(mode, pos.getX(), pos.getY(), pos.getZ(), vertexConsumers, matrices);
    }

    public void renderCompost(CompostMode mode, BlockPos pos, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlays) {
        var color = Color.average(Color.WHITE, mode.getColor(), Math.pow(mode.getProgress(), 4));
        var r = color.r;
        var g = color.g;
        var b = color.b;
        var a = color.a;

        matrices.push();

        var amount = mode.getAmount();
        var yScale = MathHelper.clamp((float) ((yMax - yMin) * Math.min(amount, 1.0f)), 0, 1);

        var result = mode.getResult();
        if (result.getItem() instanceof BlockItem blockItem) {
            var block = blockItem.getBlock().getDefaultState();
            var model = MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(block);
            var consumer = vertexConsumers.getBuffer(TexturedRenderLayers.getEntityCutout());
            matrices.translate(xMin, yMin, xMin);
            matrices.scale(xzScale, yScale, xzScale);
            BlockModelRendererFlags.setColorOverride(true);
            MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(matrices.peek(), consumer, block, model, r, g, b, light, overlays);
            BlockModelRendererFlags.setColorOverride(false);
        } else {
            matrices.translate(xMin, yMin, xMin);
            matrices.scale(xzScale, yScale, xzScale);
            MinecraftClient.getInstance().getItemRenderer().renderItem(mode.getResult(), ModelTransformation.Mode.NONE, light, overlays, matrices, vertexConsumers, (int) pos.asLong());
        }
        matrices.pop();
    }

    public void renderItem(ItemStack stack, BlockPos pos, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlays, float level) {
        var yScale = (yMax - yMin) * level;

        matrices.push();
        matrices.translate(0.5, yMin + yScale / 2, 0.5);
        matrices.scale(xzScale, yScale, xzScale);
        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.NONE, light, overlays, matrices, vertexConsumers, (int) pos.asLong());
        matrices.pop();
    }

    private void renderAlchemyMode(AlchemyMode mode, double x, double y, double z, VertexConsumerProvider vertexConsumerProvider, MatrixStack matrices) {
        var before = mode.getBefore();
        if (before instanceof FluidMode fluidMode) {
            renderFluidMode(fluidMode, vertexConsumerProvider, matrices);
        } else if (before instanceof ItemMode itemMode) {
            //renderItem(itemMode.getStack(), 1.0, x, y, z);
        } else if (before instanceof AlchemyMode alchemyMode) {
            renderAlchemyMode(alchemyMode, x, y, z, vertexConsumerProvider, matrices);
        } else if (before instanceof CompostMode compostMode) {
            renderCompostMode(compostMode, x, y, z, matrices);
        }

        var after = mode.getAfter();
        if (after instanceof FluidMode fluidMode) {
            renderFluidMode(fluidMode, vertexConsumerProvider, matrices);
        } else if (before instanceof ItemMode itemMode) {
            //renderItem(itemMode.getStack(), 1.0, x, y, z);
        } else if (before instanceof AlchemyMode alchemyMode) {
            renderAlchemyMode(alchemyMode, x, y, z, vertexConsumerProvider, matrices);
        } else if (before instanceof CompostMode compostMode) {
            renderCompostMode(compostMode, x, y, z, matrices);
        }

    }

    public void renderCompostMode(CompostMode mode, double x, double y, double z, MatrixStack matrices) {
        var yScale = (float) ((yMax - yMin) * Math.min(mode.getAmount(), 1.0));

        var color = Color.average(Color.WHITE, mode.getColor(), Math.pow(mode.getProgress(), 4));

        matrices.push();
        matrices.translate(0.5, yMin + yScale / 2.0, 0.5);
        matrices.scale(xzScale, yScale, xzScale);
        //RendSy.color4f(color.r, color.g, color.b, color.a)
        //MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.NONE, light, overlays, matrices, vertexConsumers, (int) pos.asLong());
        matrices.pop();
    }

    public void renderFluidMode(FluidMode mode, VertexConsumerProvider vertexConsumerProvider, MatrixStack matrices) {
        //TODO: reimplement rendering with fapi fluids
        //renderFluidVolume(mode.getFluid(), (double) mode.getFluid().amount().as1620() / FluidAmount.BUCKET.as1620(), vertexConsumerProvider, matrices);
    }

    public void renderFluidVolume(FluidVolume volume, double level, VertexConsumerProvider vertexConsumerProvider, MatrixStack matrices) {
        var yRender = (yMax - yMin) * level + yMin;
        volume.render(List.of(FluidRenderFace.createFlatFace(xMin, yMin, zMin, xMax, yRender, zMax, 1.0, Direction.UP)), vertexConsumerProvider, matrices);
    }

}
