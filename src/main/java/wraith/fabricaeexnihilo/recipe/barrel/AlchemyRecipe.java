package wraith.fabricaeexnihilo.recipe.barrel;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.api.crafting.EntityStack;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;
import wraith.fabricaeexnihilo.api.crafting.Loot;
import wraith.fabricaeexnihilo.modules.ModRecipes;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.EmptyMode;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.RecipeContext;
import wraith.fabricaeexnihilo.util.CodecUtils;

import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class AlchemyRecipe extends BaseRecipe<AlchemyRecipe.AlchemyRecipeContext> {
    private final FluidIngredient reactant;
    private final ItemIngredient catalyst;
    private final Loot byproduct;
    private final int delay;
    private final EntityStack toSpawn;
    private final BarrelMode result;
    
    protected AlchemyRecipe(Identifier id, FluidIngredient reactant, ItemIngredient catalyst, Loot byproduct, int delay, EntityStack toSpawn, BarrelMode result) {
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
        return world.getRecipeManager().getFirstMatch(ModRecipes.ALCHEMY, new AlchemyRecipeContext(reactant, catalyst), world);
    }
    
    @Override
    public boolean matches(AlchemyRecipeContext context, World world) {
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
            FluidIngredient reactant = FluidIngredient.fromJson(json.get("reactant"));
            ItemIngredient catalyst = ItemIngredient.fromJson(json.get("catalyst"));
            Loot byproduct = json.has("byproduct") ? CodecUtils.deserializeJson(Loot.CODEC, json.get("byproduct")) : Loot.EMPTY;
            int delay = JsonHelper.getInt(json, "delay", 0);
            EntityStack toSpawn = json.has("toSpawn") ? CodecUtils.deserializeJson(EntityStack.CODEC, json.get("toSpawn")) : EntityStack.EMPTY;
            BarrelMode result = json.has("result") ? BarrelMode.fromJson(json.get("result")) : new EmptyMode();
            
            return new AlchemyRecipe(id, reactant, catalyst, byproduct, delay, toSpawn, result);
        }
    
        @Override
        public AlchemyRecipe read(Identifier id, PacketByteBuf buf) {
            FluidIngredient reactant = FluidIngredient.fromPacket(buf);
            ItemIngredient catalyst = ItemIngredient.fromPacket(buf);
            Loot byproduct = CodecUtils.deserializeNbt(Loot.CODEC, buf.readNbt());
            int delay = buf.readInt();
            EntityStack toSpawn = CodecUtils.deserializeNbt(EntityStack.CODEC, buf.readNbt());
            BarrelMode result = BarrelMode.fromPacket(buf);
    
            return new AlchemyRecipe(id, reactant, catalyst, byproduct, delay, toSpawn, result);
        }
    
        @Override
        public void write(PacketByteBuf buf, AlchemyRecipe recipe) {
            recipe.reactant.toPacket(buf);
            recipe.catalyst.toPacket(buf);
            buf.writeNbt((NbtCompound) CodecUtils.serializeNbt(Loot.CODEC, recipe.byproduct));
            buf.writeInt(recipe.delay);
            buf.writeNbt((NbtCompound) CodecUtils.serializeNbt(EntityStack.CODEC, recipe.toSpawn));
            recipe.result.toPacket(buf);
        }
    }
    
    protected static record AlchemyRecipeContext(FluidVariant reactant, Item catalyst) implements RecipeContext { }
}
