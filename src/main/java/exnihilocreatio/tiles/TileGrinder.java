package exnihilocreatio.tiles;

import exnihilocreatio.rotationalPower.CapabilityRotationalMember;
import exnihilocreatio.rotationalPower.IRotationalPowerConsumer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;

public class TileGrinder extends BaseTileEntity implements ITickable, IRotationalPowerConsumer {
    public EnumFacing facing = EnumFacing.NORTH;

    public int tickCounter = 0;
    public float rotationValue = 0;
    public float perTickRotation = 0;

    public float storedRotationalPower = 0;

    public ItemHandlerGrinder itemHandlerGrinder;

    public TileGrinder() {
        itemHandlerGrinder = new ItemHandlerGrinder();
        itemHandlerGrinder.setTe(this);
    }

    @Override
    public void update() {
        tickCounter++;

        if (tickCounter > 0 && tickCounter % 10 == 0) {
            perTickRotation = calcEffectivePerTickRotation(world,pos,facing);
            tickCounter = 0;
        }

        if (world.isRemote) {
            rotationValue += perTickRotation;
        }
    }
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemHandlerGrinder);
        if (capability == CapabilityRotationalMember.ROTIONAL_MEMBER)
            return CapabilityRotationalMember.ROTIONAL_MEMBER.cast(this);
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
                (capability == CapabilityRotationalMember.ROTIONAL_MEMBER && facing == this.facing) ||
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

}
