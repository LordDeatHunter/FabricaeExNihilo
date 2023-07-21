package wraith.fabricaeexnihilo.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mattidragon.configloader.api.DefaultedFieldCodec;

public record FabricaeExNihiloConfig(BarrelConfig barrels,
                                     CrucibleConfig crucibles,
                                     SeedConfig seeds,
                                     SieveConfig sieves,
                                     InfestedConfig infested,
                                     StrainerConfig strainers,
                                     WitchWaterConfig witchwater) {
    public static final FabricaeExNihiloConfig DEFAULT = new FabricaeExNihiloConfig(BarrelConfig.DEFAULT, CrucibleConfig.DEFAULT, SeedConfig.DEFAULT, SieveConfig.DEFAULT, InfestedConfig.DEFAULT, StrainerConfig.DEFAULT, WitchWaterConfig.DEFAULT);
    public static final Codec<FabricaeExNihiloConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    DefaultedFieldCodec.of(BarrelConfig.CODEC, "barrels", FabricaeExNihiloConfig.DEFAULT.barrels).forGetter(FabricaeExNihiloConfig::barrels),
                    DefaultedFieldCodec.of(CrucibleConfig.CODEC, "crucibles", FabricaeExNihiloConfig.DEFAULT.crucibles).forGetter(FabricaeExNihiloConfig::crucibles),
                    DefaultedFieldCodec.of(SeedConfig.CODEC, "seeds", FabricaeExNihiloConfig.DEFAULT.seeds).forGetter(FabricaeExNihiloConfig::seeds),
                    DefaultedFieldCodec.of(SieveConfig.CODEC, "sieves", FabricaeExNihiloConfig.DEFAULT.sieves).forGetter(FabricaeExNihiloConfig::sieves),
                    DefaultedFieldCodec.of(InfestedConfig.CODEC, "infested", FabricaeExNihiloConfig.DEFAULT.infested).forGetter(FabricaeExNihiloConfig::infested),
                    DefaultedFieldCodec.of(StrainerConfig.CODEC, "strainers", FabricaeExNihiloConfig.DEFAULT.strainers).forGetter(FabricaeExNihiloConfig::strainers),
                    DefaultedFieldCodec.of(WitchWaterConfig.CODEC, "witchWater", FabricaeExNihiloConfig.DEFAULT.witchwater).forGetter(FabricaeExNihiloConfig::witchwater))
            .apply(instance, FabricaeExNihiloConfig::new));
}
