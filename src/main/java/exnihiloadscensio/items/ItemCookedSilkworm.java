package exnihiloadscensio.items;

import exnihiloadscensio.util.Data;
import exnihiloadscensio.util.IHasModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCookedSilkworm extends ItemFood implements IHasModel {

	public ItemCookedSilkworm() {
		super(2, 0.6f, false);
		this.setUnlocalizedName("silkwormCooked");
		this.setRegistryName("silkwormCooked");
        Data.ITEMS.add(this);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("exnihiloadscensio:itemCookedSilkworm", "type=silkwormcooked"));
	}

}
