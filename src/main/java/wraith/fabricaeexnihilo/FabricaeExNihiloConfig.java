package wraith.fabricaeexnihilo;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import wraith.fabricaeexnihilo.util.StatusEffectStats;

import java.util.Map;

public class FabricaeExNihiloConfig {

    public ModuleConfig modules = new ModuleConfig();

    public static class ModuleConfig {

        public BarrelConfig barrels = new BarrelConfig();
        public CrucibleConfig crucibles = new CrucibleConfig();
        public SeedConfig seeds = new SeedConfig();
        public SieveConfig sieves = new SieveConfig();
        public InfestedConfig silkworms = new InfestedConfig();
        public StrainerConfig strainers = new StrainerConfig();
        public WitchWaterConfig witchWater = new WitchWaterConfig();

        public static class StrainerConfig {

            public int maxWaitTime = 6000; // Five minutes
            public int minWaitTime = 1200; // One minute
        }

        public static class BarrelConfig {

            public CompostConfig compost = new CompostConfig();
            public boolean enableAlchemy = true;
            // Requires that thorns be enabled too.
            public boolean enableBleeding = true;
            public boolean enableFluidCombination = true;
            public boolean enableMilking = true;
            public EnchantmentConfig enchantments = new EnchantmentConfig();
            public boolean isFilterWhitelist = false;
            public LeakingConfig leaking = new LeakingConfig();
            // How many ticks between updates
            public int tickRate = 6;
            public TransformingConfig transforming = new TransformingConfig();
            public String[] woodenFluidFilter = { "minecraft:lava" };

            public static class EnchantmentConfig {

                // Can barrels be enchanted with efficiency.
                public boolean efficiency = true;
                // Can barrels be enchanted with thorns.
                public boolean thorns = true;
            }

            public static class CompostConfig {

                public boolean enabled = true;
                // How much progress to add each time the barrel ticks
                public double rate = 0.01;

            }

            public static class LeakingConfig {

                public boolean enabled = true;
                // How much progress to add each time the barrel ticks
                public int radius = 2;

            }

            public static class TransformingConfig {

                // How many ticks does each block below remove
                public int boost = 15;
                // How far away can a block be and still boost the transformation rate
                public int boostRadius = 2;
                public boolean enabled = true;
                // How many ticks does it take to fully transform
                public int rate = 600;
            }
        }

        @SuppressWarnings("UnstableApiUsage")
        public static class CrucibleConfig {

            public long baseProcessRate = FluidConstants.BUCKET / 100;
            // Can crucibles be enchanted with efficiency.
            public boolean efficiency = true;
            // Can stone crucibles be enchanted with fire aspect.
            public boolean fireAspect = true;
            // How many buckets of liquid can a stone crucible store
            public int stoneVolume = 4;
            // How many ticks between updates
            public int tickRate = 20;
            // How many buckets of liquid can a wooden crucible store
            public int woodVolume = 1;
            public long woodenProcessingRate = FluidConstants.BUCKET / 60;
        }

        public static class InfestedConfig {

            public boolean enabled = true;
            // How many attempts should infested leaves make to spread each time they check.
            public int infestedSpreadAttempts = 4;
            // How many attempts should infesting leaves make to spread each time they check.
            public int infestingSpreadAttempts = 1;
            // How much progress do infesting leaves need to make before they start trying to spread.
            public double minimumSpreadPercent = 0.15;
            // How much progress should be made during each infesting leaves update
            public double progressPerUpdate = 0.015;
            // How likely are leaves to succeed at spreading.
            public double spreadChance = 0.5;
            // How many ticks between infesting leaves updates
            public int updateFrequency = 10;
        }

        //TODO: Probably should remove many of these
        public static class SeedConfig {

            public boolean cactus = true;
            public boolean carrot = true;
            public boolean chorus = true;
            public boolean enabled = true;
            public boolean flowerSeeds = true;
            public boolean grass = true;
            public boolean kelp = true;
            public boolean mycelium = true;
            public boolean netherSpores = true;
            public boolean potato = true;
            public boolean seaPickle = true;
            public boolean sugarCane = true;
            public boolean treeSeeds = true;
        }

        public static class SieveConfig {

            // Progress per click = baseProgress + efficiencyScaleFactor*efficiencyLevel + hasteScaleFactor*hasteLevel
            public double baseProgress = 0.1;
            // Can meshes be enchanted with efficiency
            public boolean efficiency = true;
            public double efficiencyScaleFactor = 0.05;
            // Can meshes be enchanted with fortune
            public boolean fortune = true;
            // Do haste beacons/potions affect sieving speed
            public boolean haste = true;
            public double hasteScaleFactor = 1.0;
            // Do meshes use up durability (WIP)
            //public boolean meshDurability = false;
            public int meshStackSize = 16;
            // How far can sieves be connected
            public int sieveRadius = 2;
        }

        public static class WitchWaterConfig {

            // What effects should players get on contact
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