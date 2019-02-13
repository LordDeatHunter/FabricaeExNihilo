package exnihilocreatio.modules.oreberries;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.util.Data;
import exnihilocreatio.util.IHasModel;
import josephcsible.oreberries.BlockOreberryBush;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ItemOreBerrySeed extends Item implements IHasModel {
    private BlockOreberryBush bush;
    public ItemOreBerrySeed(BlockOreberryBush bush){
        this.bush = bush;
        setCreativeTab(ExNihiloCreatio.tabExNihilo);
        setRegistryName("oreberry_seed."+bush.config.name.toLowerCase());
        setTranslationKey(ExNihiloCreatio.MODID + ".oreberry_seed."+bush.config.name.toLowerCase());

        Data.ITEMS.add(this);
    }

    public BlockOreberryBush getBush(){
        return bush;
    }

    public Block getBlock(){
        return getBush();
    }

    @Override
    @Nonnull
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        if(I18n.canTranslate(this.getUnlocalizedNameInefficiently(stack) + ".name"))
            return I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name").trim();
        return bush.config.name + " " + I18n.translateToLocal( "item.exnihilocreatio.oreberry_seed.name").trim();
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!facing.equals(EnumFacing.UP))
            return EnumActionResult.PASS;
        ItemStack stack = player.getHeldItem(hand);
        if (player.canPlayerEdit(pos, facing, stack) && player.canPlayerEdit(pos.add(0, 1, 0), facing, stack)) {
            final IBlockState soil = world.getBlockState(pos);

            if (soil.getBlock().canSustainPlant(soil, world, pos, EnumFacing.UP, this.bush) &&
                    world.isAirBlock(pos.add(0, 1, 0)) &&
                    this.bush.getDefaultState() != null) {
                world.setBlockState(pos.add(0, 1, 0), this.bush.getDefaultState());
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
        if(this.bush.config.special != null)
            ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("exnihilocreatio:item_oreberry_seed", "type="+this.bush.config.special));
        else
            ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("exnihilocreatio:item_oreberry_seed", "type=normal"));
    }
}
