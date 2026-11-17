package wraith.fabricaeexnihilo.client.renderers;

import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.barrels.BarrelState;
import wraith.fabricaeexnihilo.util.Color;

public class BarrelBlockEntityRenderer implements BlockEntityRenderer<BarrelBlockEntity, BarrelBlockEntityRenderer.BarrelRenderState> {

    private static final float XZ_SCALE = 12f / 16f;
    private static final float X_MIN = 2f / 16f;
    private static final float X_MAX = 14f / 16f;
    private static final float Z_MIN = 2f / 16f;
    private static final float Z_MAX = 14f / 16f;
    private static final float Y_MIN = 3f / 16f;
    private static final float Y_MAX = 15f / 16f;

    private final ItemModelManager itemModelManager;

    public BarrelBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemModelManager = ctx.itemModelManager();
    }

    @Override
    public BarrelRenderState createRenderState() {
        return new BarrelRenderState();
    }

    @Override
    public void updateRenderState(BarrelBlockEntity barrel, BarrelRenderState state, float tickDelta, Vec3d cameraPos, ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlayCommand) {
        BlockEntityRenderState.updateBlockEntityRenderState(barrel, state, crumblingOverlayCommand);
        state.barrelState = barrel.getState();

        switch (state.barrelState) {
            case EMPTY -> {}
            case FLUID -> {
                FluidVariant fluid = barrel.getFluid();
                long amount = barrel.getFluidAmount();
                state.fluidLevel = amount / (float) FluidConstants.BUCKET;
                state.fluidSprite = FluidVariantRendering.getSprite(fluid);
                if (state.fluidSprite != null) {
                    int color = FluidVariantRendering.getColor(fluid);
                    state.fluidR = ((color >> 16) & 255) / 256f;
                    state.fluidG = ((color >> 8) & 255) / 256f;
                    state.fluidB = (color & 255) / 256f;
                }
            }
            case ITEM -> {
                ItemStack item = barrel.getItem();
                if (!item.isEmpty()) {
                    state.hasItem = true;
                    this.itemModelManager.clearAndUpdate(state.itemState, item, ItemDisplayContext.NONE, barrel.getWorld(), null, (int) barrel.getPos().asLong());
                } else {
                    state.hasItem = false;
                    state.itemState.clear();
                }
            }
            case COMPOST -> {
                ItemStack result = barrel.getItem();
                double progress = barrel.getRecipeProgress();
                double amount = barrel.getCompostLevel();
                state.compostAmount = (float) MathHelper.clamp(((Y_MAX - Y_MIN) * Math.min(amount, 1.0f)), 0, 1);

                var finalColor = Color.average(Color.WHITE, new Color(0x573f17), Math.pow(progress, 4));
                state.compostR = (float) finalColor.r;
                state.compostG = (float) finalColor.g;
                state.compostB = (float) finalColor.b;

                if (result.getItem() instanceof BlockItem blockItem) {
                    state.isBlockCompost = true;
                    BlockState block = blockItem.getBlock().getDefaultState();
                    state.compostBlockModel = MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(block);
                } else {
                    state.isBlockCompost = false;
                    state.compostBlockModel = null;
                    if (!result.isEmpty()) {
                        this.itemModelManager.clearAndUpdate(state.itemState, result, ItemDisplayContext.NONE, barrel.getWorld(), null, (int) barrel.getPos().asLong());
                    } else {
                        state.itemState.clear();
                    }
                }
            }
        }
    }

    @Override
    public void render(BarrelRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        switch (state.barrelState) {
            case EMPTY -> {}
            case FLUID -> {
                if (state.fluidSprite != null) {
                    float y = MathHelper.lerp(state.fluidLevel, Y_MIN, Y_MAX);
                    Sprite sprite = state.fluidSprite;
                    float u0 = sprite.getMinU();
                    float u1 = sprite.getMaxU();
                    float v0 = sprite.getMinV();
                    float v1 = sprite.getMaxV();
                    float r = state.fluidR;
                    float g = state.fluidG;
                    float b = state.fluidB;
                    int light = state.lightmapCoordinates;
                    int overlay = OverlayTexture.DEFAULT_UV;

                    queue.submitCustom(matrices, TexturedRenderLayers.getBlockTranslucentCull(), (entry, consumer) -> {
                        consumer.vertex(entry, X_MIN, y, Z_MIN).color(r, g, b, 1.0f).texture(u0, v0).overlay(overlay).light(light).normal(entry, 0, 1, 0);
                        consumer.vertex(entry, X_MIN, y, Z_MAX).color(r, g, b, 1.0f).texture(u0, v1).overlay(overlay).light(light).normal(entry, 0, 1, 0);
                        consumer.vertex(entry, X_MAX, y, Z_MAX).color(r, g, b, 1.0f).texture(u1, v1).overlay(overlay).light(light).normal(entry, 0, 1, 0);
                        consumer.vertex(entry, X_MAX, y, Z_MIN).color(r, g, b, 1.0f).texture(u1, v0).overlay(overlay).light(light).normal(entry, 0, 1, 0);
                    });
                }
            }
            case ITEM -> {
                if (state.hasItem) {
                    var yScale = Y_MAX - Y_MIN;
                    matrices.push();
                    matrices.translate(0.5, Y_MIN + yScale / 2, 0.5);
                    matrices.scale(XZ_SCALE, yScale, XZ_SCALE);
                    state.itemState.render(matrices, queue, state.lightmapCoordinates, OverlayTexture.DEFAULT_UV, 0);
                    matrices.pop();
                }
            }
            case COMPOST -> {
                matrices.push();
                var yScale = state.compostAmount;
                matrices.translate(X_MIN, Y_MIN, X_MIN);
                matrices.scale(XZ_SCALE, yScale, XZ_SCALE);
                if (state.isBlockCompost && state.compostBlockModel != null) {
                    queue.submitBlockStateModel(matrices, TexturedRenderLayers.getEntityCutout(), state.compostBlockModel, state.compostR, state.compostG, state.compostB, state.lightmapCoordinates, OverlayTexture.DEFAULT_UV, 0);
                } else {
                    state.itemState.render(matrices, queue, state.lightmapCoordinates, OverlayTexture.DEFAULT_UV, 0);
                }
                matrices.pop();
            }
        }
    }

    public static class BarrelRenderState extends BlockEntityRenderState {
        public BarrelState barrelState = BarrelState.EMPTY;

        public float fluidLevel;
        public Sprite fluidSprite;
        public float fluidR, fluidG, fluidB;

        public boolean hasItem;
        public final ItemRenderState itemState = new ItemRenderState();

        public float compostAmount;
        public float compostR, compostG, compostB;
        public boolean isBlockCompost;
        public BlockStateModel compostBlockModel;
    }
}
