package exnihilofabrico.mixins;

import exnihilofabrico.api.registry.ExNihiloRegistries;
import exnihilofabrico.modules.tools.CrookTool;
import exnihilofabrico.modules.tools.HammerTool;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Block.class)
public abstract class BlockHarvestMixin {

    /**
     * Injects calls to the Hammer and Crook registries if the tool used is identified as a Hammer or Crook
     */
    @Inject(at = @At("RETURN"), method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/loot/context/LootContext$Builder;)Ljava/util/List;", cancellable = true)
    public void getDroppedStacks(BlockState state, LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> info) {
        ItemStack tool = builder.get(LootContextParameters.TOOL);
        if(CrookTool.isCrook(tool) && ExNihiloRegistries.CROOK.isRegistered(state.getBlock())){
            info.setReturnValue(ExNihiloRegistries.CROOK.getResult(state.getBlock(), builder.getWorld().random));
        }
        else if (HammerTool.isHammer(tool) && ExNihiloRegistries.HAMMER.isRegistered(state.getBlock())) {
            info.setReturnValue(ExNihiloRegistries.HAMMER.getResult(state.getBlock(), builder.getWorld().random));
        }

    }
}
