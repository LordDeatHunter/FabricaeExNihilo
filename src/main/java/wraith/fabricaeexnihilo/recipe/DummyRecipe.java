package wraith.fabricaeexnihilo.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

// Concerning hack to disable a recipe without crashing...
public record DummyRecipe<T extends Inventory>(Identifier id) implements Recipe<T> {
    public static final RecipeType<DummyRecipe<?>> TYPE = new RecipeType<>() {
    };
    public static final RecipeSerializer<DummyRecipe<?>> SERIALIZER = new RecipeSerializer<>() {
        @Override
        public DummyRecipe<?> read(Identifier id, JsonObject json) {
            return new DummyRecipe<>(id);
        }
    
        @Override
        public DummyRecipe<?> read(Identifier id, PacketByteBuf buf) {
            return new DummyRecipe<>(id);
        }
    
        @Override
        public void write(PacketByteBuf buf, DummyRecipe<?> recipe) {
        
        }
    };
    
    @Override
    public boolean matches(T inventory, World world) {
        return false;
    }
    
    @Override
    public ItemStack craft(T inventory) {
        return ItemStack.EMPTY;
    }
    
    @Override
    public boolean fits(int width, int height) {
        return false;
    }
    
    @Override
    public ItemStack getOutput() {
        return ItemStack.EMPTY;
    }
    
    @Override
    public Identifier getId() {
        return id;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }
    
    @Override
    public RecipeType<?> getType() {
        return TYPE;
    }
}
