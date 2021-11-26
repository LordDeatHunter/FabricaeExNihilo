package wraith.fabricaeexnihilo.json.other;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.json.BaseJson;
import wraith.fabricaeexnihilo.util.RegistryUtils;

import java.lang.reflect.Type;

public final class ItemStackJson extends BaseJson<ItemStack> {

    private ItemStackJson() {}

    public static final ItemStackJson INSTANCE = new ItemStackJson();

    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive()) {
            if (json.getAsString().isEmpty()) {
                return ItemStack.EMPTY;
            } else {
                var splits = json.getAsString().split(" x ");
                return new ItemStack(Registry.ITEM.get(new Identifier(splits[1])), Integer.parseInt(splits[0]));
            }
        } else {
            var item = Registry.ITEM.get(new Identifier(json.getAsJsonObject().get("item").getAsString()));
            var count = json.getAsJsonObject().get("count").getAsInt();
            var stack = new ItemStack(item, count);
            try {
                NbtCompound tag = StringNbtReader.parse(json.getAsJsonObject().get("tag").getAsString());
                stack.setNbt(tag);
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
            return stack;
        }
    }

    @Override
    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        if (src.isEmpty()) {
            return new JsonPrimitive("");
        } else if (!src.hasNbt()) {
            return new JsonPrimitive(src.getCount() + " x " + RegistryUtils.getId(src.getItem()));
        } else {
            var obj = new JsonObject();
            obj.add("item", new JsonPrimitive(RegistryUtils.getId(src.getItem()).toString()));
            obj.add("count", new JsonPrimitive(src.getCount()));
            //noinspection ConstantConditions
            obj.add("tag", new JsonPrimitive(src.getNbt().toString()));
            return obj;
        }
    }

}