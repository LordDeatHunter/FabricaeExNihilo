package exnihilocreatio.recipes.defaults;

import exnihilocreatio.blocks.BlockSieve;
import exnihilocreatio.items.ore.ItemOre;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.FluidTransformRegistry;
import exnihilocreatio.registries.registries.HeatRegistry;
import exnihilocreatio.registries.registries.OreRegistry;
import exnihilocreatio.registries.registries.SieveRegistry;
import exnihilocreatio.texturing.Color;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import static net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder("thermalfoundation")
public class ThermalFoundation implements IRecipeDefaults {
    @Getter
    public String MODID = "thermalfoundation";

    @ObjectHolder("material")
    public static final Item THERMAL_MATERIAL = null;

    @ObjectHolder("fluid_pyrotheum")
    public static final Block MOTLEN_PYROTHEUM = null;

    public void registerSieve(SieveRegistry registry) {
        OreRegistry oreRegistry = ExNihiloRegistryManager.ORE_REGISTRY;
        ItemOre platinum = oreRegistry.getOreItem("platinum");

        if (platinum != null) {
            registry.register("gravel", new ItemInfo(platinum), 0.02f, BlockSieve.MeshType.FLINT.getID());
            registry.register("gravel", new ItemInfo(platinum), 0.04f, BlockSieve.MeshType.IRON.getID());
            registry.register("gravel", new ItemInfo(platinum), 0.12f, BlockSieve.MeshType.DIAMOND.getID());
        }
    }

    @Override
    public void registerHeat(HeatRegistry registry) {
        registry.register(new BlockInfo(MOTLEN_PYROTHEUM, -1), 15);
    }

    @Override
    public void registerFluidTransform(FluidTransformRegistry registry) {

    }

    public void registerOreChunks(OreRegistry registry) {
        //noinspection ConstantConditions
        if (THERMAL_MATERIAL != null) {
            if (!registry.isRegistered("copper"))
                registry.register("copper", new Color("b87333"), new ItemInfo(THERMAL_MATERIAL, 128));

            if (!registry.isRegistered("tin"))
                registry.register("tin", new Color("d3d4d5"), new ItemInfo(THERMAL_MATERIAL, 129));

            if (!registry.isRegistered("silver"))
                registry.register("silver", new Color("c0c0c0"), new ItemInfo(THERMAL_MATERIAL, 130));

            if (!registry.isRegistered("lead"))
                registry.register("lead", new Color("444f53"), new ItemInfo(THERMAL_MATERIAL, 131));

            if (!registry.isRegistered("aluminum"))
                registry.register("aluminum", new Color("CDCDCF"), new ItemInfo(THERMAL_MATERIAL, 132));

            if (!registry.isRegistered("nickel"))
                registry.register("nickel", new Color("BDBAAE"), new ItemInfo(THERMAL_MATERIAL, 133));

            if (!registry.isRegistered("platinum")){
                registry.register("platinum", new Color("d7dfe7"), new ItemInfo(THERMAL_MATERIAL, 134));
                registry.getSieveBlackList().add(registry.getOreItem("platinum"));
            }
        }
    }
}
