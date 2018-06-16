package exnihilocreatio.items;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.ModFluids;
import exnihilocreatio.util.Data;
import exnihilocreatio.util.IHasModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemDoll extends Item implements IHasModel {

    public enum DollType {
        BLAZE(0, "blaze", "minecraft:blaze", "lava", 1),
        ENDERMAN(1, "enderman", "minecraft:enderman", "witchwater", 2),
        SHULKER(2, "shulker", "minecraft:shulker", "witchwater", 1.5),
        GUARDIAN(3, "guardian", "minecraft:guardian", "water", 1),
        BLIZZ(4, "blizz", "thermalfoundation:blizz", "pyrotheum", 1),
        BLITZ(5, "blitz", "thermalfoundation:blitz", "pyrotheum", 1),
        BASALZ(6, "basalz", "thermalfoundation:basalz", "pyrotheum", 1),
        BLUESLIME(7, "blueslime", "tconstruct:blueslime", "milk", 2);

        public final int meta;
        public final String name;
        public final String entityname;
        public final String fluidname;
        public final double posYCorrection;

        private DollType(int meta, String name, String entityname, String fluidname, double posYCorrection) {
            this.meta = meta;
            this.name = name;
            this.entityname = entityname;
            this.fluidname = fluidname;
            this.posYCorrection = posYCorrection;
        }
    }

    private static final ArrayList<DollType> types = new ArrayList<>();

    public ItemDoll() {
        super();
        setUnlocalizedName("item_doll");
        setRegistryName("item_doll");

        setCreativeTab(ExNihiloCreatio.tabExNihilo);
        setHasSubtypes(true);

        types.add(DollType.BLAZE.meta,DollType.BLAZE);
        types.add(DollType.ENDERMAN.meta,DollType.ENDERMAN);
        types.add(DollType.SHULKER.meta,DollType.SHULKER);
        types.add(DollType.GUARDIAN.meta,DollType.GUARDIAN);

        if (Loader.isModLoaded("thermalfoundation")) {
            types.add(DollType.BLIZZ.meta,DollType.BLIZZ);
            types.add(DollType.BLITZ.meta,DollType.BLITZ);
            types.add(DollType.BASALZ.meta,DollType.BASALZ);
        }

        if (Loader.isModLoaded("tconstruct")) {
            types.add(DollType.BLUESLIME.meta,DollType.BLUESLIME);
        }

        Data.ITEMS.add(this);
    }

    public Fluid getSpawnFluid(ItemStack stack) {
        Fluid fluid = FluidRegistry.getFluid(types.get(stack.getMetadata()).fluidname);
        if (fluid != null)
            return fluid;
        else
            return ModFluids.fluidWitchwater;
    }

    /**
     * Spawns the mob in the world at position
     *
     * @param stack The Doll Stack
     * @param pos   Blockpos
     * @return true if spawn is successful
     */
    public boolean spawnMob(ItemStack stack, World world, BlockPos pos) {
        DollType type = types.get(stack.getMetadata());
        if (type == null) return false;

        Entity spawnee = EntityList.createEntityByIDFromName(new ResourceLocation(type.entityname), world);
        if (spawnee != null) {
            spawnee.setPosition(pos.getX(), pos.getY() + type.posYCorrection, pos.getZ());
            return world.spawnEntity(spawnee);
        } else
            return false;
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName() + "." + types.get(stack.getItemDamage()).name;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nullable CreativeTabs tab, @Nonnull NonNullList<ItemStack> list) {
        if (this.isInCreativeTab(tab))
            for (DollType type : types) {
                list.add(new ItemStack(this, 1, type.meta));
            }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void initModel(ModelRegistryEvent event) {

        List<ModelResourceLocation> locations = new ArrayList<>();
        for (DollType type : types) {
            locations.add(new ModelResourceLocation(getRegistryName(), "type=" + type.name));
        }

        ModelBakery.registerItemVariants(this, locations.toArray(new ModelResourceLocation[0]));
        ModelLoader.setCustomMeshDefinition(this, stack -> locations.get(stack.getMetadata()));

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(I18n.translateToLocal(getUnlocalizedName(stack) + ".desc"));
    }

}
