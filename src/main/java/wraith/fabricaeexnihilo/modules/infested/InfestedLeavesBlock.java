package wraith.fabricaeexnihilo.modules.infested;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.base.Colored;
import wraith.fabricaeexnihilo.util.Color;
import wraith.fabricaeexnihilo.util.Lazy;

public class InfestedLeavesBlock extends LeavesBlock implements Colored, NonInfestableLeavesBlock {
    private final Identifier old;
    private final Lazy<Block> leafBlock;
    
    public InfestedLeavesBlock(Identifier leafBlock, Settings settings) {
        super(settings);
        this.old = leafBlock;
        this.leafBlock = new Lazy<>(() -> Registry.BLOCK.get(leafBlock));
    }
    
    @Override
    public int getColor(int index) {
        return Color.WHITE.toInt();
    }
    
    public Identifier getOld() {
        return old;
    }
    
    public Block getLeafBlock() {
        return leafBlock.get();
    }
    
    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }
    
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        if (world.isClient) {
            return;
        }
        InfestedHelper.tryToSpreadFrom(world, pos, FabricaeExNihilo.CONFIG.modules.silkworms.infestedSpreadAttempts);
    }
}