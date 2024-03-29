package wraith.fabricaeexnihilo.modules.tools;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wraith.fabricaeexnihilo.modules.ModTags;

public class HammerItem extends ToolItem {

    public HammerItem(ToolMaterial material, FabricItemSettings settings) {
        super(material, settings);
    }

    public static boolean isHammer(ItemStack stack) {
        return stack.getItem() instanceof HammerItem || stack.isIn(ModTags.HAMMERS);
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        return isSuitableFor(state) ? getMaterial().getMiningSpeedMultiplier() : 1F;
    }

    @Override
    public boolean isSuitableFor(BlockState state) {
        return state.isIn(ModTags.HAMMERABLES);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient && state.getHardness(world, pos) != 0.0f) {
            stack.damage(1, miner, ent -> ent.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
        return true;
    }
}
