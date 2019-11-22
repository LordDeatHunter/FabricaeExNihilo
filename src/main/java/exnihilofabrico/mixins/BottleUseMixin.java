package exnihilofabrico.mixins;

import exnihilofabrico.impl.BottleHarvestingImpl;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GlassBottleItem.class)
public abstract class BottleUseMixin extends Item {

    public BottleUseMixin(Settings settings) { super(settings); }

    @Shadow
    native protected ItemStack fill(ItemStack emptyBottle, PlayerEntity player, ItemStack filledBottle);

    @Inject(at=@At("RETURN"), method="use", cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack held = cir.getReturnValue().getValue();
        if(held.getItem() instanceof GlassBottleItem) {
            HitResult hitResult = rayTrace(world, user, RayTraceContext.FluidHandling.SOURCE_ONLY);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = ((BlockHitResult)hitResult).getBlockPos();
                if (world.canPlayerModifyAt(user, blockPos)) {
                    BlockState target = world.getBlockState(blockPos);
                    ItemStack result = BottleHarvestingImpl.INSTANCE.getResult(target);
                    if(!result.isEmpty()) {
                        cir.setReturnValue(new TypedActionResult<>(ActionResult.SUCCESS, this.fill(held, user, result)));
                        return;
                    }
                }
            }
        }
        cir.cancel();
    }
}
