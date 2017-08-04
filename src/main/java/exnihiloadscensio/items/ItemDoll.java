package exnihiloadscensio.items;

import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.ModFluids;
import exnihiloadscensio.util.Data;
import exnihiloadscensio.util.IHasModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class ItemDoll extends Item implements IHasModel {

    public static final String BLAZE = "blaze";
    public static final String ENDERMAN = "enderman";

    private static ArrayList<String> names = new ArrayList<String>();

    public ItemDoll() {
        super();
        setUnlocalizedName("item_doll");
        setRegistryName("item_doll");

        setCreativeTab(ExNihiloAdscensio.tabExNihilo);
        setHasSubtypes(true);

        names.add(BLAZE);
        names.add(ENDERMAN);

        Data.ITEMS.add(this);
    }

    public Fluid getSpawnFluid(ItemStack stack) {
        return stack.getItemDamage() == 0 ? FluidRegistry.LAVA : ModFluids.fluidWitchwater;
    }

    /**
     * Spawns the mob in the world at position
     *
     * @param stack The Doll Stack
     * @param pos   Blockpos
     * @return true if spawn is successful
     */
    public boolean spawnMob(ItemStack stack, World world, BlockPos pos) {
        if (stack.getItemDamage() == 0) {
            EntityBlaze blaze = new EntityBlaze(world);
            blaze.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());

            return world.spawnEntity(blaze);
        } else {
            EntityEnderman enderman = new EntityEnderman(world);
            enderman.setPosition(pos.getX(), pos.getY() + 2, pos.getZ());

            return world.spawnEntity(enderman);
        }
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName() + "." + names.get(stack.getItemDamage());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nullable CreativeTabs tab, NonNullList<ItemStack> list) {
        if (this.isInCreativeTab(tab))
            for (int i = 0; i < names.size(); i++)
                list.add(new ItemStack(this, 1, i));
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        for (int i = 0; i < names.size(); i++) {
            String variant = "type=" + names.get(i);
            ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation("exnihiloadscensio:itemDoll", variant));
        }
    }

}
