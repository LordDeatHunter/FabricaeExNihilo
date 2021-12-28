package wraith.fabricaeexnihilo.mixins;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.fabricaeexnihilo.modules.tools.CrookItem;
import wraith.fabricaeexnihilo.modules.tools.HammerItem;
import wraith.fabricaeexnihilo.recipe.ToolRecipe;

import java.util.List;

@Mixin(AbstractBlock.class)
public abstract class BlockHarvestMixin {

    /**
     * Injects calls to the Hammer and Crook registries if the tool used is identified as a Hammer or Crook
     */
    @Inject(at = @At("RETURN"), method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/loot/context/LootContext$Builder;)Ljava/util/List;", cancellable = true)
    public void getDroppedStacks(BlockState state, LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> info) {
        ItemStack tool = builder.get(LootContextParameters.TOOL);
        if(CrookItem.isCrook(tool)){
            var recipe = ToolRecipe.find(ToolRecipe.ToolType.CROOK, state.getBlock(), builder.getWorld());
            recipe.ifPresent(toolRecipe -> info.setReturnValue(List.of(toolRecipe.getResult().createStack(builder.getWorld().random))));
        } else if (HammerItem.isHammer(tool)) {
            var recipe = ToolRecipe.find(ToolRecipe.ToolType.HAMMER, state.getBlock(), builder.getWorld());
            recipe.ifPresent(toolRecipe -> info.setReturnValue(List.of(toolRecipe.getResult().createStack(builder.getWorld().random))));
        }
    }
}
