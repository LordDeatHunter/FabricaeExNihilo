package exnihilocreatio.recipes.defaults;

import exnihilocreatio.ModBlocks;
import exnihilocreatio.blocks.BlockSieve;
import exnihilocreatio.registries.registries.SieveRegistry;
import exnihilocreatio.util.ItemInfo;
import lombok.Getter;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import javax.annotation.Nullable;

@ObjectHolder("draconicevolution")
public class DraconicEvolution implements IRecipeDefaults {
    @Nullable
    @ObjectHolder("draconium_dust")
    public static final Item DRACO_DUST = null;

    @Getter
    private final String MODID = "draconicevolution";

    public void registerSieve(SieveRegistry registry) {
        registry.register(ModBlocks.endstoneCrushed, 0, new ItemInfo(DRACO_DUST, 0), 0.04f, BlockSieve.MeshType.IRON.getID());
        registry.register(ModBlocks.endstoneCrushed, 0, new ItemInfo(DRACO_DUST, 0), 0.08f, BlockSieve.MeshType.DIAMOND.getID());

        registry.register(ModBlocks.netherrackCrushed, 0, new ItemInfo(DRACO_DUST, 0), 0.02f, BlockSieve.MeshType.IRON.getID());
        registry.register(ModBlocks.netherrackCrushed, 0, new ItemInfo(DRACO_DUST, 0), 0.03f, BlockSieve.MeshType.DIAMOND.getID());
    }
}
