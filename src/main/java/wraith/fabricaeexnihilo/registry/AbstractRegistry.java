package wraith.fabricaeexnihilo.registry;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.api.crafting.EntityTypeIngredient;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;
import wraith.fabricaeexnihilo.api.crafting.WeightedList;
import wraith.fabricaeexnihilo.api.recipes.SieveRecipe;
import wraith.fabricaeexnihilo.api.recipes.ToolRecipe;
import wraith.fabricaeexnihilo.api.recipes.barrel.AlchemyRecipe;
import wraith.fabricaeexnihilo.api.recipes.barrel.FluidOnTopRecipe;
import wraith.fabricaeexnihilo.api.recipes.barrel.FluidTransformRecipe;
import wraith.fabricaeexnihilo.api.recipes.barrel.LeakingRecipe;
import wraith.fabricaeexnihilo.api.recipes.crucible.CrucibleHeatRecipe;
import wraith.fabricaeexnihilo.api.recipes.crucible.CrucibleRecipe;
import wraith.fabricaeexnihilo.api.recipes.witchwater.WitchWaterEntityRecipe;
import wraith.fabricaeexnihilo.api.recipes.witchwater.WitchWaterWorldRecipe;
import wraith.fabricaeexnihilo.json.basic.*;
import wraith.fabricaeexnihilo.json.ingredient.EntityTypeIngredientJson;
import wraith.fabricaeexnihilo.json.ingredient.FluidIngredientJson;
import wraith.fabricaeexnihilo.json.ingredient.ItemIngredientJson;
import wraith.fabricaeexnihilo.json.other.*;
import wraith.fabricaeexnihilo.json.recipe.*;
import wraith.fabricaeexnihilo.modules.sieves.MeshProperties;
import wraith.fabricaeexnihilo.util.Color;

import java.io.File;
import java.io.FileWriter;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractRegistry<T> {

    public Gson gson;

    public AbstractRegistry() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .setLenient()
                .registerTypeAdapter(Item.class, ItemJson.INSTANCE)
                .registerTypeAdapter(Block.class, BlockJson.INSTANCE)
                .registerTypeAdapter(Color.class, ColorJson.INSTANCE)
                .registerTypeAdapter(EntityType.class, EntityTypeJson.INSTANCE)
                .registerTypeAdapter(Fluid.class, FluidJson.INSTANCE)
                .registerTypeAdapter(FluidVolume.class, FluidVolumeJson.INSTANCE)
                .registerTypeAdapter(Identifier.class, IdentifierJson.INSTANCE)
                .registerTypeAdapter(ItemStack.class, ItemStackJson.INSTANCE)
                .registerTypeAdapter(MeshProperties.class, MeshPropertiesJson.INSTANCE)
                .registerTypeAdapter(VillagerProfession.class, VillagerProfessionJson.INSTANCE)
                .registerTypeAdapter(WeightedList.class, WeightedListJson.INSTANCE)
                .registerTypeAdapter(ItemIngredient.class, ItemIngredientJson.INSTANCE)
                .registerTypeAdapter(FluidIngredient.class, FluidIngredientJson.INSTANCE)
                .registerTypeAdapter(EntityTypeIngredient.class, EntityTypeIngredientJson.INSTANCE)
                /*
                 * Recipes
                 */
                // Barrel
                .registerTypeAdapter(AlchemyRecipe.class, AlchemyRecipeJson.INSTANCE)
                .registerTypeAdapter(LeakingRecipe.class, LeakingRecipeJson.INSTANCE)
                .registerTypeAdapter(FluidTransformRecipe.class, FluidTransformRecipeJson.INSTANCE)
                .registerTypeAdapter(FluidOnTopRecipe.class, FluidOnTopRecipeJson.INSTANCE)
                // Crucible
                .registerTypeAdapter(CrucibleHeatRecipe.class, CrucibleHeatRecipeJson.INSTANCE)
                .registerTypeAdapter(CrucibleRecipe.class, CrucibleRecipeJson.INSTANCE)
                // Witchwater
                .registerTypeAdapter(WitchWaterWorldRecipe.class, WitchWaterWorldRecipeJson.INSTANCE)
                .registerTypeAdapter(WitchWaterEntityRecipe.class, WitchWaterEntityRecipeJson.INSTANCE)
                // Other
                .registerTypeAdapter(ToolRecipe.class, ToolRecipeJson.INSTANCE)
                .registerTypeAdapter(SieveRecipe.class, SieveRecipeJson.INSTANCE)

                .enableComplexMapKeySerialization()
                .create();
    }

    protected abstract void registerJson(File file);

    protected abstract T serializable();

    public static <T, U extends AbstractRegistry<T>> U fromJson(File file, Supplier<U> factory, Consumer<U> defaults) {
        var registry = factory.get();
        if (file.exists() && FabricaeExNihilo.CONFIG.useJsonRecipes) {
            registry.registerJson(file);
        } else {
            defaults.accept(registry);
            if (FabricaeExNihilo.CONFIG.useJsonRecipes) {
                try {
                    var fw = new FileWriter(file);
                    registry.gson.toJson(registry.serializable(), TypeToken.class, fw);
                    fw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return registry;
    }

}
