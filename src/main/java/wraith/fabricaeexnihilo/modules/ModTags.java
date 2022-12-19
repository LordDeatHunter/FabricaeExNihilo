package wraith.fabricaeexnihilo.modules;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public final class ModTags {

    public static final TagKey<Item> BARRELS = TagKey.of(Registries.ITEM.getKey(), id("barrels"));
    public static final TagKey<Block> CROOKABLES = TagKey.of(Registries.BLOCK.getKey(), id("crookables"));
    public static final TagKey<Item> CROOKS = TagKey.of(Registries.ITEM.getKey(), id("crooks"));
    public static final TagKey<Block> HAMMERABLES = TagKey.of(Registries.BLOCK.getKey(), id("hammerables"));
    public static final TagKey<Item> HAMMERS = TagKey.of(Registries.ITEM.getKey(), id("hammers"));
    public static final TagKey<Fluid> HOT = TagKey.of(Registries.FLUID.getKey(), id("hot"));
    public static final TagKey<Block> INFESTED_LEAVES = TagKey.of(Registries.BLOCK.getKey(), id("infested_leaves"));
    public static final TagKey<Fluid> TRUE_LAVA = TagKey.of(Registries.FLUID.getKey(), id("true_lava"));
    public static final TagKey<Fluid> TRUE_WATER = TagKey.of(Registries.FLUID.getKey(), id("true_water"));
    public static final TagKey<Fluid> WITCHWATER = TagKey.of(Registries.FLUID.getKey(), id("witchwater"));
    private ModTags() {}

    public static final class Common {

        public static final TagKey<Fluid> BLOOD = TagKey.of(Registries.FLUID.getKey(), new Identifier("c", "blood"));
        public static final TagKey<Fluid> BRINE = TagKey.of(Registries.FLUID.getKey(), new Identifier("c", "brine"));
        public static final TagKey<Fluid> MILK = TagKey.of(Registries.FLUID.getKey(), new Identifier("c", "milk"));
        public static final TagKey<Block> CONCRETES = TagKey.of(Registries.BLOCK.getKey(), new Identifier("c", "concretes"));
        public static final TagKey<Block> CONCRETE_POWDERS = TagKey.of(Registries.BLOCK.getKey(), new Identifier("c", "concrete_powders"));
        public static final TagKey<Item> COOKED_MEAT = TagKey.of(Registries.ITEM.getKey(), new Identifier("c", "cooked_meat"));
        public static final TagKey<Item> RAW_MEAT = TagKey.of(Registries.ITEM.getKey(), new Identifier("c", "raw_meat"));
        public static final TagKey<Item> SEEDS = TagKey.of(Registries.ITEM.getKey(), new Identifier("c", "seeds"));
        public static final TagKey<Block> TORCHES = TagKey.of(Registries.BLOCK.getKey(), new Identifier("c", "torches"));
        public static final TagKey<Item> VEGETABLES = TagKey.of(Registries.ITEM.getKey(), new Identifier("c", "vegetables"));
        private Common() {}
    }
}
