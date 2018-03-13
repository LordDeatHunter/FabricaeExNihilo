package exnihilocreatio.registries.registries;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.compatibility.jei.barrel.fluidontop.FluidOnTopRecipe;
import exnihilocreatio.json.CustomBlockInfoJson;
import exnihilocreatio.json.CustomItemInfoJson;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.prefab.BaseRegistryList;
import exnihilocreatio.registries.types.FluidFluidBlock;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nonnull;
import java.io.FileReader;
import java.util.List;

public class FluidOnTopRegistry extends BaseRegistryList<FluidFluidBlock> {
    public FluidOnTopRegistry() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(ItemInfo.class, new CustomItemInfoJson())
                        .registerTypeAdapter(BlockInfo.class, new CustomBlockInfoJson())
                        .create(),
                ExNihiloRegistryManager.FLUID_ON_TOP_DEFAULT_REGISTRY_PROVIDERS
        );
    }

    public void register(Fluid fluidInBarrel, Fluid fluidOnTop, BlockInfo result) {
        registry.add(new FluidFluidBlock(fluidInBarrel.getName(), fluidOnTop.getName(), result));
    }

    public void register(Fluid fluidInBarrel, Fluid fluidOnTop, ItemInfo result) {
        if (result.hasBlock())
            registry.add(new FluidFluidBlock(fluidInBarrel.getName(), fluidOnTop.getName(), new BlockInfo(result.getItemStack())));
    }

    public boolean isValidRecipe(Fluid fluidInBarrel, Fluid fluidOnTop) {
        if (fluidInBarrel == null || fluidOnTop == null)
            return false;
        for (FluidFluidBlock fBlock : registry) {
            if (fBlock.getFluidInBarrel().equals(fluidInBarrel.getName()) &&
                    fBlock.getFluidOnTop().equals(fluidOnTop.getName()))
                return true;
        }

        return false;
    }

    @Nonnull
    public BlockInfo getTransformedBlock(Fluid fluidInBarrel, Fluid fluidOnTop) {
        for (FluidFluidBlock fBlock : registry) {
            if (fBlock.getFluidInBarrel().equals(fluidInBarrel.getName()) &&
                    fBlock.getFluidOnTop().equals(fluidOnTop.getName()))
                return fBlock.getResult();
        }

        return BlockInfo.EMPTY;
    }

    @Override
    protected void registerEntriesFromJSON(FileReader fr) {
        List<FluidFluidBlock> gsonInput = gson.fromJson(fr, new TypeToken<List<FluidFluidBlock>>() {
        }.getType());
        registry.addAll(gsonInput);
    }

    @Override
    public List<FluidOnTopRecipe> getRecipeList() {
        List<FluidOnTopRecipe> fluidOnTopRecipes = Lists.newLinkedList();
        getRegistry().forEach(transformer -> {
            // Make sure both fluids are registered
            if (FluidRegistry.isFluidRegistered(transformer.getFluidInBarrel())
                    && FluidRegistry.isFluidRegistered(transformer.getFluidOnTop())
                    && transformer.getResult().isValid()) {
                FluidOnTopRecipe recipe = new FluidOnTopRecipe(transformer);

                if (recipe.isValid()) {
                    fluidOnTopRecipes.add(new FluidOnTopRecipe(transformer));
                }
            }
        });
        return fluidOnTopRecipes;
    }
}
