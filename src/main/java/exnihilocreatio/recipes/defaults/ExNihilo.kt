package exnihilocreatio.recipes.defaults

import exnihilocreatio.ExNihiloCreatio
import exnihilocreatio.ModBlocks
import exnihilocreatio.ModFluids
import exnihilocreatio.ModItems
import exnihilocreatio.blocks.BlockSieve.MeshType
import exnihilocreatio.config.ModConfig
import exnihilocreatio.items.ItemResource
import exnihilocreatio.registries.registries.*
import exnihilocreatio.registries.types.Meltable
import exnihilocreatio.texturing.Color
import exnihilocreatio.util.BlockInfo
import exnihilocreatio.util.ItemInfo
import exnihilocreatio.util.Util
import net.minecraft.block.Block
import net.minecraft.block.BlockLeaves
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.oredict.OreDictionary
import java.util.*

class ExNihilo : IRecipeDefaults {
    override fun getMODID() = ExNihiloCreatio.MODID
    override fun registerCompost(registry: CompostRegistry) {
        val dirtState = BlockInfo(Blocks.DIRT)

        registry.register("treeSapling", 0.125f, dirtState)
        registry.register("treeLeaves", 0.125f, dirtState)
        registry.register("flower", 0.1f, dirtState)
        registry.register("fish", 0.15f, dirtState)
        registry.register("listAllmeatcooked", 0.20f, dirtState)

        registry.register(ItemInfo(Items.ROTTEN_FLESH), 0.1f, dirtState, Color("C45631"))

        registry.register(ItemInfo(Items.SPIDER_EYE), 0.08f, dirtState, Color("963E44"))

        registry.register(ItemInfo(Items.WHEAT), 0.08f, dirtState, Color("E3E162"))
        registry.register(ItemInfo(Items.WHEAT_SEEDS), 0.08f, dirtState, Color("35A82A"))
        registry.register(ItemInfo(Items.BREAD), 0.16f, dirtState, Color("D1AF60"))

        registry.register(BlockInfo(Blocks.BROWN_MUSHROOM), 0.10f, dirtState, Color("CFBFB6"))
        registry.register(BlockInfo(Blocks.RED_MUSHROOM), 0.10f, dirtState, Color("D6A8A5"))

        registry.register(ItemInfo(Items.PUMPKIN_PIE), 0.16f, dirtState, Color("E39A6D"))

        registry.register(ItemInfo(Items.PORKCHOP), 0.2f, dirtState, Color("FFA091"))
        registry.register(ItemInfo(Items.BEEF), 0.2f, dirtState, Color("FF4242"))
        registry.register(ItemInfo(Items.CHICKEN), 0.2f, dirtState, Color("FFE8E8"))

        registry.register(ItemInfo(ModItems.resources, ItemResource.getMetaFromName(ItemResource.SILKWORM)), 0.04f, dirtState, Color("ff9966"))
        registry.register(ItemInfo(ModItems.cookedSilkworm), 0.04f, dirtState, Color("cc6600"))

        registry.register(ItemInfo(Items.APPLE), 0.10f, dirtState, Color("FFF68F"))
        registry.register(ItemInfo(Items.MELON), 0.04f, dirtState, Color("FF443B"))
        registry.register(BlockInfo(Blocks.MELON_BLOCK), 1.0f / 6, dirtState, Color("FF443B"))
        registry.register(BlockInfo(Blocks.PUMPKIN), 1.0f / 6, dirtState, Color("FFDB66"))
        registry.register(BlockInfo(Blocks.LIT_PUMPKIN), 1.0f / 6, dirtState, Color("FFDB66"))

        registry.register(BlockInfo(Blocks.CACTUS), 0.10f, dirtState, Color("DEFFB5"))

        registry.register(ItemInfo(Items.CARROT), 0.08f, dirtState, Color("FF9B0F"))
        registry.register(ItemInfo(Items.POTATO), 0.08f, dirtState, Color("FFF1B5"))
        registry.register(ItemInfo(Items.BAKED_POTATO), 0.08f, dirtState, Color("FFF1B5"))
        registry.register(ItemInfo(Items.POISONOUS_POTATO), 0.08f, dirtState, Color("E0FF8A"))

        registry.register(BlockInfo(Blocks.WATERLILY.defaultState), 0.10f, dirtState, Color("269900"))
        registry.register(BlockInfo(Blocks.VINE.defaultState), 0.10f, dirtState, Color("23630E"))
        registry.register(BlockInfo(Blocks.TALLGRASS, 1), 0.08f, dirtState, Color("23630E"))
        registry.register(ItemInfo(Items.EGG), 0.08f, dirtState, Color("FFFA66"))
        registry.register(ItemInfo(Items.NETHER_WART), 0.10f, dirtState, Color("FF2B52"))
        registry.register(ItemInfo(Items.REEDS), 0.08f, dirtState, Color("9BFF8A"))
        registry.register(ItemInfo(Items.STRING), 0.04f, dirtState, Util.whiteColor)

        //Register any missed items
        registry.register("listAllfruit", 0.10f, dirtState, Color("35A82A"))
        registry.register("listAllveggie", 0.10f, dirtState, Color("FFF1B5"))
        registry.register("listAllGrain", 0.08f, dirtState, Color("E3E162"))
        registry.register("listAllseed", 0.08f, dirtState, Color("35A82A"))
        registry.register("listAllmeatraw", 0.15f, dirtState, Color("FFA091"))

        // Misc. Modded Items
        registry.register("dustAsh", 0.125f, dirtState, Color("C0C0C0"))
    }

