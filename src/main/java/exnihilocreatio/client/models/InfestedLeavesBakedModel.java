package exnihilocreatio.client.models;

import exnihilocreatio.blocks.leaves.BlockInfestingLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;

import static exnihilocreatio.ModBlocks.infestedLeaves;


public class InfestedLeavesBakedModel implements IBakedModel {

    private final IBakedModel defaultModel;
    private TextureAtlasSprite particleTexture;
    public final static ModelResourceLocation variantTag = new ModelResourceLocation(infestedLeaves.getRegistryName(), "normal");

    public InfestedLeavesBakedModel(IBakedModel defaultModel){
        this.defaultModel = defaultModel;
    }

    private IBakedModel handleBlockState(IBlockState state){
        if (state instanceof IExtendedBlockState){
            IBlockState copiedState = BlockInfestingLeaves.getLeafState(state);
            this.particleTexture = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(copiedState).getParticleTexture();
            return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(copiedState);
        }
        return defaultModel;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.particleTexture;
    }


    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return handleBlockState(state).getQuads(state, side, rand);
    }

    @Override
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
    public ItemOverrideList getOverrides() {
        return defaultModel.getOverrides();
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return Pair.of(this, defaultModel.handlePerspective(cameraTransformType).getRight());
    }
}
