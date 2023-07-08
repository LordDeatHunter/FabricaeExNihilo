package wraith.fabricaeexnihilo.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterFluid;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow protected abstract void swimUpward(TagKey<Fluid> fluid);

    @Shadow private int jumpingCooldown;

    @Inject(method = "tickMovement",
            at = @At(value = "INVOKE:LAST", target = "Lnet/minecraft/entity/LivingEntity;isOnGround()Z"),
            slice = @Slice(to = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;jump()V")))
    private void fabricaeexnihilo$fixSwimming(CallbackInfo ci) {
        if (!this.firstUpdate && getFluidHeight(WitchWaterFluid.TAG) > 0.0 && (!this.isOnGround() || getFluidHeight(WitchWaterFluid.TAG) > getSwimHeight())) {
            swimUpward(WitchWaterFluid.TAG);
            jumpingCooldown = 1; // Prevent default jump, resets next tick anyway
        }
    }
}
