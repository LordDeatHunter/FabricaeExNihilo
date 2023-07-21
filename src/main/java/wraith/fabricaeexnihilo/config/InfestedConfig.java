package wraith.fabricaeexnihilo.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mattidragon.configloader.api.DefaultedFieldCodec;
import io.github.mattidragon.configloader.api.GenerateMutable;

/**
 * @param infestedSpreadAttempts  How many attempts should infested leaves make to spread each time they check.
 * @param infestingSpreadAttempts How many attempts should infesting leaves make to spread each time they check.
 * @param minimumSpreadProgress    How much progress do infesting leaves need to make before they start trying to spread.
 * @param progressPerUpdate       How much progress should be made during each infesting leaves update
 * @param spreadChance            How likely are leaves to succeed at spreading.
 * @param updateFrequency         How many ticks between infesting leaves updates
 */
@GenerateMutable(useFancyMethodNames = true)
public record InfestedConfig(int infestedSpreadAttempts, int infestingSpreadAttempts,
                             double minimumSpreadProgress, double progressPerUpdate, double spreadChance,
                             int updateFrequency) implements MutableInfestedConfig.Source {
    public static final InfestedConfig DEFAULT = new InfestedConfig(4, 1, 0.15, 0.015, 0.5, 10);
    public static final Codec<InfestedConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            DefaultedFieldCodec.of(Codec.INT, "infestedSpreadAttempts", InfestedConfig.DEFAULT.infestedSpreadAttempts).forGetter(InfestedConfig::infestedSpreadAttempts),
            DefaultedFieldCodec.of(Codec.INT, "infestingSpreadAttempts", InfestedConfig.DEFAULT.infestingSpreadAttempts).forGetter(InfestedConfig::infestingSpreadAttempts),
            DefaultedFieldCodec.of(Codec.DOUBLE, "minimumSpreadProgress", InfestedConfig.DEFAULT.minimumSpreadProgress).forGetter(InfestedConfig::minimumSpreadProgress),
            DefaultedFieldCodec.of(Codec.DOUBLE, "progressPerUpdate", InfestedConfig.DEFAULT.progressPerUpdate).forGetter(InfestedConfig::progressPerUpdate),
            DefaultedFieldCodec.of(Codec.DOUBLE, "spreadChance", InfestedConfig.DEFAULT.spreadChance).forGetter(InfestedConfig::spreadChance),
            DefaultedFieldCodec.of(Codec.INT, "updateFrequency", InfestedConfig.DEFAULT.updateFrequency).forGetter(InfestedConfig::updateFrequency)
    ).apply(instance, InfestedConfig::new));
}
