package exnihilocreatio.client.color;

import exnihilocreatio.util.ItemInfo;
import mezz.jei.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 *
 * This is a clone of the JEI color thief magic to prevent a hard dependency on the mod
 *
 * Check the original source for more changes:
 * https://github.com/mezz/JustEnoughItems/blob/a339af3fceb142be5398ef856f81e3c89413349e/src/main/java/mezz/jei/color/ColorGetter.java
 *
 * @author https://github.com/mezz
 */
public final class ColorGetter {

    private ColorGetter() {

    }

    public static final HashMap<ItemInfo, exnihilocreatio.texturing.Color> colorCache = new HashMap<>();

    public static exnihilocreatio.texturing.Color getColor(ItemStack stack) {
        if (stack.isEmpty()) return exnihilocreatio.texturing.Color.INVALID_COLOR;
        ItemInfo info = new ItemInfo(stack);

        if (colorCache.containsKey(info)) {
            return colorCache.get(info);
        } else {
            List<java.awt.Color> color = ColorGetter.getColors(stack, 1);
            if (color.size() > 0) {
                java.awt.Color domColor = color.get(0);
                exnihilocreatio.texturing.Color exColor = new exnihilocreatio.texturing.Color(domColor.getRGB());
                colorCache.put(info, exColor);

                return exColor;
            }
        }

        return exnihilocreatio.texturing.Color.INVALID_COLOR;
    }

    public static List<Color> getColors(ItemStack itemStack, int colorCount) {
        try {
            return unsafeGetColors(itemStack, colorCount);
        } catch (RuntimeException | LinkageError ignored) {
            return Collections.emptyList();
        }
    }

    private static List<Color> unsafeGetColors(ItemStack itemStack, int colorCount) {
        final Item item = itemStack.getItem();
        if (itemStack.isEmpty()) {
            return Collections.emptyList();
        } else if (item instanceof ItemBlock) {
            final ItemBlock itemBlock = (ItemBlock) item;
            final Block block = itemBlock.getBlock();
            //noinspection ConstantConditions
            if (block == null) {
                return Collections.emptyList();
            }
            return getBlockColors(itemStack, block, colorCount);
        } else {
            return getItemColors(itemStack, colorCount);
        }
    }

    private static List<Color> getItemColors(ItemStack itemStack, int colorCount) {
        final ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
        final int renderColor = itemColors.colorMultiplier(itemStack, 0);
        final TextureAtlasSprite textureAtlasSprite = getTextureAtlasSprite(itemStack);
        if (textureAtlasSprite == null) {
            return Collections.emptyList();
        }
        return getColors(textureAtlasSprite, renderColor, colorCount);
    }

    private static List<Color> getBlockColors(ItemStack itemStack, Block block, int colorCount) {
        final int meta = itemStack.getMetadata();
        IBlockState blockState;
        try {
            blockState = block.getStateFromMeta(meta);
        } catch (RuntimeException | LinkageError ignored) {
            blockState = block.getDefaultState();
        }

        final BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
        final int renderColor = blockColors.colorMultiplier(blockState, null, null, 0);
        final TextureAtlasSprite textureAtlasSprite = getTextureAtlasSprite(blockState);
        if (textureAtlasSprite == null) {
            return Collections.emptyList();
        }
        return getColors(textureAtlasSprite, renderColor, colorCount);
    }

    public static List<Color> getColors(TextureAtlasSprite textureAtlasSprite, int renderColor, int colorCount) {
        final BufferedImage bufferedImage = getBufferedImage(textureAtlasSprite);
        if (bufferedImage == null) {
            return Collections.emptyList();
        }
        final List<Color> colors = new ArrayList<>(colorCount);
        final int[][] palette = ColorThief.getPalette(bufferedImage, colorCount);
        if (palette != null) {
            for (int[] colorInt : palette) {
                int red = (int) ((colorInt[0] - 1) * (float) (renderColor >> 16 & 255) / 255.0F);
                int green = (int) ((colorInt[1] - 1) * (float) (renderColor >> 8 & 255) / 255.0F);
                int blue = (int) ((colorInt[2] - 1) * (float) (renderColor & 255) / 255.0F);
                red = MathUtil.clamp(red, 0, 255);
                green = MathUtil.clamp(green, 0, 255);
                blue = MathUtil.clamp(blue, 0, 255);
                Color color = new Color(red, green, blue);
                colors.add(color);
            }
        }
        return colors;
    }

    @Nullable
    private static BufferedImage getBufferedImage(TextureAtlasSprite textureAtlasSprite) {
        final int iconWidth = textureAtlasSprite.getIconWidth();
        final int iconHeight = textureAtlasSprite.getIconHeight();
        final int frameCount = textureAtlasSprite.getFrameCount();
        if (iconWidth <= 0 || iconHeight <= 0 || frameCount <= 0) {
            return null;
        }

        BufferedImage bufferedImage = new BufferedImage(iconWidth, iconHeight * frameCount, BufferedImage.TYPE_4BYTE_ABGR);
        for (int i = 0; i < frameCount; i++) {
            int[][] frameTextureData = textureAtlasSprite.getFrameTextureData(i);
            int[] largestMipMapTextureData = frameTextureData[0];
            bufferedImage.setRGB(0, i * iconHeight, iconWidth, iconHeight, largestMipMapTextureData, 0, iconWidth);
        }

        return bufferedImage;
    }

    @Nullable
    private static TextureAtlasSprite getTextureAtlasSprite(IBlockState blockState) {
        Minecraft minecraft = Minecraft.getMinecraft();
        BlockRendererDispatcher blockRendererDispatcher = minecraft.getBlockRendererDispatcher();
        BlockModelShapes blockModelShapes = blockRendererDispatcher.getBlockModelShapes();
        TextureAtlasSprite textureAtlasSprite = blockModelShapes.getTexture(blockState);
        if (textureAtlasSprite == minecraft.getTextureMapBlocks().getMissingSprite()) {
            return null;
        }
        return textureAtlasSprite;
    }

    @Nullable
    private static TextureAtlasSprite getTextureAtlasSprite(ItemStack itemStack) {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        ItemModelMesher itemModelMesher = renderItem.getItemModelMesher();
        IBakedModel itemModel = itemModelMesher.getItemModel(itemStack);
        return itemModel.getParticleTexture();
    }
}