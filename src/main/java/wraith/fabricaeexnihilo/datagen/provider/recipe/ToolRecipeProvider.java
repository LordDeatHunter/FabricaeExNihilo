package wraith.fabricaeexnihilo.datagen.provider.recipe;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import wraith.fabricaeexnihilo.modules.ModItems;
import wraith.fabricaeexnihilo.modules.ModTags;

import java.util.function.Consumer;

import static wraith.fabricaeexnihilo.datagen.builder.recipe.ToolRecipeJsonBuilder.crooking;

public class ToolRecipeProvider extends FabricRecipeProvider {
    public ToolRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        crooking(Blocks.ACACIA_LEAVES, Items.ACACIA_SAPLING, 0.25).offerTo(exporter, "crooking/acacia_sapling");
        crooking(Blocks.BIRCH_LEAVES, Items.BIRCH_SAPLING, 0.25).offerTo(exporter, "crooking/birch_sapling");
        crooking(Blocks.DARK_OAK_LEAVES, Items.DARK_OAK_SAPLING, 0.25).offerTo(exporter, "crooking/dark_oak_sapling");
        crooking(Blocks.JUNGLE_LEAVES, Items.JUNGLE_SAPLING, 0.25).offerTo(exporter, "crooking/jungle_sapling");
        crooking(Blocks.OAK_LEAVES, Items.OAK_SAPLING, 0.25).offerTo(exporter, "crooking/oak_sapling");
        crooking(Blocks.SPRUCE_LEAVES, Items.SPRUCE_SAPLING, 0.25).offerTo(exporter, "crooking/spruce_sapling");
        crooking(BlockTags.LEAVES, ModItems.RAW_SILKWORM, 0.1, 0.2, 0.3).offerTo(exporter, "crooking/silkwork");
        crooking(BlockTags.LEAVES, Items.STICK, 0.01).offerTo(exporter, "crooking/stick");
        crooking(ModTags.INFESTED_LEAVES, Items.STRING, 0.75, 0.25).offerTo(exporter, "crooking/string");

        //TODO: hammering
    }

    @Override
    public String getName() {
        return "Tool Recipes";
    }
}
