package wraith.fabricaeexnihilo.recipe;

import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.util.Loot;
import wraith.fabricaeexnihilo.util.CodecUtils;
import wraith.fabricaeexnihilo.util.RegistryEntryLists;

import java.util.List;

public class ToolRecipe extends BaseRecipe<ToolRecipe.Context> {

    private final ToolType tool;
    private final RegistryEntryList<Block> block;
    private final Loot result;

    public ToolRecipe(Identifier id, ToolType tool, RegistryEntryList<Block> block, Loot result) {
        super(id);
        this.tool = tool;
        this.block = block;
        this.result = result;
    }

    public static List<ToolRecipe> find(ToolType type, Block block, @Nullable World world) {
        if (world == null) {
            return List.of();
        }
        return world.getRecipeManager().getAllMatches(type.type, new Context(block), world);
    }

    @Override
    public boolean matches(Context context, World world) {
        return block.contains(context.block.getRegistryEntry());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return tool.serializer;
    }

    @Override
    public RecipeType<?> getType() {
        return tool.type;
    }

    @Override
    public ItemStack getDisplayStack() {
        return result.stack();
    }

    public ToolType getTool() {
        return tool;
    }

    public RegistryEntryList<Block> getBlock() {
        return block;
    }

    public Loot getResult() {
        return result;
    }

    public enum ToolType {
        HAMMER(ModRecipes.HAMMER, ModRecipes.HAMMER_SERIALIZER),
        CROOK(ModRecipes.CROOK, ModRecipes.CROOK_SERIALIZER);

        public final RecipeType<ToolRecipe> type;
        public final RecipeSerializer<?> serializer;

        ToolType(RecipeType<ToolRecipe> type, RecipeSerializer<?> serializer) {
            this.type = type;
            this.serializer = serializer;
        }

        public static ToolType fromRecipeType(String type) {
            return switch (type) {
                case "fabricaeexnihilo:hammer" -> HAMMER;
                case "fabricaeexnihilo:crook" -> CROOK;
                default -> throw new IllegalStateException("Tried to find tool type for unknown recipe type: " + type);
            };
        }
    }

    public static class Serializer implements RecipeSerializer<ToolRecipe> {

        @Override
        public ToolRecipe read(Identifier id, JsonObject json) {
            var tool = ToolType.fromRecipeType(JsonHelper.getString(json, "type"));
            var block = RegistryEntryLists.fromJson(Registries.BLOCK.getKey(), json.get("block"));
            var result = CodecUtils.fromJson(Loot.CODEC, json.get("result"));

            return new ToolRecipe(id, tool, block, result);
        }

        @Override
        public ToolRecipe read(Identifier id, PacketByteBuf buf) {
            var tool = buf.readEnumConstant(ToolType.class);
            var block = RegistryEntryLists.fromPacket(Registries.BLOCK.getKey(), buf);
            var result = CodecUtils.fromPacket(Loot.CODEC, buf);

            return new ToolRecipe(id, tool, block, result);
        }

        @Override
        public void write(PacketByteBuf buf, ToolRecipe recipe) {
            buf.writeEnumConstant(recipe.tool);
            RegistryEntryLists.toPacket(Registries.BLOCK.getKey(), recipe.block, buf);
            CodecUtils.toPacket(Loot.CODEC, recipe.result, buf);
        }
    }

    public record Context(Block block) implements RecipeContext {
    }
}
