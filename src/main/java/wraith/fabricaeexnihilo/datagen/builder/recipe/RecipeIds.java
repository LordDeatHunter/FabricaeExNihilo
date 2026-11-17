package wraith.fabricaeexnihilo.datagen.builder.recipe;

import net.fabricmc.fabric.api.datagen.v1.recipe.FabricRecipeExporter;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

/**
 * Recipe keys for the custom builders in this package.
 *
 * <p>The builders take plain paths such as {@code "compost/cactus"}, which
 * {@link Identifier#of(String)} resolves in the {@code minecraft} namespace. On 1.21 the
 * Fabric exporter used to move those into this mod's namespace for us. 1.21.11 changed
 * {@link RecipeExporter#accept} to take a {@link RegistryKey}, which carries its namespace
 * with it, so that rewrite no longer happens and every recipe would be written as
 * {@code minecraft:}. Run the id through {@link FabricRecipeExporter#getRecipeIdentifier}
 * to keep them in this mod's namespace.
 */
final class RecipeIds {
    private RecipeIds() {
    }

    static RegistryKey<Recipe<?>> key(RecipeExporter exporter, Identifier id) {
        var namespaced = exporter instanceof FabricRecipeExporter fabric
                ? fabric.getRecipeIdentifier(id)
                : id;
        return RegistryKey.of(RegistryKeys.RECIPE, namespaced);
    }
}
