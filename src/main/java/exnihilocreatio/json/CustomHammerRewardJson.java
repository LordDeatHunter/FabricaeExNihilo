package exnihilocreatio.json;

import com.google.gson.*;
import exnihilocreatio.registries.types.HammerReward;
import exnihilocreatio.util.ItemInfo;
import exnihilocreatio.util.LogUtil;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Type;

public class CustomHammerRewardJson implements JsonDeserializer<HammerReward>, JsonSerializer<HammerReward> {
    @Override
    public JsonElement serialize(HammerReward src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("item", src.getStack().getItem().getRegistryName().toString() + ":" + src.getStack().getMetadata());
        obj.addProperty("amount", src.getStack().getCount());
        obj.addProperty("miningLevel", src.getMiningLevel());
        obj.addProperty("chance", src.getChance());
        obj.addProperty("fortuneChance", src.getFortuneChance());

        return obj;
    }

    @Override
    public HammerReward deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonHelper helper = new JsonHelper(json);
        JsonObject obj = json.getAsJsonObject();


        // gets the item in both the new and the old way
        ItemStack stack;
        if (obj.has("item")){
            ItemInfo info = new ItemInfo(helper.getString("item"));
            if (!info.isValid()) {
                LogUtil.error("Error parsing JSON: Invalid Item: " + json.toString());
                return new HammerReward(ItemStack.EMPTY, 0,0,0);
            }

            stack = new ItemStack(info.getItem(), helper.getInteger("amount"), info.getMeta());
        } else {
            stack = context.deserialize(json.getAsJsonObject().get("stack"), ItemStack.class);
        }

        int miningLevel = helper.getInteger("miningLevel");
        float chance = (float) helper.getDouble("chance");
        float fortuneChance = (float) helper.getDouble("fortuneChance");

        return new HammerReward(stack, miningLevel, chance, fortuneChance);
    }
}
