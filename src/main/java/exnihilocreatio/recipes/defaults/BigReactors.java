package exnihilocreatio.recipes.defaults;

import exnihilocreatio.ModBlocks;
import exnihilocreatio.blocks.BlockSieve.MeshType;
import exnihilocreatio.items.ore.ItemOre;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.HeatRegistry;
import exnihilocreatio.registries.registries.OreRegistry;
import exnihilocreatio.registries.registries.SieveRegistry;
import exnihilocreatio.texturing.Color;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import lombok.Getter;
import net.minecraft.item.Item;

public class BigReactors implements IRecipeDefaults {
    /*
    Mods Which use this same modid:
    * Big Reactors (the original)
    * Extreme Reactors (the fork)
    If extreme reactors ever stops updating, I bet someone will fork it and use the same modid
     */
    @Getter
    private final String MODID = "bigreactors"; // Extreme Reactors is a fork of Big Reactors and still uses the same MOD ID

    public void registerSieve(SieveRegistry registry) {
        ItemOre ore = ExNihiloRegistryManager.ORE_REGISTRY.getOreItem("yellorium");
        if (ore != null) {
            registry.register(new BlockInfo(ModBlocks.dust.getDefaultState()), new ItemInfo(ore, 0), 0.01f, MeshType.DIAMOND.getID());
        }
    }

    public void registerOreChunks(OreRegistry registry) {
        Item ingot = Item.getByNameOrId("bigreactors:ingotyellorium");
        Item dust = Item.getByNameOrId("bigreactors:dustyellorium");
        if (ingot != null) {
            ItemInfo ingotInfo = new ItemInfo(ingot, -1);
            ItemInfo dustInfo = new ItemInfo(dust, -1);
            registry.register("yellorium", new Color("DCF400"), ingotInfo, dustInfo);
            ItemOre ore = ExNihiloRegistryManager.ORE_REGISTRY.getOreItem("yellorium");
            registry.getSieveBlackList().add(ore); //Disables the default sieve recipes
        }
    }

    public void registerHeat(HeatRegistry registry) {
        BlockInfo blockYellorium = new BlockInfo("bigreactors:blockyellorium:-1");
        BlockInfo blockCyanite = new BlockInfo("bigreactors:blockcyanite:-1");
        BlockInfo blockBlutonium = new BlockInfo("bigreactors:blockblutonium:-1");

        if (blockYellorium.isValid()) {
            registry.register(blockYellorium, 10);
            registry.register(blockCyanite, 15);
            registry.register(blockBlutonium, 20);
        }
    }
}
