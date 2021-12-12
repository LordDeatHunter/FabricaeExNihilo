package wraith.fabricaeexnihilo;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import wraith.fabricaeexnihilo.util.StatusEffectStats;

import java.util.List;
import java.util.Map;

@Config(name="fabricaeexnihilo")
public class FabricaeExNihiloConfig implements ConfigData {

    @Comment("Load json files from <config>/fabricaeexnihilo/*.json")
    public boolean useJsonRecipes = false;
    @Comment("Dump generated json files to <minecraft>/fabricaeexnihilo_generated")
    public boolean dumpGeneratedResource = false;

    @ConfigEntry.Gui.CollapsibleObject
    public ModuleConfig modules = new ModuleConfig();

    public static class ModuleConfig {

        @ConfigEntry.Gui.CollapsibleObject
        public Generator generator = new Generator();

        public static class Generator {
            @Comment("Blocks to be ignored as triggers for the block generator (i.e. makes sieves/barrels/crucibles)")
            public List<String> blackList = List.of("terrestria:sakura_log");
        }

        @ConfigEntry.Gui.CollapsibleObject
        public REIConfig REI = new REIConfig();

        public static class REIConfig {
            @Comment("How many rows should show up for the compost inputs, min = 1")
            public int compostNumRows = 5;
            @Comment("How many columns should show up for the compost inputs, min = 1")
            public int compostNumCols = 8;
            @Comment("How many rows should show up for the sieve outputs, min = 3")
            public int sieveNumRows = 3;
            @Comment("How many columns should show up for the sieve outputs, min = 1")
            public int sieveNumCols = 8;
            @Comment("How many rows should show up for the crook/hammer outputs, min = 2")
            public int toolNumRows = 3;
            @Comment("How many columns should show up for the crook/hammer outputs, min = 1")
            public int toolNumCols = 8;
            @Comment("How many rows should show up for the Witch Water fluid interactions outputs, min = 3")
            public int witchwaterworldRows = 3;
            @Comment("How many columns should show up for the Witch Water fluid interactions outputs, min = 1")
            public int witchwaterworldCols = 8;
        }

        @ConfigEntry.Gui.CollapsibleObject
        public BarrelConfig barrels = new BarrelConfig();

        public static class BarrelConfig {
            public boolean enabled = true;
            @Comment("How many ticks between updates")
            public int tickRate = 6;
            @Comment("Can barrels be enchanted with efficiency.")
            public boolean efficiency = true;
            @Comment("Can barrels be enchanted with thorns.")
            public boolean thorns = true;
            public boolean enableAlchemy = true;
            @Comment("Requires that thorns be enabled too.")
            public boolean enableBleeding = true;
            public boolean enableCompost = true;
            @Comment("How much progress to add each time the barrel ticks")
            public double compostRate = 0.01;
            public boolean enableFluidOnTop = true;
            public boolean enableLeaking = true;
            @Comment("How far away can a block be and still be affected by wooden barrels leaking")
            public int leakRadius = 2;
            public boolean enableMilking = true;
            public boolean enableTransforming = true;
            @Comment("How far away can a block be and still boost the transformation rate")
            public int transformBoostRadius = 2;
            @Comment("How many ticks does it take to fully transform")
            public int transformRate = 600;
            @Comment("How many ticks does each block below remove")
            public int transformBoost = 15;
        }

        @ConfigEntry.Gui.CollapsibleObject
        public CrucibleConfig crucibles = new CrucibleConfig();

        public static class CrucibleConfig {
            public boolean enabled = true;
            @Comment("How many ticks between updates")
            public int tickRate = 20;
            public int baseProcessRate = FluidAmount.BUCKET.div(100).as1620();
            public int woodenProcessingRate = FluidAmount.BUCKET.div(60).as1620();
            @Comment("How many buckets of liquid can a stone crucible store")
            public int stoneVolume = 4;
            @Comment("How many buckets of liquid can a wooden crucible store")
            public int woodVolume = 1;
            @Comment("Can crucibles be enchanted with efficiency.")
            public boolean efficiency = true;
            @Comment("Can stone crucibles be enchanted with fire aspect.")
            public boolean fireAspect = true;
        }

        @ConfigEntry.Gui.CollapsibleObject
        public InfestedConfig silkworms = new InfestedConfig();

        public static class InfestedConfig {
            public boolean enabled = true;
            @Comment("How much progress should by made during each infesting leaves update")
            public double progressPerUpdate = 0.015;
            @Comment("How many ticks between infesting leaves updates")
            public int updateFrequency = 10;
            @Comment("How much progress do infesting leaves need to make before they start trying to spread.")
            public double minimumSpreadPercent = 0.15;
            @Comment("How likely are leaves to succeed at spreading.")
            public double spreadChance = 0.5;
            @Comment("How many attempts should infesting leaves make to spread each time they check.")
            public int infestingSpreadAttempts = 1;
            @Comment("How many attempts should infested leaves make to spread each time they check.")
            public int infestedSpreadAttempts = 4;
        }

        @ConfigEntry.Gui.CollapsibleObject
        public SeedConfig seeds = new SeedConfig();

        public static class SeedConfig {
            public boolean enabled = true;
            public boolean cactus = true;
            public boolean carrot = true;
            public boolean chorus = true;
            public boolean grass = true;
            public boolean kelp = true;
            public boolean mycelium = true;
            public boolean potato = true;
            public boolean seaPickle = true;
            public boolean sugarCane = true;
            public boolean treeSeeds = true;

            @Comment("List of blocks that the rubber sapling could turn into.")
            public List<String> rubberSeed = List.of(
                    "techreborn:rubber_sapling",
                    "ic2:rubber_sapling",
                    "terrestria:rubber_sapling"
            );
        }

        @ConfigEntry.Gui.CollapsibleObject
        public SieveConfig sieves = new SieveConfig();

        public static class SieveConfig {
            public boolean enabled = true;
            @Comment("Do meshes use up durability (WIP)")
            public boolean meshDurability = false;
            public int meshStackSize = 16;
            @Comment("How far can sieves be connected")
            public int sieveRadius = 2;
            @Comment("Progress per click = baseProgress + efficiencyScaleFactor*efficientLevel + hasteScaleFactor*hasteLevel")
            public double baseProgress = 0.1;
            public double efficiencyScaleFactor = 0.05;
            public double hasteScaleFactor = 1.0;
            @Comment("Can meshes be enchanted with efficiency")
            public boolean efficiency = true;
            @Comment("Can meshes be enchanted with fortune")
            public boolean fortune = true;
            @Comment("Do haste beacons/potions affect sieving speed")
            public boolean haste = true;
        }

        @ConfigEntry.Gui.CollapsibleObject
        public CrookConfig crooks = new CrookConfig();

        public static class CrookConfig {
            public boolean enabled = true;
        }

        @ConfigEntry.Gui.CollapsibleObject
        public HammerConfig hammers = new HammerConfig();

        public static class HammerConfig {
            public boolean enabled = true;
        }

        @ConfigEntry.Gui.CollapsibleObject
        public WitchWaterConfig witchwater = new WitchWaterConfig();

        public static class WitchWaterConfig {
            public boolean enabled = true;
            @Comment("What effects should players get on contact")
            public Map<String, StatusEffectStats> effects = Map.of(
                    "blindness", new StatusEffectStats(210, 0),
                    "hunger", new StatusEffectStats(210, 2),
                    "slowness", new StatusEffectStats(210, 0),
                    "weakness", new StatusEffectStats(210, 2),
                    "wither", new StatusEffectStats(210, 0)
            );
        }

    }

}