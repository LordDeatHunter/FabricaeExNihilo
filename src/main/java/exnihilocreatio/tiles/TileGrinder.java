package exnihilocreatio.tiles;

import exnihilocreatio.rotationalPower.IRotationalPowerConsumer;
import exnihilocreatio.rotationalPower.IRotationalPowerMember;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;

public class TileGrinder extends BaseTileEntity implements ITickable, IRotationalPowerConsumer {
    public EnumFacing facing = EnumFacing.NORTH;

    public int tickCounter = 0;
    public float rotationValue = 0;
    public float perTickRotation = 0;

    public float storedRotationalPower = 0;

    public final ItemHandlerGrinder itemHandlerGrinder;

    public TileGrinder() {
        itemHandlerGrinder = new ItemHandlerGrinder();
        itemHandlerGrinder.setTe(this);
    }

    @Override
    public void update() {
        tickCounter++;

        if (tickCounter % 10 == 0) {
            perTickRotation = calcEffectivePerTickRotation(facing);
        }

        if (world.isRemote) {
            rotationValue += perTickRotation;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) itemHandlerGrinder;
        }

        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
                super.hasCapability(capability, facing);
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        // if (currentItem != null) {
        //     NBTTagCompound currentItemTag = currentItem.writeToNBT(new NBTTagCompound());
        //     tag.setTag("currentItem", currentItemTag);
        // } TODO: implement


        NBTTagCompound itemHandlerTag = itemHandlerGrinder.serializeNBT();
        tag.setTag("itemHandlerIn", itemHandlerTag);

        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        // if (tag.hasKey("currentItem")) {
        //     currentItem = ItemInfo.readFromNBT(tag.getCompoundTag("currentItem"));
        // } else {
        //     currentItem = null;
        // } TODO: see above

        if (tag.hasKey("itemHandler")) {
            itemHandlerGrinder.deserializeNBT((NBTTagCompound) tag.getTag("itemHandlerIn"));
        }

        super.readFromNBT(tag);
    }

    @Override
    public float getMachineRotationPerTick() {
        return perTickRotation;
    }

    @Override
    public void setEffectivePerTickRotation(float rotation) {
        perTickRotation = rotation;
    }

    private float calcEffectivePerTickRotation(EnumFacing direction) {
        if (facing == direction) {
            BlockPos posProvider = pos.offset(facing.getOpposite());
            TileEntity te = world.getTileEntity(posProvider);
            if (te != null && te instanceof IRotationalPowerMember) {
                return ((IRotationalPowerMember) te).getEffectivePerTickRotation(facing) + getOwnRotation();
            } else return getOwnRotation();
        } else return 0;
    }
}
