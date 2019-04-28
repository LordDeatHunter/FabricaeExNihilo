package exnihilocreatio.config;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.items.tools.EnumCrook;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

@Config(modid = ExNihiloCreatio.MODID, name = "exnihilocreatio/ExNihiloCreatio", category = "exnihilocreatio")
public class ModConfig {
    /**
     * All Config Variables
     */
    public static final Misc misc = new Misc(); // Move this to the top so the JSON config is more visible
    @Config.Comment("These configs can be changed ClientSided without making problems with connecting to a server")
    public static final Client client = new Client();
    public static final Mechanics mechanics = new Mechanics();
    public static final Composting composting = new Composting();
    public static final InfestedLeaves infested_leaves = new InfestedLeaves();
    public static final Crooking crooking = new Crooking();
    public static final Sieve sieve = new Sieve();
    public static final Compatibility compatibility = new Compatibility();
    public static final Crucible crucible = new Crucible();
    public static final World world = new World();
    public static final Ore ore = new Ore();
    public static final WitchWater witchwater = new WitchWater();

    /**
     * All Config Classes
     */
    public static class Client {
        @Config.Comment("Enable/Disable the animation of autosieves.")
        public boolean clientFancyAutoSieveAnimations = true;
        @Config.Comment("How large of a displacement should the animation of the auto sieves have.")
        public float clientAutoSieveDisplacement = 0.03125f;
        @Config.Comment("This enables the thin Sieve model which is similar to the one in the 1.7 ex nihilo")
        public boolean thinCrucibleModel = false;
    }

    public static class Mechanics {
        public boolean enableBarrels = true;
        public boolean enableCrucible = true;
        public boolean shouldBarrelsFillWithRain = true;
        public boolean fakePlayersCanSieve = false;

        @Config.Comment({"Default max fluid temp allowed in the barrel. Does nothing if JSON configs are used.",
        "Set to a high number to disable any blacklisting. Water = 300, Lava = 1300"})
        @Config.RangeInt(min=-1)
        public int woodBarrelMaxTemp = 301;
    }

    public static class Composting {
        @Config.RangeInt(min = 1)
        public int ticksToFormDirt = 600;
    }

    public static class InfestedLeaves {
        @Config.RangeInt(min = 1)
        public int ticksToTransform = 600;
        @Config.Comment("How many ticks to wait before getting ticked again, already fully infested leaves spread much slower due to waiting for world ticks.")
        @Config.RangeInt(min = 0)
        public int leavesUpdateFrequency = 5;
        @Config.Comment("Minimum percentage to spread")
        @Config.RangeInt(min = 0, max=100)
        public int leavesSpreadPercent = 15;
        @Config.Comment("Chance to spread if it got ticked")
        @Config.RangeDouble(min=0.0, max=1.0)
        public float leavesSpreadChanceFloat = 0.5f;
    }

    public static class Crooking {
        @Config.Comment("Durability for each of the default crooks.")
        public Map<String, Integer> durability = new HashMap<>();

        Crooking(){
            for(EnumCrook crook : EnumCrook.values()) {
                durability.put(crook.getRegistryName(), crook.getDefaultDurability());
            }
        }

        @Config.RangeDouble(min=0.0, max=1.0)
        public double stringChance = 1.0;
        @Config.RangeInt(min = 0)
        public int maxStringDrop = 2;
        @Config.RangeDouble(min=0.0, max=1.0)
        public double stringFortuneChance = 1.0;
        @Config.RangeInt(min = 0)
        public int numberOfTimesToTestVanillaDrops = 3;
        @Config.Comment("Disable the ExNihilo Crooks, useful if another mod adds compatible crooks.")
        public boolean disableCrookCrafting = Loader.isModLoaded("inspirations");
    }

    public static class Misc {
        @Config.Comment({"Barrel light up if their contents do,",
                "disabling this makes `B:enableBarrelTransformLighting` do nothing"})
        public boolean enableBarrelLighting = true;
        @Config.Comment("Barrel lighting can change during transformations")
        public boolean enableBarrelTransformLighting = true; // maybe move to client?

        @Config.RequiresMcRestart
        @Config.Comment({"Enable this to to load the JSON files.",
                "Keeping this on false will only load recipes registered in code",
                "This is recommended to enable for Packmakers",
                "For players just playing with this without wanting to configure something this is recommended to keep disabled"})
        public boolean enableJSONLoading = false;

        public boolean oredictVanillaItems = true;
        @Config.Comment("Add Ex Nihilo seeds to the listAllSeeds oredict")
        public boolean oredictExNihiloSeeds = true;

        @Config.Comment({"When attempting to reuse an existing item from the oredict, what modids should be given preference."})
        public String[] oreDictPreferenceOrder = {"thermalfoundation", "magneticraft", "immersiveengineering"};
    }

