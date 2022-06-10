package wraith.fabricaeexnihilo.compatibility.jade;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.barrels.modes.AlchemyMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.CompostMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.FluidMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.ItemMode;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class BarrelProvider implements IBlockComponentProvider {
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!(blockAccessor.getBlockEntity() instanceof BarrelBlockEntity barrel)) return;
        var helper = tooltip.getElementHelper();
        var mode = barrel.getMode();
        if (mode instanceof ItemMode itemMode) {
            tooltip.add(helper.item(itemMode.getStack()));
        } else if (mode instanceof FluidMode fluidMode) {
            //TODO: make this look good
            var fluid = fluidMode.getFluid();
            var name = FluidVariantAttributes.getName(fluid);
            tooltip.add(Text.translatable("fabricaeexnihilo.hud.fluid_content", name, fluidMode.getFluidAmount() / 81, fluidMode.getFluidCapacity() / 81));
        } else if (mode instanceof CompostMode compostMode) {
            if (compostMode.getAmount() < 1) {
                tooltip.add(Text.translatable("fabricaeexnihilo.hud.barrel.compost.filling", (int) (compostMode.getAmount() * 100)));
            } else {
                tooltip.add(Text.translatable("fabricaeexnihilo.hud.barrel.compost.composting", (int) (compostMode.getProgress() * 100)));
            }
        } else if (mode instanceof AlchemyMode alchemyMode) {
            tooltip.add(Text.translatable("fabricaeexnihilo.hud.hud.alchemy.processing", (int) (100.0 / alchemyMode.getDuration() * alchemyMode.getProgress())));
        }
    }
    
    @Override
    public Identifier getUid() {
        return id("barrel");
    }
}
