package exnihilocreatio.modules;

import exnihilocreatio.ModBlocks;
import exnihilocreatio.blocks.BlockSieve;
import exnihilocreatio.config.ModConfig;
import exnihilocreatio.modules.oreberries.ItemOreBerrySeed;
import exnihilocreatio.modules.oreberries.RenderOreBerrySeed;
import exnihilocreatio.recipes.defaults.IRecipeDefaults;
import exnihilocreatio.registries.registries.SieveRegistry;
import exnihilocreatio.util.ItemInfo;
import josephcsible.oreberries.BlockOreberryBush;
import josephcsible.oreberries.OreberriesMod;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OreBerries implements IExNihiloCreatioModule, IRecipeDefaults {
    public static final List<ItemOreBerrySeed> oreberryseeds = new ArrayList<>();

    @SidedProxy(serverSide = "exnihilocreatio.modules.OreBerries$Proxy", clientSide = "exnihilocreatio.modules.OreBerries$ProxyClient")
    public static Proxy proxy;

    @Override
    public String getMODID() {
        return "oreberries";
    }

    @Override
    public void preInit(FMLPreInitializationEvent event){
        // Initialize all the seeds
        for(BlockOreberryBush bush : OreberriesMod.proxy.oreberryBushBlocks){
            oreberryseeds.add(new ItemOreBerrySeed(bush));
        }
    }
    @Override
    public void init(FMLInitializationEvent event){
        proxy.init(event);
    }

    @SideOnly(Side.CLIENT)
    public static class Proxy {
        public void init(FMLInitializationEvent event){}
    }
    public static class ProxyClient extends Proxy{
        @Override
        public void init(FMLInitializationEvent event){
            super.init(event);
            List<Item> colorizedBerries = OreBerries.oreberryseeds.stream().filter(seed -> seed.getBush().config.special == null).collect(Collectors.toList());
            Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new RenderOreBerrySeed(), colorizedBerries.toArray(new ItemOreBerrySeed[0]));
        }

    }

    @Override
    public void registerOredicts(){
        if(ModConfig.compatibility.oreberries_compat.enableOreBerryOredict)
            for(ItemOreBerrySeed seed : oreberryseeds)
                OreDictionary.registerOre("listAllseed", new ItemStack(seed));
    }

    /**
     * IRecipeDefaults Overrides
     */
    @Override
    public void registerSieve(SieveRegistry registry) {
        for(ItemOreBerrySeed seed : oreberryseeds){
            float chance = ModConfig.compatibility.oreberries_compat.baseDropChance / seed.getBush().config.rarity;
            for(Integer dim : seed.getBush().config.dimensions){
                ItemStack toSieve;
                switch(dim){
                    case 1: toSieve = new ItemStack(ModBlocks.endstoneCrushed);
                        break;
                    case -1: toSieve = new ItemStack(ModBlocks.netherrackCrushed);
                        break;
                    default: toSieve = new ItemStack(Blocks.DIRT, 1,1);
                        break;
                }
                registry.register(toSieve, new ItemInfo(seed), chance / 5f, BlockSieve.MeshType.IRON.getID());
                registry.register(toSieve, new ItemInfo(seed), chance, BlockSieve.MeshType.DIAMOND.getID());
            }
        }
    }
}
