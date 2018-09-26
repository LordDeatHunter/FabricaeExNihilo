package exnihilocreatio.modules.forestry.registry;

import exnihilocreatio.util.BlockInfo;
import lombok.Getter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nonnull;
import java.util.*;

public class HiveRequirements {
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
    private Map<Ingredient, Integer> adjacentBlocks = new HashMap<>();
    @Getter
    private Map<Ingredient, Integer> nearbyBlocks = new HashMap<>();

    // Kotlin doesn't like Lombok @AllArgsConstructor
    public HiveRequirements(BlockInfo hive, Integer dim, Set<Integer> biomes,
                            Float minT, Float maxT,
                            Integer minL, Integer maxL,
                            Integer minY, Integer maxY,
                            Map<Ingredient, Integer> adjacent, Map<Ingredient, Integer> nearby){
        this.hive = hive;
        this.dimension = dim;
        this.allowedBiomes = biomes;
        this.minTemperature = minT;
        this.maxTemperature = maxT;
        this.minElevation = minY;
        this.maxElevation = maxY;
        this.adjacentBlocks = adjacent;
        this.nearbyBlocks = nearby;
    }
    public HiveRequirements(BlockInfo hive, Integer dim, Set<Integer> biomes,
                            Map<Ingredient, Integer> adjacent, Map<Ingredient, Integer> nearby){
        this.hive = hive;
        this.dimension = dim;
        this.allowedBiomes = biomes;
        this.adjacentBlocks = adjacent;
        this.nearbyBlocks = nearby;
    }

    // Kotlin doesn't like Lombok @Getter
    public BlockInfo getHive(){
        return hive;
    }

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
        if(adjacentBlocks != null){
            IBlockState[] adjacentStates = getAdjacentBlockStates(world, pos);
            for(Ingredient ingredient : adjacentBlocks.keySet()){
                int req = adjacentBlocks.get(ingredient);
                for(IBlockState state : adjacentStates){
                    if(ingredient.test(new BlockInfo(state).getItemStack())){
                        req -= 1;
                    }
                }
                if(req > 0){
                    return false;
                }
            }
        }
        // Check Nearby Blocks
        if(nearbyBlocks != null){
            List<IBlockState> nearby = getNearbyStates(world, pos);
            for(Ingredient ingredient : nearbyBlocks.keySet()){
                int req = nearbyBlocks.get(ingredient);
                // If the same req is in the adjacent block set bump up the req due to double checking
                if(adjacentBlocks != null && adjacentBlocks.containsKey(ingredient))
                    req += adjacentBlocks.get(ingredient);
                for(IBlockState state : nearby){
                    if(ingredient.test(new BlockInfo(state).getItemStack())){
                        req -= 1;
                    }
                }
                if(req > 0){
                    return false;
                }
            }
        }

        return true;
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
