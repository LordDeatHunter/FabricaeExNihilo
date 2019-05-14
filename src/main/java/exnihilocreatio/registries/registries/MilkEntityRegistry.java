package exnihilocreatio.registries.registries;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.api.registries.IMilkEntityRegistry;
import exnihilocreatio.json.CustomBlockInfoJson;
import exnihilocreatio.json.CustomItemInfoJson;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.prefab.BaseRegistryList;
import exnihilocreatio.registries.types.Milkable;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraftforge.fluids.Fluid;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.util.List;

public class MilkEntityRegistry extends BaseRegistryList<Milkable> implements IMilkEntityRegistry {
    public MilkEntityRegistry() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(ItemInfo.class, CustomItemInfoJson.INSTANCE)
                        .registerTypeAdapter(BlockInfo.class, CustomBlockInfoJson.INSTANCE)
                        .create(),
                ExNihiloRegistryManager.MILK_ENTITY_DEFAULT_REGISTRY_PROVIDERS
        );
    }

    public void register(@NotNull Entity entityOnTop, @NotNull Fluid result, int amount, int coolDown) {
        registry.add(new Milkable(EntityList.getKey(entityOnTop).toString(), result.getName(), amount, coolDown));
    }

    public void register(@NotNull String entityOnTop, @NotNull String result, int amount, int coolDown) {
        registry.add(new Milkable(entityOnTop.toLowerCase(), result, amount, coolDown));
    }

    public boolean isValidRecipe(Entity entityOnTop) {
        return entityOnTop != null && this.isValidRecipe(entityOnTop.getName());
    }

    public boolean isValidRecipe(String entityOnTop) {
        if (entityOnTop == null) {
            return false;
        }
        for (Milkable milk : registry) {
            if (milk.getEntityOnTop().equals(entityOnTop)) {
                return true;
            }
        }
        return false;
    }

    public Milkable getMilkable(Entity entityOnTop) {
        // Returns the entire milkable object instead of having use multiple functions
        if (entityOnTop == null || EntityList.getKey(entityOnTop) == null) {
            return null;
        }
        // Convoluted comparison to maintain backwards compatibility with existing registries.
        String entityKey = EntityList.getKey(entityOnTop).toString();
        for (Milkable milk : registry) {
            if (entityKey.equals(milk.getEntityOnTop().toLowerCase())) {
                return milk;
            }
            else if (!milk.getEntityOnTop().contains(":") && entityKey.equals("minecraft:"+milk.getEntityOnTop().toLowerCase())){
                return milk;
            }
        }
        return null;
    }

    public String getResult(@NotNull Entity entityOnTop) {
        Milkable milk = getMilkable(entityOnTop);
        if(milk != null)
            return milk.getResult();
        return null;
    }

    public int getAmount(@NotNull Entity entityOnTop) {
        Milkable milk = getMilkable(entityOnTop);
        if(milk != null)
            return milk.getAmount();
        return 0;
    }

    public int getCoolDown(@NotNull Entity entityOnTop) {
        Milkable milk = getMilkable(entityOnTop);
        if(milk != null)
            return milk.getCoolDown();
        return 0;
    }

    @Override
    public void registerEntriesFromJSON(FileReader fr) {
        List<Milkable> gsonInput = gson.fromJson(fr, new TypeToken<List<Milkable>>() {
        }.getType());
        registry.addAll(gsonInput);
    }

    @Override
    public List<?> getRecipeList() {
        return Lists.newLinkedList();
    }
}
