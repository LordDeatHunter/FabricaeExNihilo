package wraith.fabricaeexnihilo.compatibility.kubejs.recipe.barrel;

import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.recipe.*;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.rhino.mod.util.NBTUtils;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import wraith.fabricaeexnihilo.compatibility.kubejs.FENKubePlugin;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.EmptyMode;
import wraith.fabricaeexnihilo.recipe.util.EntityStack;
import wraith.fabricaeexnihilo.recipe.util.Loot;
import wraith.fabricaeexnihilo.util.CodecUtils;
import wraith.fabricaeexnihilo.util.RegistryEntryLists;

import java.util.Map;

public class AlchemyRecipeJS extends RecipeJS {

    private RegistryEntryList<Fluid> reactant;
    private Ingredient catalyst;
    private Loot byproduct = Loot.EMPTY;
    private int delay = 0;
    private EntityStack toSpawn = EntityStack.EMPTY;
    private BarrelMode result = new EmptyMode();

    @Override
    public void create(RecipeArguments args) {
        reactant = FENKubePlugin.getEntryList(args, 0, Registries.FLUID);
        catalyst = parseItemInput(args.get(1));
    }

    @SuppressWarnings("unused") // Used from js
    public AlchemyRecipeJS byproduct(Object stack, double... chances) {
        this.byproduct = new Loot(ItemStackJS.of(stack), chances);
        save();
        return this;
    }

    @SuppressWarnings("unused") // Used from js
    public AlchemyRecipeJS delay(int delay) {
        this.delay = delay;
        save();
        return this;
    }

    @SuppressWarnings("unused") // Used from js
    public AlchemyRecipeJS toSpawn(String type) {
        return toSpawn(type, 1);
    }

    public AlchemyRecipeJS toSpawn(String type, int count) {
        return toSpawn(type, count, Map.of());
    }

    public AlchemyRecipeJS toSpawn(String type, int count, Object nbt) {
        this.toSpawn = new EntityStack(new Identifier(type), count, NBTUtils.toTagCompound(nbt));
        save();
        return this;
    }

    @SuppressWarnings("unused") // Used from js
    public AlchemyRecipeJS result(Object result) {
        this.result = CodecUtils.fromJson(BarrelMode.CODEC, MapJS.json(result));
        save();
        return this;
    }

    @Override
    public boolean hasInput(IngredientMatch match) {
        return match.contains(catalyst);
    }

    @Override
    public boolean replaceInput(IngredientMatch match, Ingredient with, ItemInputTransformer transformer) {
        if (hasInput(match)) {
            catalyst = transformer.transform(this, match, catalyst, with);
            return true;
        }
        return false;
    }

    @Override
    public boolean hasOutput(IngredientMatch match) {
        return match.contains(byproduct.stack());
    }

    @Override
    public boolean replaceOutput(IngredientMatch match, ItemStack with, ItemOutputTransformer transformer) {
        if (hasOutput(match)) {
            byproduct = new Loot(transformer.transform(this, match, byproduct.stack(), with), byproduct.chances());
            return true;
        }
        return false;
    }

    @Override
    public void deserialize() {
        this.reactant = RegistryEntryLists.fromJson(Registries.FLUID.getKey(), json.get("reactant"));
        this.catalyst = Ingredient.fromJson(json.get("catalyst"));
        if (json.has("byproduct"))
            this.byproduct = CodecUtils.fromJson(Loot.CODEC, json.get("byproduct"));
        if (json.has("delay"))
            this.delay = JsonHelper.getInt(json, "delay");
        if (json.has("toSpawn"))
            this.toSpawn = CodecUtils.fromJson(EntityStack.CODEC, json.get("toSpawn"));
        if (json.has("result"))
            this.result = CodecUtils.fromJson(BarrelMode.CODEC, json.get("result"));
    }

    @Override
    public void serialize() {
        if (serializeInputs) {
            json.add("reactant", RegistryEntryLists.toJson(Registries.FLUID.getKey(), reactant));
            json.add("catalyst", catalyst.toJson());
            json.addProperty("delay", delay);
        }
        if (serializeOutputs) {
            json.add("byproduct", CodecUtils.toJson(Loot.CODEC, byproduct));
            json.add("toSpawn", CodecUtils.toJson(EntityStack.CODEC, toSpawn));
            json.add("result", CodecUtils.toJson(BarrelMode.CODEC, result));
        }
    }
}
