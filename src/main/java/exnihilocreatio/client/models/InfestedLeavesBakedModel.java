package exnihilocreatio.client.models;

import exnihilocreatio.blocks.BlockInfestingLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;

import static exnihilocreatio.ModBlocks.infestedLeaves;


public class InfestedLeavesBakedModel implements IBakedModel {

    public static final ModelResourceLocation variantTag = new ModelResourceLocation(infestedLeaves.getRegistryName(), "normal");
    private final IBakedModel defaultModel;
    private TextureAtlasSprite particleTexture;

    public InfestedLeavesBakedModel(IBakedModel defaultModel) {
        this.defaultModel = defaultModel;
    }

    private IBakedModel handleBlockState(IBlockState state) {
        if (state instanceof IExtendedBlockState) {
            IBlockState copiedState = BlockInfestingLeaves.getLeafState(state);

            this.particleTexture = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(copiedState).getParticleTexture();
            return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(copiedState);
        }
        return defaultModel;
    }

    @Override
    @Nonnull
    public TextureAtlasSprite getParticleTexture() {
        return this.particleTexture;
    }


    @Override
    @Nonnull
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return handleBlockState(state).getQuads(state, side, rand);
    }

    @Override
    @Nonnull
    public ItemCameraTransforms getItemCameraTransforms() {
        return defaultModel.getItemCameraTransforms();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return defaultModel.isBuiltInRenderer();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return defaultModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return defaultModel.isGui3d();
    }

    @Override
    @Nonnull
    public ItemOverrideList getOverrides() {
        return defaultModel.getOverrides();
    }

    @Override
    @Nonnull
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(@Nonnull ItemCameraTransforms.TransformType cameraTransformType) {
        return Pair.of(this, defaultModel.handlePerspective(cameraTransformType).getRight());
    }
}
