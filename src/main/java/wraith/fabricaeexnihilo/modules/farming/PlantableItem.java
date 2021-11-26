package wraith.fabricaeexnihilo.modules.farming;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlantableItem extends Item {

    private final List<BlockState> plants;

    public PlantableItem(Block plant, FabricItemSettings settings) {
        this(Collections.singletonList(plant.getDefaultState()), settings);
    }

    public PlantableItem(List<BlockState> plants, FabricItemSettings settings) {
        super(settings);
        this.plants = plants;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var world = context.getWorld();
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        var plantPos = context.getBlockPos().offset(context.getSide());
        var shuffledPlants = new ArrayList<>(plants);
        Collections.shuffle(shuffledPlants);
        for (var plant : shuffledPlants) {
            if (placementCheck(context) && plant.canPlaceAt(world, plantPos)) {
                world.setBlockState(plantPos, plant);
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
