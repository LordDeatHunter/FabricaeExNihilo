package exnihilocreatio.client.models;

import net.minecraft.util.EnumFacing;

public class ModelVertex {

    public final EnumFacing face;
    public final double x, y, z;
    public final double u, v;
    public ModelVertex(
            final EnumFacing side,
            final double x,
            final double y,
            final double z,
            final double u1,
            final double v1)
    {
        face = side;
        this.x = x;
        this.y = y;
        this.z = z;

        final double texMultiplier = 16;

        u = u1 * texMultiplier;
        v = v1 * texMultiplier;
    }
}
