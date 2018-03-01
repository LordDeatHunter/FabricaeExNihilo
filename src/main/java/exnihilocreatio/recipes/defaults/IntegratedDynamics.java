package exnihilocreatio.recipes.defaults;

import exnihilocreatio.blocks.BlockSieve.MeshType;
import exnihilocreatio.registries.registries.SieveRegistry;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import lombok.Getter;
import net.minecraft.block.BlockDirt;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;

public class IntegratedDynamics implements IRecipeDefaults {
    // Integrated Dynamics Support
    @Nullable
    @GameRegistry.ObjectHolder("integrateddynamics:menril_sapling")
    public static final Item MENRIL_SAPLING = null;
    @Getter
    public String MODID = "integrateddynamics";

    public void registerSieve(SieveRegistry registry) {
        // TODO: Consider instead using witch water to mutate a normal sapling into a menril sapling.
        registry.register(new BlockInfo(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT)), new ItemInfo(MENRIL_SAPLING, 0), 0.1f, MeshType.DIAMOND.getID());
        registry.register("dirt", new ItemInfo(MENRIL_SAPLING, 0), 0.02f, MeshType.DIAMOND.getID());
    }
}
