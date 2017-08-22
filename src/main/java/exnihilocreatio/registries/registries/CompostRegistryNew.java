package exnihilocreatio.registries.registries;

import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.json.CustomItemInfoJson;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.prefab.BaseRegistryMap;
import exnihilocreatio.registries.types.Compostable;
import exnihilocreatio.texturing.Color;
import exnihilocreatio.util.ItemInfo;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Map;

public class CompostRegistryNew extends BaseRegistryMap<ItemInfo, Compostable> {

    public CompostRegistryNew() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(ItemInfo.class, new CustomItemInfoJson())
                        .create(),
                ExNihiloRegistryManager.getDefaultCompostRecipeHandlers()
        );
    }

    public void register(Item item, int meta, float value, IBlockState state, Color color) {
        register(new ItemInfo(item, meta), new Compostable(value, color, new ItemInfo(state)));
    }

    public void register(Block block, int meta, float value, IBlockState state, Color color) {
        register(Item.getItemFromBlock(block), meta, value, state, color);
    }

    public Compostable getItem(Item item, int meta) {
        return getItem(new ItemInfo(item, meta));
    }

    public Compostable getItem(ItemStack stack) {
        return getItem(new ItemInfo(stack));
    }

    public Compostable getItem(ItemInfo info) {
        return registry.get(info);
    }

    public boolean containsItem(Item item, int meta) {
        return containsItem(new ItemInfo(item, meta));
    }

    public boolean containsItem(ItemStack stack) {
        return containsItem(new ItemInfo(stack));
    }

    public boolean containsItem(ItemInfo info) {
        return registry.containsKey(info);
    }

    @SideOnly(Side.CLIENT)
    public void recommendAllFood(File file) {
        if (FMLCommonHandler.instance().getSide().isServer()) {
            return;
        }

        IBlockState dirt = Blocks.DIRT.getDefaultState();
        Color brown = new Color("7F3F0F");

        Map<String, Compostable> recommended = Maps.newHashMap();

        for (Item item : Item.REGISTRY) {
            if (item instanceof ItemFood) {
                ItemFood food = (ItemFood) item;

                NonNullList<ItemStack> stacks = NonNullList.create();
                food.getSubItems(CreativeTabs.FOOD, stacks);

                for (ItemStack foodStack : stacks) {
                    ItemInfo foodItemInfo = new ItemInfo(foodStack);

                    if (!containsItem(foodItemInfo)) {
                        int hungerRestored = food.getHealAmount(foodStack);

                        recommended.put(foodItemInfo.toString(), new Compostable(hungerRestored * 0.025F, brown, new ItemInfo(dirt)));
                    }
                }
            }
        }

        String json = gson.toJson(recommended, new TypeToken<Map<String, Compostable>>() {
        }.getType());

        try {
            Files.write(file.toPath(), json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerEntriesFromJSON(FileReader fr) {
        Map<String, Compostable> gsonInput = gson.fromJson(fr, new TypeToken<Map<String, Compostable>>() {
        }.getType());

        for (Map.Entry<String, Compostable> entry : gsonInput.entrySet()) {
            registry.put(new ItemInfo(entry.getKey()), entry.getValue());
        }
    }
}
