package exnihilocreatio.modules.forestry.registry;

import exnihilocreatio.util.BlockInfo;
import lombok.Getter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nonnull;
import java.util.*;

public class HiveRequirements {
    @Getter
    private BlockInfo hive = BlockInfo.EMPTY;
    @Getter
    private Integer dimension = null;
    @Getter
    private Set<Integer> allowedBiomes = null;
    @Getter
    private Float minTemperature = null;
    @Getter
    private Float maxTemperature = null;
    @Getter
    private Integer minLight = null;
    @Getter
    private Integer maxLight = null;
    @Getter
    private Integer minElevation = null;
    @Getter
    private Integer maxElevation = null;
    @Getter
    private Map<BlockInfo, Integer> adjacentBlocks = new HashMap<>();
    @Getter
    private Map<BlockInfo, Integer> nearbyBlocks = new HashMap<>();


    public boolean check(@Nonnull World world, @Nonnull BlockPos pos){
        // Check dimension
        if(dimension != null && !dimension.equals(world.provider.getDimension()))
            return false;
        // Check Biome
        if(allowedBiomes != null && !allowedBiomes.contains(Biome.getIdForBiome(world.getBiome(pos))))
            return false;

        // Check Temperature
        final float temp = world.getBiome(pos).getDefaultTemperature();
        if(!rangeCheck(temp, minTemperature, maxTemperature))
            return false;

        // Check Light
        final float light = world.getLight(pos);
        if(!rangeCheck(light, minLight, maxLight))
            return false;

        // Check Elevation
        if(!rangeCheck(pos.getY(), minElevation, maxElevation))
            return false;

        // Check Adjacent Blocks --- could probably be done smarter
        IBlockState[] adjacentStates = getAdjacentBlockStates(world, pos);
        for(BlockInfo info : adjacentBlocks.keySet()){
            int req = adjacentBlocks.get(info);
            for(IBlockState state : adjacentStates){
                if(info.equals(state)){
                    req -= 1;
                }
            }
            if(req > 0){
                return false;
            }
        }
        // Check Nearby Blocks
        List<IBlockState> nearby = getNearbyStates(world, pos);
        for(BlockInfo info : nearbyBlocks.keySet()){
            int req = nearbyBlocks.get(info);
            // If the same req is in the adjacent block set bump up the req due to double checking
            if(adjacentBlocks.containsKey(info))
                req += adjacentBlocks.get(info);
            for(IBlockState state : nearby){
                if(info.equals(state)){
                    req -= 1;
                }
            }
            if(req > 0){
                return false;
            }
        }

        return false;
    }

    private static boolean rangeCheck(Number toTest, Number min, Number max){
        if(min != null && min.floatValue() > toTest.floatValue())
            return false;
        if(max != null && max.floatValue() < toTest.floatValue())
            return false;
        return true;
    }

    private static IBlockState[] getAdjacentBlockStates(@Nonnull World world, @Nonnull BlockPos pos){
        return new IBlockState[]{
                world.getBlockState(pos.up()),
                world.getBlockState(pos.down()),
                world.getBlockState(pos.north()),
                world.getBlockState(pos.south()),
                world.getBlockState(pos.east()),
                world.getBlockState(pos.west())
        };
    }

    private static List<IBlockState> getNearbyStates(@Nonnull World world, @Nonnull BlockPos pos){
        ArrayList<IBlockState> nearby = new ArrayList<>();
        for (int i = -3; i < 3; i++)
            for (int j = -3; j < 3; j++)
                for (int k = -1; k < 1; k++)
                    if(i != 0 || j != 0 || k != 0) // Ignore Center Block, aka the hive
                        if(rangeCheck(pos.getY() + j, 0, 255)) // Ignore blocks above/below build limit
                            nearby.add(world.getBlockState(pos.add(i,j,k)));
        return nearby;
    }
}