    override fun registerCrook(registry: CrookRegistry) {
        registry.register("treeLeaves", ItemResource.getResourceStack(ItemResource.SILKWORM), 0.1f, 0f)
    }

    override fun registerSieve(registry: SieveRegistry) {
        //Stone Pebble
        registry.register("dirt", ItemInfo(ModItems.pebbles), getDropChance(1f), MeshType.STRING.id)
        registry.register("dirt", ItemInfo(ModItems.pebbles), getDropChance(1f), MeshType.STRING.id)
        registry.register("dirt", ItemInfo(ModItems.pebbles), getDropChance(0.5f), MeshType.STRING.id)
        registry.register("dirt", ItemInfo(ModItems.pebbles), getDropChance(0.5f), MeshType.STRING.id)
        registry.register("dirt", ItemInfo(ModItems.pebbles), getDropChance(0.1f), MeshType.STRING.id)
        registry.register("dirt", ItemInfo(ModItems.pebbles), getDropChance(0.1f), MeshType.STRING.id)

        //Granite Pebble
        registry.register("dirt", ItemInfo(ModItems.pebbles, 1), getDropChance(0.5f), MeshType.STRING.id)
        registry.register("dirt", ItemInfo(ModItems.pebbles, 1), getDropChance(0.1f), MeshType.STRING.id)

        //Diorite Pebble
        registry.register("dirt", ItemInfo(ModItems.pebbles, 2), getDropChance(0.5f), MeshType.STRING.id)
        registry.register("dirt", ItemInfo(ModItems.pebbles, 2), getDropChance(0.1f), MeshType.STRING.id)

        //Andesite Pebble
        registry.register("dirt", ItemInfo(ModItems.pebbles, 3), getDropChance(0.5f), MeshType.STRING.id)
        registry.register("dirt", ItemInfo(ModItems.pebbles, 3), getDropChance(0.1f), MeshType.STRING.id)

        registry.register("dirt", ItemInfo(Items.WHEAT_SEEDS), getDropChance(0.7f), MeshType.STRING.id)
        registry.register("dirt", ItemInfo(Items.MELON_SEEDS), getDropChance(0.35f), MeshType.STRING.id)
        registry.register("dirt", ItemInfo(Items.PUMPKIN_SEEDS), getDropChance(0.35f), MeshType.STRING.id)

        //Ancient Spores
        registry.register("dirt", ItemInfo(ModItems.resources, 3), getDropChance(0.05f), MeshType.STRING.id)
        //Grass Seeds
        registry.register("dirt", ItemInfo(ModItems.resources, 4), getDropChance(0.05f), MeshType.STRING.id)


        registry.register("sand", ItemInfo(Items.DYE, 3), getDropChance(0.03f), MeshType.STRING.id)
        registry.register("sand", ItemInfo(Items.PRISMARINE_SHARD), getDropChance(0.02f), MeshType.DIAMOND.id)

        registry.register("gravel", ItemInfo(Items.FLINT), getDropChance(0.25f), MeshType.FLINT.id)
        registry.register("gravel", ItemInfo(Items.COAL), getDropChance(0.125f), MeshType.FLINT.id)
        registry.register("gravel", ItemInfo(Items.DYE, 4), getDropChance(0.05f), MeshType.FLINT.id)

        registry.register("gravel", ItemInfo(Items.DIAMOND), getDropChance(0.008f), MeshType.IRON.id)
        registry.register("gravel", ItemInfo(Items.EMERALD), getDropChance(0.008f), MeshType.IRON.id)

        registry.register("gravel", ItemInfo(Items.DIAMOND), getDropChance(0.016f), MeshType.DIAMOND.id)
        registry.register("gravel", ItemInfo(Items.EMERALD), getDropChance(0.016f), MeshType.DIAMOND.id)


        registry.register(BlockInfo(Blocks.SOUL_SAND), ItemInfo(Items.QUARTZ), getDropChance(1f), MeshType.FLINT.id)
        registry.register(BlockInfo(Blocks.SOUL_SAND), ItemInfo(Items.QUARTZ), getDropChance(0.33f), MeshType.FLINT.id)

        registry.register(BlockInfo(Blocks.SOUL_SAND), ItemInfo(Items.NETHER_WART), getDropChance(0.1f), MeshType.STRING.id)

        registry.register(BlockInfo(Blocks.SOUL_SAND), ItemInfo(Items.GHAST_TEAR), getDropChance(0.02f), MeshType.DIAMOND.id)
        registry.register(BlockInfo(Blocks.SOUL_SAND), ItemInfo(Items.QUARTZ), getDropChance(1f), MeshType.DIAMOND.id)
        registry.register(BlockInfo(Blocks.SOUL_SAND), ItemInfo(Items.QUARTZ), getDropChance(0.8f), MeshType.DIAMOND.id)

        registry.register("dust", ItemInfo(Items.DYE, 15), getDropChance(0.2f), MeshType.STRING.id)
        registry.register("dust", ItemInfo(Items.GUNPOWDER), getDropChance(0.07f), MeshType.STRING.id)

        registry.register("dust", ItemInfo(Items.REDSTONE), getDropChance(0.125f), MeshType.IRON.id)
        registry.register("dust", ItemInfo(Items.REDSTONE), getDropChance(0.25f), MeshType.DIAMOND.id)

        registry.register("dust", ItemInfo(Items.GLOWSTONE_DUST), getDropChance(0.0625f), MeshType.IRON.id)
        registry.register("dust", ItemInfo(Items.BLAZE_POWDER), getDropChance(0.05f), MeshType.IRON.id)

        // Custom Ores for other mods
        val oreRegistry = exnihilocreatio.api.ExNihiloCreatioAPI.ORE_REGISTRY

        // Gold from nether rack
        val gold = oreRegistry.getOreItem("gold")
        if (gold != null) {
            registry.register(BlockInfo(ModBlocks.netherrackCrushed), ItemInfo(gold, 0), getDropChance(0.25f), MeshType.FLINT.id)
            registry.register(BlockInfo(ModBlocks.netherrackCrushed), ItemInfo(gold, 0), getDropChance(0.25f), MeshType.IRON.id)
            registry.register(BlockInfo(ModBlocks.netherrackCrushed), ItemInfo(gold, 0), getDropChance(0.4f), MeshType.DIAMOND.id)
        }

        // All default Ores
        for (ore in oreRegistry.itemOreRegistry) {
            if (oreRegistry.sieveBlackList.contains(ore)) continue
            when {
                ore.ore.name === "iron" -> {
                    registry.register("gravel", ItemInfo(ore), getDropChance(0.1f), MeshType.FLINT.id)
                    registry.register("gravel", ItemInfo(ore), getDropChance(0.15f), MeshType.IRON.id)
                    registry.register("gravel", ItemInfo(ore), getDropChance(0.25f), MeshType.DIAMOND.id)
                    registry.register(BlockInfo(Blocks.SAND, 1), ItemInfo(ore), getDropChance(0.4f), MeshType.DIAMOND.id)
                }
                ore.ore.name === "uranium" -> {
                    registry.register("gravel", ItemInfo(ore), getDropChance(0.008f), MeshType.FLINT.id)
                    registry.register("gravel", ItemInfo(ore), getDropChance(0.009f), MeshType.IRON.id)
                    registry.register("gravel", ItemInfo(ore), getDropChance(0.01f), MeshType.DIAMOND.id)
                }
                else -> {
                    registry.register("gravel", ItemInfo(ore), getDropChance(0.05f), MeshType.FLINT.id)
                    registry.register("gravel", ItemInfo(ore), getDropChance(0.075f), MeshType.IRON.id)
                    registry.register("gravel", ItemInfo(ore), getDropChance(0.15f), MeshType.DIAMOND.id)
                }
            }
        }
        // Seeds
        for (seed in ModItems.itemSeeds) {
            registry.register("dirt", ItemInfo(seed), getDropChance(0.05f), MeshType.STRING.id)
        }

        leavesSapling.forEach { leaves, sapling ->
            val blockLeaves = Block.getBlockFromItem(leaves.itemStack.item) as BlockLeaves
            val chance = blockLeaves.getSaplingDropChance(blockLeaves.defaultState) / 100f

            registry.register(leaves, sapling, Math.min(chance * 1, 1.0f), MeshType.STRING.id)
            registry.register(leaves, sapling, Math.min(chance * 2, 1.0f), MeshType.FLINT.id)
            registry.register(leaves, sapling, Math.min(chance * 3, 1.0f), MeshType.IRON.id)
            registry.register(leaves, sapling, Math.min(chance * 4, 1.0f), MeshType.DIAMOND.id)

            //Apple
            registry.register(leaves, ItemInfo(Items.APPLE), 0.05f, MeshType.STRING.id)
            registry.register(leaves, ItemInfo(Items.APPLE), 0.10f, MeshType.FLINT.id)
            registry.register(leaves, ItemInfo(Items.APPLE), 0.15f, MeshType.IRON.id)
            registry.register(leaves, ItemInfo(Items.APPLE), 0.20f, MeshType.DIAMOND.id)

            //Golden Apple
            registry.register(leaves, ItemInfo(Items.GOLDEN_APPLE), 0.001f, MeshType.STRING.id)
            registry.register(leaves, ItemInfo(Items.GOLDEN_APPLE), 0.003f, MeshType.FLINT.id)
            registry.register(leaves, ItemInfo(Items.GOLDEN_APPLE), 0.005f, MeshType.IRON.id)
            registry.register(leaves, ItemInfo(Items.GOLDEN_APPLE), 0.01f, MeshType.DIAMOND.id)

            //Silk Worm
            registry.register(leaves, ItemInfo(ItemResource.getResourceStack(ItemResource.SILKWORM)), 0.025f, MeshType.STRING.id)
            registry.register(leaves, ItemInfo(ItemResource.getResourceStack(ItemResource.SILKWORM)), 0.05f, MeshType.FLINT.id)
            registry.register(leaves, ItemInfo(ItemResource.getResourceStack(ItemResource.SILKWORM)), 0.1f, MeshType.IRON.id)
            registry.register(leaves, ItemInfo(ItemResource.getResourceStack(ItemResource.SILKWORM)), 0.2f, MeshType.DIAMOND.id)
        }
    }

