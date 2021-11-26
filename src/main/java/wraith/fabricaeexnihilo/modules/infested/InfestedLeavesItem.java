package wraith.fabricaeexnihilo.modules.infested;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import wraith.fabricaeexnihilo.modules.base.IHasColor;
import wraith.fabricaeexnihilo.util.Color;

public class InfestedLeavesItem extends BlockItem implements IHasColor {

    public InfestedLeavesItem(InfestedLeavesBlock block, FabricItemSettings settings) {
        super(block, settings);
    }

    @Override
    public int getColor(int index) {
        return Color.WHITE.toInt();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public MutableText getName() {
        return new TranslatableText("block.fabricaeexnihilo.infested", getBlock().getName());
    }

    @Override
    public Text getName(ItemStack stack) {
        return this.getName();
    }

}
