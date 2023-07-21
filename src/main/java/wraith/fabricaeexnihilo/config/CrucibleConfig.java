package wraith.fabricaeexnihilo.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mattidragon.configloader.api.DefaultedFieldCodec;
import io.github.mattidragon.configloader.api.GenerateMutable;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;

@SuppressWarnings("UnstableApiUsage")
@GenerateMutable(useFancyMethodNames = true)
public record CrucibleConfig(int stoneProcessingRate, int woodProcessingRate, int stoneVolume, int woodVolume,
                             boolean efficiency, boolean fireAspect,
                             int tickRate) implements MutableCrucibleConfig.Source {
    public static final CrucibleConfig DEFAULT = new CrucibleConfig((int) (FluidConstants.BUCKET / 100), (int) (FluidConstants.BUCKET / 60), 4, 1, true, true, 20);
    public static final Codec<CrucibleConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            DefaultedFieldCodec.of(Codec.INT, "fireproofProcessingRate", CrucibleConfig.DEFAULT.stoneProcessingRate).forGetter(CrucibleConfig::stoneProcessingRate),
            DefaultedFieldCodec.of(Codec.INT, "woodProcessingRate", CrucibleConfig.DEFAULT.woodProcessingRate).forGetter(CrucibleConfig::woodProcessingRate),
            DefaultedFieldCodec.of(Codec.INT, "stoneVolume", CrucibleConfig.DEFAULT.stoneVolume).forGetter(CrucibleConfig::stoneVolume),
            DefaultedFieldCodec.of(Codec.INT, "woodVolume", CrucibleConfig.DEFAULT.woodVolume).forGetter(CrucibleConfig::woodVolume),
            DefaultedFieldCodec.of(Codec.BOOL, "efficiency", CrucibleConfig.DEFAULT.efficiency).forGetter(CrucibleConfig::efficiency),
            DefaultedFieldCodec.of(Codec.BOOL, "fireAspect", CrucibleConfig.DEFAULT.fireAspect).forGetter(CrucibleConfig::fireAspect),
            DefaultedFieldCodec.of(Codec.INT, "tickRate", CrucibleConfig.DEFAULT.tickRate).forGetter(CrucibleConfig::tickRate)
    ).apply(instance, CrucibleConfig::new));
}
