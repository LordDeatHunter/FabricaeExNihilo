package exnihilocreatio.items;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.ModItems;
import exnihilocreatio.blocks.BlockInfestingLeaves;
import exnihilocreatio.util.Data;
import exnihilocreatio.util.IHasModel;
import exnihilocreatio.util.Util;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class ItemResource extends Item implements IHasModel {

    public static final String PORCELAIN_CLAY = "porcelain_clay";
    public static final String SILKWORM = "silkworm";
    public static final String ANCIENT_SPORES = "ancient_spores";
    public static final String GRASS_SEEDS = "grass_seeds";
    public static final String DOLL_BASE = "doll";
    public static final String ROD_STONE = "rod_stone";
    public static final String GEAR_STONE = "gear_stone";

    private static final ArrayList<String> names = new ArrayList<>();

    public ItemResource() {
        super();

        setCreativeTab(ExNihiloCreatio.tabExNihilo);
        setTranslationKey("item_material");
        setRegistryName("item_material");
        setHasSubtypes(true);
        Data.ITEMS.add(this);

        names.add(0, "removed");
        names.add(1, PORCELAIN_CLAY);
        names.add(2, SILKWORM);
        names.add(3, ANCIENT_SPORES);
        names.add(4, GRASS_SEEDS);
        names.add(5, DOLL_BASE);
        names.add(6, ROD_STONE);
        names.add(7, GEAR_STONE);
    }

    public static ItemStack getResourceStack(String name) {
        return getResourceStack(name, 1);
    }

    public static ItemStack getResourceStack(String name, int quantity) {
        return new ItemStack(ModItems.resources, quantity, names.indexOf(name));
    }

    public static int getMetaFromName(String name) {
        return names.indexOf(name);
    }

    @Override
    @Nonnull
    public String getTranslationKey(ItemStack stack) {
        return getTranslationKey() + "." + names.get(stack.getItemDamage());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> list) {
        if (this.isInCreativeTab(tab))
            for (int i = 1; i < names.size(); i++) {
                list.add(new ItemStack(this, 1, i));
            }
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItemDamage() == names.indexOf(SILKWORM)) {
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() != Blocks.AIR && !(state.getBlock() instanceof BlockInfestingLeaves))
                if (Util.isLeaves(state)) {
                    BlockInfestingLeaves.infestLeafBlock(world, state, pos);
                    stack.shrink(1);
                    return EnumActionResult.SUCCESS;
                }
        }
        if (stack.getItemDamage() == names.indexOf(ANCIENT_SPORES) || stack.getItemDamage() == names.indexOf(GRASS_SEEDS)) {
            IBlockState state = world.getBlockState(pos);
            if (state != Blocks.AIR.getDefaultState() && state.getBlock() != Blocks.AIR && state.getBlock() == Blocks.DIRT) {
                IBlockState transformTo = stack.getItemDamage() == names.indexOf(ANCIENT_SPORES) ? Blocks.MYCELIUM.getDefaultState() : Blocks.GRASS.getDefaultState();
                world.setBlockState(pos, transformTo);
                stack.shrink(1);

                return EnumActionResult.SUCCESS;
            }
        }

        return EnumActionResult.PASS;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        if(player.world.isRemote)
            return false;

        switch(stack.getMetadata()) {
            case 3: return ancientSporesInteraction(stack, player, target, hand);
            default: return false;
        }
    }

    private boolean ancientSporesInteraction(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        if(target.getClass() == EntityCow.class ) {
            replaceMob(target.world, target, new EntityMooshroom(target.world));
            if(!player.capabilities.isCreativeMode)
                stack.shrink(1);
            return true;
        }
        if(target.getClass() == EntityPig.class) {
            replaceMob(target.world, target, new EntityPigZombie(target.world));
            if(!player.capabilities.isCreativeMode)
                stack.shrink(1);
            return true;
        }
        return false;
    }

    private void replaceMob(World world, EntityLivingBase toKill, EntityLivingBase toSpawn) {
        toSpawn.setLocationAndAngles(toKill.posX, toKill.posY, toKill.posZ, toKill.rotationYaw, toKill.rotationPitch);
        toSpawn.renderYawOffset = toKill.renderYawOffset;
        toSpawn.setHealth(toSpawn.getMaxHealth() * toKill.getHealth() / toKill.getMaxHealth());

        toKill.setDead();
        world.spawnEntity(toSpawn);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void initModel(ModelRegistryEvent e) {
        for (int i = 0; i < names.size(); i++) {
            String variant = "type=" + names.get(i);

            ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation(getRegistryName(), variant));
        }
    }

}
