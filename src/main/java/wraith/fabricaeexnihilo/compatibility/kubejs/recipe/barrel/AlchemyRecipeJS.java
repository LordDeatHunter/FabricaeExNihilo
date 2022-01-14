package wraith.fabricaeexnihilo.compatibility.kubejs.recipe.barrel;

import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.kubejs.util.MapJS;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.EmptyMode;
import wraith.fabricaeexnihilo.recipe.util.*;
import wraith.fabricaeexnihilo.util.CodecUtils;

public class AlchemyRecipeJS extends RecipeJS {
    private FluidIngredient reactant;
    private ItemIngredient catalyst;
    private Loot byproduct = Loot.EMPTY;
    private int delay = 0;
    private EntityStack toSpawn = EntityStack.EMPTY;
    private BarrelMode result = new EmptyMode();
    
    @Override
    public void create(ListJS listJS) {
        reactant = CodecUtils.fromJson(FluidIngredient.CODEC, new JsonPrimitive(listJS.get(0).toString()));
        catalyst = CodecUtils.fromJson(ItemIngredient.CODEC, new JsonPrimitive(listJS.get(1).toString()));
    }
    
    @SuppressWarnings("unused") // Used from js
    public AlchemyRecipeJS byproduct(ItemStackJS stack, double... chances) {
        this.byproduct = new Loot(stack.getItemStack(), chances);
        return this;
    }
    
    @SuppressWarnings("unused") // Used from js
    public AlchemyRecipeJS delay(int delay) {
        this.delay = delay;
        return this;
    }
    
    @SuppressWarnings("unused") // Used from js
    public AlchemyRecipeJS toSpawn(String type) {
        return toSpawn(type, 1);
    }
    
    // TODO: Figure out how to allow for nbt
    public AlchemyRecipeJS toSpawn(String type, int count) {
        this.toSpawn = new EntityStack(Registry.ENTITY_TYPE.get(new Identifier(type)), count);
        return this;
    }
    
    @SuppressWarnings("unused") // Used from js
    public AlchemyRecipeJS result(MapJS result) {
        this.result = CodecUtils.fromJson(BarrelMode.CODEC, MapJS.json(result));
        return this;
    }
    
    @Override
    public void deserialize() {
        this.reactant = CodecUtils.fromJson(FluidIngredient.CODEC, json.get("reactant"));
        this.catalyst = CodecUtils.fromJson(ItemIngredient.CODEC, json.get("catalyst"));
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
        json.add("reactant", CodecUtils.toJson(FluidIngredient.CODEC, reactant));
        json.add("catalyst", CodecUtils.toJson(ItemIngredient.CODEC, catalyst));
        json.add("byproduct", CodecUtils.toJson(Loot.CODEC, byproduct));
        json.addProperty("delay", delay);
        json.add("toSpawn", CodecUtils.toJson(EntityStack.CODEC, toSpawn));
        json.add("result", CodecUtils.toJson(BarrelMode.CODEC, result));
    }
}
