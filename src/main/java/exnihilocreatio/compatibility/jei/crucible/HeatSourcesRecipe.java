package exnihilocreatio.compatibility.jei.crucible;

import exnihilocreatio.ModBlocks;
import exnihilocreatio.blocks.BlockCrucibleStone;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.RenderTickCounter;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Credit goes to >>>> https://github.com/thraaawn/CompactMachines/blob/1.12.1/src/main/java/org/dave/compactmachines3/jei/MultiblockRecipeWrapper.java
public class HeatSourcesRecipe implements IRecipeWrapper {
    private final List<ItemStack> inputs;
    private final BlockInfo blockInfo;
    private final String heatAmountString;

    public HeatSourcesRecipe(IGuiHelper guiHelper, BlockInfo blockInfo, int heatAmount) {
        this.blockInfo = blockInfo;


        ItemStack item = blockInfo.getItemStack();
        if (item.isEmpty()){
            if (blockInfo.getBlock() instanceof IFluidBlock){

            }


        }




        inputs = new ArrayList<>(Collections.singleton(item));
        heatAmountString = String.valueOf(heatAmount) + "x";
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, inputs);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawString(heatAmountString, 24, 12, Color.gray.getRGB());

        // int reqX = (int) Math.ceil((float)(maxPos.getX() - minPos.getX() +2) / 2.0f);
        // int reqZ = (int) Math.ceil((float)(maxPos.getZ() - minPos.getZ() +2) / 2.0f);


        GlStateManager.pushMatrix();
        GlStateManager.translate(0F, 0F, 216.5F);


        // minecraft.fontRenderer.drawString(recipe.getDimensionsString(), 153-mc.fontRenderer.getStringWidth(recipe.getDimensionsString()), 19 * 5 + 10, 0x444444);

        GlStateManager.popMatrix();

        float angle = RenderTickCounter.renderTicks * 45.0f / 128.0f;

        // When we want to render translucent blocks we might need this
        //double c = MathHelper.cos((float)(Math.PI * (double)angle / 180.0));
        //double s = MathHelper.sin((float)(Math.PI * (double)angle / 180.0));
        //Collections.sort(toRender, (a,b) -> - Double.compare((double)a.getZ() * c - (double)a.getX() * s, (double)b.getX() * c - (double)b.getX() * s));
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();

        // Init GlStateManager
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableFog();
        GlStateManager.disableLighting();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(7425);
        } else {
            GlStateManager.shadeModel(7424);
        }

        GlStateManager.pushMatrix();

        // Center on recipe area
        GlStateManager.translate(50, 30, 20);

        // Shift it a bit down so one can properly see 3d
        GlStateManager.rotate(-25.0f, 1.0f, 0.0f, 0.0f);

        // Rotate per our calculated time
        GlStateManager.rotate(angle, 0.0f, 1.0f, 0.0f);

        // Scale down to gui scale
        GlStateManager.scale(16.0f, -16.0f, 16.0f);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.setTranslation(-.5, 0, -.5);

        GlStateManager.enableCull();

        IBlockState crucible = ModBlocks.crucibleStone.getDefaultState().withProperty(BlockCrucibleStone.FIRED, true);
        IBlockState state = blockInfo.getBlock().getDefaultState();

        BlockPos pos = new BlockPos(0, 0, 0);

        // Aaaand render
        buffer.begin(7, DefaultVertexFormats.BLOCK);
        GlStateManager.disableAlpha();
        this.renderBlock(blockrendererdispatcher, buffer, BlockRenderLayer.SOLID, crucible, new BlockPos(0, 1, 0), minecraft.world);

        this.renderBlock(blockrendererdispatcher, buffer, BlockRenderLayer.SOLID, state, pos, minecraft.world);
        GlStateManager.enableAlpha();
        this.renderBlock(blockrendererdispatcher, buffer, BlockRenderLayer.CUTOUT_MIPPED, state, pos, minecraft.world);
        this.renderBlock(blockrendererdispatcher, buffer, BlockRenderLayer.CUTOUT, state, pos, minecraft.world);
        GlStateManager.shadeModel(7425);
        this.renderBlock(blockrendererdispatcher, buffer, BlockRenderLayer.TRANSLUCENT, state, pos, minecraft.world);
        tessellator.draw();

        buffer.setTranslation(0, 0, 0);
        GlStateManager.popMatrix();
    }

    @SideOnly(Side.CLIENT)
    public void renderBlock(BlockRendererDispatcher blockrendererdispatcher, BufferBuilder buffer, BlockRenderLayer renderLayer, IBlockState blockState, BlockPos pos, IBlockAccess access) {

            if (!blockState.getBlock().canRenderInLayer(blockState, renderLayer)) {
                return;
            }

            ForgeHooksClient.setRenderLayer(renderLayer);

            try {
                blockrendererdispatcher.renderBlock(blockState, pos, access, buffer);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ForgeHooksClient.setRenderLayer(null);
    }
}
