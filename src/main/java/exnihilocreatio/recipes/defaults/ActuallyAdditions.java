package exnihilocreatio.recipes.defaults;

import exnihilocreatio.blocks.BlockSieve;
import exnihilocreatio.registries.registries.SieveRegistry;
import exnihilocreatio.util.ItemInfo;
import lombok.Getter;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;

public class ActuallyAdditions implements IRecipeDefaults {
    @Nullable
    @GameRegistry.ObjectHolder("actuallyadditions:item_misc")
    // 5 = Black Quartz
    public static final Item AA_ITEM_MISC = null;
    @Getter
    public final String MODID = "actuallyadditions";

    public void registerSieve(SieveRegistry registry) {
        //noinspection ConstantConditions
        if (AA_ITEM_MISC != null) {
            // Actually Additions crashes if these are actually registered
            registry.register("sand", new ItemInfo(AA_ITEM_MISC, 5), 0.02f, BlockSieve.MeshType.IRON.getID());
            registry.register("sand", new ItemInfo(AA_ITEM_MISC, 5), 0.05f, BlockSieve.MeshType.DIAMOND.getID());
        }
    }
}
