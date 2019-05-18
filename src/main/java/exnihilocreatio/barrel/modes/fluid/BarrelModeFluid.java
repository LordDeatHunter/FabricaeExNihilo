package exnihilocreatio.barrel.modes.fluid;

import exnihilocreatio.barrel.BarrelFluidHandler;
import exnihilocreatio.barrel.IBarrelMode;
import exnihilocreatio.barrel.modes.transform.BarrelModeFluidTransform;
import exnihilocreatio.networking.MessageBarrelModeUpdate;
import exnihilocreatio.networking.PacketHandler;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.types.FluidTransformer;
import exnihilocreatio.texturing.Color;
import exnihilocreatio.tiles.TileBarrel;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class BarrelModeFluid implements IBarrelMode {

    private final BarrelItemHandlerFluid handler;

    public BarrelModeFluid() {
        handler = new BarrelItemHandlerFluid(null);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        // TODO Auto-generated method stub

    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isTriggerItemStack(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isTriggerFluidStack(FluidStack stack) {
        return stack != null;
    }

    @Override
    public String getName() {
        return "fluid";
    }

    @Override
    public List<String> getWailaTooltip(TileBarrel barrel, List<String> currenttip) {
        if (barrel.getTank().getFluid() != null) {
            currenttip.add(barrel.getTank().getFluid().getLocalizedName());
            currenttip.add("Amount: " + barrel.getTank().getFluidAmount() + "mb");
        } else {
            currenttip.add("Empty");
        }

        return currenttip;
    }

    @Override
    public void onBlockActivated(World world, TileBarrel barrel, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);

        if (!stack.isEmpty()) {
            ItemStack remainder = getHandler(barrel).insertItem(0, stack, false);

            int size = remainder.isEmpty() ? 0 : remainder.getCount();

            if (stack.getItem().hasContainerItem(stack)) {
                ItemStack container = stack.getItem().getContainerItem(stack);

                // Should always be 1 but LET'S JUST MAKE SURE
                container.setCount(stack.getCount() - size);

                if (!player.inventory.addItemStackToInventory(container)) {
                    player.getEntityWorld().spawnEntity(new EntityItem(player.getEntityWorld(), player.posX, player.posY, player.posZ, container));
                }
            }

            player.setHeldItem(hand, remainder);
        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getTextureForRender(TileBarrel barrel) {
        return Util.getTextureFromFluidStack(barrel.getTank().getFluid());
    }

    @Override
    public Color getColorForRender(TileBarrel barrel) {
        if(barrel.getTank().getFluid() != null)
            return new Color(barrel.getTank().getFluid().getFluid().getColor());
        return Util.whiteColor;
    }

    @Override
    public float getFilledLevelForRender(TileBarrel barrel) {
        double amount = barrel.getTank().getFluidAmount();
        return (float) ((amount / Fluid.BUCKET_VOLUME) * 0.9375F);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void update(TileBarrel barrel) {
        // Fluids on top.
        if (barrel.getTank().getFluid() != null) {
            FluidTank tank = barrel.getTank();
            if (tank.getFluid() == null || tank.getFluid().amount != tank.getCapacity())
                return;

            Fluid fluidInBarrel = tank.getFluid().getFluid();

            BlockPos barrelPos = barrel.getPos();
            BlockPos pos = new BlockPos(barrelPos.getX(), barrelPos.getY() + 1, barrelPos.getZ());
            Block onTop = barrel.getWorld().getBlockState(pos).getBlock();

            Fluid fluidOnTop = null;
            if (onTop instanceof BlockLiquid) {
                fluidOnTop = onTop.getMaterial(barrel.getWorld().getBlockState(pos)) == Material.WATER
                        ? FluidRegistry.WATER : FluidRegistry.LAVA;
            }

            if (!onTop.equals(Blocks.AIR) && onTop instanceof IFluidBlock) {
                fluidOnTop = ((IFluidBlock) onTop).getFluid();
            }

            if (ExNihiloRegistryManager.FLUID_ON_TOP_REGISTRY.isValidRecipe(fluidInBarrel, fluidOnTop)) {
                BlockInfo info = ExNihiloRegistryManager.FLUID_ON_TOP_REGISTRY.getTransformedBlock(fluidInBarrel, fluidOnTop);
                tank.drain(tank.getCapacity(), true);
                barrel.setMode("block");
                PacketHandler.sendToAllAround(new MessageBarrelModeUpdate("block", barrel.getPos()), barrel);

                barrel.getMode().addItem(info.getItemStack(), barrel);

                return;
            }

            // Fluid transforming time!
            if (ExNihiloRegistryManager.FLUID_TRANSFORM_REGISTRY.containsKey(barrel.getTank().getFluid().getFluid().getName())) {
                List<FluidTransformer> transformers = ExNihiloRegistryManager.FLUID_TRANSFORM_REGISTRY
                        .getFluidTransformers(barrel.getTank().getFluid().getFluid().getName());

                boolean found = false;
                for (int radius = 0; radius <= 2; radius++) {
                    for (FluidTransformer transformer : transformers) {
                        if (!ExNihiloRegistryManager.BARREL_LIQUID_BLACKLIST_REGISTRY.isBlacklisted(barrel.getTier(), transformer.getOutputFluid())
                                && (Util.isSurroundingBlocksAtLeastOneOf(transformer.getTransformingBlocks(),
                                barrel.getPos().add(0, -1, 0), barrel.getWorld(), radius)
                                || Util.isSurroundingBlocksAtLeastOneOf(transformer.getTransformingBlocks(),
                                barrel.getPos(), barrel.getWorld(), radius))) {
                            // Time to start the process.
                            FluidStack fstack = tank.getFluid();
                            tank.setFluid(null);

                            barrel.setMode("fluidTransform");
                            BarrelModeFluidTransform mode = (BarrelModeFluidTransform) barrel.getMode();

                            mode.setTransformer(transformer);
                            mode.setInputStack(fstack);
                            mode.setOutputStack(FluidRegistry.getFluidStack(transformer.getOutputFluid(), 1000));

                            PacketHandler.sendNBTUpdate(barrel);
                            found = true;
                        }
                    }
                    if (found) break;
                }
            }
        }
    }

    @Override
    public void addItem(ItemStack stack, TileBarrel barrel) {
    }

    @Override
    public ItemStackHandler getHandler(TileBarrel barrel) {
        handler.setBarrel(barrel);
        return handler;
    }

    @Override
    public FluidTank getFluidHandler(TileBarrel barrel) {
        BarrelFluidHandler handler = barrel.getTank();
        handler.setBarrel(barrel);
        return handler;
    }

    @Override
    public boolean canFillWithFluid(TileBarrel barrel) {
        return true;
    }

}
