package exnihilocreatio.registries.registries.testReg;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.json.CustomItemInfoJson;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.manager.ICompostDefaultRegistryProvider;
import exnihilocreatio.registries.registries.prefab.BaseRegistryCollection;
import exnihilocreatio.registries.registries.prefab.BaseRegistryMap;
import exnihilocreatio.registries.types.Compostable;
import exnihilocreatio.texturing.Color;
import exnihilocreatio.util.ItemInfo;
import lombok.Getter;
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
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class CompostRegistry extends BaseRegistryMap<Map<ItemInfo, Compostable>> {

    public CompostRegistry() {
        super(
                new GsonBuilder().setPrettyPrinting().registerTypeAdapter(ItemInfo.class, new CustomItemInfoJson()).create(),
                new HashMap<>()
        );
    }

    public void register(ItemInfo item, Compostable compostable) {
        registry.put(item, compostable);
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

    public void registerDefaults() {
        for (ICompostDefaultRegistryProvider provider : ExNihiloRegistryManager.getDefaultCompostRecipeHandlers()) {
            provider.registerCompostRecipeDefaults();
        }
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
}
