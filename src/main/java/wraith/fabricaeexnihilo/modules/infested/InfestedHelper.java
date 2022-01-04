package wraith.fabricaeexnihilo.modules.infested;

import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.util.RegistryUtils;

import java.util.HashMap;

public final class InfestedHelper {
    
    private InfestedHelper() {
    }
    
    public static final HashMap<Identifier, InfestedLeavesBlock> LEAF_TO_INFESTED = new HashMap<>();
    
    public static ActionResult tryToInfest(World world, BlockPos pos) {
        if (world.isClient) {
            return ActionResult.PASS;
        }
        var originalState = world.getBlockState(pos);
        if (!LEAF_TO_INFESTED.containsKey(RegistryUtils.getId(originalState.getBlock()))) {
            return ActionResult.PASS;
        }
        var infestedBlock = getInfestedLeavesBlock(originalState.getBlock());
        
        var newState = ModBlocks.INFESTING_LEAVES
                .getDefaultState()
                .with(LeavesBlock.DISTANCE, originalState.get(LeavesBlock.DISTANCE))
                .with(LeavesBlock.PERSISTENT, originalState.get(LeavesBlock.PERSISTENT));
        
        world.setBlockState(pos, newState);
        
        if (!(world.getBlockEntity(pos) instanceof InfestingLeavesBlockEntity blockEntity)) {
            return ActionResult.PASS;
        }
        
        blockEntity.setTarget(infestedBlock);
        
        return ActionResult.SUCCESS;
    }
    
    public static void tryToSpreadFrom(World world, BlockPos pos) {
        tryToSpreadFrom(world, pos, 1);
    }
    
    public static void tryToSpreadFrom(World world, BlockPos pos, int tries) {
        for (var attempt = 0; attempt < tries; ++attempt) {
            if ((world.random.nextFloat()) < FabricaeExNihilo.CONFIG.modules.silkworms.spreadChance) {
                var i = world.random.nextInt(3) - 1;
                var j = world.random.nextInt(3) - 1;
                var k = world.random.nextInt(3) - 1;
                var newPos = pos.add(new Vec3i(i, j, k));
                if (World.isValid(newPos)) {
                    tryToInfest(world, newPos);
                }
            }
        }
    }
    
    private static InfestedLeavesBlock getInfestedLeavesBlock(Block block) {
        return LEAF_TO_INFESTED.getOrDefault(Registry.BLOCK.getId(block), ModBlocks.INFESTED_LEAVES.values().stream().findFirst().orElse(null));
    }
    
    static {
        for (var entry : ModBlocks.INFESTED_LEAVES.values()) {
            LEAF_TO_INFESTED.put(entry.getLeafBlock(), entry);
        }
    }
    
}
