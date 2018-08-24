package exnihilocreatio.recipes.defaults;

import exnihilocreatio.ModBlocks;
import exnihilocreatio.ModItems;
import exnihilocreatio.blocks.BlockSieve;
import exnihilocreatio.registries.registries.*;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import exnihilocreatio.util.LogUtil;
import exnihilocreatio.util.StackInfo;
import forestry.api.apiculture.*;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleSpecies;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import scala.xml.Null;

import java.util.LinkedHashMap;
import java.util.Map;

public class Forestry implements IRecipeDefaults {
    @Getter
    public String MODID = "forestry";


    @Override
    public void registerCompost(CompostRegistry registry) {
        BlockInfo dirtState = new BlockInfo(Blocks.DIRT);

        // Compost Drones
        // Compost Peat
        // Compost Compost (yo dawg)
        registry.register("forestry:fertilizerBio", 0.5f, dirtState);
        // Compost Mouldy Wheat, Decaying Wheat, Mulch
        registry.register("forestry:mouldyWheat", 0.0625f, dirtState);
        registry.register("forestry:decayingWheat", 0.125f, dirtState);
        registry.register("forestry:mulch", 0.25f, dirtState);
        // Compost Wood Pulp
        // Compost Pollens
    }

    @Override
    public void registerCrook(CrookRegistry registry) {
        // All Leaves for Forest Bees
        registry.register("treeLeaves", getDroneInfo("forestry.speciesForest").getItemStack(), 0.01f, 0.2f);
        registry.register("treeLeaves", getPrincessInfo("forestry.speciesForest").getItemStack(), 0.005f, 0.1f);
        // Jungle Leaves for Tropical
        registry.register("treeLeaves", getDroneInfo("forestry.speciesTropical").getItemStack(), 0.01f, 0.2f);
        registry.register("treeLeaves", getPrincessInfo("forestry.speciesTropical").getItemStack(), 0.005f, 0.1f);
    }

    @Override
    public void registerSieve(SieveRegistry registry) {
        // Sand for Apatite
        registry.register("sand", new ItemInfo("forestry:apatite"), 0.01f, BlockSieve.MeshType.STRING.getID());
        registry.register("sand", new ItemInfo("forestry:apatite"), 0.02f, BlockSieve.MeshType.FLINT.getID());
        registry.register("sand", new ItemInfo("forestry:apatite"), 0.03f, BlockSieve.MeshType.IRON.getID());
        registry.register("sand", new ItemInfo("forestry:apatite"), 0.05f, BlockSieve.MeshType.DIAMOND.getID());
        // Sand for Modest Bees
        registry.register("sand", getDroneInfo("forestry.speciesModest"), 0.05f, BlockSieve.MeshType.DIAMOND.getID());
        registry.register("sand", getPrincessInfo("forestry.speciesModest"), 0.01f, BlockSieve.MeshType.DIAMOND.getID());
        // Snow for Wintry Bees
        registry.register(Blocks.SNOW, -1, getDroneInfo("forestry.speciesWintry"), 0.05f, BlockSieve.MeshType.DIAMOND.getID());
        registry.register(Blocks.SNOW, -1, getPrincessInfo("forestry.speciesWintry"), 0.01f, BlockSieve.MeshType.DIAMOND.getID());
        // Humus for Marshy Bees
        registry.register(new BlockInfo("forestry:humus"), getDroneInfo("forestry.speciesMarshy"), 0.05f, BlockSieve.MeshType.DIAMOND.getID());
        registry.register(new BlockInfo("forestry:humus"), getPrincessInfo("forestry.speciesMarshy"), 0.01f, BlockSieve.MeshType.DIAMOND.getID());
        // Crushed End Stone for Ender Bees
        registry.register(new BlockInfo("exnihilocreatio:block_endstone_crushed"), getDroneInfo("forestry.speciesEnded"), 0.05f, BlockSieve.MeshType.DIAMOND.getID());
        registry.register(new BlockInfo("exnihilocreatio:block_endstone_crushed"), getPrincessInfo("forestry.speciesEnded"), 0.01f, BlockSieve.MeshType.DIAMOND.getID());
        // Grass for Meadows
        registry.register("grass", getDroneInfo("forestry.speciesMeadows"), 0.05f, BlockSieve.MeshType.DIAMOND.getID());
        registry.register("grass", getPrincessInfo("forestry.speciesMeadows"), 0.01f, BlockSieve.MeshType.DIAMOND.getID());


    }

    @Override
    public void registerCrucibleStone(CrucibleRegistry registry) {
        // Melt down honey drops
        registry.register("dropHoney", FluidRegistry.getFluid("for.honey"), 10);
    }

    private StackInfo getDroneInfo(String species){
        return new ItemInfo(getBeeStack(species, EnumBeeType.DRONE));
    }

    private StackInfo getPrincessInfo(String species){
        return new ItemInfo(getBeeStack(species, EnumBeeType.PRINCESS));
    }

    private ItemStack getBeeStack(String speciesName, EnumBeeType beeType){
        IAlleleBeeSpecies species = null;
        for(String uid : AlleleManager.alleleRegistry.getRegisteredAlleles().keySet()){
            if(uid.equals(speciesName)){
                IAllele allele = AlleleManager.alleleRegistry.getAllele(uid);
                if(allele instanceof IAlleleBeeSpecies){
                    species = (IAlleleBeeSpecies) allele;
                    break;
                }
            }
        }
        if(species == null){
            for (IAllele allele : AlleleManager.alleleRegistry.getRegisteredAlleles().values()){
                if(allele instanceof IAlleleBeeSpecies && allele.getAlleleName().equals(speciesName)){
                    species = (IAlleleBeeSpecies) allele;
                    break;
                }
            }
        }
        if(species == null){
            LogUtil.error("Could not find bee species: "+speciesName);
            return ItemStack.EMPTY;
        }
        IAllele[] template = BeeManager.beeRoot.getTemplate(species);
        IBeeGenome genome = BeeManager.beeRoot.templateAsGenome(template);
        IBee bee = BeeManager.beeRoot.getBee(genome);

        return BeeManager.beeRoot.getMemberStack(bee, beeType);
    }
}
