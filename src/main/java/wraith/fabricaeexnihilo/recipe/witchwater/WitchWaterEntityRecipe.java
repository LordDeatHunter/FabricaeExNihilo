package wraith.fabricaeexnihilo.recipe.witchwater;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.RecipeContext;

import java.util.Optional;

public class WitchWaterEntityRecipe extends BaseRecipe<WitchWaterEntityRecipe.Context> {
    private final EntityType<?> target;
    private final NbtPathArgumentType.NbtPath nbt;
    private final EntityType<?> result;

    public WitchWaterEntityRecipe(Identifier id, EntityType<?> target, NbtPathArgumentType.NbtPath nbt, EntityType<?> result) {
        super(id);
        this.target = target;
        this.nbt = nbt;
        this.result = result;
    }

    public static Optional<WitchWaterEntityRecipe> find(Entity entity, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.WITCH_WATER_ENTITY, new Context(entity), world);
    }

    @Override
    public boolean matches(Context context, World world) {
        if (target != context.entity.getType())
            return false;
        return nbt.count(context.entity.writeNbt(new NbtCompound())) > 0;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.WITCH_WATER_ENTITY_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.WITCH_WATER_ENTITY;
    }

    @Override
    public ItemStack getDisplayStack() {
        var egg = SpawnEggItem.forEntity(result);
        return egg == null ? ItemStack.EMPTY : egg.getDefaultStack();
    }

    public EntityType<?> getTarget() {
        return target;
    }

    public NbtPathArgumentType.NbtPath getNbt() {
        return nbt;
    }

    public EntityType<?> getResult() {
        return result;
    }

    protected record Context(Entity entity) implements RecipeContext {}

    public static class Serializer implements RecipeSerializer<WitchWaterEntityRecipe> {

        @Override
        public WitchWaterEntityRecipe read(Identifier id, JsonObject json) {
            var target = Registries.ENTITY_TYPE.get(new Identifier(JsonHelper.getString(json, "target")));
            var result = Registries.ENTITY_TYPE.get(new Identifier(JsonHelper.getString(json, "result")));
            var nbt = parseNbtPath(JsonHelper.getString(json, "nbt", "{}"));

            return new WitchWaterEntityRecipe(id, target, nbt, result);
        }

        @Override
        public WitchWaterEntityRecipe read(Identifier id, PacketByteBuf buf) {
            var target = Registries.ENTITY_TYPE.get(buf.readIdentifier());
            var nbt = parseNbtPath(buf.readString());
            var result = Registries.ENTITY_TYPE.get(buf.readIdentifier());

            return new WitchWaterEntityRecipe(id, target, nbt, result);
        }

        @Override
        public void write(PacketByteBuf buf, WitchWaterEntityRecipe recipe) {
            buf.writeIdentifier(Registries.ENTITY_TYPE.getId(recipe.target));
            buf.writeString(recipe.nbt.toString()); // toString returns original string
            buf.writeIdentifier(Registries.ENTITY_TYPE.getId(recipe.result));
        }

        private static NbtPathArgumentType.NbtPath parseNbtPath(String string) {
            NbtPathArgumentType.NbtPath nbt;
            try {
                var reader = new StringReader(string);
                nbt = NbtPathArgumentType.nbtPath().parse(reader);
                var remaining = reader.getRemaining();
                if (remaining.length() > 0) {
                    throw new IllegalArgumentException("Found trailing data after nbt path: " + remaining);
                }
            } catch (CommandSyntaxException e) {
                throw new IllegalStateException("Invalid nbt filter", e);
            }
            return nbt;
        }
    }
}
