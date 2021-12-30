package wraith.fabricaeexnihilo.recipe.barrel;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.modules.ModRecipes;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.EmptyMode;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.RecipeContext;
import wraith.fabricaeexnihilo.recipe.util.EntityStack;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;
import wraith.fabricaeexnihilo.recipe.util.ItemIngredient;
import wraith.fabricaeexnihilo.recipe.util.Loot;
import wraith.fabricaeexnihilo.util.CodecUtils;

import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class AlchemyRecipe extends BaseRecipe<AlchemyRecipe.Context> {
    private final FluidIngredient reactant;
    private final ItemIngredient catalyst;
    private final Loot byproduct;
    private final int delay;
    private final EntityStack toSpawn;
    private final BarrelMode result;
    
    public AlchemyRecipe(Identifier id, FluidIngredient reactant, ItemIngredient catalyst, Loot byproduct, int delay, EntityStack toSpawn, BarrelMode result) {
        super(id);
        this.reactant = reactant;
        this.catalyst = catalyst;
        this.byproduct = byproduct;
        this.delay = delay;
        this.toSpawn = toSpawn;
        this.result = result;
}
    
    
    public static Optional<AlchemyRecipe> find(FluidVariant reactant, Item catalyst, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.ALCHEMY, new Context(reactant, catalyst), world);
    }
    
    @Override
    public boolean matches(Context context, World world) {
        return reactant.test(context.reactant) && catalyst.test(context.catalyst);
    }
    
    @Override
    public ItemStack getDisplayStack() {
        return byproduct.stack();
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ALCHEMY_SERIALIZER;
    }
    
    @Override
    public RecipeType<?> getType() {
        return ModRecipes.ALCHEMY;
    }
    
    public FluidIngredient getReactant() {
        return reactant;
    }
    
    public ItemIngredient getCatalyst() {
        return catalyst;
    }
    
    public Loot getByproduct() {
        return byproduct;
    }
    
    public int getDelay() {
        return delay;
    }
    
    public EntityStack getToSpawn() {
        return toSpawn;
    }
    
    public BarrelMode getResult() {
        return result;
    }
    
    public static class Serializer implements RecipeSerializer<AlchemyRecipe> {
        @Override
        public AlchemyRecipe read(Identifier id, JsonObject json) {
            FluidIngredient reactant = CodecUtils.fromJson(FluidIngredient.CODEC, json.get("reactant"));
            ItemIngredient catalyst = CodecUtils.fromJson(ItemIngredient.CODEC, json.get("catalyst"));
            Loot byproduct = json.has("byproduct") ? CodecUtils.fromJson(Loot.CODEC, json.get("byproduct")) : Loot.EMPTY;
            int delay = JsonHelper.getInt(json, "delay", 0);
            EntityStack toSpawn = json.has("toSpawn") ? CodecUtils.fromJson(EntityStack.CODEC, json.get("toSpawn")) : EntityStack.EMPTY;
            BarrelMode result = json.has("result") ? CodecUtils.fromJson(BarrelMode.CODEC, json.get("result")) : new EmptyMode();
            
            return new AlchemyRecipe(id, reactant, catalyst, byproduct, delay, toSpawn, result);
        }
    
        @Override
        public AlchemyRecipe read(Identifier id, PacketByteBuf buf) {
            FluidIngredient reactant = CodecUtils.fromPacket(FluidIngredient.CODEC, buf);
            ItemIngredient catalyst = CodecUtils.fromPacket(ItemIngredient.CODEC, buf);
            Loot byproduct = CodecUtils.fromPacket(Loot.CODEC, buf);
            int delay = buf.readInt();
            EntityStack toSpawn = CodecUtils.fromPacket(EntityStack.CODEC, buf);
            BarrelMode result = CodecUtils.fromPacket(BarrelMode.CODEC, buf);
    
            return new AlchemyRecipe(id, reactant, catalyst, byproduct, delay, toSpawn, result);
        }
    
        @Override
        public void write(PacketByteBuf buf, AlchemyRecipe recipe) {
            CodecUtils.toPacket(FluidIngredient.CODEC, recipe.reactant, buf);
            CodecUtils.toPacket(ItemIngredient.CODEC, recipe.catalyst, buf);
            CodecUtils.toPacket(Loot.CODEC, recipe.byproduct, buf);
            buf.writeInt(recipe.delay);
            CodecUtils.toPacket(EntityStack.CODEC, recipe.toSpawn, buf);
            CodecUtils.toPacket(BarrelMode.CODEC, recipe.result, buf);
        }
    }
    
    protected static record Context(FluidVariant reactant, Item catalyst) implements RecipeContext { }
}
