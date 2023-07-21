package wraith.fabricaeexnihilo.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.fabricaeexnihilo.modules.ModItems;

@Debug(export = true)
@Mixin(GlassBottleItem.class)
public abstract class BottleUseMixin extends Item {
    public BottleUseMixin(Settings settings) {
        super(settings);
    }

    @Shadow
    abstract protected ItemStack fill(ItemStack emptyBottle, PlayerEntity player, ItemStack filledBottle);

    /*
    We use a mixin for this over the fapi hook because it actually improves compatibility.
    If someone else modifies bottle behaviour we have a higher chance to interact with their changes like this.

    Injection point has to be carefully chosen so that the hit result local is in scope.
    We have to inject before the canPlayerModifyAt check because the jump after it causes a frame change
    where the local goes out of scope as the original code doesn't need it anymore.

    We want to capture the hit result local to avoid raycasting again.
    It also makes this mixin more reliable as the raycast is only done once.
    */
    @Inject(method = "use", at = @At(value = "INVOKE", target="Lnet/minecraft/world/World;canPlayerModifyAt(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;)Z"), cancellable = true)
    private void fabricaeexnihilo$collectSalt(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir, @Local BlockHitResult hitResult) {
        if (!world.canPlayerModifyAt(user, hitResult.getBlockPos())) return;
        if (!world.getBlockState(hitResult.getBlockPos()).isIn(BlockTags.SAND)) return;

        cir.setReturnValue(TypedActionResult.success(this.fill(user.getStackInHand(hand), user, new ItemStack(ModItems.SALT_BOTTLE))));
    }
}
