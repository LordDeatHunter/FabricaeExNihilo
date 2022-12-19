package wraith.fabricaeexnihilo.modules.other;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class EndCakeBlock extends CakeBlock {

    public EndCakeBlock(Settings settings) {
        super(settings);
    }

    protected static ActionResult tryEat(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (world.getRegistryKey() == World.END) {
            return ActionResult.FAIL;
        }
        if (!(world instanceof ServerWorld serverWorld) || player.hasVehicle() || player.hasPassengers() || !player.canUsePortals()) {
            return ActionResult.FAIL;
        }
        int i = state.get(BITES);
        if (i < 6) {
            world.setBlockState(pos, state.with(BITES, i + 1), Block.NOTIFY_ALL);
        } else {
            world.removeBlock(pos, false);
            world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);
        }
        RegistryKey<World> registryKey = world.getRegistryKey() == World.END ? World.OVERWORLD : World.END;
        ServerWorld destination = serverWorld.getServer().getWorld(registryKey);
        if (destination == null) {
            return ActionResult.FAIL;
        }
        player.moveToWorld(destination);
        return ActionResult.SUCCESS;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (world.isClient) {
            if (tryEat(world, pos, state, player).isAccepted()) {
                return ActionResult.SUCCESS;
            }
            if (itemStack.isEmpty()) {
                return ActionResult.CONSUME;
            }
        }
        return tryEat(world, pos, state, player);
    }

}
