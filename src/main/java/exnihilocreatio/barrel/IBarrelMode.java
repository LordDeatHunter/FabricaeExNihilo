package exnihilocreatio.barrel;

import exnihilocreatio.texturing.Color;
import exnihilocreatio.texturing.SpriteColor;
import exnihilocreatio.tiles.TileBarrel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public interface IBarrelMode {

    void writeToNBT(NBTTagCompound tag);

    void readFromNBT(NBTTagCompound tag);

    boolean isTriggerItemStack(ItemStack stack);

    boolean isTriggerFluidStack(FluidStack stack);

    String getName();

    void onBlockActivated(World world, TileBarrel barrel, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ);

    @SideOnly(Side.CLIENT)
    TextureAtlasSprite getTextureForRender(TileBarrel barrel);

    Color getColorForRender();

    default SpriteColor getSpriteColor(TileBarrel barrel){
        return new SpriteColor(getTextureForRender(barrel), getColorForRender());
    }

    float getFilledLevelForRender(TileBarrel barrel);

    void update(TileBarrel barrel);

    void addItem(ItemStack stack, TileBarrel barrel);

    ItemStackHandler getHandler(TileBarrel barrel);

    FluidTank getFluidHandler(TileBarrel barrel);

    boolean canFillWithFluid(TileBarrel barrel);

    List<String> getWailaTooltip(TileBarrel barrel, List<String> currenttip);

}
