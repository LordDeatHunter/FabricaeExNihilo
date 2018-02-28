package exnihilocreatio.items;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.ModFluids;
import exnihilocreatio.util.Data;
import exnihilocreatio.util.IHasModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemDoll extends Item implements IHasModel {

    public static final String BLAZE = "blaze"; // 0
    public static final String ENDERMAN = "enderman"; // 1
    public static final String SHULKER = "shulker"; // 2
    public static final String GUARDIAN = "guardian"; // 3

    private static final ArrayList<String> names = new ArrayList<>();

    public ItemDoll() {
        super();
        setUnlocalizedName("item_doll");
        setRegistryName("item_doll");

        setCreativeTab(ExNihiloCreatio.tabExNihilo);
        setHasSubtypes(true);

        names.add(BLAZE);
        names.add(ENDERMAN);
        names.add(SHULKER);
        names.add(GUARDIAN);

        Data.ITEMS.add(this);
    }

    public Fluid getSpawnFluid(ItemStack stack) {
        switch (stack.getMetadata()) {
            case 0:
                return FluidRegistry.LAVA;
            case 3:
                return FluidRegistry.WATER;
            default:
                return ModFluids.fluidWitchwater;
        }
    }

    /**
     * Spawns the mob in the world at position
     *
     * @param stack The Doll Stack
     * @param pos   Blockpos
     * @return true if spawn is successful
     */
    public boolean spawnMob(ItemStack stack, World world, BlockPos pos) {
        switch (stack.getMetadata()){
            case 0:
                EntityBlaze blaze = new EntityBlaze(world);
                blaze.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());

                return world.spawnEntity(blaze);
            case 1:
                EntityEnderman enderman = new EntityEnderman(world);
                enderman.setPosition(pos.getX(), pos.getY() + 2, pos.getZ());

                return world.spawnEntity(enderman);
            case 2:
                EntityShulker shulker = new EntityShulker(world);
                shulker.setPosition(pos.getX(), pos.getY() + 1.5, pos.getZ());

                return world.spawnEntity(shulker);
            case 3:
                EntityGuardian guardian = new EntityGuardian(world);
                guardian.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());

                return world.spawnEntity(guardian);
            default:
                return false;
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
    @Override
    public void initModel(ModelRegistryEvent event) {

//        for (int i = 0; i < names.size(); i++) {
//            String variant = "type=" + names.get(i);
//            ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation("exnihilocreatio:itemDoll", variant));
//        }

         List<ModelResourceLocation> locations = new ArrayList<>();
        for (String name : names) {
            locations.add(new ModelResourceLocation(getRegistryName(), "type=" + name));
        }

         ModelBakery.registerItemVariants(this, locations.toArray(new ModelResourceLocation[0]));
         ModelLoader.setCustomMeshDefinition(this, stack -> locations.get(stack.getMetadata()));

    }

}
