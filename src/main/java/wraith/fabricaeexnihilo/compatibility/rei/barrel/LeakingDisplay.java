package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.block.Material;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.recipe.barrel.LeakingRecipe;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public record LeakingDisplay(LeakingRecipe recipe) implements Display {

    @Override
    public List<EntryIngredient> getInputEntries() {
        var entity = recipe.getBlock().asREIEntries();
        var fluid = recipe.getFluid().asREIEntries();
        var barrels = ModBlocks.BARRELS.values().stream().filter(barrel -> barrel.getMaterial() != Material.STONE).map(EntryIngredients::of).toList();
        return Stream.of(entity, fluid, barrels).flatMap(List::stream).toList();
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return Collections.singletonList(EntryIngredients.of(recipe.getResult()));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.LEAKING;
    }

}
