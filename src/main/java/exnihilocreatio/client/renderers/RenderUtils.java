package exnihilocreatio.client.renderers;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class RenderUtils {

    /**
     * @author Blue Sinrize
     * taken from here: https://github.com/BluSunrize/ImmersiveEngineering/blob/master/src/main/java/blusunrize/immersiveengineering/client/ClientUtils.java#L1871
     */
    @SideOnly(Side.CLIENT)
    public static void renderModelTESRFast(List<BakedQuad> quads, BufferBuilder renderer, World world, BlockPos pos) {
        int brightness = world.getCombinedLight(pos, 0);
        int l1 = (brightness >> 0x10) & 0xFFFF;
        int l2 = brightness & 0xFFFF;
        for (BakedQuad quad : quads) {
            int[] vData = quad.getVertexData();
            VertexFormat format = quad.getFormat();
            int size = format.getIntegerSize();
            int uv = format.getUvOffsetById(0) / 4;
            for (int i = 0; i < 4; ++i) {
                renderer
                        .pos(Float.intBitsToFloat(vData[size * i]),
                                Float.intBitsToFloat(vData[size * i + 1]),
                                Float.intBitsToFloat(vData[size * i + 2]))
                        .color(255, 255, 255, 255)
                        .tex(Float.intBitsToFloat(vData[size * i + uv]), Float.intBitsToFloat(vData[size * i + uv + 1]))
                        .lightmap(l1, l2)
                        .endVertex();
            }
        }
    }
}