    override fun registerHammer(registry: HammerRegistry) {
        registry.register("cobblestone", ItemStack(Blocks.GRAVEL, 1), 0, 1.0f, 0.0f)
        registry.register("gravel", ItemStack(Blocks.SAND, 1), 0, 1.0f, 0.0f)
        registry.register("sand", ItemStack(ModBlocks.dust, 1), 0, 1.0f, 0.0f)
        registry.register("netherrack", ItemStack(ModBlocks.netherrackCrushed, 1), 0, 1.0f, 0.0f)
        registry.register("endstone", ItemStack(ModBlocks.endstoneCrushed, 1), 0, 1.0f, 0.0f)

        registry.register("stoneAndesite", ItemStack(ModBlocks.crushedAndesite), 0, 1f, 0f)
        registry.register("stoneGranite", ItemStack(ModBlocks.crushedGranite), 0, 1f, 0f)
        registry.register("stoneDiorite", ItemStack(ModBlocks.crushedDiorite), 0, 1f, 0f)

        registry.register("crushedGranite", ItemStack(Blocks.SAND, 1, 1), 0, 1.0f, 0.0f)


        // Hammer concrete into concrete powder
        for (meta in 0..15)
            registry.register(BlockInfo.getStateFromMeta(Blocks.CONCRETE, meta), ItemStack(Blocks.CONCRETE_POWDER, 1, meta), 1, 1.0f, 0.0f)

    }

