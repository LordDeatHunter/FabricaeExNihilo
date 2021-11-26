package wraith.fabricaeexnihilo.compatibility.modules.techreborn;

import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.api.compatibility.IFabricaeExNihiloModule;
import wraith.fabricaeexnihilo.api.crafting.Lootable;
import wraith.fabricaeexnihilo.api.registry.*;
import wraith.fabricaeexnihilo.util.Color;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.ID;
import static wraith.fabricaeexnihilo.util.ItemUtils.getExNihiloBlock;
import static wraith.fabricaeexnihilo.util.ItemUtils.getExNihiloItem;

public class TechReborn implements IFabricaeExNihiloModule {
    @Override
    public void registerAlchemy(IAlchemyRegistry registry) {

    }

    @Override
    public void registerCompost(ICompostRegistry registry) {

    }

    @Override
    public void registerLeaking(ILeakingRegistry registry) {

    }

    @Override
    public void registerFluidOnTop(IFluidOnTopRegistry registry) {

    }

    @Override
    public void registerFluidTransform(IFluidTransformRegistry registry) {

    }

    @Override
    public void registerMilking(IMilkingRegistry registry) {

    }

    @Override
    public void registerCrucibleHeat(ICrucibleHeatRegistry registry) {
        registry.register(Registry.FLUID.get(new Identifier("techreborn:nitro_diesel")), 16);
    }

    @Override
    public void registerCrucibleStone(ICrucibleRegistry registry) {

    }

    @Override
    public void registerCrucibleWood(ICrucibleRegistry registry) {

    }

    @Override
    public void registerMesh(IMeshRegistry registry) {
        registry.register(
                ID("carbon_mesh"), ToolMaterials.IRON.getEnchantability(), "item.techreborn.carbon_fiber", Color.BLACK, new Identifier("techreborn:carbon_fiber")
        );
    }

    @Override
    public void registerSieve(ISieveRegistry registry) {
        var carbonMesh = getExNihiloItem("carbon_mesh");
        var goldMesh = getExNihiloItem("mesh_gold");
        var diamondMesh = getExNihiloItem("mesh_diamond");

        var crushedNetherrack = getExNihiloBlock("crushed_netherrack");
        var crushedEndstone = getExNihiloBlock("crushed_endstone");

        Item gem;
        gem = Registry.ITEM.get(new Identifier("techreborn:ruby_gem"));
        registry.register(goldMesh, Blocks.GRAVEL, new Lootable(gem, 0.01));
        registry.register(carbonMesh, Blocks.GRAVEL, new Lootable(gem, 0.05));

        gem = Registry.ITEM.get(new Identifier("techreborn:red_garnet_gem"));
        registry.register(diamondMesh, Blocks.GRAVEL, new Lootable(gem, 0.01));
        registry.register(carbonMesh, Blocks.GRAVEL, new Lootable(gem, 0.05));

        gem = Registry.ITEM.get(new Identifier("techreborn:sapphire_gem"));
        registry.register(diamondMesh, Fluids.WATER, Blocks.GRAVEL, new Lootable(gem, 0.01));
        registry.register(carbonMesh, Fluids.WATER, Blocks.GRAVEL, new Lootable(gem, 0.05));

        gem = Registry.ITEM.get(new Identifier("techreborn:yellow_garnet_gem"));
        registry.register(diamondMesh, crushedNetherrack, new Lootable(gem, 0.01));
        registry.register(carbonMesh, Registry.FLUID.get(new Identifier("techreborn:sodium_persulfate")), crushedNetherrack, new Lootable(gem, 0.05));

        gem = Registry.ITEM.get(new Identifier("techreborn:peridot_gem"));
        registry.register(goldMesh, crushedEndstone, new Lootable(gem, 0.01));
        registry.register(carbonMesh, crushedEndstone, new Lootable(gem, 0.05));
    }

    @Override
    public void registerCrook(IToolRegistry registry) {

    }

    @Override
    public void registerHammer(IToolRegistry registry) {

    }

    @Override
    public void registerWitchWaterWorld(IWitchWaterWorldRegistry registry) {

    }

    @Override
    public void registerWitchWaterEntity(IWitchWaterEntityRegistry registry) {

    }

    @Override
    public void registerOres(IOreRegistry registry) {

    }
}
