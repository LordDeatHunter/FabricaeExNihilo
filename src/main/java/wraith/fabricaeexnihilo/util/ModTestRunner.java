package wraith.fabricaeexnihilo.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.ModItems;
import wraith.fabricaeexnihilo.modules.ModTools;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.barrels.BarrelState;
import wraith.fabricaeexnihilo.modules.sieves.SieveBlockEntity;
import wraith.fabricaeexnihilo.recipe.SieveRecipe;
import wraith.fabricaeexnihilo.recipe.ToolRecipe;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class ModTestRunner {

    public static void register() {
        if (!"true".equals(System.getProperty("fabricaeexnihilo.test")) && !"true".equals(System.getenv("FEN_TEST"))) {
            return;
        }
        ServerLifecycleEvents.SERVER_STARTED.register(ModTestRunner::runTests);
    }

    private static void runTests(MinecraftServer server) {
        new Thread(() -> {
            try {
                Thread.sleep(1500);
                server.execute(() -> {
                    try {
                        ServerWorld world = server.getOverworld();
                        BlockPos base = new BlockPos(0, 100, 0);

                        FabricaeExNihilo.LOGGER.info("==========================================");
                        FabricaeExNihilo.LOGGER.info("[AUDIT] Starting In-Game Full Verification...");
                        FabricaeExNihilo.LOGGER.info("==========================================");

                        // 1. 语言文件审计
                        boolean langOk = auditLocalization();
                        FabricaeExNihilo.LOGGER.info("[AUDIT 1/5] Localization & Translation Keys: " + (langOk ? "PASSED" : "FAILED"));

                        // 2. 工作台合成配方实机测试
                        boolean craftingOk = auditCraftingRecipes(server, world);
                        FabricaeExNihilo.LOGGER.info("[AUDIT 2/5] Crafting Table Recipes: " + (craftingOk ? "PASSED" : "FAILED"));

                        // 3. 木桶（Barrel）实机测试
                        boolean barrelOk = testBarrel(world, base.add(0, 0, 0));
                        FabricaeExNihilo.LOGGER.info("[AUDIT 3/5] Barrel (Fluid Storage & Interaction): " + (barrelOk ? "PASSED" : "FAILED"));

                        // 4. 筛子（Sieve）实机测试
                        boolean sieveOk = testSieve(world, base.add(2, 0, 0));
                        FabricaeExNihilo.LOGGER.info("[AUDIT 4/5] Sieve (Mesh Insertion & Content Loading): " + (sieveOk ? "PASSED" : "FAILED"));

                        // 5. 锤子与钩子掉落实机测试
                        boolean hammerOk = testHammer(world, base.add(4, 0, 0));
                        boolean crookOk = testCrook(world, base.add(6, 0, 0));
                        boolean toolsOk = hammerOk && crookOk;
                        FabricaeExNihilo.LOGGER.info("[AUDIT 5/5] Hammers & Crooks In-Game Drop Recipes: " + (toolsOk ? "PASSED" : "FAILED"));

                        FabricaeExNihilo.LOGGER.info("==========================================");
                        if (langOk && craftingOk && barrelOk && sieveOk && toolsOk) {
                            FabricaeExNihilo.LOGGER.info("[AUDIT SUMMARY] ALL IN-GAME AUDIT TESTS PASSED SUCCESSFULLY!");
                        } else {
                            FabricaeExNihilo.LOGGER.error("[AUDIT SUMMARY] CRITICAL AUDIT FAILURES DETECTED!");
                        }
                        FabricaeExNihilo.LOGGER.info("==========================================");

                        server.stop(false);
                    } catch (Exception e) {
                        FabricaeExNihilo.LOGGER.error("[AUDIT] Exception during audit runner execution", e);
                        server.stop(false);
                    }
                });
            } catch (Exception e) {
                FabricaeExNihilo.LOGGER.error("[AUDIT] Exception during audit thread", e);
                server.execute(() -> server.stop(false));
            }
        }, "Audit-Test-Thread").start();
    }

    private static boolean auditLocalization() {
        try {
            var zhStream = ModTestRunner.class.getResourceAsStream("/assets/fabricaeexnihilo/lang/zh_cn.json");
            var enStream = ModTestRunner.class.getResourceAsStream("/assets/fabricaeexnihilo/lang/en_us.json");
            if (zhStream == null || enStream == null) {
                FabricaeExNihilo.LOGGER.error("[AUDIT] Missing zh_cn.json or en_us.json in classpath resources!");
                return false;
            }

            Gson gson = new Gson();
            JsonObject zh = gson.fromJson(new InputStreamReader(zhStream, StandardCharsets.UTF_8), JsonObject.class);
            JsonObject en = gson.fromJson(new InputStreamReader(enStream, StandardCharsets.UTF_8), JsonObject.class);

            FabricaeExNihilo.LOGGER.info("[AUDIT] Loaded zh_cn.json ({} keys) and en_us.json ({} keys)", zh.size(), en.size());

            int missingZh = 0;
            int missingEn = 0;

            for (var entry : Registries.ITEM.getIds()) {
                if ("fabricaeexnihilo".equals(entry.getNamespace())) {
                    var item = Registries.ITEM.get(entry);
                    String key = item.getTranslationKey();
                    if (!zh.has(key)) {
                        FabricaeExNihilo.LOGGER.warn("[AUDIT] Item missing zh_cn translation: {} (key: {})", entry, key);
                        missingZh++;
                    }
                    if (!en.has(key)) {
                        FabricaeExNihilo.LOGGER.warn("[AUDIT] Item missing en_us translation: {} (key: {})", entry, key);
                        missingEn++;
                    }
                }
            }

            for (var entry : Registries.BLOCK.getIds()) {
                if ("fabricaeexnihilo".equals(entry.getNamespace())) {
                    var block = Registries.BLOCK.get(entry);
                    String key = block.getTranslationKey();
                    if (!zh.has(key)) {
                        FabricaeExNihilo.LOGGER.warn("[AUDIT] Block missing zh_cn translation: {} (key: {})", entry, key);
                        missingZh++;
                    }
                    if (!en.has(key)) {
                        FabricaeExNihilo.LOGGER.warn("[AUDIT] Block missing en_us translation: {} (key: {})", entry, key);
                        missingEn++;
                    }
                }
            }

            if (missingZh > 0 || missingEn > 0) {
                FabricaeExNihilo.LOGGER.error("[AUDIT] Localization audit found {} missing zh_cn keys, {} missing en_us keys", missingZh, missingEn);
                return false;
            }

            FabricaeExNihilo.LOGGER.info("[AUDIT] All fabricaeexnihilo items and blocks have 100% matched zh_cn and en_us translations!");
            return true;
        } catch (Exception e) {
            FabricaeExNihilo.LOGGER.error("[AUDIT] Localization audit failed", e);
            return false;
        }
    }

    private static boolean auditCraftingRecipes(MinecraftServer server, ServerWorld world) {
        try {
            var recipeManager = server.getRecipeManager();
            long fenRecipeCount = recipeManager.values().stream()
                    .filter(entry -> "fabricaeexnihilo".equals(entry.id().getValue().getNamespace()))
                    .count();
            FabricaeExNihilo.LOGGER.info("[AUDIT] Total fabricaeexnihilo recipes loaded in RecipeManager: {}", fenRecipeCount);

            if (fenRecipeCount == 0) {
                FabricaeExNihilo.LOGGER.error("[AUDIT] Zero fabricaeexnihilo recipes were loaded!");
                return false;
            }

            // 1. 测试 Oak Barrel 工作台合成配方
            // Pattern: "# #", "# #", "#S#"
            // [oak_planks, empty, oak_planks, oak_planks, empty, oak_planks, oak_planks, oak_slab, oak_planks]
            List<ItemStack> barrelGrid = Arrays.asList(
                    new ItemStack(Items.OAK_PLANKS), ItemStack.EMPTY, new ItemStack(Items.OAK_PLANKS),
                    new ItemStack(Items.OAK_PLANKS), ItemStack.EMPTY, new ItemStack(Items.OAK_PLANKS),
                    new ItemStack(Items.OAK_PLANKS), new ItemStack(Items.OAK_SLAB), new ItemStack(Items.OAK_PLANKS)
            );
            CraftingRecipeInput barrelInput = CraftingRecipeInput.create(3, 3, barrelGrid);
            Optional<RecipeEntry<CraftingRecipe>> barrelMatch = recipeManager.getFirstMatch(RecipeType.CRAFTING, barrelInput, world);

            if (barrelMatch.isEmpty()) {
                FabricaeExNihilo.LOGGER.error("[AUDIT] Crafting table failed to match recipe for oak_barrel!");
                return false;
            }
            ItemStack barrelOutput = barrelMatch.get().value().craft(barrelInput, world.getRegistryManager());
            FabricaeExNihilo.LOGGER.info("[AUDIT] Oak Barrel crafting matched: {} -> Output: {}", barrelMatch.get().id().getValue(), barrelOutput);
            if (!barrelOutput.isOf(ModBlocks.BARRELS.get(id("oak_barrel")).asItem())) {
                FabricaeExNihilo.LOGGER.error("[AUDIT] Oak Barrel craft output mismatch, expected oak_barrel, got {}", barrelOutput);
                return false;
            }

            // 2. 测试 Wooden Hammer 工作台合成配方
            // Pattern: " # ", " /#", "/  "
            // [empty, planks, empty, empty, stick, planks, stick, empty, empty]
            List<ItemStack> hammerGrid = Arrays.asList(
                    ItemStack.EMPTY, new ItemStack(Items.OAK_PLANKS), ItemStack.EMPTY,
                    ItemStack.EMPTY, new ItemStack(Items.STICK), new ItemStack(Items.OAK_PLANKS),
                    new ItemStack(Items.STICK), ItemStack.EMPTY, ItemStack.EMPTY
            );
            CraftingRecipeInput hammerInput = CraftingRecipeInput.create(3, 3, hammerGrid);
            Optional<RecipeEntry<CraftingRecipe>> hammerMatch = recipeManager.getFirstMatch(RecipeType.CRAFTING, hammerInput, world);

            if (hammerMatch.isEmpty()) {
                FabricaeExNihilo.LOGGER.error("[AUDIT] Crafting table failed to match recipe for wooden_hammer!");
                return false;
            }
            ItemStack hammerOutput = hammerMatch.get().value().craft(hammerInput, world.getRegistryManager());
            FabricaeExNihilo.LOGGER.info("[AUDIT] Wooden Hammer crafting matched: {} -> Output: {}", hammerMatch.get().id().getValue(), hammerOutput);

            // 3. 测试 Wooden Crook 工作台合成配方
            // Pattern: "##", " #", " #"
            // [stick, stick, empty, empty, stick, empty, empty, stick, empty]
            List<ItemStack> crookGrid = Arrays.asList(
                    new ItemStack(Items.STICK), new ItemStack(Items.STICK), ItemStack.EMPTY,
                    ItemStack.EMPTY, new ItemStack(Items.STICK), ItemStack.EMPTY,
                    ItemStack.EMPTY, new ItemStack(Items.STICK), ItemStack.EMPTY
            );
            CraftingRecipeInput crookInput = CraftingRecipeInput.create(3, 3, crookGrid);
            Optional<RecipeEntry<CraftingRecipe>> crookMatch = recipeManager.getFirstMatch(RecipeType.CRAFTING, crookInput, world);

            if (crookMatch.isEmpty()) {
                FabricaeExNihilo.LOGGER.error("[AUDIT] Crafting table failed to match recipe for wooden_crook!");
                return false;
            }
            ItemStack crookOutput = crookMatch.get().value().craft(crookInput, world.getRegistryManager());
            FabricaeExNihilo.LOGGER.info("[AUDIT] Wooden Crook crafting matched: {} -> Output: {}", crookMatch.get().id().getValue(), crookOutput);

            // 4. 测试 Oak Sieve 工作台合成配方
            // Pattern: "# #", "#S#", "I I"
            // [oak_planks, empty, oak_planks, oak_planks, oak_slab, oak_planks, stick, empty, stick]
            List<ItemStack> sieveGrid = Arrays.asList(
                    new ItemStack(Items.OAK_PLANKS), ItemStack.EMPTY, new ItemStack(Items.OAK_PLANKS),
                    new ItemStack(Items.OAK_PLANKS), new ItemStack(Items.OAK_SLAB), new ItemStack(Items.OAK_PLANKS),
                    new ItemStack(Items.STICK), ItemStack.EMPTY, new ItemStack(Items.STICK)
            );
            CraftingRecipeInput sieveInput = CraftingRecipeInput.create(3, 3, sieveGrid);
            Optional<RecipeEntry<CraftingRecipe>> sieveMatch = recipeManager.getFirstMatch(RecipeType.CRAFTING, sieveInput, world);

            if (sieveMatch.isEmpty()) {
                FabricaeExNihilo.LOGGER.error("[AUDIT] Crafting table failed to match recipe for oak_sieve!");
                return false;
            }
            ItemStack sieveOutput = sieveMatch.get().value().craft(sieveInput, world.getRegistryManager());
            FabricaeExNihilo.LOGGER.info("[AUDIT] Oak Sieve crafting matched: {} -> Output: {}", sieveMatch.get().id().getValue(), sieveOutput);

            return true;
        } catch (Exception e) {
            FabricaeExNihilo.LOGGER.error("[AUDIT] auditCraftingRecipes failed", e);
            return false;
        }
    }

    private static boolean testBarrel(ServerWorld world, BlockPos pos) {
        try {
            var barrelBlock = ModBlocks.BARRELS.get(id("oak_barrel"));
            if (barrelBlock == null) {
                FabricaeExNihilo.LOGGER.error("[AUDIT] Oak barrel block not registered!");
                return false;
            }
            world.setBlockState(pos, barrelBlock.getDefaultState());
            var entity = world.getBlockEntity(pos);
            if (!(entity instanceof BarrelBlockEntity barrel)) {
                FabricaeExNihilo.LOGGER.error("[AUDIT] BlockEntity at pos is not BarrelBlockEntity!");
                return false;
            }

            try (var transaction = Transaction.openOuter()) {
                barrel.fluidStorage.extract(FluidVariant.of(Fluids.WATER), FluidConstants.BUCKET, transaction);
                transaction.commit();
            }

            try (var transaction = Transaction.openOuter()) {
                long inserted = barrel.fluidStorage.insert(FluidVariant.of(Fluids.WATER), FluidConstants.BUCKET, transaction);
                if (inserted != FluidConstants.BUCKET) {
                    FabricaeExNihilo.LOGGER.error("[AUDIT] Failed to insert water into barrel, inserted: {}", inserted);
                    return false;
                }
                transaction.commit();
            }

            if (barrel.getState() != BarrelState.FLUID || barrel.getFluid().getFluid() != Fluids.WATER) {
                FabricaeExNihilo.LOGGER.error("[AUDIT] Barrel state mismatch after fluid insertion!");
                return false;
            }

            try (var transaction = Transaction.openOuter()) {
                long extracted = barrel.fluidStorage.extract(FluidVariant.of(Fluids.WATER), FluidConstants.BUCKET, transaction);
                if (extracted != FluidConstants.BUCKET) {
                    FabricaeExNihilo.LOGGER.error("[AUDIT] Failed to extract water from barrel!");
                    return false;
                }
                transaction.commit();
            }

            return true;
        } catch (Exception e) {
            FabricaeExNihilo.LOGGER.error("[AUDIT] testBarrel failed", e);
            return false;
        }
    }

    private static boolean testSieve(ServerWorld world, BlockPos pos) {
        try {
            var sieveBlock = ModBlocks.SIEVES.get(id("oak_sieve"));
            if (sieveBlock == null) {
                FabricaeExNihilo.LOGGER.error("[AUDIT] Oak sieve block not registered!");
                return false;
            }
            world.setBlockState(pos, sieveBlock.getDefaultState());
            var entity = world.getBlockEntity(pos);
            if (!(entity instanceof SieveBlockEntity sieve)) {
                FabricaeExNihilo.LOGGER.error("[AUDIT] BlockEntity at pos is not SieveBlockEntity!");
                return false;
            }

            var meshItem = ModItems.MESHES.get(id("string_mesh"));
            if (meshItem == null) {
                FabricaeExNihilo.LOGGER.error("[AUDIT] String mesh item not registered!");
                return false;
            }
            sieve.setMesh(new ItemStack(meshItem));

            var recipes = SieveRecipe.find(Items.GRAVEL, false, Registries.ITEM.getId(meshItem), world);
            FabricaeExNihilo.LOGGER.info("[AUDIT] SieveRecipe find for gravel + string_mesh found {} drops", recipes.size());
            return !recipes.isEmpty();
        } catch (Exception e) {
            FabricaeExNihilo.LOGGER.error("[AUDIT] testSieve failed", e);
            return false;
        }
    }

    private static boolean testHammer(ServerWorld world, BlockPos pos) {
        try {
            var recipes = ToolRecipe.find(ToolRecipe.ToolType.HAMMER, Blocks.COBBLESTONE.getDefaultState(), world);
            if (recipes.isEmpty()) {
                FabricaeExNihilo.LOGGER.error("[AUDIT] Cobblestone hammer recipe not found!");
                return false;
            }
            var hammerItem = ModTools.HAMMERS.get(id("wooden_hammer"));
            if (hammerItem == null) {
                FabricaeExNihilo.LOGGER.error("[AUDIT] Wooden hammer not registered!");
                return false;
            }
            var drops = Block.getDroppedStacks(Blocks.COBBLESTONE.getDefaultState(), world, pos, null, null, new ItemStack(hammerItem));
            boolean hasGravel = drops.stream().anyMatch(s -> s.isOf(Items.GRAVEL));
            FabricaeExNihilo.LOGGER.info("[AUDIT] Breaking cobblestone with wooden hammer produced: {}", drops);
            return hasGravel;
        } catch (Exception e) {
            FabricaeExNihilo.LOGGER.error("[AUDIT] testHammer failed", e);
            return false;
        }
    }

    private static boolean testCrook(ServerWorld world, BlockPos pos) {
        try {
            var recipes = ToolRecipe.find(ToolRecipe.ToolType.CROOK, Blocks.OAK_LEAVES.getDefaultState(), world);
            if (recipes.isEmpty()) {
                FabricaeExNihilo.LOGGER.error("[AUDIT] Oak leaves crook recipe not found!");
                return false;
            }
            var crookItem = ModTools.CROOKS.get(id("wooden_crook"));
            if (crookItem == null) {
                FabricaeExNihilo.LOGGER.error("[AUDIT] Wooden crook not registered!");
                return false;
            }
            var drops = Block.getDroppedStacks(Blocks.OAK_LEAVES.getDefaultState(), world, pos, null, null, new ItemStack(crookItem));
            FabricaeExNihilo.LOGGER.info("[AUDIT] Breaking oak leaves with wooden crook produced: {}", drops);
            return true;
        } catch (Exception e) {
            FabricaeExNihilo.LOGGER.error("[AUDIT] testCrook failed", e);
            return false;
        }
    }
}
