package exnihiloadscensio.items;

import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.util.Data;
import exnihiloadscensio.util.IHasModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCookedSilkworm extends ItemFood implements IHasModel {

    public ItemCookedSilkworm() {
        super(2, 0.6f, false);
        this.setUnlocalizedName("item_cooked_silkworm");
        this.setRegistryName("item_cooked_silkworm");
        Data.ITEMS.add(this);
    }

    @Override
    protected boolean isInCreativeTab(CreativeTabs targetTab) {
        return targetTab == ExNihiloAdscensio.tabExNihilo || targetTab == CreativeTabs.FOOD;
    }
}
