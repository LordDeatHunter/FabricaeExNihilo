package wraith.fabricaeexnihilo.client;

import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.modules.base.Colored;
import wraith.fabricaeexnihilo.util.Color;

public final class FabricaeExNihiloBlockColorProvider implements BlockColorProvider {

    private FabricaeExNihiloBlockColorProvider() {}

    public static final FabricaeExNihiloBlockColorProvider INSTANCE = new FabricaeExNihiloBlockColorProvider();

    @Override
    public int getColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex) {
        return state.getBlock() instanceof Colored colored ? colored.getColor(tintIndex) : Color.WHITE.toInt();
    }

}