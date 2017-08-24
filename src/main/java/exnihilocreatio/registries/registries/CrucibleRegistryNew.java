package exnihilocreatio.registries.registries;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.json.CustomBlockInfoJson;
import exnihilocreatio.json.CustomItemInfoJson;
import exnihilocreatio.registries.manager.IDefaultRecipeProvider;
import exnihilocreatio.registries.registries.prefab.BaseRegistryMap;
import exnihilocreatio.registries.types.Meltable;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.io.FileReader;
import java.util.List;
import java.util.Map;

public class CrucibleRegistryNew extends BaseRegistryMap<ItemInfo, Meltable> {
    public CrucibleRegistryNew(List<? extends IDefaultRecipeProvider> defaultRecipeProviders) {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(ItemInfo.class, new CustomItemInfoJson())
                        .registerTypeAdapter(BlockInfo.class, new CustomBlockInfoJson())
                        .create(),
                defaultRecipeProviders
        );
    }

    public void register(ItemInfo item, Fluid fluid, int amount) {
        register(item, new Meltable(fluid.getName(), amount));
    }

    public void register(ItemStack stack, Fluid fluid, int amount) {
        register(new ItemInfo(stack), new Meltable(fluid.getName(), amount));
    }

    public void register(ItemInfo item, Meltable meltable) {
        registry.put(item, meltable);
    }

    public boolean canBeMelted(ItemStack stack) {
        return canBeMelted(new ItemInfo(stack));
    }

    public boolean canBeMelted(ItemInfo info) {
        return registry.containsKey(info) && FluidRegistry.isFluidRegistered(registry.get(info).getFluid());
    }

    public Meltable getMeltable(ItemStack stack) {
        ItemInfo info = new ItemInfo(stack);

        return registry.get(info);
    }

    public Meltable getMeltable(ItemInfo info) {
        return registry.get(info);
    }

    @Override
    protected void registerEntriesFromJSON(FileReader fr) {
        Map<String, Meltable> gsonInput = gson.fromJson(fr, new TypeToken<Map<String, Meltable>>() {
        }.getType());

        for (Map.Entry<String, Meltable> entry : gsonInput.entrySet()) {
            register(new ItemInfo(entry.getKey()), entry.getValue());
        }
    }
}