    override fun registerHeat(registry: HeatRegistry) {
        // Vanilla fluids are weird, the "flowing" variant is simply a temporary state of checking if it can flow.
        // So, once the lava has spread out all the way, it will all actually be "still" lava.
        // Thanks Mojang <3
        registry.register(BlockInfo(Blocks.FLOWING_LAVA), 3)
        registry.register(BlockInfo(Blocks.LAVA), 3)
        registry.register(BlockInfo(Blocks.FIRE), 4)
        registry.register(BlockInfo(Blocks.TORCH), 1)
    }

    override fun registerBarrelLiquidBlacklist(registry: BarrelLiquidBlacklistRegistry) {
        registry.register(ModBlocks.barrelWood.tier, "lava")
        registry.register(ModBlocks.barrelWood.tier, "fire_water")
        registry.register(ModBlocks.barrelWood.tier, "rocket_fuel")
        registry.register(ModBlocks.barrelWood.tier, "pyrotheum")
    }

    override fun registerFluidOnTop(registry: FluidOnTopRegistry) {
        registry.register(FluidRegistry.LAVA, FluidRegistry.WATER, BlockInfo(Blocks.OBSIDIAN.defaultState))
        registry.register(FluidRegistry.WATER, FluidRegistry.LAVA, BlockInfo(Blocks.COBBLESTONE.defaultState))
    }

