package exnihilocreatio.barrel.modes.fluid;

import exnihilocreatio.barrel.modes.mobspawn.BarrelModeMobSpawn;
import exnihilocreatio.items.ItemDoll;
import exnihilocreatio.networking.MessageBarrelModeUpdate;
import exnihilocreatio.networking.PacketHandler;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.types.FluidBlockTransformer;
import exnihilocreatio.tiles.TileBarrel;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.EntityInfo;
import lombok.Setter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class BarrelItemHandlerFluid extends ItemStackHandler {

    @Setter
    private TileBarrel barrel;

    public BarrelItemHandlerFluid(TileBarrel barrel) {
        super(1);
        this.barrel = barrel;
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        FluidTank tank = barrel.getTank();

        if (tank.getFluid() == null)
            return stack;

        // Fluid to block transformations
        if (ExNihiloRegistryManager.FLUID_BLOCK_TRANSFORMER_REGISTRY.canBlockBeTransformedWithThisFluid(tank.getFluid().getFluid(), stack) && tank.getFluidAmount() == tank.getCapacity()) {
            FluidBlockTransformer transformer = ExNihiloRegistryManager.FLUID_BLOCK_TRANSFORMER_REGISTRY.getTransformation(tank.getFluid().getFluid(), stack);

            if (transformer != null) {
                BlockInfo info = transformer.getOutput();
                int spawnCount = transformer.getSpawnCount();
                if (!simulate) {
                    tank.drain(tank.getCapacity(), true);
                    barrel.setMode("block");
                    PacketHandler.sendToAllAround(new MessageBarrelModeUpdate("block", barrel.getPos()), barrel);

                    barrel.getMode().addItem(info.getItemStack(), barrel);
                    if(spawnCount > 0){
                        int spawnRange = transformer.getSpawnRange();
                        EntityInfo entityInfo = transformer.getToSpawn();
                        for(int i=0; i<spawnCount; i++){
                            entityInfo.spawnEntityNear(barrel.getPos(), spawnRange, barrel.getWorld());
                        }
                    }
                }

                ItemStack ret = stack.copy();
                ret.shrink(1);

                return ret.isEmpty() ? ItemStack.EMPTY : ret;
            }

        }

        // Fluid to Fluid transformations
        String fluidItemFluidOutput = ExNihiloRegistryManager.FLUID_ITEM_FLUID_REGISTRY.getFLuidForTransformation(tank.getFluid().getFluid(), stack);
        if (fluidItemFluidOutput != null && tank.getFluidAmount() == tank.getCapacity()) {
            if (!simulate) {
                tank.drain(tank.getCapacity(), true);
                barrel.setMode("fluid");
                PacketHandler.sendToAllAround(new MessageBarrelModeUpdate("block", barrel.getPos()), barrel);
                tank.fill(FluidRegistry.getFluidStack(fluidItemFluidOutput, tank.getCapacity()), true);
                PacketHandler.sendNBTUpdate(barrel);
            }
            ItemStack ret = stack.copy();
            ret.shrink(1);

            return ret.isEmpty() ? ItemStack.EMPTY : ret;
        }


        // Checks for mobspawn
        if (!stack.isEmpty() && tank.getFluidAmount() == tank.getCapacity() && stack.getItem() instanceof ItemDoll
                && ((ItemDoll) stack.getItem()).getSpawnFluid(stack) == tank.getFluid().getFluid()) {
            if (!simulate) {
                barrel.getTank().drain(Fluid.BUCKET_VOLUME, true);
                barrel.setMode("mobspawn");
                ((BarrelModeMobSpawn) barrel.getMode()).setDollStack(stack);
                PacketHandler.sendNBTUpdate(barrel);
            }
            ItemStack ret = stack.copy();
            ret.shrink(1);

            return ret;
        }

        return stack;
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }
}
