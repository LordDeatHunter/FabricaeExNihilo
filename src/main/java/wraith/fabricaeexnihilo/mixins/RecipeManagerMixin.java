package wraith.fabricaeexnihilo.mixins;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.fabricaeexnihilo.recipe.DummyRecipe;

import java.util.Optional;
import java.util.stream.StreamSupport;

// TODO: Should be replaced by resource conditions once they are added to fapi.
/**
 * Allows us to skip recipes we don't want to use. Specifically exclude mod specific recipes included by default.
 * Works by intercepting any unwanted recipe and producing a dummy recipe that does nothing.
 */
@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Inject(method = "deserialize", at = @At("HEAD"), cancellable = true)
    private static void interceptRecipes(Identifier id, JsonObject json, CallbackInfoReturnable<Recipe<?>> cir) {
        if (json.has("fabricaeexnihilo:requiredMod") && !FabricLoader.getInstance().isModLoaded(JsonHelper.getString(json, "fabricaeexnihilo:requiredMod"))) {
            cir.setReturnValue(new DummyRecipe<>(id));
        }
        if (json.has("fabricaeexnihilo:requiredItems") && StreamSupport.stream(JsonHelper.getArray(json, "fabricaeexnihilo:requiredItems").spliterator(), false)
                .map(JsonElement::getAsString)
                .map(Identifier::new)
                .map(Registry.ITEM::getOrEmpty)
                .anyMatch(Optional::isEmpty)) {
            cir.setReturnValue(new DummyRecipe<>(id));
        }
    }
}
