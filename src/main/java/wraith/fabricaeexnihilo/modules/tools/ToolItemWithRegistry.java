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
import wraith.fabricaeexnihilo.api.registry.ToolRecipeRegistry;

public abstract class ToolItemWithRegistry extends ToolItem {

    private final ToolRecipeRegistry registry;
    private final float blockBreakingSpeed;

    public ToolItemWithRegistry(ToolMaterial material, ToolRecipeRegistry registry, FabricItemSettings settings) {
        super(material, settings);
        this.registry = registry;
        this.blockBreakingSpeed = material.getMiningSpeedMultiplier();
    }

    @Override
    public boolean isSuitableFor(BlockState state) {
        return registry.isRegistered(state.getBlock());
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        return isSuitableFor(state) ? blockBreakingSpeed : 1F;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient && state.getHardness(world, pos) != 0.0f) {
            stack.damage(1, miner, ent -> ent.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
        return true;
    }

}
