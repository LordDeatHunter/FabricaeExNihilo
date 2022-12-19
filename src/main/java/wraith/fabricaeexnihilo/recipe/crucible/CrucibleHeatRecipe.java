package wraith.fabricaeexnihilo.recipe.crucible;

import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
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
import wraith.fabricaeexnihilo.util.RegistryEntryLists;

import java.util.Optional;

public class CrucibleHeatRecipe extends BaseRecipe<CrucibleHeatRecipe.Context> {

    private final RegistryEntryList<Block> block;
    private final int heat;

    public CrucibleHeatRecipe(Identifier id, RegistryEntryList<Block> block, int heat) {
        super(id);
        this.block = block;
        this.heat = heat;
    }

    public static Optional<CrucibleHeatRecipe> find(Block block, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.CRUCIBLE_HEAT, new Context(block), world);
    }

    @Override
    public boolean matches(Context context, World world) {
        return block.contains(context.block.getRegistryEntry());
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
        return block.stream().map(RegistryEntry::value).map(Block::asItem).map(Item::getDefaultStack).findFirst().orElse(ItemStack.EMPTY);
    }

    public RegistryEntryList<Block> getBlock() {
        return block;
    }

    public int getHeat() {
        return heat;
    }

    public static class Serializer implements RecipeSerializer<CrucibleHeatRecipe> {

        @Override
        public CrucibleHeatRecipe read(Identifier id, JsonObject json) {
            var block = RegistryEntryLists.fromJson(Registries.BLOCK.getKey(), json.get("block"));
            var heat = JsonHelper.getInt(json, "heat");

            return new CrucibleHeatRecipe(id, block, heat);
        }

        @Override
        public CrucibleHeatRecipe read(Identifier id, PacketByteBuf buf) {
            var block = RegistryEntryLists.fromPacket(Registries.BLOCK.getKey(), buf);
            var heat = buf.readInt();

            return new CrucibleHeatRecipe(id, block, heat);
        }

        @Override
        public void write(PacketByteBuf buf, CrucibleHeatRecipe recipe) {
            RegistryEntryLists.toPacket(Registries.BLOCK.getKey(), recipe.block, buf);
            buf.writeInt(recipe.heat);
        }
    }

    protected record Context(Block block) implements RecipeContext {
    }
}
