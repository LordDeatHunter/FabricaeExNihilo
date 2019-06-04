package exnihilofabrico

object ExNihiloConfig {
    object Client {
    }
    object Compatibility {
        object Vanilla {

        }
    }
    object Misc {
        object Lighting {
            var barrelsEmitLight = true
            var barrelsEmitLightTransforming = true
            var cruciblesEmitLight = true
        }
    }
    object Modules {
        object Barrels {
            var enabled = true
            var fillWithRain = true
            var woodenBarrelMaxTemp = 301
            var barrelsCanBeEnchanted = true

            object Alchemy {
                var enabled = true
            }

            object Composting {
                var enabled = true
                var progressPerTick = 0.002
            }

            object Enchantments {
                var efficiency = true
            }
        }
        object Crucibles {
            var enabled = true
            var tickRate = 20

            object Enchantments {
                var efficiency = true
                var flame = true
            }
        }
        object Farming {
            var enabled = true
            object InfestedLeaves {
                var progressPerUpdate = 0.015
                var updateFrequency = 10
                var minimumSpreadPercent = 15
                var spreadChance = 0.5
            }
            object Seeds {
                var carrot = true
                var chorus = true
                var kelp = true
                var potato = true
                var seaPickle = true
                var sugarCane = true
                var treeSeeds = true
            }
        }
        object Sieve {
            var enabled = true
            var fishDropFromWaterLoggedSieves = true
            var meshDurability = false
            var meshStackSize = 16

            object Progress {
                val baseProgress = 0.1
                val efficiencyScaleFactor = 0.05
                val hasteScaleFactor = 1.0
            }

            object Enchantments {
                var efficiency = true
                var fortune = true
                var haste = true
            }
        }
        object Tools {
            object Crooks {
                var enabled = true
                var durability = true
                var vanillaDropAttempts = 3
            }
            object Hammers {
                var enabled = true
                var durability = true
            }
            object Wrenches {
                var enabled = true
                var durability = true
            }
        }
        object WitchWater {
            var enabled = true

            object Debuffs {
                var blindness = true
                var hunger = true
                var poison = true
                var wither = true
            }
        }
    }
}