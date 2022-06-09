package wraith.fabricaeexnihilo.compatibility.jade;

import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import snownee.jade.Jade;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IProgressStyle;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.barrels.modes.*;

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
            tooltip.add(new TranslatableText("fabricaeexnihilo.hud.fluid_content", name, fluidMode.getFluidAmount()/81, fluidMode.getFluidCapacity()/81));
        } else if (mode instanceof CompostMode compostMode) {
            if (compostMode.getAmount() < 1) {
                tooltip.add(new TranslatableText("fabricaeexnihilo.hud.barrel.compost.filling", (int) (compostMode.getAmount() * 100)));
            } else {
                tooltip.add(new TranslatableText("fabricaeexnihilo.hud.barrel.compost.composting", (int) (compostMode.getProgress() * 100)));
            }
        } else if (mode instanceof AlchemyMode alchemyMode) {
            tooltip.add(new TranslatableText("fabricaeexnihilo.hud.hud.alchemy.processing", (int) (100.0 / alchemyMode.getDuration() * alchemyMode.getProgress())));
        }
    }
    
    @Override
    public Identifier getUid() {
        return id("barrel");
    }
}
