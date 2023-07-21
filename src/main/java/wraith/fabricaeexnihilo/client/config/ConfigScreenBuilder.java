package wraith.fabricaeexnihilo.client.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.config.*;

import java.util.ArrayList;

public class ConfigScreenBuilder {
    public static Screen create(Screen parent) {
        var config = FabricaeExNihilo.CONFIG.get();
        var barrels = config.barrels().toMutable();
        var crucibles = config.crucibles().toMutable();
        var seeds = config.seeds().toMutable();
        var sieves = config.sieves().toMutable();
        var infested = config.infested().toMutable();
        var strainers = config.strainers().toMutable();
        var witchwater = config.witchwater().toMutable();
        var misc = config.misc().toMutable();

        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("config.fabricaeexnihlio.title"))
                .save(() -> FabricaeExNihilo.CONFIG.set(new FabricaeExNihiloConfig(barrels.toImmutable(), crucibles.toImmutable(), seeds.toImmutable(), sieves.toImmutable(), infested.toImmutable(), strainers.toImmutable(), witchwater.toImmutable(), misc.toImmutable())))
                .category(createBarrelCategory(barrels))
                .category(createCrucibleCategory(crucibles))
                .category(createInfestedCategory(infested))
                .category(createSeedCategory(seeds))
                .category(createSieveCategory(sieves))
                .category(createStrainerCategory(strainers))
                .category(createWitchwaterRedirectCategory())
                .category(createMiscCategory(misc))
                .build()
                .generateScreen(parent);
    }

    private static ConfigCategory createBarrelCategory(MutableBarrelConfig barrels) {
        var bleeding = Option.<Boolean>createBuilder()
                .name(Text.translatable("config.fabricaeexnihlio.option.barrels.bleeding"))
                .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.barrels.bleeding.description")))
                .controller(TickBoxControllerBuilder::create)
                .binding(BarrelConfig.DEFAULT.bleeding(), barrels::getBleeding, barrels::setBleeding)
                .build();
        return ConfigCategory.createBuilder()
                .name(Text.translatable("config.fabricaeexnihlio.category.barrels"))
                .option(Option.<Double>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.barrels.compostRate"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.barrels.compostRate.description")))
                        .controller(option -> DoubleFieldControllerBuilder.create(option).range(0d, 1d))
                        .binding(BarrelConfig.DEFAULT.compostRate(), barrels::getCompostRate, barrels::setCompostRate)
                        .build())
                .option(bleeding)
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.barrels.milking"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.barrels.milking.description")))
                        .controller(TickBoxControllerBuilder::create)
                        .binding(BarrelConfig.DEFAULT.milking(), barrels::getMilking, barrels::setMilking)
                        .build())
                .option(Option.<Integer>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.barrels.leakRadius"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.barrels.leakRadius.description")))
                        .controller(option -> IntegerFieldControllerBuilder.create(option).range(0, 20))
                        .binding(BarrelConfig.DEFAULT.leakRadius(), barrels::getLeakRadius, barrels::setLeakRadius)
                        .build())
                .option(Option.<Integer>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.barrels.tickRate"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.barrels.tickRate.description")))
                        .controller(option -> IntegerFieldControllerBuilder.create(option).min(1))
                        .binding(BarrelConfig.DEFAULT.tickRate(), barrels::getTickRate, barrels::setTickRate)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.barrels.efficiency"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.barrels.efficiency.description")))
                        .controller(TickBoxControllerBuilder::create)
                        .binding(BarrelConfig.DEFAULT.efficiency(), barrels::getEfficiency, barrels::setEfficiency)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.barrels.thorns"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.barrels.thorns.description")))
                        .controller(TickBoxControllerBuilder::create)
                        .binding(BarrelConfig.DEFAULT.thorns(), barrels::getThorns, barrels::setThorns)
                        .listener((option, value) -> bleeding.setAvailable(value))
                        .build())
                .build();
    }

    private static ConfigCategory createCrucibleCategory(MutableCrucibleConfig crucibles) {
        return ConfigCategory.createBuilder()
                .name(Text.translatable("config.fabricaeexnihlio.category.crucibles"))
                .option(Option.<Integer>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.crucibles.stoneProcessingRate"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.crucibles.stoneProcessingRate.description")))
                        .controller(option -> IntegerFieldControllerBuilder.create(option).min(1))
                        .binding(CrucibleConfig.DEFAULT.stoneProcessingRate(), crucibles::getStoneProcessingRate, crucibles::setStoneProcessingRate)
                        .build())
                .option(Option.<Integer>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.crucibles.woodProcessingRate"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.crucibles.woodProcessingRate.description")))
                        .controller(option -> IntegerFieldControllerBuilder.create(option).min(1))
                        .binding(CrucibleConfig.DEFAULT.woodProcessingRate(), crucibles::getWoodProcessingRate, crucibles::setWoodProcessingRate)
                        .build())
                .option(Option.<Integer>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.crucibles.stoneVolume"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.crucibles.stoneVolume.description")))
                        .controller(option -> IntegerFieldControllerBuilder.create(option).min(1))
                        .binding(CrucibleConfig.DEFAULT.stoneVolume(), crucibles::getStoneVolume, crucibles::setStoneVolume)
                        .build())
                .option(Option.<Integer>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.crucibles.woodVolume"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.crucibles.woodVolume.description")))
                        .controller(option -> IntegerFieldControllerBuilder.create(option).min(1))
                        .binding(CrucibleConfig.DEFAULT.woodVolume(), crucibles::getWoodVolume, crucibles::setWoodVolume)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.crucibles.efficiency"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.crucibles.efficiency.description")))
                        .controller(TickBoxControllerBuilder::create)
                        .binding(CrucibleConfig.DEFAULT.efficiency(), crucibles::getEfficiency, crucibles::setEfficiency)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.crucibles.fireAspect"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.crucibles.fireAspect.description")))
                        .controller(TickBoxControllerBuilder::create)
                        .binding(CrucibleConfig.DEFAULT.fireAspect(), crucibles::getFireAspect, crucibles::setFireAspect)
                        .build())
                .option(Option.<Integer>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.crucibles.tickRate"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.crucibles.tickRate.description")))
                        .controller(option -> IntegerFieldControllerBuilder.create(option).min(1))
                        .binding(CrucibleConfig.DEFAULT.tickRate(), crucibles::getTickRate, crucibles::setTickRate)
                        .build())
                .build();
    }

    private static ConfigCategory createInfestedCategory(MutableInfestedConfig infested) {
        return ConfigCategory.createBuilder()
                .name(Text.translatable("config.fabricaeexnihlio.category.infested"))
                .option(Option.<Integer>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.infested.infestedSpreadAttempts"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.infested.infestedSpreadAttempts.description")))
                        .controller(option -> IntegerFieldControllerBuilder.create(option).min(0))
                        .binding(InfestedConfig.DEFAULT.infestedSpreadAttempts(), infested::getInfestedSpreadAttempts, infested::setInfestedSpreadAttempts)
                        .build())
                .option(Option.<Integer>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.infested.infestingSpreadAttempts"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.infested.infestingSpreadAttempts.description")))
                        .controller(option -> IntegerFieldControllerBuilder.create(option).min(0))
                        .binding(InfestedConfig.DEFAULT.infestingSpreadAttempts(), infested::getInfestingSpreadAttempts, infested::setInfestingSpreadAttempts)
                        .build())
                .option(Option.<Double>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.infested.minimumSpreadProgress"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.infested.minimumSpreadProgress.description")))
                        .controller(option -> DoubleSliderControllerBuilder.create(option).range(0d, 1d).step(0.05))
                        .binding(InfestedConfig.DEFAULT.minimumSpreadProgress(), infested::getMinimumSpreadProgress, infested::setMinimumSpreadProgress)
                        .build())
                .option(Option.<Double>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.infested.progressPerUpdate"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.infested.progressPerUpdate.description")))
                        .controller(option -> DoubleFieldControllerBuilder.create(option).range(0d, 1d))
                        .binding(InfestedConfig.DEFAULT.progressPerUpdate(), infested::getProgressPerUpdate, infested::setProgressPerUpdate)
                        .build())
                .option(Option.<Double>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.infested.spreadChance"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.infested.spreadChance.description")))
                        .controller(option -> DoubleSliderControllerBuilder.create(option).range(0d, 1d).step(0.1))
                        .binding(InfestedConfig.DEFAULT.spreadChance(), infested::getSpreadChance, infested::setSpreadChance)
                        .build())
                .option(Option.<Integer>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.infested.updateFrequency"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.infested.updateFrequency.description")))
                        .controller(option -> IntegerFieldControllerBuilder.create(option).min(0))
                        .binding(InfestedConfig.DEFAULT.updateFrequency(), infested::getUpdateFrequency, infested::setUpdateFrequency)
                        .build())
                .build();
    }

    private static ConfigCategory createSeedCategory(MutableSeedConfig seeds) {
        var options = new ArrayList<Option<Boolean>>();
        options.add(Option.<Boolean>createBuilder()
                .name(Text.translatable("config.fabricaeexnihlio.option.seeds.cactus"))
                .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.category.seeds.description")))
                .controller(TickBoxControllerBuilder::create)
                .binding(SeedConfig.DEFAULT.cactus(), seeds::getCactus, seeds::setCactus)
                .build());
        options.add(Option.<Boolean>createBuilder()
                .name(Text.translatable("config.fabricaeexnihlio.option.seeds.chorus"))
                .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.category.seeds.description")))
                .controller(TickBoxControllerBuilder::create)
                .binding(SeedConfig.DEFAULT.chorus(), seeds::getChorus, seeds::setChorus)
                .build());
        options.add(Option.<Boolean>createBuilder()
                .name(Text.translatable("config.fabricaeexnihlio.option.seeds.flowerSeeds"))
                .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.category.seeds.description")))
                .controller(TickBoxControllerBuilder::create)
                .binding(SeedConfig.DEFAULT.flowerSeeds(), seeds::getFlowerSeeds, seeds::setFlowerSeeds)
                .build());
        options.add(Option.<Boolean>createBuilder()
                .name(Text.translatable("config.fabricaeexnihlio.option.seeds.grass"))
                .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.category.seeds.description")))
                .controller(TickBoxControllerBuilder::create)
                .binding(SeedConfig.DEFAULT.grass(), seeds::getGrass, seeds::setGrass)
                .build());
        options.add(Option.<Boolean>createBuilder()
                .name(Text.translatable("config.fabricaeexnihlio.option.seeds.kelp"))
                .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.category.seeds.description")))
                .controller(TickBoxControllerBuilder::create)
                .binding(SeedConfig.DEFAULT.kelp(), seeds::getKelp, seeds::setKelp)
                .build());
        options.add(Option.<Boolean>createBuilder()
                .name(Text.translatable("config.fabricaeexnihlio.option.seeds.mycelium"))
                .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.category.seeds.description")))
                .controller(TickBoxControllerBuilder::create)
                .binding(SeedConfig.DEFAULT.mycelium(), seeds::getMycelium, seeds::setMycelium)
                .build());
        options.add(Option.<Boolean>createBuilder()
                .name(Text.translatable("config.fabricaeexnihlio.option.seeds.netherSpores"))
                .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.category.seeds.description")))
                .controller(TickBoxControllerBuilder::create)
                .binding(SeedConfig.DEFAULT.netherSpores(), seeds::getNetherSpores, seeds::setNetherSpores)
                .build());
        options.add(Option.<Boolean>createBuilder()
                .name(Text.translatable("config.fabricaeexnihlio.option.seeds.seaPickle"))
                .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.category.seeds.description")))
                .controller(TickBoxControllerBuilder::create)
                .binding(SeedConfig.DEFAULT.seaPickle(), seeds::getSeaPickle, seeds::setSeaPickle)
                .build());
        options.add(Option.<Boolean>createBuilder()
                .name(Text.translatable("config.fabricaeexnihlio.option.seeds.sugarCane"))
                .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.category.seeds.description")))
                .controller(TickBoxControllerBuilder::create)
                .binding(SeedConfig.DEFAULT.sugarCane(), seeds::getSugarCane, seeds::setSugarCane)
                .build());

        var enabledOption = Option.<Boolean>createBuilder()
                .name(Text.translatable("config.fabricaeexnihlio.option.seeds.enabled"))
                .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.seeds.enabled.description")))
                .controller(TickBoxControllerBuilder::create)
                .binding(SeedConfig.DEFAULT.enabled(), seeds::getEnabled, seeds::setEnabled)
                .listener((option, value) -> options.forEach(option2 -> option2.setAvailable(value)))
                .build();
        return ConfigCategory.createBuilder()
                .name(Text.translatable("config.fabricaeexnihlio.category.seeds"))
                .option(enabledOption)
                .options(options)
                .build();
    }

    private static ConfigCategory createSieveCategory(MutableSieveConfig sieves) {
        var efficiencyScaleFactor = Option.<Double>createBuilder()
                .name(Text.translatable("config.fabricaeexnihlio.option.sieves.efficiencyScaleFactor"))
                .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.sieves.efficiencyScaleFactor.description")))
                .controller(option -> DoubleFieldControllerBuilder.create(option).min(0d))
                .binding(SieveConfig.DEFAULT.efficiencyScaleFactor(), sieves::getEfficiencyScaleFactor, sieves::setEfficiencyScaleFactor)
                .build();
        var hasteScaleFactor = Option.<Double>createBuilder()
                .name(Text.translatable("config.fabricaeexnihlio.option.sieves.hasteScaleFactor"))
                .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.sieves.hasteScaleFactor.description")))
                .controller(option -> DoubleFieldControllerBuilder.create(option).min(0d))
                .binding(SieveConfig.DEFAULT.hasteScaleFactor(), sieves::getHasteScaleFactor, sieves::setHasteScaleFactor)
                .build();

        return ConfigCategory.createBuilder()
                .name(Text.translatable("config.fabricaeexnihlio.category.sieves"))
                .option(Option.<Double>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.sieves.baseProgress"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.sieves.baseProgress.description")))
                        .controller(option -> DoubleFieldControllerBuilder.create(option).range(0d, 1d))
                        .binding(SieveConfig.DEFAULT.baseProgress(), sieves::getBaseProgress, sieves::setBaseProgress)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.sieves.fortune"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.sieves.fortune.description")))
                        .controller(TickBoxControllerBuilder::create)
                        .binding(SieveConfig.DEFAULT.fortune(), sieves::getFortune, sieves::setFortune)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.sieves.efficiency"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.sieves.efficiency.description")))
                        .controller(TickBoxControllerBuilder::create)
                        .binding(SieveConfig.DEFAULT.efficiency(), sieves::getEfficiency, sieves::setEfficiency)
                        .listener((option, value) -> efficiencyScaleFactor.setAvailable(value))
                        .build())
                .option(efficiencyScaleFactor)
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.sieves.haste"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.sieves.haste.description")))
                        .controller(TickBoxControllerBuilder::create)
                        .binding(SieveConfig.DEFAULT.haste(), sieves::getHaste, sieves::setHaste)
                        .listener((option, value) -> hasteScaleFactor.setAvailable(value))
                        .build())
                .option(hasteScaleFactor)
                .option(Option.<Integer>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.sieves.meshStackSize"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.sieves.meshStackSize.description")))
                        .controller(option -> IntegerSliderControllerBuilder.create(option).range(1, 64).step(1))
                        .binding(SieveConfig.DEFAULT.meshStackSize(), sieves::getMeshStackSize, sieves::setMeshStackSize)
                        .build())
                .option(Option.<Integer>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.sieves.sieveRadius"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.sieves.sieveRadius.description")))
                        .controller(option -> IntegerFieldControllerBuilder.create(option).range(0, 100))
                        .binding(SieveConfig.DEFAULT.sieveRadius(), sieves::getSieveRadius, sieves::setSieveRadius)
                        .build())
                .build();
    }

    private static ConfigCategory createStrainerCategory(MutableStrainerConfig strainers) {
        var min = Option.<Integer>createBuilder()
                .name(Text.translatable("config.fabricaeexnihlio.option.strainers.minWaitTime"))
                .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.strainers.minWaitTime.description")))
                .controller(option -> IntegerFieldControllerBuilder.create(option).min(1))
                .binding(StrainerConfig.DEFAULT.minWaitTime(), strainers::getMinWaitTime, strainers::setMinWaitTime)
                .build();
        var max = Option.<Integer>createBuilder()
                .name(Text.translatable("config.fabricaeexnihlio.option.strainers.maxWaitTime"))
                .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.strainers.maxWaitTime.description")))
                .controller(option -> IntegerFieldControllerBuilder.create(option).min(1))
                .binding(StrainerConfig.DEFAULT.maxWaitTime(), strainers::getMaxWaitTime, strainers::setMaxWaitTime)
                .build();
        min.addListener((option, value) -> {
            if (value > max.pendingValue()) max.requestSet(value);
        });
        max.addListener((option, value) -> {
            if (value < min.pendingValue()) min.requestSet(value);
        });
        return ConfigCategory.createBuilder()
                .name(Text.translatable("config.fabricaeexnihlio.category.strainers"))
                .option(min)
                .option(max)
                .build();
    }

    private static ConfigCategory createWitchwaterRedirectCategory() {
        return ConfigCategory.createBuilder()
                .name(Text.translatable("config.fabricaeexnihlio.category.witchwater"))
                .option(ButtonOption.createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.witchwater.effects_not_available"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.witchwater.effects_not_available.description")))
                        .text(Text.translatable("config.fabricaeexnihlio.option.witchwater.effects_not_available.action"))
                        // We can't open the config file itself because the code for opening links on windows doesn't seem to work with files
                        .action((screen, button) -> Util.getOperatingSystem().open(FabricLoader.getInstance().getConfigDir().toUri()))
                        .build())
                .build();
    }

    private static ConfigCategory createMiscCategory(MutableMiscConfig misc) {
        return ConfigCategory.createBuilder()
                .name(Text.translatable("config.fabricaeexnihlio.category.misc"))
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.fabricaeexnihlio.option.misc.enableSaltCollection"))
                        .description(OptionDescription.of(Text.translatable("config.fabricaeexnihlio.option.misc.enableSaltCollection.description")))
                        .controller(TickBoxControllerBuilder::create)
                        .binding(MiscConfig.DEFAULT.enableSaltCollection(), misc::getEnableSaltCollection, misc::setEnableSaltCollection)
                        .build())
                .build();
    }
}
