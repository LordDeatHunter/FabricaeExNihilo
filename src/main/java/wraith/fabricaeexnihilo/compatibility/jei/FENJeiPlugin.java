package wraith.fabricaeexnihilo.compatibility.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.fabricmc.fabric.api.client.recipe.v1.sync.ClientRecipeSynchronizedEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.modules.ModBlocks;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * JEI integration for Fabricae Ex Nihilo.
 *
 * <p>Registered through the {@code jei_mod_plugin} entrypoint. On Fabric, JEI
 * discovers plugins through that entrypoint rather than through the
 * {@code @JeiPlugin} annotation. When JEI is not installed the entrypoint is
 * never queried, so this class is never loaded and no hard runtime dependency
 * is required.
 */
public class FENJeiPlugin implements IModPlugin {

    public static final RecipeType<JeiSieveRecipe> SIEVE =
            RecipeType.create("fabricaeexnihilo", "sieve", JeiSieveRecipe.class);

    private static final Identifier UID = Identifier.of("fabricaeexnihilo", "jei");
    private static final AtomicBoolean SYNC_LISTENER_REGISTERED = new AtomicBoolean(false);
    private static volatile IJeiRuntime runtime;

    @Override
    public Identifier getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new SieveRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(SIEVE, SieveRecipeCollector.collect());
        registerSyncListener();
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
    }

    @Override
    public void onRuntimeUnavailable() {
        runtime = null;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        for (var sieve : ModBlocks.SIEVES.values()) {
            registration.addRecipeCatalyst(new ItemStack(sieve), SIEVE);
        }
    }

    /**
     * Keep the collector's snapshot up to date, but never append recipes at runtime.
     * <p>
     * {@code registerRecipes} already succeeds: the integrated-server fallback in
     * {@link SieveRecipeCollector} returns the real recipes even before Fabric's
     * custom sync packet arrives, so the category is never empty on a client.
     * <p>
     * Adding the freshly collected list again (which this used to do after hiding
     * the old entries) does not replace anything: {@code IRecipeManager} has no
     * remove-recipe call, and {@code hideRecipes} only filters lookups - the
     * already-registered entries keep being drawn. The result was two recipe pages
     * stacked into one layout, which rendered as a first output slot stretched over
     * the whole grid with the second page's slots drawn on top of it.
     */
    private static void registerSyncListener() {
        if (!SYNC_LISTENER_REGISTERED.compareAndSet(false, true)) {
            return;
        }
        ClientRecipeSynchronizedEvent.EVENT.register((client, synced) ->
                SieveRecipeCollector.onRecipesSynchronized(synced));
    }
}
