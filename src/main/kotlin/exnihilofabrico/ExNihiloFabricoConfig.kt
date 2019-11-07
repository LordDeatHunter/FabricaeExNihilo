package exnihilofabrico

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
    var crucibles = CrucibleConfig()
    var silkworms = InfestedConfig()
    var seeds = SeedConfig()
    var sieves = SieveConfig()
    var crooks = CrookConfig()
    var hammer = HammerConfig()
    var wrench = WrenchConfig()
    var witchwater = WitchWaterConfig()
}

class GeneralConfig {
    @Comment("Load json files from <config>/exnihilofabrico/*.json")
    var useJsonRecipes: Boolean = true
}

class CrucibleConfig {
    var enabled: Boolean = true
    @Comment("How many ticks between updates")
    var tickRate = 20
    @Comment("How many buckets of liquid can a stone crucible store")
    var stoneVolume = 4
    @Comment("How many buckets of liquid can a wooden crucible store")
    var woodVolume = 1
    @Comment("Can crucibles be enchanted with efficiency.")
    var efficiency: Boolean = true
    @Comment("Can stone crucibles be enchanted with flame.")
    var flame: Boolean = true
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
    var carrot: Boolean = true
    var chorus: Boolean = true
    var kelp: Boolean = true
    var potato: Boolean = true
    var seaPickle: Boolean = true
    var sugarCane: Boolean = true
    var treeSeeds: Boolean = true
}
class SieveConfig {
    var enabled: Boolean = true
    @Comment("Do meshes use up durability")
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
    @Comment("Do crooks use durability")
    var durability: Boolean = true
    var vanillaDropAttempts = 3
}
class HammerConfig {
    var enabled: Boolean = true
    @Comment("Do hammers use durability")
    var durability: Boolean = true
}
class WrenchConfig {
    var enabled: Boolean = true
    @Comment("Do wrenches use durability")
    var durability: Boolean = true
}
class WitchWaterConfig {
    var enabled: Boolean = true
    @Comment("What effects should players get on contact")
    var effects = mutableMapOf(
        Identifier("blindness") to Pair<Int,Int>(210,0),
        Identifier("hunger") to Pair<Int,Int>(210,2),
        Identifier("slowness") to Pair<Int,Int>(210,0),
        Identifier("weakness") to Pair<Int,Int>(210,2),
        Identifier("wither") to Pair<Int,Int>(210,0)
    )
}