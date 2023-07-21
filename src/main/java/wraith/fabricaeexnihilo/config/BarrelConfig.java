package wraith.fabricaeexnihilo.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mattidragon.configloader.api.DefaultedFieldCodec;
import io.github.mattidragon.configloader.api.GenerateMutable;

/**
 * @param bleeding Requires that thorns be enabled too.
 * @param tickRate       How many ticks between updates
 */
@GenerateMutable(useFancyMethodNames = true)
public record BarrelConfig(double compostRate,
                           boolean bleeding,
                           boolean milking,
                           int leakRadius,
                           int tickRate,
                           boolean efficiency,
                           boolean thorns) implements MutableBarrelConfig.Source {
    public static final BarrelConfig DEFAULT = new BarrelConfig(0.01, true, true, 2, 6, true, true);
    public static final Codec<BarrelConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            DefaultedFieldCodec.of(Codec.DOUBLE, "compostRate", BarrelConfig.DEFAULT.compostRate).forGetter(BarrelConfig::compostRate),
            DefaultedFieldCodec.of(Codec.BOOL, "bleeding", BarrelConfig.DEFAULT.bleeding).forGetter(BarrelConfig::bleeding),
            DefaultedFieldCodec.of(Codec.BOOL, "milking", BarrelConfig.DEFAULT.milking).forGetter(BarrelConfig::milking),
            DefaultedFieldCodec.of(Codec.INT, "leakRadius", BarrelConfig.DEFAULT.leakRadius).forGetter(BarrelConfig::leakRadius),
            DefaultedFieldCodec.of(Codec.INT, "tickRate", BarrelConfig.DEFAULT.tickRate).forGetter(BarrelConfig::tickRate),
            DefaultedFieldCodec.of(Codec.BOOL, "efficiency", BarrelConfig.DEFAULT.efficiency).forGetter(BarrelConfig::efficiency),
            DefaultedFieldCodec.of(Codec.BOOL, "thorns", BarrelConfig.DEFAULT.thorns).forGetter(BarrelConfig::thorns)
    ).apply(instance, BarrelConfig::new));
}
