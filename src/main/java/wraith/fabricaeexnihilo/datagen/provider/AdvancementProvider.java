package wraith.fabricaeexnihilo.datagen.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.ItemDurabilityChangedCriterion;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.ModItems;
import wraith.fabricaeexnihilo.modules.ModTags;
import wraith.fabricaeexnihilo.modules.ModTools;
import wraith.fabricaeexnihilo.modules.fluids.BloodFluid;
import wraith.fabricaeexnihilo.modules.fluids.BrineFluid;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterFluid;

import java.util.function.Consumer;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class AdvancementProvider extends FabricAdvancementProvider {

    public AdvancementProvider(FabricDataOutput generator) {
        super(generator);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> exporter) {
        var root = Advancement.Builder.create()
            .display(ModBlocks.SIEVES.get(id("oak_sieve")),
                Text.translatable("advancements.fabricaeexnihilo.root.title"),
                Text.translatable("advancements.fabricaeexnihilo.root.description"),
                new Identifier("textures/gui/advancements/backgrounds/stone.png"),
                AdvancementFrame.TASK,
                true, true, false)
            .criterion("crafting_table", InventoryChangedCriterion.Conditions.items(Items.CRAFTING_TABLE))
            .build(exporter, "fabricaeexnihilo:root");

        // Barrels, clay and blood
        var barrel = task(ModBlocks.BARRELS.get(id("oak_barrel")),
            "barrel",
            root,
            "barrel",
            InventoryChangedCriterion.Conditions.items(ItemPredicate.Builder.create().tag(ModTags.BARRELS).build()),
            exporter);
        goal(BloodFluid.BUCKET,
            "blood",
            barrel,
            "blood",
            InventoryChangedCriterion.Conditions.items(BloodFluid.BUCKET),
            exporter);
        var clay = task(Items.CLAY,
            "clay",
            barrel,
            "clay",
            InventoryChangedCriterion.Conditions.items(Items.CLAY),
            exporter);
        var porcelain = task(ModItems.PORCELAIN,
            "porcelain",
            clay,
            "porcelain",
            InventoryChangedCriterion.Conditions.items(ModItems.PORCELAIN),
            exporter);
        task(ModBlocks.CRUCIBLES.get(id("porcelain_crucible")),
            "crucible",
            porcelain,
            "porcelain_crucible",
            InventoryChangedCriterion.Conditions.items(ModBlocks.CRUCIBLES.get(id("porcelain_crucible"))),
            exporter);
        task(ModTools.CROOKS.get(id("clay_crook")),
            "clay_crook_broken",
            clay,
            "clay_crook_broken",
            ItemDurabilityChangedCriterion.Conditions.create(ItemPredicate.Builder.create().items(ModTools.CROOKS.get(id("clay_crook"))).build(), NumberRange.IntRange.ANY),
            exporter);

        var crook = task(ModTools.CROOKS.get(id("wooden_crook")),
            "crook",
            root,
            "crook",
            InventoryChangedCriterion.Conditions.items(ModTools.CROOKS.get(id("wooden_crook"))),
            exporter);
        var silkworm = task(ModItems.RAW_SILKWORM,
            "silkworm",
            crook,
            "silkworm",
            InventoryChangedCriterion.Conditions.items(ModItems.RAW_SILKWORM),
            exporter);

        // Meshes
        var stringMesh = task(ModItems.MESHES.get(id("string_mesh")),
            "string_mesh",
            silkworm,
            "mesh",
            InventoryChangedCriterion.Conditions.items(ModItems.MESHES.get(id("string_mesh"))),
            exporter);
        var flintMesh = task(ModItems.MESHES.get(id("flint_mesh")),
            "flint_mesh",
            stringMesh,
            "mesh",
            InventoryChangedCriterion.Conditions.items(ModItems.MESHES.get(id("flint_mesh"))),
            exporter);
        var ironMesh = task(ModItems.MESHES.get(id("iron_mesh")),
            "iron_mesh",
            flintMesh,
            "mesh",
            InventoryChangedCriterion.Conditions.items(ModItems.MESHES.get(id("iron_mesh"))),
            exporter);
        var diamondMesh = task(ModItems.MESHES.get(id("diamond_mesh")),
            "diamond_mesh",
            ironMesh,
            "mesh",
            InventoryChangedCriterion.Conditions.items(ModItems.MESHES.get(id("diamond_mesh"))),
            exporter);
        task(ModItems.MESHES.get(id("netherite_mesh")),
            "netherite_mesh",
            diamondMesh,
            "mesh",
            InventoryChangedCriterion.Conditions.items(ModItems.MESHES.get(id("netherite_mesh"))),
            exporter);
        var copperMesh = task(ModItems.MESHES.get(id("copper_mesh")),
            "copper_mesh",
            flintMesh,
            "mesh",
            InventoryChangedCriterion.Conditions.items(ModItems.MESHES.get(id("copper_mesh"))),
            exporter);
        var goldMesh = task(ModItems.MESHES.get(id("gold_mesh")),
            "gold_mesh",
            copperMesh,
            "mesh",
            InventoryChangedCriterion.Conditions.items(ModItems.MESHES.get(id("gold_mesh"))),
            exporter);
        task(ModItems.MESHES.get(id("emerald_mesh")),
            "emerald_mesh",
            goldMesh,
            "mesh",
            InventoryChangedCriterion.Conditions.items(ModItems.MESHES.get(id("emerald_mesh"))),
            exporter);

        // Witchwater
        var spores = task(ModItems.SEEDS.get(id("mycelium_seeds")),
            "ancient_spores",
            stringMesh,
            "ancient_spores",
            InventoryChangedCriterion.Conditions.items(ModItems.SEEDS.get(id("mycelium_seeds"))),
            exporter);
        goal(WitchWaterFluid.BUCKET,
            "witchwater",
            spores,
            "witchwater",
            InventoryChangedCriterion.Conditions.items(WitchWaterFluid.BUCKET),
            exporter);

        // Rocks n' hammers
        var pebbles = task(ModItems.PEBBLES.get(id("stone_pebble")),
            "pebbles",
            stringMesh,
            "pebble",
            InventoryChangedCriterion.Conditions.items(ModItems.PEBBLES.get(id("stone_pebble"))),
            exporter);
        var cobblestone = task(Items.COBBLESTONE,
            "cobblestone",
            pebbles,
            "cobblestone",
            InventoryChangedCriterion.Conditions.items(Items.COBBLESTONE),
            exporter);
        var woodenHammer = task(ModTools.HAMMERS.get(id("wooden_hammer")),
            "wooden_hammer",
            cobblestone,
            "hammer",
            InventoryChangedCriterion.Conditions.items(ModTools.HAMMERS.get(id("wooden_hammer"))),
            exporter);
        task(ModTools.HAMMERS.get(id("golden_hammer")),
            "golden_hammer",
            woodenHammer,
            "hammer",
            InventoryChangedCriterion.Conditions.items(ModTools.HAMMERS.get(id("golden_hammer"))),
            exporter);
        var stoneHammer = task(ModTools.HAMMERS.get(id("stone_hammer")),
            "stone_hammer",
            woodenHammer,
            "hammer",
            InventoryChangedCriterion.Conditions.items(ModTools.HAMMERS.get(id("stone_hammer"))),
            exporter);
        var ironHammer = task(ModTools.HAMMERS.get(id("iron_hammer")),
            "iron_hammer",
            stoneHammer,
            "hammer",
            InventoryChangedCriterion.Conditions.items(ModTools.HAMMERS.get(id("iron_hammer"))),
            exporter);
        var diamondHammer = task(ModTools.HAMMERS.get(id("diamond_hammer")),
            "diamond_hammer",
            ironHammer,
            "hammer",
            InventoryChangedCriterion.Conditions.items(ModTools.HAMMERS.get(id("diamond_hammer"))),
            exporter);
        task(ModTools.HAMMERS.get(id("netherite_hammer")),
            "netherite_hammer",
            diamondHammer,
            "hammer",
            InventoryChangedCriterion.Conditions.items(ModTools.HAMMERS.get(id("netherite_hammer"))),
            exporter);
        var gravel = task(Items.GRAVEL,
            "gravel",
            woodenHammer,
            "gravel",
            InventoryChangedCriterion.Conditions.items(Items.GRAVEL),
            exporter);
        var sand = task(Items.SAND,
            "sand",
            gravel,
            "sand",
            InventoryChangedCriterion.Conditions.items(Items.SAND),
            exporter);
        var silt = task(ModBlocks.CRUSHED.get(id("silt")),
            "silt",
            sand,
            "silt",
            InventoryChangedCriterion.Conditions.items(ModBlocks.CRUSHED.get(id("silt"))),
            exporter);
        task(ModBlocks.CRUSHED.get(id("dust")),
            "dust",
            silt,
            "dust",
            InventoryChangedCriterion.Conditions.items(ModBlocks.CRUSHED.get(id("dust"))),
            exporter);

        // Salt
        var salt = task(ModItems.SALT_BOTTLE,
            "salt",
            sand,
            "salt",
            InventoryChangedCriterion.Conditions.items(ModItems.SALT_BOTTLE),
            exporter);
        goal(BrineFluid.BUCKET,
            "brine",
            salt,
            "brine",
            InventoryChangedCriterion.Conditions.items(BrineFluid.BUCKET),
            exporter);
    }

    private Advancement task(ItemConvertible icon, String name, Advancement parent, String criterionName, CriterionConditions criterionConditions, Consumer<Advancement> exporter) {
        return Advancement.Builder.create()
            .display(icon,
                Text.translatable("advancements.fabricaeexnihilo." + name + ".title"),
                Text.translatable("advancements.fabricaeexnihilo." + name + ".description"),
                null,
                AdvancementFrame.TASK,
                true, true, false)
            .parent(parent)
            .criterion(criterionName, criterionConditions)
            .build(exporter, "fabricaeexnihilo:" + name);
    }

    private Advancement goal(ItemConvertible icon, String name, Advancement parent, String criterionName, CriterionConditions criterionConditions, Consumer<Advancement> exporter) {
        return Advancement.Builder.create()
            .display(icon,
                Text.translatable("advancements.fabricaeexnihilo." + name + ".title"),
                Text.translatable("advancements.fabricaeexnihilo." + name + ".description"),
                null,
                AdvancementFrame.GOAL,
                true, true, false)
            .parent(parent)
            .criterion(criterionName, criterionConditions)
            .build(exporter, "fabricaeexnihilo:" + name);
    }
}
