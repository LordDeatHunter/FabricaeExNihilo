package exnihilocreatio.registries.registries;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.api.registries.IFluidItemFluidRegistry;
import exnihilocreatio.json.CustomBlockInfoJson;
import exnihilocreatio.json.CustomItemInfoJson;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.prefab.BaseRegistryList;
import exnihilocreatio.registries.types.FluidItemFluid;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import exnihilocreatio.util.StackInfo;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.util.List;

public class FluidItemFluidRegistry extends BaseRegistryList<FluidItemFluid> implements IFluidItemFluidRegistry {

    public FluidItemFluidRegistry() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(ItemInfo.class, CustomItemInfoJson.INSTANCE)
                        .registerTypeAdapter(StackInfo.class, CustomItemInfoJson.INSTANCE)
                        .registerTypeAdapter(BlockInfo.class, CustomBlockInfoJson.INSTANCE)
                        .create(),
                ExNihiloRegistryManager.FLUID_ITEM_FLUID_DEFAULT_REGISTRY_PROVIDERS
        );
    }

    public void register(@NotNull String inputFluid, @NotNull StackInfo reactant, @NotNull String outputFluid) {
        registry.add(new FluidItemFluid(inputFluid, reactant, outputFluid));
    }

    public void register(@NotNull Fluid inputFluid, @NotNull StackInfo reactant, @NotNull Fluid outputFluid) {
        registry.add(new FluidItemFluid(inputFluid.getName(), reactant, outputFluid.getName()));
    }

    public String getFLuidForTransformation(@NotNull Fluid fluid, @NotNull ItemStack stack) {
        ItemInfo info = new ItemInfo(stack);

        for (FluidItemFluid transformer : registry) {
            if (fluid.getName().equals(transformer.getInputFluid()) && info.equals(transformer.getReactant())) {
                return transformer.getOutput();
            }
        }

        return null;
    }

    @Override
    protected void registerEntriesFromJSON(FileReader fr) {
        List<FluidItemFluid> gsonInput = gson.fromJson(fr, new TypeToken<List<FluidItemFluid>>() {
        }.getType());
        registry.addAll(gsonInput);
    }

    @Override
    public List<?> getRecipeList() {
        return Lists.newLinkedList();
    }
}
