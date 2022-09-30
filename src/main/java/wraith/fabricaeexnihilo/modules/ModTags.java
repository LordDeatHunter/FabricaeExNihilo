package wraith.fabricaeexnihilo.modules;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public final class ModTags {
    private ModTags() {}

    public static final TagKey<Item> CROOKS = TagKey.of(Registry.ITEM_KEY, id("crooks"));
    public static final TagKey<Item> HAMMERS = TagKey.of(Registry.ITEM_KEY, id("hammers"));
    public static final TagKey<Item> BARRELS = TagKey.of(Registry.ITEM_KEY, id("barrels"));

    public static final TagKey<Block> HAMMERABLES = TagKey.of(Registry.BLOCK_KEY, id("hammerables"));
    public static final TagKey<Block> CROOKABLES = TagKey.of(Registry.BLOCK_KEY, id("crookables"));
    public static final TagKey<Block> INFESTED_LEAVES = TagKey.of(Registry.BLOCK_KEY, id("infested_leaves"));

    public static final TagKey<Fluid> HOT = TagKey.of(Registry.FLUID_KEY, id("hot"));
    public static final TagKey<Fluid> WITCHWATER = TagKey.of(Registry.FLUID_KEY, id("witchwater"));
    public static final TagKey<Fluid> TRUE_LAVA = TagKey.of(Registry.FLUID_KEY, id("true_lava"));
    public static final TagKey<Fluid> TRUE_WATER = TagKey.of(Registry.FLUID_KEY, id("true_water"));

    public static final class Common {
        private Common() {}

        public static final TagKey<Block> CONCRETES = TagKey.of(Registry.BLOCK_KEY, new Identifier("c", "concretes"));
        public static final TagKey<Block> CONCRETE_POWDERS = TagKey.of(Registry.BLOCK_KEY, new Identifier("c", "concrete_powders"));
        public static final TagKey<Block> TORCHES = TagKey.of(Registry.BLOCK_KEY, new Identifier("c", "torches"));

        public static final TagKey<Item> VEGETABLES = TagKey.of(Registry.ITEM_KEY, new Identifier("c", "vegetables"));
        public static final TagKey<Item> SEEDS = TagKey.of(Registry.ITEM_KEY, new Identifier("c", "seeds"));
        public static final TagKey<Item> RAW_MEAT = TagKey.of(Registry.ITEM_KEY, new Identifier("c", "raw_meat"));
        public static final TagKey<Item> COOKED_MEAT = TagKey.of(Registry.ITEM_KEY, new Identifier("c", "cooked_meat"));

        public static final TagKey<Fluid> BRINE = TagKey.of(Registry.FLUID_KEY, new Identifier("c", "brine"));
        public static final TagKey<Fluid> BLOOD = TagKey.of(Registry.FLUID_KEY, new Identifier("c", "blood"));
    }
}
