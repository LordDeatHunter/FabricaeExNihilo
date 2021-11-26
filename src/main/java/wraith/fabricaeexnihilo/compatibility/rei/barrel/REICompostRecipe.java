package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record REICompostRecipe(List<ItemStack> inputs, ItemStack output) {

    public REICompostRecipe(List<ItemStack> inputs, ItemStack output) {
        this.inputs = inputs;
        this.output = output;
    }

    public REICompostRecipe(List<ItemStack> inputs) {
        this(inputs, ItemStack.EMPTY);
    }

    public REICompostRecipe(ItemStack output) {
        this(new ArrayList<>(), output);
    }

    public List<ItemStack> getInputs() {
        return inputs;
    }

    public ItemStack getOutput() {
        return output;
    }

    public List<EntryIngredient> reiInputs() {
        return inputs.stream().map(EntryIngredients::of).toList();
    }

    public List<EntryIngredient> reiOutput() {
        return Collections.singletonList(EntryIngredients.of(output));
    }

}
