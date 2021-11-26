package wraith.fabricaeexnihilo.util;

import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public final class VoxelShapeHelper {

    private VoxelShapeHelper() {}

    public static VoxelShape union(VoxelShape... shapes) {
        var output = VoxelShapes.empty();
        for(var shape : shapes){
            output = VoxelShapes.combine(output, shape, BooleanBiFunction.OR);
        }
        return output.simplify();
    }

    public static VoxelShape intersection(VoxelShape... shapes) {
        var output = VoxelShapes.fullCube();
        for(var shape : shapes){
            output = VoxelShapes.combine(output, shape, BooleanBiFunction.AND);
        }
        return output.simplify();
    }

}
