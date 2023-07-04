package wraith.fabricaeexnihilo.mixins;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterBlock;

@Mixin(FluidBlock.class)
public class FluidBlockMixin {
    @Inject(method = "receiveNeighborFluids", at = @At("HEAD"), cancellable = true)
    public void receiveNeighborFluids(World world, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        //noinspection ConstantValue
        if ((FluidBlock) (Object) this instanceof WitchWaterBlock) {
            cir.setReturnValue(WitchWaterBlock.receiveNeighborFluids(world, pos));
            cir.cancel();
        }
    }
}
