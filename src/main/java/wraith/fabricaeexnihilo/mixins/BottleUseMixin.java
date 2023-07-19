package wraith.fabricaeexnihilo.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.fabricaeexnihilo.modules.ModItems;
import wraith.fabricaeexnihilo.util.ItemUtils;

@Mixin(GlassBottleItem.class)
public abstract class BottleUseMixin extends Item {
    public BottleUseMixin(Settings settings) {
        super(settings);
    }

    @Shadow
    abstract protected ItemStack fill(ItemStack emptyBottle, PlayerEntity player, ItemStack filledBottle);

    @ModifyReturnValue(method = "use", at = @At("TAIL"))
    private TypedActionResult<ItemStack> fabricaeexnihilo$collectSalt(TypedActionResult<ItemStack> original, World world, PlayerEntity user, Hand hand, @Local BlockHitResult hitResult) {
        if (hitResult.getType() != HitResult.Type.BLOCK) return original;
        if (world.getBlockState(hitResult.getBlockPos()).isIn(BlockTags.SAND)) return original;
        return TypedActionResult.success(this.fill(original.getValue(), user, new ItemStack(ModItems.SALT_BOTTLE)));
    }
}
