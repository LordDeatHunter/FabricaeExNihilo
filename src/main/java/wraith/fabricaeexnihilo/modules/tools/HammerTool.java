package wraith.fabricaeexnihilo.modules.tools;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import wraith.fabricaeexnihilo.api.registry.FabricaeExNihiloRegistries;
import wraith.fabricaeexnihilo.modules.ModTags;

public class HammerTool extends ToolItemWithRegistry {

    public HammerTool(ToolMaterial material, FabricItemSettings settings) {
        super(material, FabricaeExNihiloRegistries.HAMMER, settings);
    }

    @Override
    public boolean isSuitableFor(BlockState state) {
        return FabricaeExNihiloRegistries.HAMMER.isRegistered(state.getBlock());
    }

    public static boolean isHammer(ItemStack stack) {
        return stack.getItem() instanceof HammerTool || ModTags.HAMMER_TAG.contains(stack.getItem());
    }

}
