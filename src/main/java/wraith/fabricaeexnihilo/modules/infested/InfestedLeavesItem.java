package wraith.fabricaeexnihilo.modules.infested;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import wraith.fabricaeexnihilo.modules.base.Colored;
import wraith.fabricaeexnihilo.util.Color;

public class InfestedLeavesItem extends BlockItem implements Colored {

    private final InfestedLeavesBlock leafBlock;

    public InfestedLeavesItem(InfestedLeavesBlock block, FabricItemSettings settings) {
        super(block, settings);
        this.leafBlock = block;
    }

    @Override
    public int getColor(int index) {
        return Color.WHITE.toInt();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public MutableText getName() {
        return leafBlock.getName();
    }

    @Override
    public Text getName(ItemStack stack) {
        return this.getName();
    }

}
