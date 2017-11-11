package exnihilocreatio.client.models;

import net.minecraft.util.EnumFacing;

public class ModelVertex {

    public final EnumFacing face;
    public final double x, yMultiplier, z;
    public final double u, v;
    public final double uMultiplier, vMultiplier;
    public ModelVertex(
            final EnumFacing side,
            final double x,
            final double y,
            final double z,
            final double u1,
            final double v1,
            final double u2,
            final double v2 )
    {
        face = side;
        this.x = x;
        yMultiplier = y;
        this.z = z;

        final double texMultiplier = 16;

        u = u1 * texMultiplier;
        v = v1 * texMultiplier;
        uMultiplier = u2 * texMultiplier;
        vMultiplier = v2 * texMultiplier;
    }
}
