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
    @Inject(at = @At("RETURN"), method = "getDroppedStacks", cancellable = true)
    public void getDroppedStacks(BlockState state, LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> info) {
        ItemStack tool = builder.get(LootContextParameters.TOOL);
        if (CrookItem.isCrook(tool) || HammerItem.isHammer(tool)) {
            var recipes = ToolRecipe.find(CrookItem.isCrook(tool) ? ToolRecipe.ToolType.CROOK : ToolRecipe.ToolType.HAMMER, state, builder.getWorld());
            // Non-tool block. Just use default drops
            if (recipes.isEmpty())
                return;
            info.setReturnValue(recipes.stream()
                    .map(ToolRecipe::getResult)
                    .map(loot -> loot.createStack(builder.getWorld().random))
                    .toList());
        }
    }
}
