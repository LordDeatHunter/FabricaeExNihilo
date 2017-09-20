package exnihilocreatio.tiles;

import exnihilocreatio.registries.registries.CrucibleRegistry;
import exnihilocreatio.registries.types.Meltable;
import exnihilocreatio.texturing.Color;
import exnihilocreatio.texturing.SpriteColor;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import exnihilocreatio.util.Util;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;

public abstract class TileCrucibleBase extends BaseTileEntity implements ITickable {
    public static final int MAX_ITEMS = 4;

    @Getter
    protected FluidTankBase tank;

    @Getter
    protected int solidAmount;

    @Getter
    protected ItemInfo currentItem;
    protected int ticksSinceLast = 0;

    @Getter
    protected CrucibleItemHandler itemHandler;
    @Getter
    protected CrucibleRegistry crucibleRegistry;

    public TileCrucibleBase(CrucibleRegistry crucibleRegistry) {
        tank = new FluidTankBase(4 * Fluid.BUCKET_VOLUME, this);
        tank.setCanFill(false);

        itemHandler = new CrucibleItemHandler(this, crucibleRegistry);
        this.crucibleRegistry = crucibleRegistry;
    }

    @Override
    public abstract void update();

    public abstract int getHeatRate();

    /**
     * Returns array of FLUID color and Item Color
     * ITEMCOLOR is index 0
     * FLUIDCOLOR is index 1
     */
    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    public SpriteColor[] getSpriteAndColor() {
        SpriteColor[] spriteColors = new SpriteColor[2];

        int noItems = itemHandler.getStackInSlot(0).isEmpty() ? 0 : itemHandler.getStackInSlot(0).getCount();
        if (noItems == 0 && currentItem == null && tank.getFluidAmount() == 0) //Empty!
            return spriteColors;

        FluidStack fluid = tank.getFluid();
        if (fluid != null && fluid.amount > 0) //Nothing being melted.
        {
            Color color = new Color(fluid.getFluid().getColor(), false);
            spriteColors[1] = new SpriteColor(Util.getTextureFromFluidStack(fluid), color);
        }

        IBlockState block = null;
        Color color = Util.whiteColor;

        if (currentItem != null) {
            Meltable meltable = crucibleRegistry.getMeltable(currentItem);
            BlockInfo override = meltable.getTextureOverride();

            if (override == null) {
                if (Block.getBlockFromItem(currentItem.getItem()) != Blocks.AIR) {
                    block = Block.getBlockFromItem(currentItem.getItem())
                            .getStateFromMeta(currentItem.getMeta());
                }
            } else {
                block = override.getBlockState();
            }

            if (block != null) {
                color = new Color(Minecraft.getMinecraft().getBlockColors().colorMultiplier(block, world, pos, 0), true);
            }
        }

        spriteColors[0] = new SpriteColor(Util.getTextureFromBlockState(block), color);

        return spriteColors;
    }

    @SideOnly(Side.CLIENT)
    public float getFilledAmount() {
        int itemCount = itemHandler.getStackInSlot(0).isEmpty() ? 0 : itemHandler.getStackInSlot(0).getCount();
        if (itemCount == 0 && currentItem == null && tank.getFluidAmount() == 0) //Empty!
            return 0;

        float fluidProportion = ((float) tank.getFluidAmount()) / tank.getCapacity();
        if (itemCount == 0 && currentItem == null) //Nothing being melted.
            return fluidProportion;

        float solidProportion = ((float) itemCount) / MAX_ITEMS;
        if (currentItem != null) {
            Meltable meltable = crucibleRegistry.getMeltable(currentItem);
            if (meltable != null)
                solidProportion += ((double) solidAmount) / (MAX_ITEMS * meltable.getAmount());
        }

        return solidProportion > fluidProportion ? solidProportion : fluidProportion;
    }

    @SideOnly(Side.CLIENT)
    public float getFluidProportion() {
        return ((float) tank.getFluidAmount()) / tank.getCapacity();
    }

    @SideOnly(Side.CLIENT)
    public float getSolidProportion() {
        int itemCount = itemHandler.getStackInSlot(0).isEmpty() ? 0 : itemHandler.getStackInSlot(0).getCount();
        float solidProportion = ((float) itemCount) / MAX_ITEMS;
        if (currentItem != null) {
            Meltable meltable = crucibleRegistry.getMeltable(currentItem);
            if (meltable != null)
                solidProportion += ((double) solidAmount) / (MAX_ITEMS * meltable.getAmount());
        }
        return solidProportion;
    }


    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ, IFluidHandler handler) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.isEmpty()) {
            return false;
        }

        Boolean result = FluidUtil.interactWithFluidHandler(player, hand, handler);

        if (result) {
            if (!player.isCreative()) {
                stack.shrink(1);
            }

            markDirtyClient();
            return true;
        }

        //Adding a meltable.
        ItemStack addStack = stack.copy();
        addStack.setCount(1);
        ItemStack insertStack = itemHandler.insertItem(0, addStack, true);
        if (!ItemStack.areItemStacksEqual(addStack, insertStack)) {
            itemHandler.insertItem(0, addStack, false);

            if (currentItem == null) currentItem = new ItemInfo(stack);

            if (!player.isCreative()) stack.shrink(1);

            markDirtyClient();
            return true;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            // itemHandler.setTe(this);
            return (T) itemHandler;
        }
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return (T) tank;

        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
                capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ||
                super.hasCapability(capability, facing);
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        if (currentItem != null) {
            NBTTagCompound currentItemTag = currentItem.writeToNBT(new NBTTagCompound());
            tag.setTag("currentItem", currentItemTag);
        }

        tag.setInteger("solidAmount", solidAmount);

        NBTTagCompound itemHandlerTag = itemHandler.serializeNBT();
        tag.setTag("itemHandler", itemHandlerTag);

        NBTTagCompound tankTag = tank.writeToNBT(new NBTTagCompound());
        tag.setTag("tank", tankTag);

        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        if (tag.hasKey("currentItem")) {
            currentItem = ItemInfo.readFromNBT(tag.getCompoundTag("currentItem"));
        } else {
            currentItem = null;
        }

        solidAmount = tag.getInteger("solidAmount");

        if (tag.hasKey("itemHandler")) {
            itemHandler.deserializeNBT((NBTTagCompound) tag.getTag("itemHandler"));
        }

        if (tag.hasKey("tank")) {
            tank.readFromNBT((NBTTagCompound) tag.getTag("tank"));
        }

        super.readFromNBT(tag);
    }
}
