package exnihilocreatio.util;

import exnihilocreatio.blocks.BlockInfestingLeaves;
import exnihilocreatio.texturing.Color;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.round;

public class Util {

    public static final Color whiteColor = new Color(1f, 1f, 1f, 1f);
    public static final Color blackColor = new Color(0f, 0f, 0f, 1f);
    public static final Color greenColor = new Color(0f, 1f, 0f, 1f);

    public static void dropItemInWorld(TileEntity source, EntityPlayer player, ItemStack stack, double speedFactor) {
        if (stack == null || stack.isEmpty())
            return;

        int hitOrientation = player == null ? 0 : MathHelper.floor(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        double stackCoordX = 0.0D, stackCoordY = 0.0D, stackCoordZ = 0.0D;

        switch (hitOrientation) {
            case 0:
                stackCoordX = source.getPos().getX() + 0.5D;
                stackCoordY = source.getPos().getY() + 0.5D + 1;
                stackCoordZ = source.getPos().getZ() - 0.25D;
                break;
            case 1:
                stackCoordX = source.getPos().getX() + 1.25D;
                stackCoordY = source.getPos().getY() + 0.5D + 1;
                stackCoordZ = source.getPos().getZ() + 0.5D;
                break;
            case 2:
                stackCoordX = source.getPos().getX() + 0.5D;
                stackCoordY = source.getPos().getY() + 0.5D + 1;
                stackCoordZ = source.getPos().getZ() + 1.25D;
                break;
            case 3:
                stackCoordX = source.getPos().getX() - 0.25D;
                stackCoordY = source.getPos().getY() + 0.5D + 1;
                stackCoordZ = source.getPos().getZ() + 0.5D;
                break;
            default:
                break;
        }

        EntityItem droppedEntity = new EntityItem(source.getWorld(), stackCoordX, stackCoordY, stackCoordZ, stack);

        if (player != null) {
            Vec3d motion = new Vec3d(player.posX - stackCoordX, player.posY - stackCoordY, player.posZ - stackCoordZ);
            motion.normalize();
            droppedEntity.motionX = motion.x;
            droppedEntity.motionY = motion.y;
            droppedEntity.motionZ = motion.z;
            double offset = 0.25D;
            droppedEntity.move(MoverType.SELF, motion.x * offset, motion.y * offset, motion.z * offset);
        }

        droppedEntity.motionX *= speedFactor;
        droppedEntity.motionY *= speedFactor;
        droppedEntity.motionZ *= speedFactor;

        source.getWorld().spawnEntity(droppedEntity);
    }

    @SideOnly(Side.CLIENT)
    public static TextureAtlasSprite getTextureFromBlockState(IBlockState state) {
        if (state == null)
            return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();

        return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
    }

    @SideOnly(Side.CLIENT)
    public static TextureAtlasSprite getTextureFromFluidStack(FluidStack stack) {
        if (stack != null && stack.getFluid() != null) {
            Fluid fluid = stack.getFluid();

            if (fluid.getStill(stack) != null) {
                return Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.getStill().toString());
            }
        }

        return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
    }

    public static boolean isSurroundingBlocksAtLeastOneOf(BlockInfo[] blocks, BlockPos pos, World world, int radius) {
        ArrayList<BlockInfo> blockList = new ArrayList<>(Arrays.asList(blocks));
        for (int xShift = -1 * radius; xShift <= radius; xShift++) {
            for (int zShift = -1 * radius; zShift <= radius; zShift++) {
                BlockPos checkPos = pos.add(xShift, 0, zShift);
                BlockInfo checkBlock = new BlockInfo(world.getBlockState(checkPos));
                if (blockList.contains(checkBlock))
                    return true;
            }
        }

        return false;
    }

    public static int getNumSurroundingBlocksAtLeastOneOf(BlockInfo[] blocks, BlockPos pos, World world) {

        int ret = 0;
        ArrayList<BlockInfo> blockList = new ArrayList<>(Arrays.asList(blocks));
        for (int xShift = -2; xShift <= 2; xShift++) {
            for (int zShift = -2; zShift <= 2; zShift++) {
                BlockPos checkPos = pos.add(xShift, 0, zShift);
                BlockInfo checkBlock = new BlockInfo(world.getBlockState(checkPos));
                if (blockList.contains(checkBlock))
                    ret++;
            }
        }


        return ret;
    }

    public static int getLightValue(FluidStack fluid) {
        if (fluid != null && fluid.getFluid() != null) {
            return fluid.getFluid().getLuminosity(fluid);
        } else {
            return 0;
        }
    }

    public static float weightedAverage(float a, float b, float percent) {
        return a * percent + b * (1 - percent);
    }

    public static ItemStack getBucketStack(Fluid fluid) {
        return FluidUtil.getFilledBucket(new FluidStack(fluid, 1000));
    }

    public static boolean compareItemStack(ItemStack stack1, ItemStack stack2){
        if (stack1.getMetadata() == OreDictionary.WILDCARD_VALUE || stack2.getMetadata() == OreDictionary.WILDCARD_VALUE){
            return stack1.getItem() == stack2.getItem();
        }
        else return stack1.getItem() == stack2.getItem() && stack1.getMetadata() == stack2.getMetadata();
    }

    public static int interpolate(int low, int high, float amount){
        if(amount > 1.0f) return high;
        if(amount < 0.0f) return low;
        return low + round((high-low)*amount);
    }

    public static NonNullList<BlockPos> getNearbyLeaves(World world, BlockPos pos){
        NonNullList<BlockPos> blockPos = NonNullList.create();
        for (BlockPos checkPos : BlockPos.getAllInBox(new BlockPos(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1), new BlockPos(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1))) {
            IBlockState newState = world.getBlockState(checkPos);
            if (newState.getBlock() != Blocks.AIR && !(newState.getBlock() instanceof BlockInfestingLeaves)) {
                if (Util.isLeaves(newState))
                    blockPos.add(checkPos);
            }
        }
        //if (!blockStates.isEmpty()) LogUtil.info("Obtained getNearbyLeaves");
        return blockPos;
    }

    public static boolean isLeaves(IBlockState state){
        ItemStack itemStack = new ItemStack(state.getBlock());
        return OreDictionary.getOres("treeLeaves").stream().anyMatch(stack1 -> Util.compareItemStack(stack1, itemStack));
    }

}
