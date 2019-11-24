package exnihilofabrico

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import blue.endless.jankson.Comment
import io.github.cottonmc.cotton.config.annotations.ConfigFile
import net.minecraft.util.Identifier

@ConfigFile(name="ExNihiloFabrico")
class ExNihiloFabricoConfig {
    @Comment("Different pieces of Ex Nihilo Fabrico")
    var modules = ModuleConfig()
}

class ModuleConfig {
    var general = GeneralConfig()
    var barrels = BarrelConfig()
    var crucibles = CrucibleConfig()
    var silkworms = InfestedConfig()
    var seeds = SeedConfig()
    var sieves = SieveConfig()
    var crooks = CrookConfig()
    var hammer = HammerConfig()
    var witchwater = WitchWaterConfig()
}

class GeneralConfig {
    @Comment("Load json files from <config>/exnihilofabrico/*.json")
    var useJsonRecipes: Boolean = false
    @Comment("Dump generated json files to <minecraft>/exnihilofabrico_generated")
    var dumpGeneratedResource: Boolean = false
}

class BarrelConfig {
    var enabled: Boolean = true
    @Comment("How many ticks between updates")
    var tickRate = 6
    @Comment("Can barrels be enchanted with efficiency.")
    var efficiency: Boolean = true
    @Comment("Can barrels be enchanted with thorns.")
    var thorns: Boolean = true
    var enableAlchemy: Boolean = true
    @Comment("Requires that thorns be enabled too.")
    var enableBleeding: Boolean = true
    var enableCompost: Boolean = true
    @Comment("How much progress to add each time the barrel ticks")
    var compostRate = 0.01
    var enableFluidOnTop: Boolean = true
    var enableLeaking: Boolean = true
    @Comment("How far away can a block be and still be affected by wooden barrels leaking")
    var leakRadius: Int = 2
    var enableMilking: Boolean = true
    var enableTransforming: Boolean = true
    @Comment("How far away can a block be and still boost the transformation rate")
    var transformBoostRadius: Int = 2
    @Comment("How many ticks does it take to fully transform")
    var transformRate: Int = 600
    @Comment("How many ticks does each block below remove")
    var transformBoost: Int = 15
}

class CrucibleConfig {
    var enabled: Boolean = true
    @Comment("How many ticks between updates")
    var tickRate = 20
    var baseProcessRate = FluidVolume.BUCKET / 100
    var woodenProcessingRate = FluidVolume.BUCKET / 60
    @Comment("How many buckets of liquid can a stone crucible store")
    var stoneVolume = 4
    @Comment("How many buckets of liquid can a wooden crucible store")
    var woodVolume = 1
    @Comment("Can crucibles be enchanted with efficiency.")
    var efficiency: Boolean = true
    @Comment("Can stone crucibles be enchanted with fire aspect.")
    var fireAspect: Boolean = true
}

class InfestedConfig {
    var enabled: Boolean = true
    @Comment("How much progress should by made during each infesting leaves update")
    var progressPerUpdate = 0.015
    @Comment("How many ticks between infesting leaves updates")
    var updateFrequency = 10
    @Comment("How much progress do infesting leaves need to make before they start trying to spread.")
    var minimumSpreadPercent = 0.15
    @Comment("How likely are leaves to succeed at spreading.")
    var spreadChance = 0.5
    @Comment("How many attempts should infesting leaves make to spread each time they check.")
    var infestingSpreadAttempts = 1
    @Comment("How many attempts should infested leaves make to spread each time they check.")
    var infestedSpreadAttempts = 4
}
class SeedConfig {
    var enabled: Boolean = true
    var cactus: Boolean = true
    var carrot: Boolean = true
    var chorus: Boolean = true
    var grass: Boolean = true
    var kelp: Boolean = true
    var mycelium: Boolean = true
    var potato: Boolean = true
    var seaPickle: Boolean = true
    var sugarCane: Boolean = true
    var treeSeeds: Boolean = true
}
class SieveConfig {
    var enabled: Boolean = true
    @Comment("Do meshes use up durability (WIP)")
    var meshDurability: Boolean = false
    var meshStackSize = 16
    @Comment("How far can sieves be connected")
    var sieveRadius = 2
    @Comment("Progress per click = baseProgress + efficiencyScaleFactor*efficientLevel + hasteScaleFactor*hasteLevel")
    var baseProgress = 0.1
    var efficiencyScaleFactor = 0.05
    var hasteScaleFactor = 1.0
    @Comment("Can Sieves be enchanted with efficiency")
    var efficiency: Boolean = true
    @Comment("Can Sieves be enchanted with fortune")
    var fortune: Boolean = true
    @Comment("Do haste beacons/potions affect sieving speed")
    var haste: Boolean = true
}
class CrookConfig{
    var enabled: Boolean = true
}
class HammerConfig {
    var enabled: Boolean = true
}
class WitchWaterConfig {
    var enabled: Boolean = true
    @Comment("What effects should players get on contact")
    var effects = mutableMapOf(
        Identifier("blindness") to Pair(210,0),
        Identifier("hunger") to Pair(210,2),
        Identifier("slowness") to Pair(210,0),
        Identifier("weakness") to Pair(210,2),
        Identifier("wither") to Pair(210,0)
    )
}