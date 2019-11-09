package exnihilofabrico.mixins;

import exnihilofabrico.impl.BucketFluidAccessor;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BucketItem.class)
public class BucketItemMixin implements BucketFluidAccessor {

    @Shadow @Final private Fluid fluid;

    public Fluid getFluid() {return this.fluid;}
}
