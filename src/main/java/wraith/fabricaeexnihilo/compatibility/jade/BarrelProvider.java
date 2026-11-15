package wraith.fabricaeexnihilo.compatibility.jade;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.impl.ui.ItemStackElement;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class BarrelProvider implements IBlockComponentProvider {
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!(blockAccessor.getBlockEntity() instanceof BarrelBlockEntity barrel)) return;

        switch (barrel.getState()) {
            case EMPTY -> {
                if (barrel.isCrafting()) {
                    tooltip.add(Text.translatable("fabricaeexnihilo.hud.barrel.alchemy.processing", progress(barrel)));
                }
            }
            case FLUID -> {
                var fluid = barrel.getFluid();
                tooltip.add(Text.translatable("fabricaeexnihilo.hud.fluid_content",
                        FluidVariantAttributes.getName(fluid),
                        barrel.getFluidAmount() / 81,
                        barrel.fluidStorage.getCapacity() / 81));
                if (barrel.isCrafting()) {
                    tooltip.add(Text.translatable("fabricaeexnihilo.hud.barrel.alchemy.processing", progress(barrel)));
                }
            }
            case ITEM -> {
                var stack = barrel.getItem();
                if (!stack.isEmpty()) {
                    tooltip.add(ItemStackElement.of(stack));
                    tooltip.append(stack.getName());
                }
            }
            case COMPOST -> {
                var result = barrel.getItem();
                if (!result.isEmpty()) {
                    tooltip.add(ItemStackElement.of(result));
                    tooltip.append(result.getName());
                }
                if (barrel.getCompostLevel() < 1) {
                    tooltip.add(Text.translatable("fabricaeexnihilo.hud.barrel.compost.filling", (int) (barrel.getCompostLevel() * 100)));
                } else {
                    tooltip.add(Text.translatable("fabricaeexnihilo.hud.barrel.compost.composting", progress(barrel)));
                }
            }
        }
    }

    private static int progress(BarrelBlockEntity barrel) {
        return (int) (barrel.getRecipeProgress() * 100);
    }

    @Override
    public Identifier getUid() {
        return id("barrel");
    }
}
