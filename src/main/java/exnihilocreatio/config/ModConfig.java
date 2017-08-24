package exnihilocreatio.config;

import exnihilocreatio.ExNihiloCreatio;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = ExNihiloCreatio.MODID, name = "exnihilocreatio/ExNihiloCreatio", category = "exnihilocreatio")
public class ModConfig {
    /**
     * All Config Variables
     */
    @Config.Comment("These configs can be changed ClientSided without making problems with connecting to a server")
    public static Client client = new Client();
    public static Mechanics mechanics = new Mechanics();
    public static Composting composting = new Composting();
    public static InfestedLeaves infested_leaves = new InfestedLeaves();
    public static Crooking crooking = new Crooking();
    public static Misc misc = new Misc();
    public static Sieve sieve = new Sieve();
    public static Compatibility compatibility = new Compatibility();
    public static Crucible crucible = new Crucible();

    /**
     * All Config Classes
     */
    public static class Client {
        public boolean clientFancyAutoSieveAnimations = true;
    }

    public static class Mechanics {
        public boolean enableBarrels = true;
        public boolean enableCrucible = true;
        public boolean shouldBarrelsFillWithRain = true;
        public boolean fakePlayersCanSieve = false;
    }

    public static class Composting {
        public int ticksToFormDirt = 600;
    }

    public static class InfestedLeaves {
        public int ticksToTransform = 600;
        public int leavesUpdateFrequency = 40;
        public double leavesSpreadChance = 0.0015;
        public boolean doLeavesUpdateClient = true;
    }

    public static class Crooking {
        public double stringChance = 1.0;
        public double stringFortuneChance = 1.0;
        public int numberOfTimesToTestVanillaDrops = 3;
    }

    public static class Misc {
        public boolean enableBarrelTransformLighting = true; // maybe move to client?

        @Config.RequiresMcRestart
        @Config.Comment({"Enable this to to load the JSON files.",
                "Keeping this on false will only load recipes registered in code",
                "This is recommended to enable for Packmakers",
                "For players just playing with this without wanting to configure something this is recommended to keep disabled"})
        public boolean enableJSONLoading = false;
    }

    public static class Sieve {
        public int sieveSimilarRadius = 2;
        public int autoSieveRadius = 2;
        public boolean setFireToMacroUsers = false;
    }

    public static class Compatibility {
        @Config.RequiresMcRestart
        public TinkersConstructCompat tinkers_construct_compat = new TinkersConstructCompat();

        public static class TinkersConstructCompat {
            public boolean doTinkersConstructCompat = true;
            public boolean addModifer = true;
            public boolean addMeltingOfChunks = true;
            public double ingotsPerChunkWhenMelting = 2.0;

            public boolean addMeltingOfDust = true;
            public double ingotsPerDustWhenMelting = 1.0;

        }
    }

    public static class Crucible {
        public int woodenCrucibleSpeed = 4;
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
