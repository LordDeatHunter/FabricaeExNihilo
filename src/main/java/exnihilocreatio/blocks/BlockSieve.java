package exnihilocreatio.blocks;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.compatibility.ITOPInfoProvider;
import exnihilocreatio.config.ModConfig;
import exnihilocreatio.items.ItemMesh;
import exnihilocreatio.tiles.TileSieve;
import exnihilocreatio.util.Util;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Map;

public class BlockSieve extends BlockBase implements ITileEntityProvider, ITOPInfoProvider {

    public static final PropertyEnum<MeshType> MESH = PropertyEnum.create("mesh", MeshType.class);

    public BlockSieve() {
        super(Material.WOOD, "block_sieve");
        this.setHardness(2.0f);
        this.setCreativeTab(ExNihiloCreatio.tabExNihilo);
        this.setDefaultState(this.blockState.getBaseState().withProperty(MESH, MeshType.NO_RENDER));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote)
            return true;

        // I think this should work. Let's just go with it.
        if (player instanceof FakePlayer && !ModConfig.mechanics.fakePlayersCanSieve) {
            return false;
        }

        ItemStack heldItem = player.getHeldItem(hand);
        TileSieve te = (TileSieve) world.getTileEntity(pos);

        if (te != null) {
            if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemMesh) {
                //Adding a mesh.
                ItemStack meshStack = heldItem.copy();
                meshStack.setCount(1);
                boolean done = te.setMesh(meshStack, false);

                if (done) {
                    if (!player.isCreative())
                        heldItem.shrink(1);
                    return true;
                }
            }
            if (heldItem.isEmpty() && !te.getMeshStack().isEmpty() && player.isSneaking() && te.setMesh(ItemStack.EMPTY, true)) {
                //Removing a mesh.
                Util.dropItemInWorld(te, player, te.getMeshStack(), 0.02f);
                te.setMesh(ItemStack.EMPTY, false);
                return true;
            }

            if (te.addBlock(heldItem)) {
                if (!player.isCreative())
                    heldItem.shrink(1);

                for (int xOffset = -1 * ModConfig.sieve.sieveSimilarRadius; xOffset <= ModConfig.sieve.sieveSimilarRadius; xOffset++) {
                    for (int zOffset = -1 * ModConfig.sieve.sieveSimilarRadius; zOffset <= ModConfig.sieve.sieveSimilarRadius; zOffset++) {
                        TileEntity entity = world.getTileEntity(pos.add(xOffset, 0, zOffset));
                        if (entity != null && entity instanceof TileSieve) {
                            TileSieve sieve = (TileSieve) entity;

                            if (!heldItem.isEmpty() && te.isSieveSimilarToInput(sieve)) {
                                if (sieve.addBlock(heldItem) && !player.isCreative()) {
                                    heldItem.shrink(1);
                                }
                            }
                        }
                    }
                }
                return true;
            }

            ArrayList<BlockPos> toSift = new ArrayList<>();
            for (int xOffset = -1 * ModConfig.sieve.sieveSimilarRadius; xOffset <= ModConfig.sieve.sieveSimilarRadius; xOffset++) {
                for (int zOffset = -1 * ModConfig.sieve.sieveSimilarRadius; zOffset <= ModConfig.sieve.sieveSimilarRadius; zOffset++) {
                    TileEntity entity = world.getTileEntity(pos.add(xOffset, 0, zOffset));
                    if (entity != null && entity instanceof TileSieve) {
                        TileSieve sieve = (TileSieve) entity;

                        if (te.isSieveSimilar(sieve))
                            toSift.add(pos.add(xOffset, 0, zOffset));
                    }
                }
            }
            for (BlockPos posIter : toSift) {
                if (posIter != null) {
                    TileSieve sieve = (TileSieve) world.getTileEntity(posIter);

                    if (sieve != null) {
                        sieve.doSieving(player, false);
                    }
                }
            }
            return true;
        }

        return true;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, MESH);
    }

    @Override
    @Nonnull
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null) {
            TileSieve sieve = (TileSieve) te;
            if (!sieve.getMeshStack().isEmpty())
                Util.dropItemInWorld(sieve, null, sieve.getMeshStack(), 0.02f);
        }

        super.breakBlock(world, pos, state);
    }


    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileSieve();
    }

    //region >>>> RENDERING OPTIONS
    @Override
    @Deprecated
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return false;
    }
    //endregion

    @Override
    @SideOnly(Side.CLIENT)
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world,
                             IBlockState blockState, IProbeHitData data) {

        TileSieve sieve = (TileSieve) world.getTileEntity(data.getPos());
        if (sieve == null)
            return;

        if (sieve.getMeshStack().isEmpty()) {
            probeInfo.text("Mesh: None");
            return;
        }
        probeInfo.text("Mesh: " + I18n.format(sieve.getMeshStack().getUnlocalizedName() + ".name"));

        if (mode == ProbeMode.EXTENDED) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(sieve.getMeshStack());
            for (Map.Entry<Enchantment, Integer> enchantment : enchantments.entrySet()) {
                probeInfo.text(TextFormatting.BLUE + enchantment.getKey().getTranslatedName(enchantment.getValue()));
            }
        }

    }

    public enum MeshType implements IStringSerializable {
        NONE(0, "none"), STRING(1, "string"), FLINT(2, "flint"), IRON(3, "iron"), DIAMOND(4, "diamond"), NO_RENDER(5, "no_render");

        private final int id;
        private final String name;

        MeshType(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public static MeshType getMeshTypeByID(int meta) {
            switch (meta) {
                case 1:
                    return STRING;
                case 2:
                    return FLINT;
                case 3:
                    return IRON;
                case 4:
                    return DIAMOND;
                case 5:
                    return NO_RENDER;
            }

            return NONE;
        }

        @Override
        @Nonnull
        public String getName() {
            return name;
        }

        public int getID() {
            return id;
        }

        @Override
        public String toString() {
            return getName();
        }
    }

}
