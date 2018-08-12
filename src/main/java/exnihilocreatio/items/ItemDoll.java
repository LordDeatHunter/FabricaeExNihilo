package exnihilocreatio.items;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.ModFluids;
import exnihilocreatio.util.Data;
import exnihilocreatio.util.IHasModel;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
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
import java.util.List;

public class ItemDoll extends Item implements IHasModel {

    private static final Int2ObjectMap<DollType> existingTypes = new Int2ObjectArrayMap<>();

    public ItemDoll() {
        super();
        setTranslationKey("item_doll");
        setRegistryName("item_doll");

        setCreativeTab(ExNihiloCreatio.tabExNihilo);
        setHasSubtypes(true);

        existingTypes.put(DollType.BLAZE.meta, DollType.BLAZE);
        existingTypes.put(DollType.ENDERMAN.meta, DollType.ENDERMAN);
        existingTypes.put(DollType.SHULKER.meta, DollType.SHULKER);
        existingTypes.put(DollType.GUARDIAN.meta, DollType.GUARDIAN);

        if (Loader.isModLoaded("thermalfoundation")) {
            existingTypes.put(DollType.BLIZZ.meta, DollType.BLIZZ);
            existingTypes.put(DollType.BLITZ.meta, DollType.BLITZ);
            existingTypes.put(DollType.BASALZ.meta, DollType.BASALZ);
        }

        if (Loader.isModLoaded("tconstruct")) {
            existingTypes.put(DollType.BLUESLIME.meta, DollType.BLUESLIME);
        }

        Data.ITEMS.add(this);
    }

    public Fluid getSpawnFluid(ItemStack stack) {
        Fluid fluid = FluidRegistry.getFluid(existingTypes.get(stack.getMetadata()).fluidname);
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
        DollType type = existingTypes.get(stack.getMetadata());
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
    public String getTranslationKey(ItemStack stack) {
        return getTranslationKey() + "." + DollType.getByMeta(stack.getMetadata()).name;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nullable CreativeTabs tab, @Nonnull NonNullList<ItemStack> list) {
        if (this.isInCreativeTab(tab))
            for (DollType type : existingTypes.values()) {
                list.add(new ItemStack(this, 1, type.meta));
            }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void initModel(ModelRegistryEvent event) {

        Int2ObjectMap<ModelResourceLocation> locations = new Int2ObjectArrayMap<>();
        for (DollType type : DollType.values()) {
            locations.put(type.meta, new ModelResourceLocation(getRegistryName(), "type=" + type.name));
        }

        ModelBakery.registerItemVariants(this, locations.values().toArray(new ModelResourceLocation[0]));
        ModelLoader.setCustomMeshDefinition(this, stack -> locations.get(stack.getMetadata()));

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (existingTypes.containsKey(stack.getMetadata())) {
            tooltip.add(I18n.format(getTranslationKey(stack) + ".desc"));
        } else {
            tooltip.add(I18n.format("debug.mod_not_installed.desc", DollType.getByMeta(stack.getMetadata()).modid));
        }
    }

    public enum DollType {
        // @formatter:off
        BLAZE(      0, "blaze",     "minecraft",        "minecraft:blaze",          "lava",         1),
        ENDERMAN(   1, "enderman",  "minecraft",        "minecraft:enderman",       "witchwater",   2),
        SHULKER(    2, "shulker",   "minecraft",        "minecraft:shulker",        "witchwater",   1.5),
        GUARDIAN(   3, "guardian",  "minecraft",        "minecraft:guardian",       "water",        1),
        BLIZZ(      4, "blizz",     "thermalfoundation","thermalfoundation:blizz",  "pyrotheum",    1),
        BLITZ(      5, "blitz",     "thermalfoundation","thermalfoundation:blitz",  "pyrotheum",    1),
        BASALZ(     6, "basalz",    "thermalfoundation","thermalfoundation:basalz", "pyrotheum",    1),
        BLUESLIME(  7, "blueslime", "tconstruct",       "tconstruct:blueslime",      "milk",        2);
        // @formatter:on
        private static final Int2ObjectMap<DollType> ALL_TYPES = new Int2ObjectArrayMap<>();

        static {
            for (DollType dollType : values()) {
                ALL_TYPES.put(dollType.meta, dollType);
            }
        }

        public final int meta;
        public final String name;
        public final String modid;
        public final String entityname;
        public final String fluidname;
        public final double posYCorrection;

        DollType(int meta, String name, String modid, String entityname, String fluidname, double posYCorrection) {
            this.meta = meta;
            this.name = name;
            this.modid = modid;
            this.entityname = entityname;
            this.fluidname = fluidname;
            this.posYCorrection = posYCorrection;
        }

        public static DollType getByMeta(int meta) {
            return ALL_TYPES.get(meta);
        }
    }

}
