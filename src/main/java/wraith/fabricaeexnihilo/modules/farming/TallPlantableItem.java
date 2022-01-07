package wraith.fabricaeexnihilo.modules.farming;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TallPlantableItem extends Item {
    
    private final List<TallPlantBlock> plants;
    
    public TallPlantableItem(TallPlantBlock plant, FabricItemSettings settings) {
        this(Collections.singletonList(plant), settings);
    }
    
    public TallPlantableItem(List<TallPlantBlock> plants, FabricItemSettings settings) {
        super(settings);
        this.plants = plants;
    }
    
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var world = context.getWorld();
        var plantPos = context.getBlockPos().offset(context.getSide());
        var shuffledPlants = new ArrayList<>(plants);
        Collections.shuffle(shuffledPlants);
        for (var plant : shuffledPlants) {
            var lower = plant.getDefaultState().with(TallPlantBlock.HALF, DoubleBlockHalf.LOWER);
            var upper = plant.getDefaultState().with(TallPlantBlock.HALF, DoubleBlockHalf.UPPER);
            if (placementCheck(context) && lower.canPlaceAt(world, plantPos) && world.isAir(plantPos.up())) {
                world.setBlockState(plantPos, lower);
                world.setBlockState(plantPos.up(), upper);
                var player = context.getPlayer();
                if (player != null && !player.isCreative()) {
                    context.getStack().decrement(1);
                }
                return ActionResult.SUCCESS;
            }
        }
        return super.useOnBlock(context);
    }
    
    public boolean placementCheck(ItemUsageContext context) {
        return true;
    }
    
}
