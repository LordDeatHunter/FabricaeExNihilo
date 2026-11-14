package wraith.fabricaeexnihilo.recipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class RecipeHelper {

    @SuppressWarnings("unchecked")
    public static <I extends RecipeInput, T extends Recipe<I>> List<RecipeEntry<T>> getAllMatches(RecipeType<T> type, I input, @Nullable World world) {
        if (world instanceof ServerWorld serverWorld) {
            return serverWorld.getRecipeManager().values().stream()
                    .filter(entry -> entry.value().getType() == type)
                    .map(entry -> (RecipeEntry<T>) entry)
                    .filter(entry -> entry.value().matches(input, world))
                    .toList();
        }
        return List.of();
    }

    @SuppressWarnings("unchecked")
    public static <I extends RecipeInput, T extends Recipe<I>> Optional<RecipeEntry<T>> getFirstMatch(RecipeType<T> type, I input, @Nullable World world) {
        if (world instanceof ServerWorld serverWorld) {
            return serverWorld.getRecipeManager().getFirstMatch(type, input, world);
        }
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> List<RecipeEntry<T>> listAllOfType(RecipeType<T> type, @Nullable World world) {
        if (world instanceof ServerWorld serverWorld) {
            return serverWorld.getRecipeManager().values().stream()
                    .filter(entry -> entry.value().getType() == type)
                    .map(entry -> (RecipeEntry<T>) entry)
                    .toList();
        }
        return List.of();
    }

    public static Optional<RecipeEntry<?>> get(Identifier id, @Nullable World world) {
        if (id == null || world == null) return Optional.empty();
        if (world instanceof ServerWorld serverWorld) {
            return serverWorld.getRecipeManager().values().stream()
                    .filter(entry -> entry.id().getValue().equals(id))
                    .findFirst();
        }
        return Optional.empty();
    }
}
