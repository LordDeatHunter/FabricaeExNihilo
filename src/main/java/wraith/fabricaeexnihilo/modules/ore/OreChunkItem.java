package wraith.fabricaeexnihilo.modules.ore;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import wraith.fabricaeexnihilo.modules.base.HasColor;
import wraith.fabricaeexnihilo.util.Color;

public class OreChunkItem extends Item implements HasColor {

    private final OreProperties properties;

    public OreChunkItem(OreProperties properties, Settings settings) {
        super(settings);
        this.properties = properties;
    }

    @Override
    public int getColor(int index) {
        return index == 1 ? properties.getColor().toInt() : Color.WHITE.toInt();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Text getName() {
        return new TranslatableText("item.fabricaeexnihilo.chunk", new TranslatableText(properties.getDisplayName()));
    }

    @Override
    public Text getName(ItemStack stack) {
        return new TranslatableText("item.fabricaeexnihilo.chunk", new TranslatableText(properties.getDisplayName()));
    }

}
