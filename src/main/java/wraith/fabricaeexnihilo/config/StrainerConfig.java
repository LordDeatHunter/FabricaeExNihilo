package wraith.fabricaeexnihilo.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mattidragon.configloader.api.DefaultedFieldCodec;
import io.github.mattidragon.configloader.api.GenerateMutable;

/**
 * @param maxWaitTime Five minutes
 * @param minWaitTime One minute
 */
@GenerateMutable(useFancyMethodNames = true)
public record StrainerConfig(int minWaitTime, int maxWaitTime) implements MutableStrainerConfig.Source {
    public static final StrainerConfig DEFAULT = new StrainerConfig(1200, 6000);
    public static final Codec<StrainerConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            DefaultedFieldCodec.of(Codec.INT, "minWaitTime", StrainerConfig.DEFAULT.minWaitTime).forGetter(StrainerConfig::minWaitTime),
            DefaultedFieldCodec.of(Codec.INT, "maxWaitTime", StrainerConfig.DEFAULT.maxWaitTime).forGetter(StrainerConfig::maxWaitTime)
    ).apply(instance, StrainerConfig::new));
}
