package wraith.fabricaeexnihilo.recipe.crucible;

import com.google.gson.JsonObject;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.RecipeContext;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;

import java.util.Optional;
import java.util.function.Predicate;

public class CrucibleHeatRecipe extends BaseRecipe<CrucibleHeatRecipe.Context> {
    private final BlockIngredient block;
    private final int heat;

    public CrucibleHeatRecipe(Identifier id, BlockIngredient block, int heat) {
        super(id);
        this.block = block;
        this.heat = heat;
    }

    public static Optional<CrucibleHeatRecipe> find(BlockState state, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.CRUCIBLE_HEAT, new Context(state), world);
    }

    @Override
    public boolean matches(Context context, World world) {
        return block.test(context.state);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.CRUCIBLE_HEAT_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.CRUCIBLE_HEAT;
    }

    @Override
    public ItemStack getDisplayStack() {
        return block.getValue().map(ItemStack::new, tag -> Registries.BLOCK
                .getEntryList(tag)
                .stream()
                .flatMap(RegistryEntryList::stream)
                .map(RegistryEntry::value)
                .map(ItemStack::new)
                .filter(Predicate.not(ItemStack::isEmpty))
                .findFirst()
                .orElse(ItemStack.EMPTY));
    }

    public BlockIngredient getBlock() {
        return block;
    }

    public int getHeat() {
        return heat;
    }

    public static class Serializer implements RecipeSerializer<CrucibleHeatRecipe> {
        @Override
        public CrucibleHeatRecipe read(Identifier id, JsonObject json) {
            var block = BlockIngredient.fromJson(JsonHelper.getElement(json, "block"));
            var heat = JsonHelper.getInt(json, "heat");

            return new CrucibleHeatRecipe(id, block, heat);
        }

        @Override
        public CrucibleHeatRecipe read(Identifier id, PacketByteBuf buf) {
            var block = BlockIngredient.fromPacket(buf);
            var heat = buf.readInt();

            return new CrucibleHeatRecipe(id, block, heat);
        }

        @Override
        public void write(PacketByteBuf buf, CrucibleHeatRecipe recipe) {
            recipe.block.toPacket(buf);
            buf.writeInt(recipe.heat);
        }
    }

    protected record Context(BlockState state) implements RecipeContext {
    }
}
