package exnihilocreatio.items.seeds;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.config.ModConfig;
import exnihilocreatio.util.Data;
import exnihilocreatio.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemRubberSeed extends Item implements IHasModel {
    private List<IPlantable> plants = new ArrayList<>();
    public ItemRubberSeed() {
        setRegistryName("item_seed_rubber");
        setTranslationKey("item_seed_rubber");

        for(String s : ModConfig.compatibility.rubber_compat.rubberSeed){
            Block block = Block.getBlockFromName(s);
            if(block != null && block instanceof IPlantable)
                plants.add((IPlantable) block);
        }
        if(plants.size() > 0)
            Data.ITEMS.add(this);
    }

    @Override
    public CreativeTabs getCreativeTab() {
        return ExNihiloCreatio.tabExNihilo;
    }

    public void addPlant(IPlantable plant){
        plants.add(plant);
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!facing.equals(EnumFacing.UP))
            return EnumActionResult.PASS;

        ItemStack stack = player.getHeldItem(hand);

        if (player.canPlayerEdit(pos, facing, stack) && player.canPlayerEdit(pos.add(0, 1, 0), facing, stack)) {
            IBlockState soil = world.getBlockState(pos);

            // Filter Plants to just ones that can be planted on this block.
            List<IPlantable> validPlants = plants.stream().filter(p -> soil.getBlock().canSustainPlant(soil, world, pos, EnumFacing.UP, p)).collect(Collectors.toList());
            final IBlockState plant = validPlants.get(world.rand.nextInt(validPlants.size())).getPlant(world, pos);

            if (world.isAirBlock(pos.add(0, 1, 0))) {
                world.setBlockState(pos.add(0, 1, 0), plant);
                if(!player.isCreative())
                    stack.shrink(1);
                return EnumActionResult.SUCCESS;
            }
        }

        return EnumActionResult.PASS;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void initModel(ModelRegistryEvent e) {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("exnihilocreatio:item_seed", "type=rubber"));
    }
}
