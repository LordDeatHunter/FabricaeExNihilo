package wraith.fabricaeexnihilo.recipe.barrel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.JsonHelper;

public sealed interface BarrelRecipeTrigger {
    static BarrelRecipeTrigger fromJson(JsonObject json) {
        var type = JsonHelper.getString(json, "type");
        return switch (type) {
            case "tick" -> new Tick(JsonHelper.getFloat(json, "chance", 1));
            case "insert_item" -> new ItemInserted(Ingredient.fromJson(json.get("item")));
            default -> throw new JsonParseException("Unknown trigger type: " + type);
        };
    }

    static BarrelRecipeTrigger fromPacket(PacketByteBuf buf) {
        var type = buf.readByte();
        return switch (type) {
            case 0 -> new Tick(buf.readFloat());
            case 1 -> new ItemInserted(Ingredient.fromPacket(buf));
            default -> throw new JsonParseException("Unknown trigger type id: " + type);
        };
    }

    void toPacket(PacketByteBuf buf);

    JsonObject toJson();

    record Tick(float chance) implements BarrelRecipeTrigger {
        @Override
        public void toPacket(PacketByteBuf buf) {
            buf.writeByte(0);
            buf.writeFloat(chance);
        }

        @Override
        public JsonObject toJson() {
            var json = new JsonObject();
            json.addProperty("type", "tick");
            json.addProperty("chance", chance);
            return json;
        }
    }

    record ItemInserted(Ingredient predicate) implements BarrelRecipeTrigger {
        @Override
        public void toPacket(PacketByteBuf buf) {
            buf.writeByte(1);
            predicate.write(buf);
        }

        @Override
        public JsonObject toJson() {
            var json = new JsonObject();
            json.addProperty("type", "insert_item");
            json.add("item", predicate.toJson());
            return json;
        }
    }
}
