package exnihilofabrico.mixins;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin  {
    @Shadow
    private Map<RecipeType<?>, Map<Identifier, Recipe<?>>> recipeMap;

    public Map<RecipeType<?>, Map<Identifier, Recipe<?>>> exnihilo_getRecipeMap() {
        return recipeMap;
    }

    public void exnihilo_setRecipeMap(Map<RecipeType<?>, Map<Identifier, Recipe<?>>> map) {
        recipeMap = map;
    }

    public void exnihilo_putrecipe(Map<RecipeType<?>, Map<Identifier, Recipe<?>>> map) {
        recipeMap = map;
    }
}
