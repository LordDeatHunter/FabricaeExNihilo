package wraith.fabricaeexnihilo.modules.tools;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import wraith.fabricaeexnihilo.api.registry.FabricaeExNihiloRegistries;
import wraith.fabricaeexnihilo.modules.ModTags;

public class CrookTool extends ToolItemWithRegistry {

    public CrookTool(ToolMaterial material, FabricItemSettings settings) {
        super(material, FabricaeExNihiloRegistries.CROOK, settings);
    }

    @Override
    public boolean isSuitableFor(BlockState state) {
        return FabricaeExNihiloRegistries.CROOK.isRegistered(state.getBlock());
    }

    public static boolean isCrook(ItemStack stack) {
        return stack.getItem() instanceof CrookTool || ModTags.CROOK_TAG.contains(stack.getItem());
    }

}