    override fun registerOreChunks(registry: OreRegistry) {
        registry.register("gold", Color("FFFF00"), ItemInfo(Items.GOLD_INGOT, 0))
        registry.register("iron", Color("BF8040"), ItemInfo(Items.IRON_INGOT, 0))

        //TODO: Better way, will most likely never grab as it is just called before many mods init their oredict
        if (!OreDictionary.getOres("oreCopper").isEmpty()) {
            registry.register("copper", Color("FF9933"), null)
        }

        if (!OreDictionary.getOres("oreTin").isEmpty()) {
            registry.register("tin", Color("E6FFF2"), null)
        }

        if (!OreDictionary.getOres("oreAluminium").isEmpty() || !OreDictionary.getOres("oreAluminum").isEmpty()) {
            registry.register("aluminium", Color("BFBFBF"), null)
        }

        if (!OreDictionary.getOres("oreLead").isEmpty()) {
            registry.register("lead", Color("330066"), null)
        }

        if (!OreDictionary.getOres("oreSilver").isEmpty()) {
            registry.register("silver", Color("F2F2F2"), null)
        }

        if (!OreDictionary.getOres("oreNickel").isEmpty()) {
            registry.register("nickel", Color("FFFFCC"), null)
        }

        if (!OreDictionary.getOres("oreUranium").isEmpty()) {
            registry.register("uranium", Color("4E5B43"), null)
        }
    }

    override fun registerFluidTransform(registry: FluidTransformRegistry) {
        registry.register("water", "witchwater", 12000, arrayOf(BlockInfo(Blocks.MYCELIUM.defaultState)), arrayOf(BlockInfo(Blocks.BROWN_MUSHROOM.defaultState), BlockInfo(Blocks.RED_MUSHROOM.defaultState)))
    }

    override fun registerFluidBlockTransform(registry: FluidBlockTransformerRegistry) {
        registry.register(FluidRegistry.WATER, "dust", ItemInfo(ItemStack(Blocks.CLAY)))
        registry.register(FluidRegistry.LAVA, "dustRedstone", ItemInfo(ItemStack(Blocks.NETHERRACK)))
        registry.register(FluidRegistry.LAVA, "dustGlowstone", ItemInfo(ItemStack(Blocks.END_STONE)))
        registry.register(ModFluids.fluidWitchwater, "sand", ItemInfo(ItemStack(Blocks.SOUL_SAND)))

        if (FluidRegistry.isFluidRegistered("milk")) {
            registry.register(FluidRegistry.getFluid("milk"), ItemInfo(ItemStack(Blocks.BROWN_MUSHROOM)), ItemInfo(ItemStack(Blocks.SLIME_BLOCK)), "Slime")
            registry.register(FluidRegistry.getFluid("milk"), ItemInfo(ItemStack(Blocks.RED_MUSHROOM)), ItemInfo(ItemStack(Blocks.SLIME_BLOCK)), "Slime")
        }

        // Vanilla Concrete
        for (meta in 0..15)
            registry.register(FluidRegistry.WATER, ItemInfo(ItemStack(Blocks.CONCRETE_POWDER, 1, meta)), ItemInfo(ItemStack(Blocks.CONCRETE, 1, meta)))
    }

