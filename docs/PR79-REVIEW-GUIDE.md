# PR #79 — Port to Minecraft 1.21.11 (Fabric)

**A review guide, not a changelog.** This document answers three questions for a reviewer of a
753-file port: *where should I spend my attention*, *why was each change made*, and *what does a
player actually see differently*. It is grouped by **kind of change**, not by file, because a
mechanical rename and a gameplay fix have nothing in common except their commit.

## 0. Meta

| | |
|---|---|
| **PR** | #79 · *Port to Minecraft 1.21.11 (Fabric)* |
| **Base → head** | `1.21` @ `333b0fa8` → `1.21.11-configtoolkit` @ `2b32c719` → `cd647e12` (final fix commit, branch `1.21.11-configtoolkit`; the HEAD this guide was finalised against; repo `Thatgfsj-minecraft/FabricaeExNihilo`) |
| **Scale** | **11 commits · 753 files · +6576 / −4758** (387 modified, 230 added, 125 deleted, 11 renamed; base→`2b32c719`, plus +594 / −149 in the final fix commit `cd647e12`) |
| **Core dependency drift** | Minecraft **1.21 → 1.21.11** · Yarn 1.21+build.2 → 1.21.11+build.6 · Loader 0.15.11 → 0.18.4 · Fabric API 0.100.3+1.21 → 0.141.6+1.21.11 · Loom 1.6-SNAPSHOT → `net.fabricmc.fabric-loom-remap` 1.19-SNAPSHOT (resolves to 1.19.4 on the author's machine) · Gradle 8.6 → 9.8.2 · **Java 21 bytecode, built on a JVM 25** |
| **Optional deps** | YACL 3.5.0 → 3.8.1 · configtoolkit 1.1.1 → 1.1.2 · ModMenu 11.0.1 → 17.0.1 · WTHIT 12.2.1 → 18.2.3 · Jade 15.0.4 → 21.1.6 · **JEI added (27.35.0.90)** · Megane 8.4.0 (no 1.21.11 build) · REI 16.0.729 and EMI 1.1.9+1.21 **unchanged, not ported** |
| **Purpose** | Port the mod to 1.21.11 and keep the integrations that can still support it (JEI, Jade, WTHIT) compiling and shipping; leave REI/EMI source in the tree, out of the build, until those viewers have 1.21.11 APIs. |

It is a port PR, not a feature PR — but not only a port. The diff is one large port commit
(`085dd43d`, 659 files) plus ten follow-up commits that repair what the port broke: an
automation/rendering/sync pass (`6ea48c49`), deletion of a dev-only audit harness (`f08f5b5b`), a
manifest-conflict fix (`b7732a5d`), the configtoolkit annotation processor restored (`9d5042ab`),
three behaviour fixes (`75760872`, `27a690d0`, `2b32c719`), the build rework the review asked for
(`4a943d08`), a datagen re-enable with a full data regeneration (`671ca048`), and a final fix commit for
the findings in this guide (`cd647e12`).

It supersedes **#78** (same title, closed 2026-11-15): this branch carries that work plus the
follow-ups, so it can be reviewed as one diff or the two fix commits can be cherry-picked.

**Not included:** the REI and EMI integrations (no 1.21.11 API to compile against), and the Megane
runtime integration — its manifest entry is restored in `cd647e12`, but there is still no 1.21.11
Megane build to load (§4.3). **New here:** the JEI integration did not exist on `1.21` — it is added
by this branch, sieve recipes only.

### 0.1 Commit map

| # | Commit | Subject | Files | ± |
|---|---|---|---|---|
| 1 | `085dd43d` | feat: Port to Minecraft 1.21.11 (Fabric) | 659 | +5450 / −3472 |
| 2 | `6ea48c49` | fix: automation, rendering and sync fixes for crucibles and barrels | 53 | +1268 / −145 |
| 3 | `f08f5b5b` | chore: remove the in-game audit runner and tidy up the 1.21.11 branch | 10 | +47 / −540 |
| 4 | `b7732a5d` | fix: resolve the manifest conflicts from the 1.21 rebase | 4 | +7 / −19 |
| 5 | `9d5042ab` | fix: restore configtoolkit annotation processor for config generation | 9 | +1 / −398 |
| 6 | `75760872` | fix: correct crushed block dust colour and end cake teleport target | 2 | +13 / −6 |
| 7 | `27a690d0` | fix: declare the particle texture on the mesh item model | 1 | +1 / −1 |
| 8 | `4a943d08` | build: bring the build setup in line with 1.21.11 | 8 | +182 / −265 |
| 9 | `671ca048` | fix: make the fabric datagen work again and regenerate the data | 236 | +828 / −1139 |
| 10 | `2b32c719` | fix(jei): use the 18x18 standard slot background for sieve outputs | 1 | +7 / −1 |
| 11 | `cd647e12` | fix: post-review fixes — optional tags, barrel drop, Megane entry, mixin array (summary) | 14 | +594 / −149 |

Note: two commits describe work that a later commit undoes (`9d5042ab` reverses `085dd43d`'s
"decouple from configtoolkit", `f08f5b5b` removes the audit runner that `085dd43d` added), so the
net diff against `1.21` is cleaner than the commit list suggests.

Row 11 (`cd647e12`) is the final fix commit, applied on top of `2b32c719`; it is the tip this guide's
fix statuses refer to, and it does not change the PR's own diff. Unless a section says otherwise, every
`file:line` reference in this guide was resolved at `2b32c719`; `cd647e12` touches five checked-in files
besides nine generated tag files (`BarrelBlockEntity`, the two tag providers, `fabric.mod.json` and the
mixin config), so line numbers in those files may drift by a line or two after it.

---

## How to read this

| Section | What it is | How to read it |
|---|---|---|
| §0 | What this PR is, in one screen | read |
| §1 | Change classification: where review time goes | read |
| §A | Mechanical port, grouped by what forced it | skim |
| **§B** | **Behaviour fixes — the only place a port can hide a gameplay regression** | **read every line** |
| §C | Build system: loom/Gradle/JVM, datagen wiring, flags | read if you review the build |
| §D | Data regeneration: 530 regenerated files, 1066 at HEAD | do **not** read file by file (one exception: §D5) |
| §E | Removals, grouped by reason | skim |
| §4 | What is **not** ported (REI, EMI, Megane) and what a player sees | read before judging completeness |
| §5 | MattiDragon's nine review comments, each mapped to the HEAD state | read |
| §6 | Defects found while writing this guide (not in any review); 6.1–6.4 fixed in `cd647e12` | read before merge |

### Impact markers

| Marker | Meaning | In-game scenario |
|---|---|---|
| 🟢 | Purely internal, no player-visible change | write "no player-visible change" |
| 🟡 | Behaviour tweak (numbers, details, tooltips, visuals) | required |
| 🔴 | Mechanics change — gameplay, saves, or a regression | required |
| ⚠️ | Compatibility risk (old saves, addons, other mods) | required |

Inside §B the marker is the **in-game severity of the change** (🔴 mechanics/saves, 🟡 tweak, 🟢 none), not a
statement that the item is or is not a behaviour fix — every item in §B changes behaviour.

**Provenance of claims.** Everything here is derived from `git diff 333b0fa8..2b32c719`, the files
at HEAD `2b32c719`, and the PR/issue text. Two labels are used where the evidence is weaker:

- *"unverified"* — the claim follows from code/config but was not reproduced (no build and no game
  run were performed for this review).
- *"derived from code, not observed in game"* — the scenario is what the code must produce, but
  nobody watched it happen.

The PR description and the author's 2026-11-16 test report are treated as claims to check, not as
facts; both are out of date in places (§5, §D5). If someone can only run the game twice, the two
things most worth watching are the barrel duplication (§B1) and the crushed-block dust colour (§B18).

---

## 1. Change classification — where review time goes

| Class | Contents | Scale | Review cost | Risk |
|---|---|---|---|---|
| **A — Mechanical port** | API/obligation changes that follow from 1.21.11 itself: registry keys in `Item.Settings`, item model definitions, `ReadView`/`WriteView` block-entity storage, the `Recipe` interface rewrite, `ToolMaterial` as a record, render-state block-entity rendering, datagen API renames. Plus the hand-written datapack files reformatted to the new recipe shape (compat recipes; same content). | 98 Java files in the PR (themes A1–A10 in §A); 105 hand-written compat recipes reformatted | **Skim.** Nothing here is meant to change behaviour; §A names the themes and the handful of places where a rename could silently change behaviour. | 🟢 |
| **B — Behaviour fixes** | The four commits where the port's own judgement enters gameplay: crucible/barrel automation, rendering and sync (`6ea48c49`); crushed-block dust colour and end-cake teleport (`75760872`); mesh item particle texture (`27a690d0`); JEI sieve slot background (`2b32c719`). | 19 items in §B; ~20 Java files plus one model and the lang keys | **Read every line.** This is the only class where a port can hide a regression, and each item carries an explicit before/after in-game scenario. | 🔴 |
| **C — Build system** | Loom/plugin id, Gradle 9.8.2, build JVM 25, CI, content filters, datagen wiring (`inherit client`, `jar.dependsOn runDatagen`, `clean.delete`), removal of the manifest-rewriting hack, mod-flag semantics, configtoolkit processor. | 7 Gradle/wrapper files + 2 CI workflows + 2 changelog files | **Medium.** Few files, but every line has a failure mode: the build breaks, or the jar ships empty data, or the game crashes on launch. §C explains why each configuration exists. | 🟡 |
| **D — Data regeneration** | `src/main/generated` (committed datagen output, 1066 files at HEAD) regenerated by `671ca048`; the providers that produce it. | 530 generated files in the PR diff, 24 datagen-provider Java files in `671ca048` | **None — do not review file by file.** *If the code is correct the data is correct.* Review the providers and the hand-written files listed in §D4. | 🟢 *(one exception: §D5)* |
| **E — Removals** | Deleted code and assets, grouped by reason: dead code, replaced-by-a-new-system, genuinely removed. | 4 Java classes, 3 viewer assets, 1 changelog rename (plus two net-zero additions-within-the-branch) | **Low.** Check that nothing player-visible left with them. | 🟢 / ⚠️ |

The rows are attention hints, not an accounting identity — they do not sum to 753 — because a file can
carry two kinds of change (e.g. `BarrelBlockEntity.java` is mechanically ported in `085dd43d` and
then gets several behaviour fixes in `6ea48c49`). The file-type split of the
whole diff is: **753 files** = 98 Java · 530 generated · 105 hand-written datapack · 6 asset · 3
other resource · 7 build · 2 CI · 2 changelog.

> ### Also read §4 before judging completeness.
> Three optional integrations are absent, and one of them (Megane) was *dropped by accident* and only
> restored in the final fix commit (§4.3). Then, if you only read one section, read §B.
>
> Everything else in this PR is either mechanical, is the build, or is generated output. §B is the
> only place where the port's author applied judgement to gameplay, and therefore the only place
> where a port can quietly change what the game does.

**Three defects this guide found — all fixed in `cd647e12`, so check the fix rather than hunt the
bug:** breaking a barrel holding a finished product dropped it twice (§B1 / §6.1, a live duplication
bug), the generated tags hard-referenced Tech Reborn blocks (§D5 / §6.2, 🔴 for a Tech-Reborn-less
client), and the Megane entrypoint was silently gone (§4.3 / §6.3).

---

## A. Mechanical port (1.21 → 1.21.11)  [🟢]

**Scope rule.** A hunk is *mechanical* if the same line would have to be written differently only because the
Minecraft/Fabric API moved. Anything that changes what the game does, shows or stores is **not** folded in
silently: every such delta found in the 98 Java files is listed by name + path + commit in §A.11 for §B/§E.
§A items therefore carry **Change / Why / Files / Impact** with a tail marker instead of the four-part
template — per-item in-game scenarios belong to §B only: a 🟡 theme means "this theme opens a §B item", and the
scenario lives in the §B item named in the hand-off table (§A.11), not here.

**Scale.** `git diff --numstat 333b0fa8..HEAD -- src/main/java`: **98 files, +2532 / −1113**. Attribution
by commit: `085dd43d` touches 75 Java files (65 of the 98), `6ea48c49` 23, `671ca048` 24, `f08f5b5b` 4.
Java is 13 % of the PR's files and 32 % of its changed lines; it is the only part where a porting slip
changes gameplay. The other ~530 files are generated datagen output (§D).

**What 1.21.11 forced.** Six breaks cover almost every mechanical hunk: (1) an item/block must know its
registry key *before construction* (`Settings#registryKey`) — the translation key and item-model lookup
derive from it; (2) item colour/model data moved into item model definitions under `assets/<ns>/items/` with
tint sources; (3) `BlockEntity` NBT moved to `ReadView`/`WriteView` and `Enchantment` became a final record;
(4) block-entity rendering moved to extract-a-render-state + `OrderedRenderCommandQueue`, which also deleted
two rendering mixins; (5) `Recipe` gained `getIngredientPlacement`/`getRecipeBookCategory` and generic
`RecipeSerializer<T>`/`RecipeType<T>`; (6) `ToolMaterial` became a record with a *tag*-based repair set,
`ToolItem` disappeared, and `LeavesBlock`/`FallingBlock` gained abstract hooks.

### A.0 Theme map

| # | Theme | Files | Churn | Marker | Non-mechanical part |
|---|-------|-------|-------|--------|---------------------|
| A1 | Registration: registry key before construction | 5 | +73/−43 | 🟢 | addon-API note only |
| A2 | Item components, tool materials, enchantment record | 7 | +97/−141 | 🟡 | §B14 (repair items) |
| A3 | Block API signatures; `ItemActionResult` → `ActionResult` | 12 | +105/−87 | 🟡 | §B6 (FAIL vs PASS) |
| A4 | Block entity storage (`ReadView`/`WriteView`) and BE wiring | 7 | +606/−173 | 🟡 | §B1, §B3–B5, §B7–B10 (automation/sync) |
| A5 | Recipe interface, lookup wrappers, codecs | 15 | +154/−66 | 🟡 | see A5 — no diff-level behaviour change |
| A6 | Datagen providers (models, recipes, tags) | 20 | +521/−252 | 🟢 | §D (regenerated data) |
| A7 | Client renderers, item colour, render mixins | 9 | +372/−242 | 🟡 | §B7 (crucible visuals) |
| A8 | Mixins and accessors | 7 | +39/−72 | 🟢 | §E (2 mixins deleted) |
| A9 | Compat: Jade, WTHIT, JEI | 9 | +499/−13 | 🟡 | §B11, §B12, §B15–B16 |
| A10 | Entrypoint, config, small utilities | 7 | +66/−24 | 🟡 | §B18 (dust colour) |
| | **Total** | **98** | **+2532/−1113** | | |

Each file is counted once, by its dominant change; the sums are exact against `numstat.txt`.

### A1. Registration: the registry key is now part of the settings  [🟢]

**Change.** New `ModItems.createSettings(Identifier)` wraps `new Item.Settings().registryKey(...)`; every item
literal goes through it; `ModBlocks.END_CAKE`/`INFESTED_LEAVES_SETTINGS` and `ModFluids.getBlockSettings(id)`/
`getBucketItemSettings(id)` gained the call; `EntrypointHelper` injects the key into the settings an addon passes in.
**Why.** An item derives its translation key — hence its name and its `assets/items/<name>.json` model lookup —
from the settings key, so `new Item(settings)` + `Registry.register(..., id, item)` no longer yields a nameable item
(`DatagenItems.java:94-96`).
**Files.** `ModItems.java:21-22`, `ModBlocks.java:41,43`, `ModFluids.java:23-30`,
`EntrypointHelper.java:88,102,116,127,141,151,170,184`, `DatagenItems.java:92-97` — 5 files.
**Impact.** No player-visible change. API note: `EntrypointHelper` now *mutates* the caller's `Settings`; a plugin
reusing one `Settings` for two blocks would get the first id twice (addon-API contract change, not a save change).

### A2. Item components, tool materials, enchantment record  [🟡]

**Change.** `ModToolMaterials` stops implementing `ToolMaterial` and becomes an enum holding `ToolMaterial`
records; `CrookItem`/`HammerItem` stop extending `ToolItem` and set `maxDamage(material.durability())`
themselves; `EnchantableBlockItem`/`MeshItem` drop the `isEnchantable()`/`getEnchantability()` overrides for
`settings.enchantable(n)`; `EnchantmentContainer` stores `Identifier → level` and re-resolves through the
registry on use.
**Why.** `ToolMaterial` is a record (`durability()`, `speed()`, `enchantmentValue()`) whose repair input is a
`TagKey<Item>`, so an `Ingredient` supplier is impossible; `ToolItem` is gone; `Enchantment` exposes no key.
**Files.** `ModToolMaterials.java:16-21`, `CrookItem.java:13-20`, `MeshItem.java:14-16`,
`EnchantableBlockItem.java:11`, `EnchantmentContainer.java:18,37-44`, `ModTools.java:26-40` — 7 files.
**Impact.** 🟡 because the repair input had to be *substituted*: BONE/BLAZE/TERRACOTTA/PURPUR/PRISMARINE now
repair from `ItemTags.STONE_TOOL_MATERIALS` and CLAY from `ItemTags.PLANKS` (`ModToolMaterials.java:16-21`).
A gameplay decision inside a mechanical signature change → §B14; highest-risk §A item.

### A3. Block API signatures and the `ActionResult` rewrite  [🟡]

**Change.** `getStateForNeighborUpdate` gained `WorldView`/`ScheduledTickView`/pos/random, `neighborUpdate`
gained `WireOrientation` and became `protected`, `isTransparent` takes only the state, `canPlayerModifyAt` →
`canEntityModifyAt`, `onUseWithItem` returns `ActionResult`, block drops moved from `Block#onStateReplaced` to
`BlockEntity#onBlockReplaced`, `LeavesBlock` takes a particle chance and gained `spawnLeafParticle`,
`FallingBlock#getColor` became abstract, custom block types need a `MapCodec`.
**Why.** All are signature/visibility changes in the vanilla hierarchy; `ItemActionResult` was merged into
`ActionResult`, and drop handling moved into the block entity.
**Files.** `SieveBlock.java:48,65,90-101`, `CrucibleBlock.java:82-92`, `InfestedLeavesBlock.java:30,41`,
`DummyFallingBlock.java:22-31`, `BarrelBlockEntity.java:108-113` — 12 files (incl. 3 fluids).
**Impact.** 🟡 — the rename is not behaviour-preserving: `SKIP_DEFAULT_BLOCK_INTERACTION` has no exact
counterpart, and the blanket mapping to `PASS` is wrong at two sites, hence `BarrelBlockEntity.java:173` and
`CrucibleBlockEntity.java:108` return `ActionResult.FAIL` (a fluid container used to be handed to vanilla
`BucketItem` and poured onto the neighbouring block) → §B6.

### A4. Block entity storage, sync and wiring  [🟡]

**Change.** `readNbt(Compound, WrapperLookup)`/`writeNbt(...)` became `readData(ReadView)`/`writeData(WriteView)`
with `super` chaining; `BlockEntityType.Builder.create(...).build(null)` became
`FabricBlockEntityTypeBuilder.create(...).build()` in five entities; `toInitialChunkDataNbt` calls
`createComponentlessNbt`; `EnchantmentContainer` serialises via `unboundedMap(Identifier, INT)`.
**Why.** The NBT-backed view API, the vanilla builder's registry-key parameter and component-bearing chunk
payloads are gone.
**Files.** `BarrelBlockEntity.java:181-207`, `CrucibleBlockEntity.java:188-215`, `BaseBlockEntity.java:35-37`,
`SieveBlockEntity.java:240-266`, `InfestingLeavesBlockEntity.java:79-92` — 7 files.
**Save compatibility (checked, not assumed).** On-disk *keys* are unchanged — barrels
`state/fluid/fluidAmount/stack/compostLevel/recipe/recipeProgress`, crucibles
`render/fluid/contained/queued/heat/requiresFireproof/enchantments`, sieves `mesh/contents/progress`,
infesting leaves `progress/target`; the nested `enchantments` compound keeps its old shape (`"<id>": level`),
so old worlds load their enchantments. The one added lenience is clamping `contained` to capacity on load
(`CrucibleBlockEntity.java:210-212`) → §B9.
**Impact.** 🟡 — most of the +606 is not storage but new automation code (§A.11 #1–#4).

### A5. Recipe interface, lookup wrappers and codecs  [🟡]

**Change.** `BaseRecipe` drops `fits`/`getResult` for `craft`, `getIngredientPlacement()` (`NONE`) and
`getRecipeBookCategory()` (`CRAFTING_MISC`); serializers/types became generic; the `world.getRecipeManager().*`
lookups now go through the new `RecipeHelper`; `Ingredient.ALLOW_EMPTY_CODEC` → `Ingredient.CODEC`;
`PacketCodecs.BOOL` → `PacketCodecs.BOOLEAN.cast()`; `RecipeContext` implements `RecipeInput` directly with
`size()`/`getStackInSlot()`.
**Why.** The `Recipe` interface was reshaped and `RecipeManager`'s lookups moved off `World`.
**Files.** `BaseRecipe.java:14-28`, `RecipeContext.java:20-34`, `RecipeHelper.java:16-56`,
`SieveRecipe.java:56-63,93-101`, `BarrelRecipeTrigger.java:82`, `CopyEnchantmentsLootFunction.java:31` — 15 files.
**Impact.** 🟡 two non-mechanical items hide here: (a) `RecipeContext.isEmpty()` now returns `false`
(`RecipeContext.java:32-34`) because 1.21.11's `getFirstMatch` short-circuits on an empty input — without it
barrel/crucible/milking/heat lookups silently match nothing; (b) `Ingredient.CODEC` rejects an empty ingredient
list where `ALLOW_EMPTY_CODEC` accepted one. No FEN recipe contains an empty list (grep of
`src/main/generated` + `src/main/resources`, 0 hits), so only a third-party datapack that shipped one is
affected. The `isEmpty` override was added inside `085dd43d` (base `1.21` never needed it because
that short-circuit did not exist), so it is not a behaviour change of the PR as a whole — but it is the
load-bearing line that made barrel/crucible/milking/heat lookups work on 1.21.11 at all.

### A6. Datagen providers  [🟢]

**Change.** `FabricModelProvider` moved to Fabric's client-datagen package and now writes **two** artefacts per
item — the model JSON and the `assets/<ns>/items/` definition (`ModelProvider.java:186-190`);
`FabricRecipeProvider#generate` became `getRecipeGenerator(...)` returning a `RecipeGenerator`
(`BaseRecipeProvider.java:41-51`); `offerTo` takes `RegistryKey.of(RegistryKeys.RECIPE, ...)`; tag builders take
`RegistryKeys` and need `.mapped(v -> Registries.ITEM.getKey(v).orElseThrow())` (`ItemTagProvider.java:29-32`);
advancement criteria take the item lookup explicitly.
**Why / Files.** The datagen API was rewritten around registries and item definitions — which is *why* 163
`assets/*/items/*.json` files appear in the PR (all 163 are additions per `--name-status`, generated not
hand-written, §D): `ModelProvider.java:186-203`,
`BaseRecipeProvider.java:38-64`, `ItemTagProvider.java:29-46`, new `datagen/builder/recipe/RecipeIds.java` —
20 files, +521/−252.
**Impact.** No player-visible change of its own; the output is §D's subject. One content note: optional-mod wood
sets are now null-guarded in the model and recipe providers (`BaseRecipeProvider.java:123-126`,
`ModelProvider.java:118-125`) but **not** in the tag providers — the mechanism behind the `rubber_*` tag errors
in the 2026-11-16 test report (§D).

### A7. Client renderers, item colour, render mixins  [🟡]

**Change.** All five block-entity renderers were rewritten to the render-state API —
`BlockEntityRenderer<T, S>`, `createRenderState()`, `updateRenderState(...)` using `ItemModelManager`/
`ItemRenderState`, then `render(state, matrices, OrderedRenderCommandQueue, CameraRenderState)`; the FRAPI
`RendererAccess`/`MutableQuadView` fluid quad became four explicit `consumer.vertex(...)` submits
(`BarrelBlockEntityRenderer.java:127`); `RenderLayer.getCutout()` → `BlockRenderLayer.CUTOUT` and
`BlockRenderLayerMap` moved package (`FabricaeExNihiloClient.java:41-46`).
**Why.** Immediate-mode block-entity drawing and the FRAPI quad emitter were removed; `submitBlockStateModel`
takes an explicit tint colour; item colour moved from `ColorProviderRegistry.ITEM` to model tint sources.
**Files.** `BarrelBlockEntityRenderer.java:50-175`, `CrucibleBlockEntityRenderer.java:46-125`,
`ModelProvider.java:151`, `FluidRenderManager.java:23-24` — 9 files.
**Deleted, mechanically replaced.** `client/FENItemColorProvider.java` (tints are now baked into the item
definition — `ModelProvider.java:151` passes `((MeshItem) item).getColor(0)` as a `ConstantTintSource`, and
the generated definition states it, e.g. `.../items/adamantite_mesh.json` carries
`"tints": [{"type": "minecraft:constant", "value": -5636096}]`; equivalent for every `Colored` item FEN
registers, pebbles/ore pieces were white in both paths) and
`client/BlockModelRendererFlags.java` + `mixins/client/BlockModelRendererMixin.java` (the flags only forced a
colour through `BlockModelRenderer#renderQuads`; the new `submitBlockStateModel(..., r, g, b, ...)` at
`BarrelBlockEntityRenderer.java:151` takes it directly).
**Impact.** 🟡 only for one deliberate new rule in the crucible renderer: the floating solid model is kept
while the tank still holds fluid and its height follows `max(queued, contained)` instead of `queued`
(`CrucibleBlockEntityRenderer.java:61-77`, 6ea48c49) → §B7.

### A8. Mixins and accessors  [🟢]

**Change.** Accessor types follow renames (`LootContextType` → `ContextType`); `BottleUseMixin` follows
`canPlayerModifyAt` → `canEntityModifyAt` and `TypedActionResult` → `ActionResult`; `AnvilScreenHandlerMixin`'s
constructor gained `ForgingSlotsManager`; `ItemModelGeneratorAccess` went from one writer accessor to
`@Accessor("modelCollector")` + `@Accessor("output")` + `@Invoker("register")`.
**Why.** Every target signature changed; `ItemModelGenerator#register(Item, Model)` became private and always
uses the default texture, so custom-texture items need the `output` sink (`ItemModelGeneratorAccess.java:14-21`).
**Files.** `ItemModelGeneratorAccess.java:23-36`, `BottleUseMixin.java:43-48`, `AnvilScreenHandlerMixin.java:19-21`,
`LootContextTypesAccess.java:10-14` — 7 files, +39/−72.
**Deleted.** `mixins/EnchantmentMixin.java` — `Enchantment` is a final record, so it cannot be mixed into; the
equivalent hook, Fabric's `EnchantmentEvents.ALLOW_ENCHANTING`, was already registered at base
(`BonusEnchantingManager.java:36`), so the mixin was redundant rather than a lost feature → §E.
**Impact.** No player-visible change.

### A9. Compat: Jade, WTHIT, JEI  [🟡]

**Change (port).** Jade/WTHIT plumbing follows the new APIs (`JadeIds` import, `ItemStackElement` type,
`addTooltipCollectedCallback`); the crucible component keys off `getDisplayStack()`.
**Change (new code, 6ea48c49 + 2b32c719).** JEI is **not** a port. Four new files — `FENJeiPlugin.java`
(+90), `JeiSieveRecipe.java` (+30), `SieveRecipeCategory.java` (+168), `SieveRecipeCollector.java` (+155) — plus
a new server-side opt-in, `RecipeSynchronization.synchronizeRecipeSerializer(ModRecipes.SIEVE_SERIALIZER)`
(`FabricaeExNihilo.java:91-94`), because vanilla no longer syncs full recipes to clients.
**Why / Files.** Jade/WTHIT only needed signature updates. JEI is new because REI and EMI are excluded from
the build, so without it the mod would ship with no recipe viewer at all; the recipe-sync opt-in exists because
vanilla no longer syncs full recipe data to clients. Neither is a mechanical hunk.
`FENJadePlugin.java:20-30`, `BarrelProvider.java:17-52`, `CrucibleComponentProvider.java:16-22`,
`FENJeiPlugin.java:26-89` — 9 files, +499/−13 (JEI alone +443).
**Impact.** 🟡 — player-visible tooltip and recipe-viewer content changed in the same commits: Jade barrel
tooltip rewrite and removal of Jade's generic fluid line (§B11), the crucible "contents" line (§B7), the JEI
sieve category (§B15–B16), and a real bug fix where `InfestingLeavesProvider` was registered against
`StrainerBlock.class` (§B12).

### A10. Entrypoint, config, small utilities  [🟡]

**Change / Files.** `FabricaeExNihilo` adds the sieve serializer sync; `WitchWaterConfig` uses
`Identifier.ofVanilla(...)`; `ModLootContextTypes` uses `ContextType`; `EmptyInventory.size()` replaces
`getSize()`; `EndCakeBlock` follows `ActionResult`; `DummyFallingBlock` implements the now abstract
`getColor` — `FabricaeExNihilo.java:91-94`, `WitchWaterConfig.java:14-18`, `ModLootContextTypes.java:12`,
`EmptyInventory.java:11`, `DummyFallingBlock.java:22-31`; 7 files, +66/−24.
**Impact.** 🟡 one item — the first pass returned a constant `0` from `DummyFallingBlock#getColor`, making
crushed-block dust pure black; 75760872 returns the block's map colour (`DummyFallingBlock.java:31`) → §B18.

### A.11 Hand-off: Java changes that are NOT mechanical

Named so each can be located in §B/§E. None is folded into the themes above.

| # | What changed | Where (HEAD) | Commit | Section |
|---|--------------|--------------|--------|---------|
| 1 | Barrel implements `SidedInventory`+`Hopper`: virtual input/output slots for hopper automation | `BarrelBlockEntity.java:48,406-543`, `BarrelItemStorage.java:32-36` | 6ea48c49 | §B4 — and §B1 (double drop), §B3 (stack loss) |
| 2 | `isCrafting()` = "a recipe is running" (was `recipeProgress > 0`) | `BarrelBlockEntity.java:329-336` | 6ea48c49 | §B3, §B4 |
| 3 | Fluid container on barrel/crucible returns `ActionResult.FAIL` instead of PASS | `BarrelBlockEntity.java:168-174`, `CrucibleBlockEntity.java:100-108` | 6ea48c49 | §B6 |
| 4 | Crucible `SidedInventory`, capacity clamps, per-tick `broadcast()`, `getDisplayStack()`, heat on server only | `CrucibleBlockEntity.java:55,173,188-215,241-247` | 6ea48c49 | §B5, §B7–B9 |
| 5 | Repair inputs replaced by item tags | `ModToolMaterials.java:16-21` | 085dd43d | §B14 |
| 6 | `RecipeContext.isEmpty()` returns false so `getFirstMatch` runs; `Ingredient.CODEC` tightens parsing | `RecipeContext.java:32-34`, `SieveRecipe.java:93` | 085dd43d | see A5 (no diff-level behaviour change) |
| 7 | Jade `InfestingLeavesProvider` was registered against `StrainerBlock.class` | `FENJadePlugin.java:20` | 6ea48c49 | §B12 |
| 8 | Jade barrel tooltip rewritten (real capacity, progress in all states, item names); Jade's generic fluid line removed for barrels | `BarrelProvider.java:17-52`, `FENJadePlugin.java:23-30` | 6ea48c49 | §B11 |
| 9 | Jade + WTHIT report the crucible's solid separately from the melt queue | `CrucibleProvider.java:19-26`, `CrucibleComponentProvider.java:16-22` | 6ea48c49 | §B7 |
| 10 | Crucible renderer keeps the solid while fluid remains and heights it by `max(queued, contained)` | `CrucibleBlockEntityRenderer.java:61-77` | 6ea48c49 | §B7 |
| 11 | Crushed-block dust colour = map colour (first pass returned 0 = black) | `DummyFallingBlock.java:22-31` | 75760872 | §B18 |
| 12 | New JEI sieve category + recipe-sync opt-in | `FENJeiPlugin.java`, `SieveRecipeCategory.java`, `FabricaeExNihilo.java:91-94` | 6ea48c49, 2b32c719 | §B15–B16 |
| 13 | Infested/infesting leaves override `spawnLeafParticle` to do nothing | `InfestedLeavesBlock.java:40-42`, `InfestingLeavesBlock.java:27-29` | 085dd43d | §6.9 (unverified) |
| 14 | In-game audit runner `util/ModTestRunner.java` added, then removed | net zero in base..head | 085dd43d → f08f5b5b | §E1.1 |
| 15 | Eight hand-written `config/Mutable*Config.java` added, then deleted when the configtoolkit processor was restored | net zero in base..head | 085dd43d → 9d5042ab | §E2.4, §C12 |

Items 14–15 are invisible in `git diff base..head` (added and removed inside the branch) but they are port
history and explain the "+47/−540" and "+1/−398" commits in the commit map (§0.1).

### A.12 The three large hand-edits — is §B required?

`BarrelBlockEntity` (+295/−47), `CrucibleBlockEntity` (+201/−46) and the renderers (`BarrelBlockEntityRenderer`
+134/−66, `CrucibleBlockEntityRenderer` +85/−44, plus sieve/strainer/infesting) are the only files where the
port stopped being a find-and-replace. Split by commit the answer differs. In **085dd43d** the block entities
only did the storage migration and the renderers only the render-state split — both mechanical, and the diff
shape supports it: one field per key with unchanged key names, drawing bodies moved behind
`createRenderState`/`updateRenderState` with identical constants (`XZ_SCALE`, `Y_MIN`, `Y_MAX`) and transforms.
In **6ea48c49** both block entities gained ~200 lines no API change asked for (hopper slot emulation, `FAIL`
returns, client sync, capacity clamps, `getDisplayStack`) and the crucible renderer gained the "fluid wins when
it outweighs the solid" rule. So: **port-commit content is mechanical and needs no §B coverage; every 6ea48c49
hunk in these files is a behaviour change and belongs to §B** (#1–#6, #10 in §A.11).

## B. Behaviour fixes  [🔴]

Every item below changes what a player sees or can do: the change, why it exists (port-artefact regression /
upstream-parity restoration / new behaviour), and a tester's repro. Unless stated otherwise the scenarios are derived
from the code and the vanilla 1.21.11 sources — nobody has run them in game yet.

Markers: 🔴 mechanics change · 🟡 minor behaviour tweak · 🟢 no player-visible change · ⚠️ compatibility risk.

---

### B1. A barrel holding a finished product drops it twice when broken  [🔴]  commit 6ea48c49, fixed in cd647e12

**Status:** Fixed in `cd647e12` — the override now clears the block entity's own output stack before it
calls `super.onBlockReplaced(...)`, so the inherited `Inventory` scatter finds an empty container and only
the override's copy drops. The analysis below is the evidence chain for the fix.

**Change:** `BarrelBlock.onStateReplaced` (which dropped the stored item) was deleted; the drop moved into
`BarrelBlockEntity#onBlockReplaced` (`modules/barrels/BarrelBlockEntity.java:107-113`), which spawns
`getItem().copy()` and then calls `super.onBlockReplaced(pos, state)`.
**Why:** The barrel now implements `SidedInventory` (`:48`), i.e. vanilla's `Container`. In 1.21.11 the default
`BlockEntity#onBlockReplaced` drops the contents of every `Container` and vanilla calls it on block removal
(`WorldChunk` in 1.21.11 Yarn — `LevelChunk` in Mojang mappings — calls `BlockEntity#onBlockReplaced`, whose default body
scatters any `Inventory` via `ItemScatterer.spawn`). The override drops a
copy first, so the inherited call finds the field still populated and drops it again.
**In-game impact (at `2b32c719`, before the fix):** breaking a barrel with a finished product yielded two
stacks of it.
**What actually happens in game** (derived from code, not observed): compost 8 saplings into a barrel until it shows
dirt, break the barrel with a pickaxe → two dirt item entities instead of one. On 1.21 the barrel was not a
`Container`, so it dropped once; with `cd647e12` it drops once again.

---

### B2. Eating the end cake teleports the player like a real end portal again  [🔴]  commit 75760872

**Change:** `EndCakeBlock#tryEat` calls `((EndPortalBlock) Blocks.END_PORTAL).createTeleportTarget(serverWorld, player, pos)`
and passes it straight to `player.teleportTo` (`modules/other/EndCakeBlock.java:39,43`), replacing a hand-built
`new TeleportTarget(destination, Vec3d.ofBottomCenter(destination.getSpawnPoint().getPos()), Vec3d.ZERO, yaw, pitch, NO_OP)`.
**Why:** Port-artefact regression fixed. The 1.21 base already called the vanilla method
(`git show 333b0fa8:.../EndCakeBlock.java`); 085dd43d replaced it. Vanilla's `EndPortalBlock#createTeleportTarget` (Mojang name: `getPortalDestination`) additionally builds the 5×5 obsidian platform (`EndPlatformFeature.createEndPlatform`: one obsidian layer, three
blocks of air cleared above), sets the arrival yaw to `Direction.WEST.toYRot()` instead of keeping the eating player's yaw, and attaches
`PLAY_PORTAL_SOUND.then(PLACE_PORTAL_TICKET)` (portal sound + chunk ticket). Only the Overworld→End branch is
reachable from the cake — `tryEat` refuses to run in the End (`:26-28`).
**In-game impact:** the first end-cake trip no longer ends in the void, and the arrival looks and sounds like a portal.
**What actually happens in game** (derived from code): place the end cake, right-click six times with an empty hand.
Before: the screen fades, the player appears at the End's spawn coordinates with no obsidian under them — open void on a
fresh world — still facing the Overworld direction, no portal sound. After: the platform is built, they arrive facing
west, the portal sound plays and the destination chunk is force-loaded.

---

### B3. A hopper feeding a timed barrel recipe deletes the rest of its stack  [🔴]  commit 6ea48c49

**Change:** `BarrelItemStorage#insert` (`modules/barrels/BarrelItemStorage.java:18-50`) consumes the whole offered
stack for instant (duration 0) recipes but accepts exactly one item for timed recipes and returns 1. Vanilla hoppers
push through `Inventory#setStack`, which returns `void`, so `BarrelBlockEntity#setStack` (`:464-483`) hands over the
whole stack and the hopper assumes all of it moved.
**Why:** A documented compromise: the vanilla container API cannot report a partial accept, and vanilla hoppers never
consult Fabric's `ItemStorage.SIDED`. New behaviour — on 1.21 hoppers could not feed a barrel at all, so nothing could
be lost.
**In-game impact:** feeding a stack of alchemy inputs by hopper destroys all but one of them.
**What actually happens in game** (derived from code): barrel full of lava, 16 blaze dolls in a hopper above it. One
doll starts the 20-tick `summon_blaze` recipe; the other 15 are consumed by the hopper's `setStack` and vanish. On
1.21 the hopper simply refused and the dolls stayed in it.

---

### B4. Barrels accept items from hoppers and give finished products back  [🔴]  commit 6ea48c49

**Change:** `BarrelBlockEntity` implements `SidedInventory` and `Hopper` (`:48`) with two virtual slots —
`INPUT_SLOT = 0` (always empty, receives via `setStack`) and `OUTPUT_SLOT = 1` (finished product, read/remove only) —
visible from every face (`:400-533`). `canInsert` requires a matching insert recipe and refuses while a recipe runs or
a pile is full; `canExtract` only exposes the finished item.
**Why:** New capability. The 1.21 barrel exposed only `ItemStorage`, which vanilla hoppers never consult. Vanilla
1.21.11 insertion only needs `Container`; the extra `Hopper` interface is redundant for the hopper path and has no
player-visible effect (the code comment's justification is not supported by the vanilla bytecode).
**In-game impact:** barrels become automatable for the first time.
**What actually happens in game** (derived from code): a hopper of saplings above a barrel, a hopper under the barrel
pointing into a chest → the barrel composts, the lower hopper pulls each finished dirt out, repeating until the
saplings run out. Before: both hoppers did nothing; the dirt had to be taken by right-clicking.

---

### B5. Crucibles accept meltables from hoppers  [🔴]  commit 6ea48c49

**Change:** `CrucibleBlockEntity` implements `SidedInventory` (`:55`) with one virtual input slot (`:408-471`).
`setStack` pushes the whole stack through `itemStorage`; `canInsert`/`canAccept` re-use the manual insert rules
(recipe match, capacity, fluid type, fireproof) (`:457-497`); `getAvailableSlots` returns `NO_SLOTS` for `DOWN` so a
hopper below cannot pull items.
**Why:** New capability — vanilla hoppers never consult `ItemStorage.SIDED`, so the only ways to fill a crucible were
hand-feeding or modded pipes.
**In-game impact:** crucibles can be fed automatically with the same acceptance rules as manual insertion.
**What actually happens in game** (derived from code): a hopper of cobblestone above a porcelain crucible with a heat
source below → the crucible melts the cobblestone into lava on its own until the tank is full; a hopper underneath
extracts nothing (the fluid leaves as fluid, not as items).

---

### B6. Clicking a crucible or barrel with a bucket no longer pours the bucket into the world  [🟡]  commit 6ea48c49

**Change:** the "held item is a fluid container" branches return `ActionResult.FAIL` where they used to return
`PASS`: `CrucibleBlockEntity#activate` (`:108`) when the container cannot take fluid from the crucible, and
`BarrelBlockEntity#insertFromHand` (`:173`) when no fluid could be moved either way.
**Why:** Port-artefact regression, partially repaired. 1.21 returned `ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION`
(`git show 333b0fa8:.../CrucibleBlockEntity.java:109`), and the port mapped every `ItemActionResult` constant to
`ActionResult.PASS` (`git show 085dd43d:...`); `PASS` hands the click to the held item, so `BucketItem` ran and
placed the bucket's fluid against a neighbouring block. `FAIL` is the 1.21.11 spelling of the old "skip the default
block interaction".
**In-game impact:** a lava bucket aimed at an empty or wrong-fluid crucible stays in the player's hand.
**What actually happens in game** (derived from code): hold a lava bucket, right-click an empty crucible. Before: lava
appears on the block next to the crucible (or the click falls through to the bucket). After: nothing happens, the
bucket is unchanged. **Not fully fixed:** `BarrelBlockEntity#activate` still returns `PASS` while a recipe runs
(`:84-87`), so a bucket clicked on a busy barrel can still empty itself into the world.

---

### B7. The crucible keeps showing the solid it is melting from  [🟡]  commit 6ea48c49

**Change:** new `CrucibleBlockEntity#getDisplayStack` (`:173-177`) reports the stored solid while either the melt
queue or the tank is non-empty; the renderer uses it and heights the item model by `max(queued, contained)` instead of
`queued`; the Jade and WTHIT providers print "Solid: <item> x<n>" from it (new key
`fabricaeexnihilo.hud.crucible.contents`, `lang/en_us.json:190`).
**Why:** Bug fix, not port-specific: `queued` reaches 0 the instant the last input melts, so the floating item vanished
while its fluid was still inside and the block looked empty.
**In-game impact:** the crucible no longer appears to empty itself when melting finishes.
**What actually happens in game** (derived from code): drop one cobblestone into a lit porcelain crucible. Before: the
rotating cobblestone disappeared the moment it finished melting although the tank held the 250 mB of lava it had
just produced, and the
tooltip dropped the solid line. After: the cobblestone stays (shrinking to the fluid level) and the tooltip keeps
"Solid: Cobblestone x1" until the lava is drained.

---

### B8. Crucible fluid level and tooltips update while melting  [🟡]  commit 6ea48c49

**Change:** `tick()` calls `broadcast()` (`:228, 241-246`, `ServerWorld.updateListeners` on the block state) after
each processing step, and `readData` only recomputes `heat` on the server (`:204-206`) instead of on both sides.
**Why:** Port-artefact regression. `readData` runs on both sides; recomputing the heat on the client looked up the block
below in a possibly stale chunk and reset the synced value to 0, which could stop a `requiresFireproof` crucible from
melting. The missing broadcast meant the client only refreshed on block events, so the displayed amount lagged.
**In-game impact:** the displayed amount and "Solid remaining" line track the server.
**What actually happens in game** (derived from code): insert 20 cobblestone into a lit crucible and watch it without
touching the block. Before: the tooltip kept the value from the last interaction (e.g. "Solid remaining: 60mb") while
the tank filled, and a client with a stale chunk below could see the block stall. After: the level climbs live.

---

### B9. Over-capacity crucible contents are clamped when the world loads  [⚠️]  commit 6ea48c49

**Change:** `readData` clamps `contained` to `[0, getMaxCapacity()]` and `queued` to `>= 0` (`:209-210`);
`tick()` refuses to process past the capacity (`:222`).
**Why:** One processing step could previously add more than the remaining room (Efficiency and a hot block multiply
the processing speed), leaving the tank above its limit and un-drainable. The clamp makes such a block usable again but
**deletes** the excess.
**In-game impact:** an already overfilled crucible loses the excess fluid when the chunk loads.
**What actually happens in game** (derived from code): load an old save with an overfilled crucible (possible on the
1.21 branch too — the base `tick` had no upper clamp). Before: stuck at the illegal amount forever. After: silently
reduced to the capacity; the block works again. No crash.
**Old-save verdict:** compatible but lossy for the overfilled case.

---

### B10. Finished barrel products can no longer be pulled out forever  [🟡]  commit 6ea48c49

**Change:** `getStack(OUTPUT_SLOT)` returns the live field and re-syncs the state when it is empty (`:416-436`);
`removeStack` keeps the `ITEM` state in step with the stack (`:438-462`); the `Snapshot` restore copies the stack
instead of aliasing it (`:610-620`); `BarrelItemStorage#extract` copies before decrementing (`:65`);
`BarrelRecipeAction`'s compost action and `fillCompost` copy the result (`:297-311`); `dropInventoryAtPlayer`
spawns a copy (`:99`).
**Why:** Two aliasing bugs: hopper-like mods mutated the returned stack in place, so the barrel never noticed the
extraction and kept advertising the item; and a transaction rollback restored an already-shrunk snapshot, which read in
game as "the product will not disappear no matter how often you pull it".
**In-game impact:** the output slot now behaves like a real slot — pull once, it is gone.
**What actually happens in game** (derived from code): a hopper (or a pipe that mutates the stack in place) pulls from
a barrel holding finished dirt → the dirt leaves after one pull, and the next pull finds the barrel empty. Before: it
could be pulled repeatedly, or refused to leave.

---

### B11. Jade barrel overlay shows the product, the compost preview and no duplicate fluid line  [🟡]  commit 6ea48c49

**Change:** `BarrelProvider` no longer returns early for a crafting barrel; the FLUID state prints the fluid line
plus the "Transforming" progress line, the ITEM state prints the item and its name, the compost state prints the
result preview, the fluid total comes from
`fluidStorage.getCapacity()` instead of a literal `1000`, and `FENJadePlugin` removes Jade's universal fluid-storage
line for barrels (`compatibility/jade/FENJadePlugin.java:20-30`).
**Why:** HUD bug fix, pre-existing (not a port regression): the 1.21 provider replaced the entire tooltip with the
progress line whenever a recipe ran, so a busy barrel showed neither fluid nor contents. The capacity change is
cosmetically neutral — `FluidConstants.BUCKET` is 81000 and the printed value is divided by 81, so both spellings
print 1000 for the current 1-bucket barrel.
**In-game impact:** a barrel always shows what is in it when looked at with Jade.
**What actually happens in game** (derived from code): look at a barrel transforming water into witchwater. Before:
only "Transforming: 40%". After: the fluid line plus the progress line; an empty barrel no longer shows a bare
"0 / 1000mb" line from Jade's own fluid integration.

---

### B12. Jade shows infesting-leaves progress on infested leaves, not on strainers  [🟡]  commit 6ea48c49

**Change:** `FENJadePlugin` registers `InfestingLeavesProvider` for `InfestingLeavesBlock.class` instead of
`StrainerBlock.class` (`FENJadePlugin.java:20`).
**Why:** Long-standing copy-paste bug — the 1.21 base had the same wrong target
(`git show 333b0fa8:.../jade/FENJadePlugin.java`). The provider only matches `InfestingLeavesBlockEntity`, so it never
displayed anything anywhere.
**In-game impact:** "Progress: n%" now appears when looking at infested leaves with Jade.
**What actually happens in game** (derived from code): look at partially infested oak leaves with Jade → a progress
line appears; strainers lose a provider that could never match them.

---

### B13. Twelve new crucible → lava recipes (gameplay numbers)  [🟡]  commit 6ea48c49

**Change:** `datagen/provider/recipe/CrucibleRecipeProvider.java:66-84` adds lava recipes for deepslate, cobbled
deepslate, polished deepslate, blackstone, polished blackstone, stone bricks, basalt, smooth basalt, polished basalt
(20 250 droplets, i.e. 250 mB) and tuff, calcite, dripstone block (10 125 droplets, i.e. 125 mB), all
`requiresFireproofCrucible`; generated JSON under
`src/main/generated/data/fabricaeexnihilo/recipe/crucible/lava_from_*.json`.
**Why:** New content, neither a port fix nor upstream parity — none of these inputs were meltable on 1.21. The commit
message's "full pot_stones set" is loose: it is twelve hard-coded items (there is no `pot_stones` tag) and gravel
already had a recipe. Existing amounts are unchanged (the only `amount` diff lines in the crucible data are additions).
**In-game impact:** deepslate, blackstone, basalt, tuff, calcite, dripstone and stone bricks become lava sources; lava
gets noticeably cheaper on a deepslate/basalt-heavy world.
**What actually happens in game** (derived from code): insert deepslate into a lit porcelain crucible → it melts into
250 mB of lava (a quarter bucket). Before: rejected, stayed in the player's hand with no explanation. Trimming the
generosity means deleting lines 66-84.

---

### B14. Tool repair materials changed for the non-vanilla tool materials  [🟡]  commit 085dd43d

**Change:** `modules/materials/ModToolMaterials.java:16-21` rebuilds BONE, BLAZE, TERRACOTTA, CLAY, PURPUR and
PRISMARINE as `new ToolMaterial(..., repairItems)` with `ItemTags.STONE_TOOL_MATERIALS` (CLAY: `ItemTags.PLANKS`)
instead of the previous per-material ingredients (bone, blaze rod, brick, clay ball, chorus fruit, prismarine/shard).
Durability, speed and enchantment value are unchanged from 1.21; attack damage was not applied before or
after (1.21's `ToolItem`, like this branch's `CrookItem`/`HammerItem`, sets only `maxDamage`).
**Why:** Port constraint — 1.21.11's `ToolMaterial` is a record whose last component is a `TagKey<Item>` (used by
`.repairable(...)`), so a two-item ingredient list cannot be expressed. Substituting a whole tag for a specific repair
item is still an unrequested behaviour change; per-material item tags would preserve parity.
**In-game impact:** bone/blaze/terracotta/purpur/prismarine crooks repair with stone tool materials, clay crooks with
any plank.
**What actually happens in game** (derived from code): damage a bone crook, put it in an anvil with a bone. Before:
repaired. After: the anvil refuses and the player has to use cobblestone.

---

### B15. JEI gains a "Sieving" recipe category  [🟡]  commit 6ea48c49

**Change:** four new files in `compatibility/jei/` (~440 lines: plugin, category, recipe type, collector), the
`jei_mod_plugin` entrypoint (`fabric.mod.json:34-35`), an opt-in for the sieve recipe serializer's client sync
(`FabricaeExNihilo.java`), and five lang keys (`fabricaeexnihilo.jei.*`).
**Why:** New feature — the 1.21 base had **no** JEI integration at all
(`git ls-tree 333b0fa8 src/main/java/wraith/fabricaeexnihilo/compatibility/jei/` is empty). Only sieving is exposed;
there is no JEI category for crucible, barrel or hammer recipes.
**In-game impact:** players can look up sieve drops and reverse-look-up "which mesh produces raw iron pieces?".
**What actually happens in game** (derived from code): press U on a mesh → one page per (input block, mesh,
waterlogged) combination: the sieved block, the mesh, an optional water bucket, and up to 6 columns of outputs each
with a "Chance: 40.0%" tooltip. Before: JEI reported no recipes for every FEN item.

---

### B16. Sieve outputs in JEI use the 18×18 slot background  [🟡]  commit 2b32c719

**Change:** `SieveRecipeCategory` output slots call `setStandardSlotBackground()` instead of
`setOutputSlotBackground()` (`compatibility/jei/SieveRecipeCategory.java:119-121`), with a doc comment on the
geometry (`:30-34`).
**Why:** Regression introduced inside this branch by 6ea48c49; it never existed on 1.21. JEI 27 documents
`setStandardSlotBackground()` as "18x18 pixels and offset by (-1, -1)" and `setOutputSlotBackground()` as "26x26
pixels and offset by (-5, -5)" (`mezz/jei/api/gui/builder/IRecipeSlotBuilder.java:48-67` in JEI 27.35.0.90). On an
18 px grid the 26 px plates overlapped by 8 px per axis.
**In-game impact:** the sieve JEI page is readable; outputs are the same size as the input slots.
**What actually happens in game** (derived from code): open "Sieving" on a recipe with two or more outputs. Before:
oversized slot plates overlapping each other and spilling over the panel border. After: a clean 18 px grid.

---

### B17. Mesh item models declare a particle texture  [🟢]  commit 27a690d0

**Change:** `assets/fabricaeexnihilo/models/item/mesh.json:3` adds `"particle": "#all"` next to
`"all": "minecraft:block/white_wool"`.
**Why:** Port artefact: 1.21.4+ requires item-domain element models to declare a particle texture and otherwise logs
"Missing texture references in model {}" (`client/resources/model/ModelManager.java:253` in 1.21.11). All ten mesh
items inherit `item/mesh`, so one line covers them.
**In-game impact:** none in normal play — the model's particle sprite is only consumed by the breaking-item particle.
**What actually happens in game** (derived from code, not observed; the two vanilla references below are unverified):
start the game and watch the log — the warning per mesh model is gone. The only consumer, `BreakingItemParticle.Provider#getSprite`
(`client/particle/BreakingItemParticle.java:84-87`), falls back to the missing sprite when the model has none; meshes
have no durability and are not eaten or thrown, so the only way to see it is a `/particle item` command with a mesh.
After the fix that particle is white wool.

---

### B18. Crushed blocks no longer spawn black dust  [🟡]  commit 75760872

**Change:** `DummyFallingBlock#getColor` returns `state.getMapColor(world, pos).color` instead of `0`
(`util/DummyFallingBlock.java:22-32`).
**Why:** 1.21.11 made `FallingBlock#getColor` abstract (yarn 1.21.11 `mappings.tiny:102789`; Mojmap
`FallingBlock#getDustColor`), so the port had to implement it and returned a constant 0, which the falling-dust
particle splits into RGB → pure black. **Correction to the PR body and the commit message:** 1.21 did have this
method, non-abstract with `return -16777216` (verified: `javap -c diu.class` from the 1.21
`minecraft-merged.jar`), so the 1.21 base also produced black dust and `BlockColors` never got a say (the particle
factory overrides its lookup for every `FallingBlock`). This is therefore **not** a port-regression fix — it changes
behaviour relative to 1.21 too. `state.getMapColor(...).color` is what vanilla `ConcretePowderBlock`, `AnvilBlock` and
`ColoredFallingBlock` do (1.21.11 sources), so the shape of the fix is idiomatic.
**In-game impact:** hammered blocks shed dust in their own colour instead of black.
**What actually happens in game** (derived from code): hammer a stone block to crushed stone, or sand to dust, and watch
the falling dust. Every crushed block used to shed black soot (on 1.21 too). After: sand-coloured for dust/silt/crushed
calcite (settings copied from `Blocks.SAND`, `EntrypointHelper.java:71-73`) and stone-grey for the gravel-derived
crushed blocks (`:61-63`; `DefaultApiModule.java:88-99`).

---

### B19. Sieve and strainer contents still drop when broken; the mesh appears immediately  [🟡]  commit 6ea48c49

**Change:** the `onStateReplaced` drop overrides in `SieveBlock`/`StrainerBlock` moved to
`SieveBlockEntity#onBlockReplaced` (`modules/sieves/SieveBlockEntity.java:153-156`) and
`StrainerBlockEntity#onBlockReplaced` (`modules/strainer/StrainerBlockEntity.java:78-83`); `SieveBlockEntity` also
calls `markForUpdate()` after every contents/mesh mutation (`:85,94,139,223`).
**Why:** 1.21.9+ moved the "drop my contents" hook from the block to the block entity (`WorldChunk` →
`BlockEntity#onBlockReplaced`). Unlike the barrel these two block entities do not implement `Inventory`, so the default
`Container` drop does not run and there is no double drop. The `markForUpdate` calls fix a stale client model:
`SieveBlockEntityRenderer` draws the mesh item, and inserting/removing one previously only sent data on the next block
update.
**In-game impact:** breaking a sieve still returns mesh and contents; inserting a mesh updates the model at once.
**What actually happens in game** (derived from code): break a sieve holding a netherite mesh and a piece of gravel →
both drop (as on 1.21). Right-click an empty sieve with a mesh → the mesh model appears instantly (before: possibly
invisible until a neighbouring block changed).

---

## C. Build system  [🟡]

Scope: build.gradle, config.gradle, gradle.properties, settings.gradle, jitpack.yml, gradle/wrapper/*, gradlew(+bat), .github/workflows/*, changelog/*, machine-local dev_settings.properties. HEAD = `2b32c719`; line numbers are that revision's. `cd647e12` does not touch any build file, so they still apply at the final HEAD.

The build diff is a sequence, not one decision: 085dd43d broke upstream's wiring (datagen entrypoint and `jar.dependsOn runDatagen` removed, source sets gated on the dev switches, proxy settings committed, manifest conflict markers merged), 6ea48c49/f08f5b5b papered over symptoms, 4a943d08 restored upstream semantics plus the requested Loom/Gradle upgrade, and 671ca048 brought datagen back. Judge the end state; C6 records the one intermediate state that would have shipped silently, and the branch's first three commits are not buildable at all - fabric.mod.json carried merge-conflict markers at 085dd43d, 6ea48c49 and f08f5b5b (invalid JSON, which processResources parses) and only became valid again in b7732a5d.

### Review comments in this section

All nine of MattiDragon's build comments are resolved in code and mapped row by row in **§5**. The
items below explain *why* each configuration exists, which is what the review was really asking.
Comment numbers (1-9) are used below as shorthand for §5's rows.

---

### C1. Loom 1.6-SNAPSHOT (-> 1.13.1 in 085dd43d) -> net.fabricmc.fabric-loom-remap 1.19-SNAPSHOT, Gradle 8.6 -> 8.14 -> 9.8.2, build JVM 21 -> 25  [🟢]  commit 4a943d08

**Change:** `fabric-loom 1.6-SNAPSHOT` (333b0fa8:8) -> `1.13.1` (085dd43d) -> plugin id `net.fabricmc.fabric-loom-remap` with `loom_version=1.19-SNAPSHOT` (build.gradle:8, gradle.properties:6); wrapper 8.6 -> 8.14 -> `gradle-9.8.2-bin` (gradle-wrapper.properties:3). The mod still emits Java 21: `options.release.set(21)` (build.gradle:207-216), fabric.mod.json:48 `"java": ">=21"`. CI follows the build JVM instead: build.yml:23 and publish.yaml:23 use 25.

**Why:** review comment 2 asked for the newest stable Loom under the new plugin id. 1.13.1 was older than the Loom versions recorded inside the Jade and JEI jars this project compiles against (C2), so it could not consume them without a hack.

**Failure mode:** the shipped jar is still Java 21 bytecode on a 1.21.11 runtime; the risk lives on the build host, not in the mod.

**What goes wrong if this is misconfigured:** a contributor whose JAVA_HOME is still 21 gets a plugin failure before compilation, with no hint that the JDK, not the code, is wrong (the mod does not need 25). "Loom 1.19 requires JVM 25" is asserted only by this PR's own comments and commit message; not reproduced here.

---

### C2. The patchLoomManifest / compatCompileDir hack: added 6ea48c49, deleted 4a943d08  [🟢]  commit 6ea48c49 -> 4a943d08

**Change:** 6ea48c49 resolved the JEI and Jade jars into detached configurations, repackaged them into `build/compat-compile/*.jar` with the `Fabric-Loom-Version:` manifest line stripped, and compiled against those copies. 4a943d08 deleted the block and went back to plain coordinates (build.gradle:136, :141).

**Why:** Loom refuses a mod jar whose manifest declares a newer Loom than the running build. The numbers in the deleted comment ("Jade (1.14.10) and JEI (1.17.20)") are those manifest values, not mod versions: the resolved jars declare Jade `Fabric-Loom-Version: 1.14.10` and JEI `1.17.20`, both newer than 1.13.1 and older than 1.19.4. The hack was necessary on 1.13.1 and is redundant after C1 - exactly review comment 5.

**Failure mode:** none - it only ever produced compile-time copies; the runtime jars were real.

**What goes wrong if this is misconfigured:** nothing. Reverting to 1.13.1 with plain coordinates makes dependency setup fail and no jar is produced; nothing is silently shipped.

---

### C3. Repository declarations and content filters restored  [🟢]  commits 085dd43d -> 4a943d08

**Change:** 085dd43d deleted the filtered blamejared repository and the insecure modmuss50 repository and rewrote `www.cursemaven.com` to `cursemaven.com`. 4a943d08 restored all three: blamejared filtered to `com.faux.ingredientextension` + `mezz.jei` (build.gradle:20-26), modmuss50 with `allowInsecureProtocol` limited to `TechReborn` (build.gradle:40-46), cursemaven back to `https://www.cursemaven.com` (build.gradle:53-58, identical to 333b0fa8:53).

**Why:** review comment 3. Without filters every resolution queries every repository; with them a missing artifact fails fast instead of being fetched from an unintended host. The insecure HTTP entry is scoped to one group so it cannot serve anything else.

**Failure mode:** none - dependency resolution only.

**What goes wrong if this is misconfigured:** nothing. A dropped filter means compiling against a different JEI/Jade build and failing later inside that mod's plugin API; a removed repository means resolution fails and no jar exists. Neither shows at HEAD, where the state equals upstream plus one allowed group.

---

### C4. Machine-local Gradle settings moved out of the repository  [🟢]  commits 085dd43d -> 4a943d08

**Change:** 085dd43d added four `systemProp.http(s).proxy*` lines to gradle.properties; 4a943d08 removed them (no `systemProp.*` at HEAD). dev_settings.properties is untouched by this PR and untracked: .gitignore:37, absent from every tree, written by config.gradle:3-14 with mod switches only - no proxy keys.

**Why:** review comment 9 - a committed proxy pointed every other contributor's Gradle at 127.0.0.1:7897 and broke their builds. The settings now live in the author's Gradle user home (`~/.gradle/gradle.properties`), the mechanism the reviewer hinted at, applied before any project property.

**Failure mode:** none.

**What goes wrong if this is misconfigured:** nothing. If the block were still committed, every other contributor's resolution fails with connection refused and the build never starts - contributor-breaking, never a runtime mistake. This is the correct fix for comment 9.

---

### C5. Datagen run inherits client, not server  [🟢]  commit 4a943d08

**Change:** `inherit server` (333b0fa8:135) -> `inherit client` (build.gradle:151), with a comment. The `//vmArg "-Dfabric-api.datagen.strict-validation"` line (build.gradle:156) is unchanged in substance - already commented at 333b0fa8:140, deleted as collateral in 085dd43d, restored verbatim in 4a943d08.

**Why:** on 1.21.11 the model generators are client-side. ModelProvider imports `net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider` and `net.minecraft.client.data.*` (ModelProvider.java:4,6-8) and FENDatagen registers it unconditionally (FENDatagen.java:19); those classes do not exist in a server run. Strict validation has no rationale anywhere in this PR - it is inherited state, so its net change is zero.

**Failure mode:** none directly; it decides whether item models exist at all (C7).

**What goes wrong if this is misconfigured:** nothing is generated if this is wrong - the datagen run dies with NoClassDefFoundError for the client datagen class before writing, and because `jar` depends on `runDatagen` (C7) the build fails instead of shipping empty data. Leaving strict validation off keeps datagen tolerant of drifted providers, which is how 671ca048 had to clean up manually.

---

### C6. clean.delete re-enabled, and the fabric-datagen entrypoint restored  [🟢]  commits 6ea48c49 / 4a943d08 / 671ca048

**Change:** upstream deletes the generated tree on `clean` (333b0fa8:166). 6ea48c49 commented it out with a five-line justification; 4a943d08 re-enabled it (build.gradle:182). 085dd43d had deleted the `fabric-datagen` entrypoint; 671ca048 restored it (fabric.mod.json:37-39).

**Why:** the 6ea48c49 comment claimed the tree was "the ONLY source ... the fabric-datagen entrypoint is not wired up ... deleting it here would silently drop 1136 tracked files". The premise was true for the branch then; the correct fix was to restore the entrypoint, not to pin the tree. Its number was wrong: 1126 files at that commit, 1066 at HEAD.

**Failure mode:** none in the final state; in the intermediate state it removes all recipes.

**What goes wrong if this is misconfigured:** at 4a943d08 the tree was clean-deleted and `jar` depended on a datagen run with no entrypoint, so `clean build` produced a jar with zero generated files: the build succeeds, the jar installs, every FEN recipe/tag/item model is missing in game. That silent window is invisible at HEAD and is the reason entrypoint and `dependsOn runDatagen` must stay together.

---

### C7. Generated data freshness: jar dependsOn runDatagen, and the three data paths  [🟢]  commit 4a943d08

**Change:** 085dd43d removed `dependsOn tasks.runDatagen`; 4a943d08 restored it (build.gradle:223) with `from("src/main/generated")` minus README/.cache (:224-227). Data also reaches the dev runtime via `runtimeOnly files("src/main/generated")` (:111) and `loom.mods.modFiles` (:165-170). The `resources.srcDir("src/main/generated")` block added in 085dd43d was removed again, and with it the `duplicatesStrategy = DuplicatesStrategy.EXCLUDE` workaround (base and HEAD have neither).

**Why:** `dependsOn` makes freshness structural: every jar re-runs datagen, so a stale tree cannot ship. The srcDir was a second copy of the same files (processed into build/resources/main and added to the jar again), which is the duplicate-entry failure the strategy masked; the two inputs are disjoint now (generated 1066 files vs resources 210, empty intersection), so the strategy is dead weight.

**Failure mode:** none directly; this is what makes C6's deletion safe.

**What goes wrong if this is misconfigured:** nothing. Cost: `./gradlew build` starts a Minecraft client per jar, writes into a git-tracked tree, and on a machine without the Loom cache `--offline` fails in `runDatagen` before the jar exists - a loud failure, never stale data. If a generated path ever collides with a resource, the jar task fails on a duplicate entry instead of silently picking one.

---

### C8. processResources no longer rewrites fabric.mod.json  [⚠️]  commits f08f5b5b -> 4a943d08

**Change:** f08f5b5b added ~25 lines that removed the jade/modmenu entrypoints and `custom["megane:modules"]` from the copied manifest and re-added them only when the matching switch was on, and dropped `waila_plugins.json` when WTHIT was off. 4a943d08 deleted both, leaving only version expansion and the JSON minifier (build.gradle:189-205).

**Why:** the rewriting existed only because 085dd43d gated source sets on the switches, so a switch-off build had no plugin class while the manifest still named it, and Fabric aborts the launch in that case. With every working compat compiled unconditionally (comment 8) the classes are always present. HEAD lists modmenu, jade and jei_mod_plugin unconditionally (fabric.mod.json:21-23,31-36) and all three classes are compiled.

**Failure mode:** none at HEAD.

**What goes wrong if this is misconfigured:** nothing now; those entrypoints are safe as soft dependencies because Fabric/Jade/ModMenu/JEI only read their own namespace when their mod is present. The risk is structural: safety now depends on the exclusion list (build.gradle:91-93) never covering jade, jei, wthit or ModmenuPlugin - if it did, the game aborts at startup for every user with "entrypoint class not found", and the guard that prevented that is gone. Only rei, emi and recipeviewer are excluded, and no compiled code references them (grep: only excluded rei/emi sources import `compatibility.recipeviewer`).

---

### C9. Mod flags now only toggle runtime loading; three switches are dead  [🟢]  commits f08f5b5b -> 4a943d08

**Change:** config.gradle still reads/writes dev_settings.properties and exports nine booleans (config.gradle:3-24), but build.gradle uses them only in `modRuntimeOnly` gates (build.gradle:113-139). Defaults were flipped in f08f5b5b: rei true -> false, wthit false -> true, jade false -> true, megane added true (config.gradle:8).

**Why:** the switch used to decide compilation, so a switch-off build silently shipped a different mod; comments 4/7/8 ask for one set of sources compiled with switches describing only the dev runtime. The default flip was required while the gate still existed (`rei=true` would have compiled non-porting sources on a fresh checkout); after 4a943d08 it is inert.

**Failure mode:** none - dev_settings.properties is untracked and never reaches a release jar.

**What goes wrong if this is misconfigured:** nothing. rei and emi are read but referenced nowhere in build.gradle (grep: no `settings.rei` / `settings.emi`), and megane is defaulted true while its only use is inside C10's commented block. Toggling them has no effect and raises no warning, which is why they should be removed or documented.

---

### C10. Megane runtime deps commented out; the integration compiles and is registered again, but has nothing to load  [⚠️]  commit 4a943d08, entry restored in cd647e12

**Change:** the `if (settings.megane) { modRuntimeOnly ... }` block became comments (build.gradle:129-134); only `modCompileOnly "lol.bai.megane:megane-api:$megane_version"` (:125) stays. The `custom["megane:modules"]` manifest entry went away too - b7732a5d dropped the whole `custom` section, and `2b32c719` has no custom key; `cd647e12` restores that one entry.

**Why:** stated reason: Megane 8.4.0 targets 1.21.x and "its title screen mixin fails on startup". Plausible (no 1.21.11 Megane build exists) but unverified here - no jar, log or crash report is in the repository.

**Failure mode:** none - there is no Megane build for 1.21.11, so the restored entrypoint has nothing to attach to and the HUD providers never load.

**What goes wrong if this is misconfigured:** nothing, because the mod cannot load on 1.21.11 today. The smell is the silent-loss shape: compatibility/megane/* is compiled and megane-api is on the compile classpath, so `FENMeganeModule` ships in the jar; with the manifest entry missing (as at `2b32c719`) it could never be constructed, and "deferred until Megane ships" and "forgot the manifest entry" were indistinguishable. `cd647e12` restores the entry, so the integration can register as soon as a Megane 1.21.11 build exists; the remaining gap is that the dev runtime deps are still commented out, so it cannot be tested here.

---

### C11. Gradle wrapper 8.14 -> 9.8.2, and why gradlew.bat shows 186 changed lines  [🟢]  commit 4a943d08

**Change:** `gradle-9.8.2-bin` (gradle-wrapper.properties:3), new gradle-wrapper.jar (43462 -> 43764 bytes), gradlew 12 lines, gradlew.bat +94/-92.

**Why:** Gradle 9 is required by the Loom 1.19 line; wrapper and scripts are upstream artefacts and must move together.

**Failure mode:** none.

**What goes wrong if this is misconfigured:** nothing. Reviewer note: the real gradlew.bat change is ~12 lines (Gradle 9 launches the wrapper with `-jar` instead of `-classpath`, adds an SPDX header, rewrites the CLASSPATH line); the rest is a line-ending flip - 0 CR bytes at 333b0fa8, 94 CRLF lines at HEAD, and there is no .gitattributes to normalise it. `git diff --ignore-cr-at-eol` shows the real diff. Nothing to review beyond "this is what Gradle 9 emits".

---

### C12. configtoolkit back to an annotation processor; dependency bumps  [🟢]  commits 9d5042ab, 085dd43d, 6ea48c49, 4a943d08

**Change:** `modImplementation include(...)` -> `modImplementation annotationProcessor(include(...))` (build.gradle:108, 9d5042ab) with the eight hand-written `Mutable*Config.java` deleted. Version bumps: modmenu 11.0.1 -> 17.0.1, wthit 12.2.1 -> 18.2.3, new badpackets 0.11.1 (:122-123), jade 15.0.4 -> 21.1.1 -> 21.1.6, yacl 3.5.0 -> 3.8.1, configtoolkit 1.1.1 -> 1.1.2, new `jei_version=27.35.0.90`. REI/EMI/architectury/cloth-basic-math dependencies removed in 085dd43d and not restored; TechReborn retargeted to `1.19` for two commits, back to `1.21` in 4a943d08 (:114).

**Why:** upstream declares the processor and does not commit generated configs (the PR body's account of #78's wrong assumption); versions must match 1.21.11; the viewers cannot return until ported, and comment 7 asks to comment out rather than restructure.

**Failure mode:** none - only YACL and configtoolkit ship; the rest are dev-runtime or compile-only.

**What goes wrong if this is misconfigured:** nothing. Removing the processor again breaks compilation of any code touching Mutable*Config, so it cannot fail silently. Three properties are now dead - `emi_version`, `rei_version`, `architectury_version` (gradle.properties:22-24) are referenced by no dependency line - a question for the author, not a bug.

---

#### CI / release

- build.yml runs `./gradlew build` on every push with temurin 25 (build.yml:18-28) and uploads build/libs. Because `jar` depends on `runDatagen` (C7), CI now runs the datagen client on every push and a datagen failure fails the workflow; on a cold Gradle cache this adds the datagen setup to every push, and a datagen run needs network for first-time asset/runtime downloads (derived from the file; no CI run observed).
- publish.yaml is a workflow_dispatch wrapper around `MattiDragon/MattiDragon/.github/workflows/publish-mc-mod.yaml@00051ad2` with `java-version: 25` (publish.yaml:22-23); the reusable workflow itself is not in this repository.
- jitpack.yml was NOT updated: still openjdk21 / 21.0.3 (jitpack.yml:1-5) while the build needs a JVM 25, and publish.yaml:24 still passes `jitpack-safe-version: true`. A JitPack build of this branch would run Gradle 9.8.2 + Loom 1.19 on Java 21 and fail. This is the one release-path defect left in the build diff (derived from the two files, not observed on JitPack).
- changelog/2.0.0+1.21.md renamed to 2.0.0+1.21.11.md in b7732a5d; it is the only changelog at HEAD.
- settings.gradle unchanged (Fabric plugin repo only), so Loom resolves from maven.fabricmc.net.

#### Consequences matrix (configuration mistake -> observable failure)

| Mistake | First visible symptom | Class |
|---|---|---|
| Build host on JDK 21, or JitPack per jitpack.yml | Gradle/plugin fails before compiling | build failure |
| Loom left at 1.13.1 with plain Jade/JEI coordinates | dependency setup rejected, no jar | build failure |
| datagen run inherits `server` | datagen dies on the client class, build aborts | build failure |
| `jar` loses `dependsOn runDatagen` while clean deletes the tree | jar builds, installs, has no recipes/tags/models | **silent data loss** |
| `fabric-datagen` entrypoint missing | datagen writes nothing, jar ships empty data | **silent data loss** |
| `resources.srcDir("src/main/generated")` re-added | jar fails on duplicate entries | build failure |
| duplicatesStrategy reintroduced instead of fixing the duplicate source | build passes, one of two conflicting files wins | **silent wrong data** |
| Exclusion list extended to jade/jei/wthit/ModmenuPlugin | game aborts at launch: entrypoint class not found | **startup crash for every user** |
| Megane block re-enabled without a 1.21.11 Megane build | stated failure: title-screen mixin crash | startup crash (unverified) |
| Committed `systemProp.*` proxy (comment 9) | every other contributor's resolution refused | build failure for others |
| Content filter removed from a repository | dependency silently resolved from another host | **silent wrong dependency** |

## D. Data regeneration  [🟢]

### D0. Overview and the one rule that matters  [🟢]  commits 085dd43d, 4a943d08, 671ca048

`src/main/generated` is committed datagen output — 1066 tracked files at head (`git ls-tree -r 2b32c719 -- src/main/generated | wc -l`). The port commit excluded the whole datagen package from the build (085dd43d), so from 085dd43d to 4a943d08 the generated tree was maintained **by hand**; 4a943d08 restored the Gradle wiring, and 671ca048 ported the providers and regenerated the tree from scratch. Since then `jar` depends on `runDatagen` (build.gradle:223) and `clean` deletes the tree (build.gradle:182), so the jar can never ship stale data: **if the code is correct the data is correct.** Review the provider code and the hand-written files in D4, not the 1066 JSON files. The one thing that rule does *not* cover is data whose correctness the provider API cannot express — that is exactly the bug in D5 (found at `671ca048`, fixed in `cd647e12`).

### D1. Why datagen was dead after the port, and what revived it  [🟢]  commits 085dd43d, 4a943d08, 671ca048

**Change:** three independent layers had to be repaired.

1. *Build layer (085dd43d, undone by 4a943d08).* The port added `sourceSets { main { java { exclude 'wraith/fabricaeexnihilo/datagen/**' … } } }`, deleted the `loom.runs.datagen` block, `copyDatagenOverrides`, `clean.delete("src/main/generated")` and the `jar { from("src/main/generated") }` block (all visible as removals in `git show 085dd43d -- build.gradle`). 4a943d08 removed the datagen exclude, re-added the run block and made it `inherit client` instead of `inherit server` — since 1.21.11 the model generators live in the client datagen package (build.gradle:146-161).
2. *API layer (671ca048).* 1.21.11 moved `net.minecraft.data.client` → `net.minecraft.client.data` and `net.minecraft.data.server.recipe` → `net.minecraft.data.recipe`; `FabricRecipeProvider` now wants `getRecipeGenerator(WrapperLookup, RecipeExporter)` (BaseRecipeProvider.java:41-52); recipe builders take a `RegistryEntryLookup<Item>` first (BaseRecipeProvider.java:78); `RecipeExporter#accept` takes a `RegistryKey`, so plain ids no longer get rewritten into this namespace and recipes would have been written under `minecraft:` (new `RecipeIds.java:12-21` routes every id through `FabricRecipeExporter#getRecipeIdentifier`); tag builders take `RegistryKey`s (BlockTagProvider.java:26-30, ItemTagProvider.java:29-32).
3. *Missing pieces (671ca048).* The `fabric-datagen` entrypoint was not registered at all — it is the +6 lines in fabric.mod.json, of which `"fabric-datagen": ["wraith.fabricaeexnihilo.datagen.FENDatagen"]` is fabric.mod.json:37-38. `EntrypointHelper.callEntrypoints` had lost the datagen branch, so recipes referring to TechReborn/Mythic Metals items hit nulls; it now registers the dummy items and skips the `shouldLoad` filter when `fabric-api.datagen` is set (EntrypointHelper.java:38-44, DatagenItems.java:92-100 — dummy items also need `Item.Settings#registryKey` since 1.21.2). The one-line mixin addition, **fabricaeexnihilo.mixin.json:15 `"ItemModelGeneratorAccess"`**, is the accessor mixin that makes item definitions writable: 1.21.11 made `ItemModelGenerator#register` private, so the mixin exposes the definition sink `output`, the model writer `modelCollector` and the `register` invoker (mixins/ItemModelGeneratorAccess.java:21-36). Every item now gets both halves — model json *and* `assets/<ns>/items/<id>.json` (ModelProvider.java:186-191).

**Why:** without 1-3 the run either does not start or silently produces data under the wrong namespace; with them, the committed tree is a build artefact of the code.

**In-game impact:** no player-visible change by itself; the visible effect is that data now tracks code and can no longer drift by hand.

**What actually happens in game:** no scenario. (Reviewer note: this mixin used to live in the **common** `mixins` array although `ItemModelGenerator` is a client-only class, while the `"client"` array was empty; a dedicated-server startup was not tested here. `cd647e12` moves it into the `client` array (§6.4), which a dedicated server never loads, so the risk is gone.)

### D2. The 1.21.11 data-layout migration  [🟢]

**Change:** the *directory* rename that this migration is usually associated with is **not** in this PR — `recipe/`, `loot_table/`, `advancement/`, `tags/item|block|fluid` are already singular at the base commit (`git ls-tree -r 333b0fa8 -- src/main/generated`), i.e. 1.21 had already done it. What actually changes is the **item-model system introduced in 1.21.4**: an item is no longer rendered from `models/item/<id>.json` by convention but from an explicit definition file under `assets/<ns>/items/<id>.json`.

Before (base, 1.21) — only a model, resolved by item id:

    models/item/oak_barrel.json       {"parent":"fabricaeexnihilo:block/oak_barrel"}
    models/item/iron_mesh.json        {"parent":"fabricaeexnihilo:item/mesh"}

After (head) — a definition that names the model, plus the model itself where one is still needed:

    assets/fabricaeexnihilo/items/oak_barrel.json
        {"model":{"type":"minecraft:model","model":"fabricaeexnihilo:block/oak_barrel"}}
    assets/fabricaeexnihilo/items/iron_mesh.json
        {"model":{"type":"minecraft:model","model":"fabricaeexnihilo:item/iron_mesh",
                  "tints":[{"type":"minecraft:constant","value":-8947849}]}}
    assets/fabricaeexnihilo/models/item/iron_mesh.json   {"parent":"fabricaeexnihilo:item/mesh"}

Block items now point their definition straight at the block model, which is why 70 `models/item/<block>.json` files could be deleted (671ca048, 70 `D`). Counts at head: 163 `items/`, 93 `models/item/`, 75 `models/block/`, 76 `blockstates/`.

**Why:** 1.21.4 moved item rendering to definitions, and `ItemModelGenerator#register` is private in 1.21.11, hence the accessor mixin.

**In-game impact:** none if both halves exist; an item without a definition is simply invisible/error-textured in inventories, which is what the hand-maintained tree risked.

**What actually happens in game:** an item sits in the inventory and renders with its model; before 671ca048 the mesh items relied on hand-copied definitions under `src/main/resources` (10 files, moved `R100` into the generated tree by 671ca048 — content identical). The mesh tint is now generated from `MeshItem#getColor` (ModelProvider.java:150-152), so the 10 hand-written copies are gone.

**`.cache` files:** the 13 files under `src/main/generated/.cache/<sha1>` on a developer disk are the datagen up-to-date cache (header `// 1.21.11 <TAB> -999999999-01-01T00:00:00 <TAB> FabricaeExNihilo/<Provider name>`, then `<input hash> <output path>` per file). They are **not** committed: `.gitignore:46` ignores `src/main/generated/.cache` and `git ls-files` reports 0. They are deterministic (fixed timestamp), and the jar excludes them (build.gradle:226).

### D3. How much was regenerated, and the rule that follows  [🟢]

**Change:** 671ca048 is 236 files, +828/−1139; 210 paths touch `src/main/generated` (130 `M`, 70 `D`, 10 `R`, which numstat reports as 210 files, +392/−868 (the 10 moved definitions count as additions)), and the other 26 are 24 Java paths (21 `M`, 2 `A`, 1 move), fabric.mod.json and the mixin json. The shape of the generated diff is almost entirely mechanical:

- 117 files change exactly one line — 70 `items/*.json` (definition repointed from `item/<id>` to `block/<id>`) and 47 `recipe/*.json` (only the missing trailing newline).
- 2 recipes change 1/2 lines: `recipe/hammer/netherite.json` and `recipe/mesh/netherite.json` lose `"count": 1` (default value, semantically identical).
- 11 tag files are rewritten (see D5), 70 `models/item/<block>.json` are deleted, 10 mesh definitions are moved in.
- PR-wide the bucket is 530 generated files, +1785/−2207 (175 `A`, 285 `M`, 70 `D`); the 175 additions are 163 item definitions from the port commit 085dd43d plus 12 crucible recipes from 6ea48c49.

**Why:** this is the reviewer's licence to skip file-by-file reading.

**In-game impact:** none — see D5 for the one place where "code correct ⇒ data correct" does not hold.

**What actually happens in game:** no scenario. The rule to apply: the tree is regenerated by `jar` (build.gradle:218-228, `dependsOn tasks.runDatagen`, `exclude ".cache"`), deleted by `clean` (build.gradle:182), and the dev runtime reads it directly (build.gradle:111, :168). A wrong committed JSON is therefore overwritten by any normal build; a wrong *provider* is not. Spot-check the provider, not the output.

### D4. Files inside the same directories that are NOT generated  [🟢]

These are hand-written and will never be rewritten by `runDatagen`; they need real review:

| path | files | what |
|---|---|---|
| `src/main/resources/data/fabricaeexnihilo/recipe/compat/**` | 58 | all hand-written compat recipes: `mythicmetals` 29, `common` 15, `techreborn` 10, `modern_industrialization` 3, `indrev` 1 |
| `src/main/resources/assets/fabricaeexnihilo/lang/{en_us,zh_cn}.json` | 2 | see D6 |
| `src/main/resources/assets/fabricaeexnihilo/textures/**` | 133 | block/item/GUI textures |
| `src/main/resources/assets/fabricaeexnihilo/models/item/mesh.json` | 1 | the only hand-written model left; the mesh template, `parent: block/block`, 8 elements, `tintindex: 0` |
| `src/main/resources/{fabric.mod.json, fabricaeexnihilo.mixin.json, waila_plugins.json, icon.png}` | 4 | metadata |
| deleted: `assets/emi/recipe/defaults/fabricaeexnihilo.json`, `textures/gui/emi/tree_icons.png`, `textures/gui/rei/glyphs.png` | 3 | EMI/REI assets, removed because those viewers are not ported |

`src/main/resources/data` contains nothing but the 58 compat recipes — every other data file in the mod is generated, which makes the hand-written set easy to audit.

**Why / In-game impact:** `mesh.json` is the one to look at: 671ca048 stopped hand-writing the mesh *definitions*, but the model they point at is still hand-written and was edited twice in this PR (tintindex `1` → `0` for the port, then `particle` added in 27a690d0). If that model is wrong, every mesh item renders wrong.

**What actually happens in game:** no scenario of its own; a wrong `mesh.json` means the 10 mesh items render as a flat cross of wool-tinted bars with the wrong particle, not a crash.

### D5. Generated tags hard-referenced TechReborn `rubber_*` blocks  [🔴] [⚠️]  present at 671ca048, fixed in cd647e12

**Status:** Fixed in `cd647e12`. Nine generated tag files write their optional entries as
`{"id": …, "required": false}` again — the `rubber_*` / `infested_rubber_leaves` entries in the files listed
below are optional, so a client without Tech Reborn loads every tag. The fix moves the providers back onto
the raw tag builder before `.mapped(...)` wraps it (that wrapper is what turned `addOptional(...)` into a bare
id), and a second consecutive `runDatagen` reports `written: 0`, so the regenerated tree is stable and the
change lives in the providers rather than in hand-edited output. The analysis below is the pre-fix state at
`2b32c719`, kept as the evidence chain.

**Change:** 31 tracked generated files mention `rubber`; five of them are tags whose entries do not exist when Tech Reborn is absent:

    tags/item/barrels.json        …, "fabricaeexnihilo:rubber_barrel"
    tags/item/crucibles.json      …, "fabricaeexnihilo:rubber_crucible"
    tags/item/sieves.json         …, "fabricaeexnihilo:rubber_sieve"
    tags/block/infested_leaves.json  …, "fabricaeexnihilo:infested_rubber_leaves"
    data/minecraft/tags/block/mineable/axe.json   rubber_barrel/crucible/sieve/strainer

plus `minecraft:mineable/shovel` and `fabricaeexnihilo:crookables`, which reference `#fabricaeexnihilo:infested_leaves` and therefore fail with it. Everything else that mentions rubber is conditioned: `recipe/barrel/rubber.json`, `advancement/recipes/decorations/barrel/rubber.json` and `loot_table/blocks/rubber_barrel.json` all carry `"fabric:load_conditions": [{"condition":"fabric:all_mods_loaded","values":["techreborn"]}]`; blockstates/models/item definitions are inert without the registered block.

**Why (mechanism, this is the finding; the API-level cause is inferred from the provider shape plus the emitted
output, not stepped through in the 1.21.11 datagen internals):** during datagen `EntrypointHelper` forces every compat module on (EntrypointHelper.java:38-44), so `ModBlocks.BARRELS` et al. contain the rubber entries; the tag providers then write them out. In 1.21 the same tags were written with optional entries — base `tags/item/barrels.json` is an array of `{"id":"fabricaeexnihilo:rubber_barrel","required":false}`. After the regeneration every entry is a **bare id**, i.e. a hard requirement. The cause is the new 1.21.11 builder shape: the providers wrap the builder with `.mapped(...)` (BlockTagProvider.java:26-30, ItemTagProvider.java:29-32) and `addOptional(Registries.BLOCK.get(id))` on that wrapper (BlockTagProvider.java:50, :76, :83, :86-88; ItemTagProvider.java:37-41) writes a plain id, while `addOptionalTag(...)` on the same wrapper still writes `"required": false` (`tags/block/crookables.json`, `hammerables.json`, `tags/fluid/hot.json` were the only files at `2b32c719` that still contained `required`). Fabric datagen offers load conditions for recipes, loot tables, advancements and dynamic registries, but **not** for tags or models — so this cannot be fixed with `withConditions`, only by emitting the entries as optional (or not emitting them).

**In-game impact:** without Tech Reborn the mod's own tag files fail to load, so the FEN advancement tab is incomplete and FEN's wooden containers lose their axe/shovel tool association; with Tech Reborn installed nothing is wrong.

**What actually happens in game** (log observed on 2026-11-16, test report §6.1 — the pre-fix state at `2b32c719`; the tree was repaired in `cd647e12`, so these errors no longer occur): 12 `ERROR` lines at startup (the report quotes 9 of them), of which 7 are tag failures (`Couldn't load tag fabricaeexnihilo:barrels … missing: fabricaeexnihilo:rubber_barrel`, same for crucibles/sieves/infested_leaves/crookables/minecraft:mineable/axe/minecraft:mineable/shovel), then `Couldn't parse data file 'fabricaeexnihilo:barrel' … Missing tag: 'fabricaeexnihilo:barrels' in 'minecraft:item'` and `Couldn't load advancements: [blood, porcelain, clay, clay_crook_broken, crucible]`. The `Missing tag` error is the proof that the tag ends up absent rather than partially filled: a failed tag file contributes nothing. Consequences, derived from that plus the code (not observed in game): the player opens the FEN advancement tab and 5 advancements are missing and `barrel` is gone; mining a wooden barrel/crucible/sieve/strainer with an axe gets no axe speed bonus (they are no longer in `minecraft:mineable/axe`, so `isSuitableFor` is false, the blocks still drop); dust/silt/crushed blocks likewise lose the shovel bonus; and the crook loses its speed bonus on leaves because `CrookItem#isCorrectForDrops` tests the now-empty `crookables` tag (CrookItem.java:31-38) — the crooking *drops* still work, they come from `ToolRecipe` through `BlockHarvestMixin`, not from the tag. With Tech Reborn installed all of these blocks exist and every tag loads, so the failure is invisible in the author's dev environment.

Second, smaller data bug visible in the same files, **pre-existing** and faithfully reproduced by datagen: `BlockTagProvider.java:71` assigns the "hoe" builder to `BlockTags.SHOVEL_MINEABLE`, so there is no `minecraft:mineable/hoe` file at all and `#fabricaeexnihilo:infested_leaves` lands in `mineable/shovel`. Base had the same line (`333b0fa8:…/BlockTagProvider.java:61`), so it is not a regression of this PR — but a reviewer reading the generated tree will see it, and it is worth fixing while the provider is being touched.

### D6. Language files  [🟡]

**Change:** not datagen. `FENDatagen` registers 12 providers and none of them is a language provider (FENDatagen.java:19-32), so both files are hand-written: `en_us.json` 390 → 471 keys (+81, 0 removed) and `zh_cn.json`, which is a **new file** added by the port commit 085dd43d (`git log --diff-filter=A`), 471 keys, key-for-key identical to en_us. The 86/33 line numbers are mostly whitespace: the file lost its blank separator lines.

The 81 new en_us keys are: 75 `item.fabricaeexnihilo.*` names for blocks that also have a `block.fabricaeexnihilo.*` name (76 such keys remain), 5 `fabricaeexnihilo.jei.*` strings for the new JEI sieve category, and 1 `fabricaeexnihilo.hud.crucible.contents` ("Solid: %s x%s").

**Why:** since 1.21.2 an item derives its name from its registry key in `Item.Settings`, and this mod never calls `useBlockPrefixedTranslationKey` (0 hits in `src/main/java`), so block items are named from `item.*`; the port added those 75 keys because the items would otherwise show raw keys. The JEI keys belong to 6ea48c49.

**In-game impact:** with the keys, block items show "Oak Barrel"; without them they would show `item.fabricaeexnihilo.oak_barrel`.

**What actually happens in game:** the player hovers an oak barrel in the inventory and reads "Oak Barrel"; the FEN advancement/HUD and the JEI sieve page are translated in en_us and zh_cn. The 75 `item.*` keys vs 76 `block.*` keys duplication looks untidy but is not a defect (block-side strings still resolve). Whether the `block.*` keys are still reachable at all was **not** verified in game.

## E. Removals  [🟢]

Scope: the 125 `D` entries of `git diff --name-status 333b0fa8..HEAD`, grouped by **reason**
(dead code / replaced by a new system / genuinely removed). Generated-data deletions are §D's
business; they are classified in E.4, not listed as removals.

**E0 — The 125 deletions, classified.**

| bucket | files | verdict |
|---|---|---|
| `src/main/generated/assets/fabricaeexnihilo/models/item/*.json` | 70 | Not a removal — item models moved to 1.21.4+ item *definitions* (`assets/fabricaeexnihilo/items/*.json`). See E.4.1 and §D. |
| `src/main/resources/data/.../recipes/compat/**` | 47 | Not a removal — directory rename `recipes/`→`recipe/` plus the 1.21.11 recipe format. See E.4.2. |
| `src/main/java/**` | 4 | Real source removals; all four are covered in E.2. |
| `src/main/resources/assets/**` (EMI defaults json + 2 GUI textures) | 3 | Genuinely removed, currently harmless (E.3.1). |
| `changelog/2.0.0+1.21.md` | 1 | Rename (E.4.4). |

Two further Java deletions in the commit list are **not** in the 125 and are **net-zero against
base**: `util/ModTestRunner.java` (382 lines, added 085dd43d, removed f08f5b5b) and the eight
`config/Mutable*Config.java` (added 085dd43d, removed 9d5042ab). Neither exists at 333b0fa8.
They get one short entry each (E.1.1, E.2.4) because a reviewer reading the commit list will
otherwise assume the branch removed them from upstream.

**E1 — Dead code. Zero in-game effect.**

### E1.1 In-game audit runner, `util/ModTestRunner.java` (382 lines)  [🟢]  commit f08f5b5b

**Change:** Deleted the class, its only call site (`FabricaeExNihilo.java`, −2 lines) and the
`tasks.named('runServer')` block in `build.gradle` that passed `-Dfabricaeexnihilo.test`.
**Why:** It was port scaffolding: a `ServerLifecycleEvents.SERVER_STARTED` hook that placed
blocks at y=100, ran the barrel/sieve/hammer/crook paths and wrote `[AUDIT n/5] ... PASSED` to
the log. It shipped in release jars and always registered a server-start listener.
**In-game impact:** None.
**What actually happens in game:** Nothing. It registered **no command**, no item and no API —
a grep of the deleted file finds `ServerLifecycleEvents` as its only registration
(`ModTestRunner.java:41-46` at `f08f5b5b^`) — and it was a no-op unless the JVM was started with
`-Dfabricaeexnihilo.test=true` or `FEN_TEST=true`. A player never had a way to reach it; a dev
running `runServer -Dfabricaeexnihilo.test=true` loses the log audit and must test by hand.
Nothing at HEAD references it (`git grep ModTestRunner\|FEN_TEST HEAD` is empty).

**E2 — Replaced by a new system. Feature preserved, old hook gone.**

### E2.1 `client/BlockModelRendererFlags` + `mixins/client/BlockModelRendererMixin` (client mixin array emptied)  [🟢]  commit 085dd43d

**Change:** Two classes deleted (31 + 34 lines) and `"client": ["client.BlockModelRendererMixin"]`
replaced by an empty array in `fabricaeexnihilo.mixin.json`; the array stays empty through `2b32c719`
(`cd647e12` later puts `ItemModelGeneratorAccess` there - §6.4).
**Why:** They only existed as a pair, to clamp the r/g/b a *block model* is drawn with while a
barrel renders its compost stack: a static flag class (`BlockModelRendererFlags.java:11-19`) read
by three `@ModifyVariable` injectors on `BlockModelRenderer#renderQuads`
(`BlockModelRendererMixin.java:20-33`, `MathHelper.clamp(red, 0, 1)`). 1.21.11 replaced that
render path with submitted render commands that take the tint as explicit arguments, which the
port uses directly: `queue.submitBlockStateModel(matrices, TexturedRenderLayers.getEntityCutout(),
state.compostBlockModel, state.compostR, state.compostG, state.compostB, …)`
(`BarrelBlockEntityRenderer.java:151`). The only call sites of the flag
(`BarrelBlockEntityRenderer.java:97-99` at base) are gone, so the mixin had nothing left to do.
**In-game impact:** None expected; the compost tint is still computed
(`Color.average(Color.WHITE, 0x573f17, progress^4)`, now stored on the render state).
**What actually happens in game:** Player fills an oak barrel with 7 dirt and waits. The block
model of the composting item is still drawn inside the barrel, tinted from the compost colour
towards crisp brown as progress rises. Scenario is *derived from code, not observed in game*.
The clamping itself was only reachable through that one call site, so no other rendering changed.

### E2.2 `mixins/EnchantmentMixin` (bonus enchantability)  [🟢]  commit 085dd43d

**Change:** Deleted the 20-line mixin and removed `"EnchantmentMixin"` from the mixin list.
**Why:** It `@ModifyReturnValue`d `Enchantment#isAcceptableItem` (`EnchantmentMixin.java:14-20`)
to return true for items in `BonusEnchantingManager.DATA`. In 1.21.11 `Enchantment` is a final
record, so the mixin cannot be applied at all; the port moved the same check onto Fabric's
supported hook, `EnchantmentEvents.ALLOW_ENCHANTING`
(`BonusEnchantingManager.java:29-42`; base `BonusEnchantingManager.java:33-34` did the same test
inside the mixin). The lookup key also changed from the `Enchantment` instance to its
`RegistryKey`.
**In-game impact:** None expected — the same item/enchantment pairs are permitted.
**What actually happens in game:** Player puts a string mesh and a book into an enchanting table
and gets Efficiency or Fortune offered (config-gated, `BonusEnchantingManager.java:58-63`);
barrels/crucibles get Efficiency/Thorns/Fire Aspect; hammers and crooks Efficiency
(`:45-66`). Same list before and after. Scenario *derived from code, not observed in game* —
the equivalence of `isAcceptableItem` and `ALLOW_ENCHANTING` is Fabric's claim, not tested here.

### E2.3 `client/FENItemColorProvider` (item tint)  [🟢]  commit 085dd43d

**Change:** Deleted the provider (21 lines) and the `ColorProviderRegistry.ITEM` registration.
Base `FabricaeExNihiloClient.java:40-46` registered it for `MESHES`, `ORE_PIECES` and
`INFESTED_LEAVES`; HEAD `FabricaeExNihiloClient.java:40` registers only the **block** provider.
**Why:** 1.21.4+ moved item colour out of a runtime registry into the item model definition
("tints"), which the mod now datagens: `ModelProvider.java:151` emits
`new ConstantTintSource(((MeshItem) item).getColor(0))`, visible in
`generated/assets/fabricaeexnihilo/items/string_mesh.json`
(`"tints": [{"type": "minecraft:constant", "value": -1}]`).
**In-game impact:** None; two of the three former registrations were already no-ops.
**What actually happens in game:** Sieve meshes keep their per-tier colour in the inventory and
in JEI, now baked into the model. `ORE_PIECES` are plain `Item`s
(`ModItems.java:28,84`) and `InfestedLeavesItem#getColor` returns white
(`InfestedLeavesItem.java:14-17`), so `FENItemColorProvider`'s non-`Colored` fallback to white
was all those two groups ever got. Scenario *derived from code, not observed in game*.

### E2.4 `config/Mutable*Config.java` ×8 (398 lines)  [🟢]  commit 9d5042ab

**Change:** Deleted eight hand-written mutable config classes and restored
`modImplementation annotationProcessor(include("com.github.mattidragon:configtoolkit:…"))` in
`build.gradle` so the processor runs again.
**Why:** They duplicated what configtoolkit's annotation processor generates at build time — which
is exactly what base does: `build.gradle:96` at 333b0fa8 already reads
`annotationProcessor(include(...))`, and the processor output is not checked in upstream.
085dd43d had replaced that with checked-in copies ([commit message: assumed the processor was
broken on JDK 21]).
**In-game impact:** None — the config file format is unchanged, so existing
`config/fabricaeexnihilo.json` files load as-is.
**What actually happens in game:** (a) the author's 2026-11-16 run reports the processor output under
`build/generated/sources/annotationProcessor/java/main/wraith/fabricaeexnihilo/config/` in the local,
gitignored `build/` directory, with all eight classes (untracked; `.gitignore:10` ignores `build/**`;
no build was performed for this review); (b) the generated
`MutableBarrelConfig` has the same fields, getters and setters as the deleted copy — the only
differences are formatting, a private constructor and `sealed … permits BarrelConfig` on the
`Source` interface; (c) `git diff 333b0fa8..HEAD -- src/main/java/wraith/fabricaeexnihilo/config/`
is 5 lines, all in `WitchWaterConfig` (`Identifier.of` → `Identifier.ofVanilla` on the same five
ids), so the serialised key set and defaults are identical to base. Player edits compost rate in
the mod config, restarts, value sticks — same as before.

**E3 — Genuinely removed.**

### E3.1 REI/EMI viewer assets: EMI recipe-tree defaults + both glyph textures  [⚠️]  commit f08f5b5b

**Change:** `assets/emi/recipe/defaults/fabricaeexnihilo.json` (139 lines; EMI's list of recipes
shown in its tree by default), `textures/gui/emi/tree_icons.png`, `textures/gui/rei/glyphs.png`.
**Why:** Their only consumers are the EMI/REI sources, which are not compiled into the jar
(E.3.2). The commit message says the audit runner and these files were "scaffolding" — for the
JSON and the PNGs that is only true while the viewers stay unported.
**In-game impact:** None today (the consumers are excluded from the build); ⚠️ for the future.
**What actually happens in game:** A player installing REI or EMI sees no FabricaeExNihilo tabs at
all — no barrel/crucible/sieve category, no glyphs — because the entrypoints are gone (E.3.2).
The forward risk is real: the deleted textures are still referenced by code that is still in the
tree — `FENEmiPlugin.java:28` (`textures/gui/emi/tree_icons.png`), `FENEmiTextures.java:9` and
eight REI category classes (`BarrelCategory.java:24`, `MilkingCategory.java:24`,
`CrucibleCategory.java:23`, `SieveCategory.java:28`, `ToolCategory.java:25`,
`WitchWaterEntityCategory.java:30`, `WitchWaterWorldCategory.java:45`) still point at
`textures/gui/rei/glyphs.png`. Whoever re-enables those source sets must restore all three files
(revert of f08f5b5b for those paths) or the category screens will draw missing textures and EMI
will lose its default recipe tree.

### E3.2 REI and EMI entrypoints + unconditional build exclusion  [⚠️]  commit 085dd43d (entrypoints), 4a943d08 (exclusion)

**Change:** `rei_client` (`PluginEntry`) and `emi` (`FENEmiPlugin`) removed from
`fabric.mod.json`; `sourceSets.main.java` now unconditionally excludes
`compatibility/rei/**`, `compatibility/emi/**` and `compatibility/recipeviewer/**`;
`config.gradle` defaults to `rei:false, emi:false`.
**Why:** Those sources do not compile against 1.21.11; 4a943d08 removed the switch-gating to
answer review comment 4 ("Excluding unported code is fine, but don't gate it behind the settings").
**In-game impact:** REI/EMI integration is absent from the build, permanently until ported.
**What actually happens in game:** Player with REI installed opens the recipe viewer on a sieve;
there is no sieve category. FEN recipes are only reachable through JEI
(`jei_mod_plugin` entrypoint, `fabric.mod.json:34-36`) when JEI is installed. Covered as a
"Known unported" item — see the cross-reference there.

### E3.3 Megane module registration dropped from the manifest, restored in cd647e12  [⚠️]  commit b7732a5d (manifest), 4a943d08 (mechanism), cd647e12 (restore)

**Change:** `"custom": {"megane:modules": ["...FENMeganeModule"], "DISABLED(...), loom:injected_interfaces": {...}}`
was gone from `fabric.mod.json` at `2b32c719`; `cd647e12` restores the `custom.megane:modules` entry (the
`loom:injected_interfaces` key stays omitted - it was a no-op).
**Why:** b7732a5d dropped the whole upstream `custom` section while clearing rebase conflict
markers, on the grounds that it named `ItemModelGeneratorAccess` and `NbtPathAccess`, "neither of
which exists on this branch". That was true for the NBT accessor (absent at base and at HEAD) but
not for the megane entry: `compatibility/megane/FENMeganeModule.java` exists and still compiles at
HEAD. f08f5b5b then re-emitted the megane and jade entrypoints conditionally from `build.gradle`;
4a943d08 deleted that manifest rewriting ("That upgrade makes the Jade/JEI jar repackaging and the
manifest rewriting unnecessary"); the entry stayed missing until `cd647e12` put it back.
**In-game impact (at `2b32c719`):** With Megane installed, FEN blocks did not publish Megane data — a
player-visible integration that worked before the port was silently gone. `cd647e12` restores the
registration.
**What actually happens in game:** At base, pointing Megane's (default) tooltip/overlay at a
barrel showed its fluid and item contents (`custom.megane:modules`,
`fabric.mod.json:48-50` at 333b0fa8, plus `BarrelFluidProvider`/`CrucibleFluidProvider`). At `2b32c719`
the same player saw nothing FEN-specific: the five megane classes compile against `megane-api`
(`build.gradle:125`, `modCompileOnly`) but no entrypoint instantiated `FENMeganeModule`, and the
megane runtime deps are commented out (`build.gradle:129-133`) because 8.4.0 targets 1.21.x. There was
no build error and no log line — the integration was inert. `cd647e12` restores the entrypoint; the only
missing piece now is a Megane build that runs on 1.21.11 (plus its dev runtime dependency).

*Note on the same area:* f08f5b5b's message also describes jar-excluding `waila_plugins.json` and
pruning optional entrypoints in `processResources`; 4a943d08 removed both mechanisms. At HEAD the
only change to `waila_plugins.json` in the whole PR is a trailing newline (`f08f5b5b`), and the
integration switches only select runtime dependencies. Anything a reviewer reads in that commit
message about manifest patching no longer describes HEAD (see §C).

**E4 — Not removals, so the 125 count reconciles.**

**E4.1 The 70 `models/item/*.json`.** Replaced by item definitions: HEAD has 163 files under
`generated/assets/fabricaeexnihilo/items/`, including `oak_barrel`, `porcelain_crucible`,
`crushed_granite`, `silt`, and the block items now point straight at their block model
(671ca048 message: "the 70 redundant `models/item/<block>.json` are gone"). Generated data — §D.

**E4.2 The 47 compat recipes.** Same recipes at a new path, with the 1.21.11 recipe shape. Git
reports D+A instead of R because the content changed by more than half:
`recipes/compat/mythicmetals/osmium_from_granite.json` (base) →
`recipe/compat/mythicmetals/osmium_from_granite.json` (HEAD, added in 085dd43d) keeps the same
`input`, the same four `rolls` probabilities and the same `fabric:registry_contains` load
condition; only `"result": "…"` became `{"id": "…"}` and `"input": {"item": …}` became a plain
string. HEAD still ships 58 files under `resources/data/fabricaeexnihilo/recipe/compat/`;
the 11 the diff shows as renames (R050–R100) are the ones whose body barely changed. No compat
sieve recipe was dropped — §D/§A.

**E4.3 Two transient mesh definitions.** `generated/assets/…/items/adamantite_mesh.json` and
`carbon_mesh.json` were deleted in 6ea48c49 and re-created in 671ca048; both exist at HEAD, so
they are not in the 125.

**E4.4 `changelog/2.0.0+1.21.md` → `changelog/2.0.0+1.21.11.md`  [🟢]  commit b7732a5d.** Rename
with a 4-line content update naming the version the branch targets. No player-visible effect.

## 4. Known unported items — what is missing, and what a player actually sees  [⚠️]

Three optional integrations are unavailable on 1.21.11 in this PR. None is a hard dependency: the mod loads,
runs and plays without them, and no FEN content depends on them. The "player sees" statements below are
derived from the manifest and build wiring at HEAD, **not** observed in a running game (no run was performed
for this review).

### 4.1 REI — not ported [⚠️]

- **Scope.** `compatibility/rei` (20 java files) and the shared `compatibility/recipeviewer` bridge (5 files)
  are still in the tree but excluded from compilation: `build.gradle:91-93`. The `rei_client` entrypoint that
  the `1.21` branch declared (`fabric.mod.json:31-33` at base) is gone, and no REI dependency is declared
  anywhere in `build.gradle` any more.
- **Reason.** REI's 1.21.11 API is a rewrite: recipe fillers take `RecipeDisplay` instead of `Recipe`,
  `Display` gained `getSerializer()`, `registerRecipeFiller` became `DisplayConsumer#beginRecipeFiller`, and
  `DisplayRegistry` moved package (per the PR body; the tree cannot confirm API details because there is no
  REI artifact to compile against). `gradle.properties:23` still pins `rei_version=16.0.729`, the same version the
  `1.21` branch compiled against, so no 1.21.11 REI was ever wired in. Porting means adding a
  `RecipeDisplay` implementation per recipe type (the PR body counts nine).
- **Not affected.** All FEN content, JEI integration, Jade/WTHIT.
- **Player sees.** With REI installed: FEN appears and works normally, but the REI panel contains no FEN
  recipes or categories (no sieve/barrel/crucible entries). No crash — FEN declares no `rei_client` entrypoint
  and its `depends` names only `fabricloader`, `fabric-api`, `minecraft`, `java`
  (`fabric.mod.json:44-49`); REI's plugin discovery is that entrypoint (the mechanism the `1.21` branch used), so
  there is nothing for it to fail on.
- **Nit for the reviewer.** `rei` is still a flag (`config.gradle:8` = `false`, `build.gradle:74`) but the
  exclusion is unconditional, so setting `rei=true` changes nothing.

### 4.2 EMI — not ported [⚠️]

- **Scope.** `compatibility/emi` (12 files) is excluded (`build.gradle:92`), the `emi` entrypoint is gone
  (`fabric.mod.json:34-36` at base), the `dev.emi:emi-fabric` dependencies are gone from `build.gradle`, and
  the EMI-only assets were deleted in f08f5b5b: `assets/emi/recipe/defaults/fabricaeexnihilo.json` (139 lines
  of default recipe trees) and `assets/fabricaeexnihilo/textures/gui/emi/tree_icons.png`.
- **Reason.** No official EMI build for 1.21.11; `gradle.properties:22` still pins `emi_version=1.1.9+1.21`
  (base: `1.1.7+1.21`). The community forward-port (PF-EMI) is described in the PR body as a different API.
  The newest EMI artifact in the local Gradle cache is `emi-fabric-1.1.23+1.21.1` — a 1.21.1 build.
- **Not affected.** All FEN content; JEI, Jade, WTHIT.
- **Player sees.** With EMI installed: no FEN recipes in EMI's index or recipe screens. No crash — no `emi`
  entrypoint, no `depends` entry, so EMI never loads FEN classes.

### 4.3 Megane — dropped by accident, entry restored in `cd647e12`, still not loadable [⚠️]

- **Scope.** `compatibility/megane` (5 files incl. `FENMeganeModule`) is **still compiled**: the source-set
  exclusions only cover REI/EMI/recipeviewer (`build.gradle:83-96`), and `megane-api:8.4.0` stays on the
  compile classpath unconditionally (`build.gradle:125`). What had been removed is the registration: at
  `2b32c719` `fabric.mod.json` had **no `custom` section at all**, so the `custom.megane:modules` key that
  pointed at `FENMeganeModule` (base `fabric.mod.json:47-55`) was gone. It was dropped in b7732a5d together
  with the already-disabled `loom:injected_interfaces` key, and **restored in `cd647e12`**.
- **Status.** The manifest entry is back, so `FENMeganeModule` is no longer dead code; what is still missing
  is a Megane build that runs on 1.21.11 (the commented runtime deps, `build.gradle:129-134`).
- **Accuracy problem in that commit.** b7732a5d's message says the block "referenced ItemModelGeneratorAccess
  and NbtPathAccess, neither of which exists on this branch". Only `NbtPathAccess` is missing — and it never
  existed in the base tree either; base referenced it only inside a key that the `DISABLED(...)` prefix had
  already neutered. `ItemModelGeneratorAccess` does exist at HEAD, as a normal accessor mixin
  (`fabricaeexnihilo.mixin.json:15`, `mixins/ItemModelGeneratorAccess.java:21-37`, used with a cast at
  `datagen/provider/ModelProvider.java:158-165`). So the two removals should be judged separately: the
  injected-interface key was a no-op, the Megane entrypoint was live.
- **Consequence.** Between b7732a5d and `cd647e12`, `FENMeganeModule` was compiled but referenced from
  nowhere (`git grep FENMeganeModule`: only its own declaration) — dead code, and it would have stayed dead
  when Megane ported. With the key restored, a working Megane 1.21.11 build would find the module.
- **Reason for the commented runtime deps** (`build.gradle:129-134`): there is no Megane 1.21.11 build.
  `megane-api:8.4.0`'s own jar manifest records `Fabric-Minecraft-Version: 1.19.2` and `Fabric-Loom-Version:
  1.1.9`; `build.gradle:126-128` records that Megane's title-screen mixin fails on 1.21.11 (not re-verified in
  a game).
- **Player sees.** Megane will not start on 1.21.11 (author's note). If a working build existed, FEN blocks
  would now publish their fluid/item data again; before `cd647e12` they would have shown nothing, because the
  module was never constructed.

#### Which of Jade / WTHIT / Megane are actually functional on 1.21.11

| Integration | Wiring at HEAD | Functional |
|---|---|---|
| **Jade** | `jade` entrypoint `fabric.mod.json:31-33`; `modCompileOnly` + flag-gated `modRuntimeOnly` `build.gradle:136-139`; `jade 21.1.6` declares `minecraft >=1.21.11` in its own `fabric.mod.json`. | yes |
| **WTHIT** | `waila_plugins.json:2-4` (unchanged except a trailing newline); `WthitPlugin` still compiled; `wthit-api 18.2.3` `modCompileOnly` at `build.gradle:119`, runtime + `badpackets 0.11.1` when the flag is on (`build.gradle:120-124`); `wthit 18.2.3` declares `minecraft >=1.21.11-`. | yes |
| **Megane** | `megane-api` on the compile classpath; `custom.megane:modules` restored in `cd647e12`; dev runtime deps commented out and no 1.21.11 Megane build. | no — nothing to load on 1.21.11 yet |

### 4.4 Other dropped things — and three that only look dropped

- `custom` / `loom:injected_interfaces` (base `fabric.mod.json:47-55`) — removed by b7732a5d; the
  `custom.megane:modules` half was **restored in `cd647e12`**, the `loom:injected_interfaces` half is a
  no-op. The one live target of the injected-interface key, `ItemModelGeneratorAccess`, was rewritten as a
  normal `@Mixin` accessor interface, so the datagen model writer still reaches the private
  `register`/`modelCollector`/`output` members. **No functional loss.** (§C explains how the accessor is used.)
- `fabric-datagen` entrypoint — dropped in 085dd43d, **restored** in 671ca048 (`fabric.mod.json:37-39`). Not
  unported; covered by §D and §5, row 1.
- `modmenu` entrypoint — dropped in 085dd43d, **restored** in 671ca048 (`fabric.mod.json:21-23`);
  `client/config/ModmenuPlugin.java` is still present.
- EMI default recipes and the EMI/REI GUI textures — deleted in f08f5b5b. They existed only to feed the two
  unported viewers; nothing else reads them.
- In-game audit runner (`util/ModTestRunner.java`, announced in 085dd43d's message) — deleted in f08f5b5b.
  Developer tool only; §E.
- JEI is not a dropped integration, but it is narrower than "JEI works" suggests: the plugin registers a
  **single** category, sieves (`compatibility/jei/FENJeiPlugin.java:41-44` → `SieveRecipeCategory`); there is no
  barrel/crucible/compost JEI category on this branch. JEI integration is new in this PR (base has no
  `jei_mod_plugin` entrypoint and no JEI dependency).

## 5. Open review items — MattiDragon, 2026-11-15, CHANGES_REQUESTED

The review was filed against 75760872 ("Some issues with the changes to the build setup. I have not yet
reviewed the code.") and contains nine inline comments, all on the build setup. The branch has since added
4a943d08 ("Addresses the review on the build setup") and 671ca048, so all nine are addressed in code; the only
leftovers are cosmetic (row 7). Nothing below is "planned"; the evidence column is `2b32c719` (the
review-time HEAD), which `cd647e12` does not affect.

| # | Comment (short quote) | Status | Evidence at HEAD | If open, what remains |
|---|---|---|---|---|
| 1 | fabric.mod.json: "Please fix and reenable datagen… Datagen ensures that they are all correct if the code is." | **Fixed in 671ca048** | entrypoint `fabric.mod.json:37-39`; `datagen/FENDatagen.java:14-33`; run config `build.gradle:146-161` (now `inherit client`, line 151); `jar` depends on the run `build.gradle:223`; 1066 generated files committed | Nothing in the mechanism. The one data defect in the regenerated output (§D5 / §6.2) was fixed after the review, in `cd647e12`. |
| 2 | "Why did you not update past 1.13.1? … use latest stable (1.19-SNAPSHOT)" | **Fixed in 4a943d08** | `build.gradle:8` `id 'net.fabricmc.fabric-loom-remap' version "${loom_version}"`; `gradle.properties:6` `loom_version=1.19-SNAPSHOT`; wrapper Gradle 9.8.2 (`gradle-wrapper.properties:3`); CI on JVM 25 (`build.yml:18-25`, `publish.yaml:23`) | Nothing. Resolved artifact is 1.19.4 (Gradle cache). |
| 3 | "Why did you remove the content filter? If you need to allow more things, add them." | **Fixed in 4a943d08** | `build.gradle:20-26`: blamejared maven with `content { includeGroup "com.faux.ingredientextension"; includeGroup "mezz.jei" }` | Nothing — the added group is exactly the "add them" case the comment allowed (JEI is published there). |
| 4 | sourceSets: "don't gate it behind the settings. They should only be used to toggle which mods load at runtime." | **Fixed in 4a943d08** | `build.gradle:83-96`: three **unconditional** excludes (rei, emi, recipeviewer) and the comment at 86-90 stating every other compat always compiles | Cosmetic only: `rei`/`emi` remain in the settings object (`build.gradle:74-75`, `config.gradle:8`) but nothing reads them. |
| 5 | "Instead of doing these hacks, just update loom and gradle." | **Fixed in 4a943d08** | `patchLoomManifest`, the detached JEI configuration and the Jade repackaging are all gone; JEI/Jade are plain maven `modCompileOnly` (`build.gradle:136-141`); loom 1.19 + Gradle 9.8.2 | Nothing. |
| 6 | "Don't edit comments for no good reason" (anchored on the `// Add generated data…` line) | **Fixed in 4a943d08** | base comments restored verbatim: `build.gradle:67` (intellij toggle), `:68` `boolean nothing = true`, `:110` `// Add generated data to runtime classpath (slightly hacky)`, `:118`, `:156`, `:164`, `:173`, `:181`, `:222` | Nothing. New comments appear only next to new logic (86-90, 122, 126-128, 147-150). |
| 7 | "Please don't change how our mod flags work. If some mod is broken, just comment out the lines instead of changing what the flags do." | **Fixed in 4a943d08**, two nits | flags now only add `modRuntimeOnly` dependencies (`build.gradle:113-124`, 137-139); Megane's runtime lines are commented out instead of re-scoped (`build.gradle:129-134`) | (a) `config.gradle:8` changed four defaults at once — rei `true→false`, wthit `false→true`, jade `false→true`, megane newly `true`. Dev-run default only, no runtime or packaging effect, but it is a change to flag behaviour; (b) the `megane` flag (`config.gradle:8`, `build.gradle:72`) is now read **nowhere** — a switch that does nothing. |
| 8 | "This is wrong. We always compile against all optional compats that work. The flags are only for in dev runtime testing." (anchored on the manifest rewrite) | **Fixed in 4a943d08** | the `processResources` `doLast` no longer touches entrypoints — it only minifies JSON (`build.gradle:196-205`); all entrypoints are declared statically in `fabric.mod.json:14-40`; `wthit`/`jade`/`modmenu`/`megane`/`jei` all compile unconditionally | Nothing. |
| 9 | gradle.properties: "Remove this. This breaks the build for everyone else." (proxy `systemProp.*`) | **Fixed in 4a943d08** | `gradle.properties` has no `systemProp` lines at all (33 lines: versions only); nothing was added in their place; per-machine overrides still go through `dev_settings.properties` (`config.gradle:3-14`), which already existed at base | Nothing. Note `dev_settings.properties` carries only the compat flags, not system properties. |

## 6. Issues found while writing this guide (not raised in the review)

These are not part of MattiDragon's review. Four of them were fixed in `cd647e12`, the fix commit
appended after this guide's audit: §6.1 (barrel double drop), §6.2 (hard-referencing tags), §6.3 (the
missing Megane entry) and §6.4 (the client-class mixin in the common array). 6.5-6.9 remain risks, stale
metadata or open questions.

### 6.1 Breaking a barrel duplicated its finished product [🔴]  fixed in cd647e12

**Status:** Fixed in `cd647e12` — the override clears its own stack field before calling `super`, so the
inherited scatter finds nothing to drop and the override's copy remains the only drop (§B1). The analysis
below is the pre-fix description.

`BarrelBlockEntity` now implements `SidedInventory` (i.e. vanilla `Inventory`) and drops its output
itself in `onBlockReplaced`, then calls `super`. In 1.21.11 the default `BlockEntity#onBlockReplaced`
scatters the contents of any `Inventory` it is called on (verified in the 1.21.11 bytecode: it checks
`instanceof Inventory` and calls `ItemScatterer.spawn`), and `WorldChunk` calls that hook on block
removal. The two drops therefore both fire: the override's copy plus the inherited scatter of the
output slot.

**What a player sees (derived from code, not observed in game):** fill a barrel with saplings until
it composts into a dirt block, then break the barrel — **two dirt items drop instead of one**. The fix
is to drop in exactly one place: either delete the override and let the inherited scatter handle the
output slot, or keep the override and do not call `super` (calling `super` alone still drops twice).
`cd647e12` takes a third route that keeps the override's copy: clear the override's own stack first, so the
inherited scatter sees an empty container. Full item and evidence chain: **§B1**.

### 6.2 Generated tags hard-referenced Tech Reborn blocks [🔴] [⚠️]  fixed in cd647e12

The committed datagen output lists `fabricaeexnihilo:rubber_*` blocks as **required** tag entries,
but those blocks only exist when Tech Reborn is loaded. Without Tech Reborn five tag files fail to
load, two more fail transitively, one advancement file fails to parse and five advancements never
load; axes/shovels lose their association with FEN blocks and crooks lose their leaf speed bonus.
The author's own 2026-11-16 test report logged the 12 `ERROR` lines and the tree had not changed
since. This is a **regression of the data regeneration**: the `1.21` tree wrote the same entries as
`{"id": …, "required": false}`, and the regenerated tree drops the marker because the 1.21.11 tag
builders route `addOptional(...)` through `.mapped(...)`. Recipes, loot tables and advancements are
guarded with `fabric:load_conditions`; tags cannot be, so the fix is to run datagen without Tech
Reborn or to emit the entries as optional.

**Status:** Fixed in `cd647e12` — the providers emit the entries as optional again, nine generated tag
files carry `required: false`, and a second `runDatagen` reports `written: 0`. Details: **§D5**.

### 6.3 Megane was compiled and shipped but could never register [⚠️]  fixed in cd647e12

`compatibility/megane` still compiles and its classes ship in the jar, but the
`custom.megane:modules` manifest entry that instantiates `FENMeganeModule` was dropped in
`b7732a5d` and stayed missing until `cd647e12` restored it; the runtime dependencies are commented out
because there is no Megane 1.21.11 build. Harmless today (nobody can install Megane on 1.21.11), and with
the entry back the mod will publish its barrel/crucible data as soon as a working Megane release appears.
Details: **§4.3**, **§E3.3**.

### 6.4 `ItemModelGeneratorAccess` was a client-class mixin in the common array [⚠️]  fixed in cd647e12

At `2b32c719`, `fabricaeexnihilo.mixin.json` listed `ItemModelGeneratorAccess` in the **common** `mixins`
array while its target, `net.minecraft.client.data.ItemModelGenerator`, lives in the client-only source
set (the same reason the datagen run had to switch to `inherit client`). The config was
`"required": true`. A dedicated-server start was **not tested** for this review, so whether Mixin would
have tolerated the absent target or aborted was unverified — but moving the entry to the `client` array
costs nothing. **Fixed in `cd647e12`:** the entry now sits in the `client` array, which a dedicated server
never loads, so the risk is gone. The same arrangement existed at `333b0fa8` with the 1.21 package, where
the target may have been present on both sides.

### 6.5 The JitPack release path still builds on Java 21 [⚠️, derived]

`.github/workflows/build.yml` and `publish.yaml` were moved to JVM 25, but `jitpack.yml` still
requests `openjdk21` / `21.0.3`, and `publish.yaml` still passes `jitpack-safe-version: true`.
Since the Loom 1.19 line needs a JVM 25 (per the PR's own comment and the class-file version of the
resolved Loom artefacts), a JitPack build of this branch should fail. Not observed on JitPack. See
**§C**.

### 6.6 Three mod switches are dead code

`config.gradle` still exposes `rei`, `emi` and `megane`, but nothing in `build.gradle` reads them
any more (REI/EMI are excluded unconditionally; Megane's block is commented out). Toggling them does
nothing and produces no warning. Either remove them or make them control what their names claim. See
**§C9**.

### 6.7 The PR description no longer describes the branch

It says "three commits on top of the port" and lists two hashes; the branch has ten. It claims
`./gradlew build` succeeds "on Java 21 with Gradle 8.14 and fabric-loom 1.13.1" — HEAD is Gradle
9.8.2, Loom 1.19-SNAPSHOT and a JVM 25 build. It says the PR "addresses the second note from
@MattiDragon" — it now addresses all nine. The author's own 2026-11-16 test report is stale in the
same direction (it says "9 commits, +6570/−4758" and predates `2b32c719`). Reviewers calibrate against
that text, so it is worth one edit. See **§5**.

### 6.8 Pre-existing: the `mineable/hoe` tag is emitted as `mineable/shovel`

`BlockTagProvider` builds the hoe tag on `BlockTags.SHOVEL_MINEABLE`, so no
`minecraft:mineable/hoe` file exists and `#fabricaeexnihilo:infested_leaves` lands in the shovel
tag. The same line exists at `333b0fa8`, so this PR faithfully reproduced an upstream bug — not a
regression, but worth fixing while the provider is open. See **§D5**.

### 6.9 Infested leaves no longer shed leaf particles (intent unverified)

Both FEN leaf blocks pass `0.01f` as the new `LeavesBlock` leaf-particle chance and then override
`spawnLeafParticle` with an empty body, so the chance has no effect. On `1.21` these blocks
inherited the leaf-particle behaviour. Net effect: the two infested-leaves blocks silently stopped
emitting falling-leaf particles. That may be deliberate (they are *infested*, not leafy), but nothing
in the diff says so — confirm with the author. See **§A.11** row 13.

---

## Appendix — where the evidence lives

| Claim type | Artefact |
|---|---|
| Whole diff | `git diff 333b0fa8..2b32c719` (753 files, +6576/−4758), plus `git show cd647e12` (14 files, +594/−149, the final fix commit) |
| Per-file churn | `git diff --numstat`: 98 Java, 530 generated, 105 datapack, 6 asset, 3 other, 7 build, 2 CI, 2 changelog |
| Commit-level story | `git show <sha> --stat`; the ten commits are tabulated in §0.1 |
| Datagen output | `src/main/generated` — 1066 tracked files at HEAD |
| Vanilla behaviour | 1.21 / 1.21.11 `minecraft-merged.jar` in the local Loom cache (bytecode), Yarn mappings |
| Review text | PR #79 review by MattiDragon, 2026-11-15; nine inline comments quoted in §5 |
| Fix commit | `cd647e12` (14 files, +594/−149), branch `1.21.11-configtoolkit`; summarised in §B1, §D5, §4.3, §6.4 |
| Prior test run | PR #79 issue comment, 2026-11-16 (author's own client test report) |

---

## Changelog for this revision

This copy applies `REVIEW-REPORT.md` (independent audit, 2026-11-17) to `PR79-REVIEW-GUIDE.md`.
The report's line numbers refer to the audited revision (1407 lines); every item was matched by
content, not by offset. No section was rewritten, nothing was deleted, and no passage the report does
not name was edited. The source `PR79-REVIEW-GUIDE.md` was not modified.

Most headline corrections were already present in the source when this pass started (the source was
revised after the audit). Those are recorded below as *already applied* and were re-verified in
place — the reported wording is genuinely gone and the corrected wording is genuinely present — so the
published file stays a single consistent document. Four spots still differed from the report and are
**edited in this revision**; they are marked accordingly.

### 1. Must-fix before publishing (report lines 65-74)

| Report item | Disposition | Evidence in this file |
|---|---|---|
| M1 · claim 4 — §B2 "four blocks of air" -> three | already applied | "one obsidian layer, three blocks of air cleared above" |
| M2 · claim 32 — §1 "2.5 % of files / 5 % of changed lines" -> 13 % / 32 % | already applied | "Java is 13 % of the PR's files and 32 % of its changed lines" |
| M3 · claim 36 — §D3 "200 files +272/−868" -> 210 files, +392/−868 | already applied | "numstat reports as 210 files, +392/−868 (the 10 moved definitions count as additions)" |
| M4 · I1 — §1 "15 items in §B" -> 19 | already applied | "19 items in §B" (§1, class B row) |
| M5 · claim 31 — `fabric.mod.json:44-48` -> 48-50 (report cites §E4.3; the passage is §E3.3 in this revision) | already applied | "`fabric.mod.json:48-50` at 333b0fa8" |
| M6 · claim 22 — §B18 `.col` -> `.color` | already applied | "`state.getMapColor(world, pos).color`" |
| M7 · claims 8, 30 — dangling "(see verification notes)" / "see D-notes" | already applied | neither string occurs anywhere; §D1 carries no forward reference |
| M8 · should-fix bundle (claims 27, 9, 11, 12, 5, 2, 15) | already applied | §4.2 "139 lines"; §B5 "`SidedInventory` (`:55`)"; §B7 "`:173-177`"; §B8 "`:228, 241-246`"; §B2 "`createTeleportTarget`" (Mojang name `getPortalDestination` kept only as a parenthetical); §B1 / §6.1 / §B19 use `WorldChunk` + `ItemScatterer`; §B11 "the FLUID state prints the fluid line plus the "Transforming" progress line, the ITEM state prints the item and its name" |

### 2. Spec-compliance gaps (report lines 76-85)

| Report item | Disposition | Evidence in this file |
|---|---|---|
| S1 — §B marker is the in-game severity | already applied | paragraph under "Impact markers": "Inside §B the marker is the **in-game severity of the change** …" |
| S2 — 🟡 with no scenario in §A2-A10 | already applied | §A scope rule: "a 🟡 theme means "this theme opens a §B item", and the scenario lives in the §B item named in the hand-off table (§A.11), not here." §D6 now carries its own "What actually happens in game" paragraph. |
| S3 — marker table 🟢 cell | already applied | the In-game scenario cell reads `write "no player-visible change"` (required phrase present) |
| S4 — prominence for "Known unported items" | already applied (wording updated again in the post-fix section) | callout: "Also read §4 before judging completeness. Three optional integrations are absent, and one of them (Megane) was *dropped by accident* and only restored in the final fix commit (§4.3)." |
| S5 — §C headings | already applied | §C items use **Failure mode** and **What goes wrong if this is misconfigured** instead of the in-game headings |
| S6 — provenance vs the §E2.4 processor-output claim | **edited in this revision** | §E2.4 (a) now reads "the author's 2026-11-16 run reports the processor output … (no build was performed for this review)", so it no longer contradicts the provenance note "no build and no game run were performed" |

### 3. Internal inconsistencies (report lines 87-97)

| Report item | Disposition | Evidence |
|---|---|---|
| I1 — "15 items" vs 19 | already applied | same as M4 |
| I2 — §B5 `:56` vs §A.11 row 4 `:55` | already applied | §B5 "`SidedInventory` (`:55`)"; §A.11 row 4 "`CrucibleBlockEntity.java:55,173,188-215,241-247`" |
| I3 — EMI defaults 140 vs 139 lines | already applied | §4.2 "139 lines"; §E3.1 / §E4.1 also say 139 |
| I4 — "2.5 %" vs "~13 %" in the same sentence | already applied | same as M2 |
| I5 — §A.11 row 1 `BarrelItemStorage.java:45` | already applied | row 1 cites `BarrelItemStorage.java:32-36` (the timed-insert branch) |
| I6 — §B11 progress line | already applied | same as M8 (last entry) |
| I7 — §C1 heading states Loom 1.13.1 as the base | already applied | "C1. Loom 1.6-SNAPSHOT (-> 1.13.1 in 085dd43d) -> net.fabricmc.fabric-loom-remap 1.19-SNAPSHOT …" |

### 4. Additional claim-table corrections not in the four headline lists

The report's claim table flags three further corrections of the same class (numeric drift or
misleading phrasing). They are applied here so the guide matches the audit:

| Report item | Correction applied |
|---|---|
| claim 18 (§B14) | "Durability, speed, attack damage and enchantability are unchanged from 1.21" -> "Durability, speed and enchantment value are unchanged from 1.21; attack damage was not applied before or after (1.21's `ToolItem`, like this branch's `CrookItem`/`HammerItem`, sets only `maxDamage`)." |
| claim 23 (§B19) | strainer drop range `modules/strainer/StrainerBlockEntity.java:77-84` -> `:78-83` |
| claim 32 (§1) | per-commit Java attribution "085dd43d touches 65 of the 98, 6ea48c49 23, 671ca048 23, f08f5b5b 3" -> "085dd43d touches 75 Java files (65 of the 98), 6ea48c49 23, 671ca048 24, f08f5b5b 4" |

### 5. Not changed

- Everything under the report's "Not re-derived (accepted or labelled)" section — the log-observation
  claims (§D5's 12 `ERROR` lines, the yaw-90 arrival), §A.0's per-theme attribution, and the
  §6.7/§D5 provenance notes — is left as the guide already labels it ("derived from code, not observed
  in game", "log observed on 2026-11-16").
- No structural rewrite, no deletion of normal content, no edit outside the items above. Through that
  revision the guide body kept its 1,413 lines; the four edits replaced nine lines with eleven.

---

### Post-fix updates (commit `cd647e12`)

The branch gained a fix commit after the audit: **`cd647e12` (14 files, +594/−149)**, applied on top of
`2b32c719` on the `1.21.11-configtoolkit` branch of `Thatgfsj-minecraft/FabricaeExNihilo`. It is the HEAD
this revision refers to. The body was updated as follows (structure unchanged, problem descriptions and
evidence chains kept):

| Section | Update |
|---|---|
| §0 Meta, §0.1 | Base→head now ends at `cd647e12` (final fix commit, the guide's HEAD); Scale notes the extra +594/−149; "nine follow-up commits" → ten; the "Not included" line no longer claims the Megane registration is absent (the entry is restored, only the 1.21.11 Megane build is missing); commit map gains row 11 (`cd647e12`, 14 files, +594/−149) plus a note that all other `file:line` references stay at `2b32c719` |
| "How to read" | §6 row notes that 6.1–6.4 are fixed in `cd647e12` |
| §1 | The "three things to check before merge" box becomes "three defects found — all fixed in `cd647e12`"; the §4 callout says Megane was dropped by accident and restored in the final fix commit |
| §B1 | Status **Fixed in `cd647e12`**; the override clears its own output stack before `super`, so the inherited `Inventory` scatter finds nothing to drop; pre-fix analysis kept, impact/scenario marked as pre-fix |
| §C | The scope line still uses `2b32c719`'s line numbers, with a note that `cd647e12` touches no build file |
| §C10 | Now describes the restored `custom.megane:modules` entry and the real remaining gap (no 1.21.11 Megane build); the silent-loss "smell" is recorded as fixed |
| §D0 | The "code correct ⇒ data correct" pointer to the D5 bug notes it was found at `671ca048` and fixed in `cd647e12` |
| §D5 | Heading and status: **Fixed in `cd647e12`** — nine generated tag files write `{"id": …, "required": false}` entries again, the `rubber_*` / `infested_rubber_leaves` entries are optional, the providers use the raw tag builder ahead of `.mapped(...)`, and a second `runDatagen` reports `written: 0`; the pre-fix analysis is kept as evidence |
| §E3.3 | Megane registration removal is now history: dropped in b7732a5d, restored in `cd647e12` |
| §4.3, §4.4 | Megane is "dropped by accident, entry restored, still not loadable on 1.21.11"; a Status bullet, the Consequence/Player-sees bullets and the Jade/WTHIT/Megane table row are updated; the `custom` bullet records the partial restore |
| §5 | Intro scopes the evidence column to `2b32c719`; row 1's trailing note now says the regenerated-output data defect was fixed in `cd647e12` |
| §6 | Intro says 6.1–6.4 were fixed after the audit; 6.1 (double drop), 6.2 (hard tags), 6.3 (Megane), 6.4 (client-class mixin) each carry a fixed status and the fix method |
| Appendix | "Whole diff" adds `git show cd647e12`; a "Fix commit" row points at the four fixed findings |

Notes on provenance for this section:

- The fix details are taken from the commit summary supplied with the fix (14 files, 9 generated tag
  files, second datagen `written: 0`, barrel clears its own stack before `super`, manifest entry
  restored, accessor mixin moved to the `"client"` array). No build and no game run were performed for
  this revision, and the repository source was not modified.
- Row 11's subject in §0.1 is a one-line summary of the four fixes; the commit message itself was not
  quoted in the hand-off.
- No other claim was re-audited: every line reference and historical statement outside the sections
  above remains that of `2b32c719` / the audit.

