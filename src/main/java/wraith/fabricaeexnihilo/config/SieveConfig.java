package wraith.fabricaeexnihilo.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mattidragon.configloader.api.DefaultedFieldCodec;
import io.github.mattidragon.configloader.api.GenerateMutable;

@GenerateMutable(useFancyMethodNames = true)
public record SieveConfig(double baseProgress, boolean efficiency, double efficiencyScaleFactor,
                          boolean fortune, boolean haste, double hasteScaleFactor, int meshStackSize,
                          int sieveRadius) implements MutableSieveConfig.Source {
    public static final SieveConfig DEFAULT = new SieveConfig(0.1, true, 0.05, true, true, 1.0, 16, 2);
    public static final Codec<SieveConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            DefaultedFieldCodec.of(Codec.DOUBLE, "baseProgress", SieveConfig.DEFAULT.baseProgress).forGetter(SieveConfig::baseProgress),
            DefaultedFieldCodec.of(Codec.BOOL, "efficiency", SieveConfig.DEFAULT.efficiency).forGetter(SieveConfig::efficiency),
            DefaultedFieldCodec.of(Codec.DOUBLE, "efficiencyScaleFactor", SieveConfig.DEFAULT.efficiencyScaleFactor).forGetter(SieveConfig::efficiencyScaleFactor),
            DefaultedFieldCodec.of(Codec.BOOL, "fortune", SieveConfig.DEFAULT.fortune).forGetter(SieveConfig::fortune),
            DefaultedFieldCodec.of(Codec.BOOL, "haste", SieveConfig.DEFAULT.haste).forGetter(SieveConfig::haste),
            DefaultedFieldCodec.of(Codec.DOUBLE, "hasteScaleFactor", SieveConfig.DEFAULT.hasteScaleFactor).forGetter(SieveConfig::hasteScaleFactor),
            DefaultedFieldCodec.of(Codec.INT, "meshStackSize", SieveConfig.DEFAULT.meshStackSize).forGetter(SieveConfig::meshStackSize),
            DefaultedFieldCodec.of(Codec.INT, "sieveRadius", SieveConfig.DEFAULT.sieveRadius).forGetter(SieveConfig::sieveRadius)
    ).apply(instance, SieveConfig::new));
}