    override fun registerFluidItemFluid(registry: FluidItemFluidRegistry) {
        registry.register(FluidRegistry.WATER, ItemInfo(ItemResource.getResourceStack(ItemResource.ANCIENT_SPORES)), ModFluids.fluidWitchwater)
    }

    override fun registerCrucibleStone(registry: CrucibleRegistry) {
        registry.register("cobblestone", FluidRegistry.LAVA, 250)
        registry.register("stone", FluidRegistry.LAVA, 250)
        registry.register("gravel", FluidRegistry.LAVA, 200)
        registry.register("sand", FluidRegistry.LAVA, 100)
        registry.register(BlockInfo(ModBlocks.dust.defaultState), FluidRegistry.LAVA, 50)

        // 1:1 Back to lava
        registry.register("netherrack", FluidRegistry.LAVA, 1000)
        registry.register("obsidian", FluidRegistry.LAVA, 1000)
    }

    override fun registerCrucibleWood(registry: CrucibleRegistry) {
        val water = Meltable(FluidRegistry.WATER.name, 250, BlockInfo(Blocks.LEAVES, 0))
        registry.register("treeLeaves", FluidRegistry.WATER, 250)
        registry.register("treeSapling", water)
        registry.register("listAllfruit", water)
        registry.register("listAllveggie", water)

        registry.register(ItemInfo(Blocks.SAPLING, 0), water)
        registry.register(ItemInfo(Blocks.SAPLING, 1), water.copy().setTextureOverrideChain(BlockInfo(Blocks.LEAVES, 1)))
        registry.register(ItemInfo(Blocks.SAPLING, 2), water.copy().setTextureOverrideChain(BlockInfo(Blocks.LEAVES, 2)))
        registry.register(ItemInfo(Blocks.SAPLING, 3), water.copy().setTextureOverrideChain(BlockInfo(Blocks.LEAVES, 3)))
        registry.register(ItemInfo(Blocks.SAPLING, 4), water.copy().setTextureOverrideChain(BlockInfo(Blocks.LEAVES2, 0)))
        registry.register(ItemInfo(Blocks.SAPLING, 5), water.copy().setTextureOverrideChain(BlockInfo(Blocks.LEAVES2, 1)))
        registry.register(ItemInfo(Items.APPLE), water)
    }

    override fun registerMilk(registry: MilkEntityRegistry) {
        registry.register("Cow", "milk", 10, 20)
    }

    private fun getDropChance(chance: Float): Float {
        return if (ModConfig.world.isSkyWorld)
            chance
        else
            chance / 100f * ModConfig.world.normalDropPercent.toFloat()
    }

    companion object {

        val leavesSapling: Map<BlockInfo, BlockInfo>
            get() {
                val saplingMap = LinkedHashMap<BlockInfo, BlockInfo>()
                saplingMap[BlockInfo(Blocks.LEAVES, 0)] = BlockInfo(Blocks.SAPLING, 0)
                saplingMap[BlockInfo(Blocks.LEAVES, 1)] = BlockInfo(Blocks.SAPLING, 1)
                saplingMap[BlockInfo(Blocks.LEAVES, 2)] = BlockInfo(Blocks.SAPLING, 2)
                saplingMap[BlockInfo(Blocks.LEAVES, 3)] = BlockInfo(Blocks.SAPLING, 3)
                saplingMap[BlockInfo(Blocks.LEAVES2, 0)] = BlockInfo(Blocks.SAPLING, 4)
                saplingMap[BlockInfo(Blocks.LEAVES2, 1)] = BlockInfo(Blocks.SAPLING, 5)

                return saplingMap
            }
    }
}
