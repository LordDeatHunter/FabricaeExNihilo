package exnihilofabrico.util

import net.minecraft.util.BooleanBiFunction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes

object VoxelShapeHelper {
    fun union(vararg shapes: VoxelShape): VoxelShape {
        var output = VoxelShapes.empty()
        for(shape in shapes){
            output = VoxelShapes.combine(output, shape, BooleanBiFunction.OR)
        }
        return output.simplify()
    }

    fun intersection(vararg shapes: VoxelShape): VoxelShape {
        var output = VoxelShapes.fullCube()
        for(shape in shapes){
            output = VoxelShapes.combine(output, shape, BooleanBiFunction.AND)
        }
        return output.simplify()
    }
}