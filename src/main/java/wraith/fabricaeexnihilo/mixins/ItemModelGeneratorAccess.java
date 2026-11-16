package wraith.fabricaeexnihilo.mixins;

import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.ItemModelOutput;
import net.minecraft.client.data.Model;
import net.minecraft.client.data.ModelSupplier;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.BiConsumer;

/**
 * Access to the bits of {@link ItemModelGenerator} that the datagen run needs but
 * that 1.21.11 no longer exposes. 1.21 had a public {@code register(Item, Model)};
 * 1.21.11 made it private and moved model writing behind {@code modelCollector},
 * so both have to be reached through the mixin here.
 */
@Mixin(ItemModelGenerator.class)
public interface ItemModelGeneratorAccess {
    @Accessor("modelCollector")
    BiConsumer<Identifier, ModelSupplier> getModelCollector();

    /**
     * The item-definition sink (the {@code assets/<ns>/items/<name>.json} files).
     * 1.21.11 made the field private; only {@code register(Item, Model)} writes to
     * it, and that one always uses the default {@code item/<name>} texture, so
     * items with a custom texture need this accessor instead.
     */
    @Accessor("output")
    ItemModelOutput getOutput();

    @Invoker("register")
    void invokeRegister(Item item, Model model);
}