    public static class Sieve {
        public Enchantments enchantments = new Enchantments();
        @Config.RangeInt(min = 0)
        public int sieveSimilarRadius = 2;
        @Config.RangeInt(min = 0)
        public int autoSieveRadius = 2;
        public boolean setFireToMacroUsers = false;
        @Config.RangeInt(min = 1, max = 64)
        public int meshMaxStackSize = 16;
        @Config.Comment({"Base progress per click. Modified by haste and efficiency."})
        @Config.RangeDouble(min=0.01f, max=1.0f)
        public float baseProgress = 0.10f;
        @Config.Comment({
                "If enabled, all meshes can obtain the results from the lower tier meshes.",
                "Note if the same item is registered in multiple tiers, then the higher tier",
                "will have a chance to drop multiples."
        })
        public boolean flattenSieveRecipes = false;
        public static class Enchantments {
            @Config.Comment("If enabled haste beacons will increase the speed at which sieves function.")
            public boolean hasteIncreasesSpeed = true;
            @Config.Comment("Enable the Sieve Efficiency enchantment.")
            public boolean enableSieveEfficiency = true;
            @Config.Comment("Additional progress per level of efficiency.")
            @Config.RangeDouble(min = 0)
            public double efficiencyScaleFactor = .05;
            @Config.Comment("Scaling factor for Haste sieving.")
            @Config.RangeDouble(min = 0)
            public double hasteScaleFactor = 1.0;
            @Config.RangeInt(min = 1)
            public int sieveEfficiencyMaxLevel = 5;
            @Config.Comment("Enable the Sieve Fortune enchantment.")
            public boolean enableSieveFortune = true;
            @Config.RangeInt(min = 1)
            public int sieveFortuneMaxLevel = 3;
            @Config.Comment("Enable the Sieve Luck Of The Sea enchantment.")
            public boolean enableSieveLuckOfTheSea = true;
            @Config.RangeInt(min = 1)
            public int sieveLuckOfTheSeaMaxLevel = 3;
        }
    }

    public static class Compatibility {
        @Config.RequiresMcRestart
        public TinkersConstructCompat tinkers_construct_compat = new TinkersConstructCompat();
        public ForestryCompat forestry_compat = new ForestryCompat();
        public RubberCompat rubber_compat = new RubberCompat();
        public MooFluidsCompat moofluids_compat = new MooFluidsCompat();
        public OreBerriesCompat oreberries_compat = new OreBerriesCompat();

        @Config.Comment("Prevents unidict from merging the ore chunks into normal ore.")
        public boolean preventUnidict = true;
        public boolean addYelloriteOreDict = true;
        public boolean dankNullIntegration = true;
        public boolean generalItemHandlerCompat = true;
        @Config.Comment("Register Ex Nihilo hammers as Magneticraft hammers.")
        public boolean magneticraftHammersCompat = true;

        public static class TinkersConstructCompat {
            public boolean doTinkersConstructCompat = true;
            public boolean addModifer = true;
            public boolean addMeltingOfChunks = true;
            public double ingotsPerChunkWhenMelting = 2.0;

            public boolean addMeltingOfDust = true;
            public double ingotsPerDustWhenMelting = 1.0;

            @Config.Comment("Add a Ex Nihilo style hammer to the tool station.")
            public boolean addExNihiloHammer = true;
            @Config.Comment({
                    "Tinkers Complement added a sledge hammer first, so by default Ex Nihilo Creatio",
                    "respects its existance (it also has more of the graphics defined)."
            })
            public boolean respectTinkersComplement = true;
            @Config.Comment("Add a Ex Nihilo style crook to the tool station.")
            public boolean addExNihiloCrook = true;

            @Config.Comment({"Let's you use the tinkers tools to look up JEI recipes, but causes",
                    "two log errors when tinkers later tries to register their default subtype handler."})
            public boolean JEItinkersTools = true;
        }

        public static class ForestryCompat {
            @Config.Comment({"How many random hives to check each time a scented hive gets a random tick. Setting this high may cause lag."})
            @Config.RangeInt(min = 1)
            public int hiveTransformTrys = 1;

        }

        public static class RubberCompat {
            @Config.Comment({"Possible results of using a rubber seed."})
            public String[] rubberSeed = {"techreborn:rubber_sapling", "ic2:sapling"};

        }

        public static class MooFluidsCompat {
            @Config.Comment({
                    "Enable barrel automation of MooFluid/FluidCows/MiniMoos Cows.",
                    "These configs apply to all of the supported cow mods."})
            public boolean enableMooFluids = true;
            public boolean fluidListIsBlackList = true;
            @Config.Comment({
                    "Which fluids should not be automatable with barrels;",
                    "or which \"should\", if fluidListIsBlackList is false."})
            public String[] fluidList = {};
            @Config.Comment({
                    "How much (mB) should be drained from a fluid cow at once.",
                    "Cow's cooldown is reset a prorated amount based on this and its max cooldown."})
            @Config.RangeInt(min = 1, max=1000)
            public int fillAmount = 10;

        }

        public static class OreBerriesCompat {
            @Config.Comment({
                    "Enable a seed for every OreBerries bush.",
                    "Default properties of the seeds are based on the oreberries config json."})
            public boolean enableOreBerrySeeds = true;
            @Config.Comment({
                    "Default likelihood that an oreberry seed is sieved;",
                    "gets divided by the rarity value in the oreberries config.",
                    "Dose nothing if JSON configs are enabled."})
            public float baseDropChance = 0.05f;
            @Config.Comment({"OreDict the seeds as listAllseed"})
            public boolean enableOreBerryOredict = true;

        }
    }

    public static class Crucible {
        @Config.RangeInt(min = 1)
        public int woodenCrucibleSpeed = 4;
    }

    public static class World {
        public boolean isSkyWorld = true;
        public int normalDropPercent = 100;
    }

    public static class Ore {
        public String chunkBaseOreDictName = "ore";
        public String pieceBaseOreDictName = "piece";
        public String dustBaseOreDictName  = "dust";
        public String ingotBaseOreDictName = "ingot";
    }

    public static class WitchWater {
        @Config.Comment("Enable custom fluid mixing")
        public boolean enableWitchWaterBlockForming = true;
    }

    /**
     * Inner Class to handle Reloading of the recipes
     */
    @Mod.EventBusSubscriber
    static class ConfigurationHolder {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(ExNihiloCreatio.MODID)) {
                ConfigManager.load(ExNihiloCreatio.MODID, Config.Type.INSTANCE);
            }
        }
    }

}
