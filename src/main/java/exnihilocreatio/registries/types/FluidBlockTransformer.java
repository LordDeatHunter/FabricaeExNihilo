package exnihilocreatio.registries.types;

import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.EntityInfo;
import exnihilocreatio.util.StackInfo;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
public class FluidBlockTransformer {

    @Getter
    private String fluidName;

    @Getter
    private StackInfo input;

    @Getter
    private BlockInfo output;

    @Getter
    private EntityInfo toSpawn;

    @Getter
    private int spawnCount;

    public FluidBlockTransformer(String fluidName, StackInfo input, BlockInfo output){
        this.fluidName = fluidName;
        this.input = input;
        this.output = output;
        this.spawnCount = 0;
    }

    public FluidBlockTransformer(String fluidName, StackInfo input, BlockInfo output, String entityName){
        this.fluidName = fluidName;
        this.input = input;
        this.output = output;
        this.toSpawn = new EntityInfo(entityName);
        this.spawnCount = 4;
    }

    public FluidBlockTransformer(String fluidName, StackInfo input, BlockInfo output, String entityName, int spawnCount){
        this.fluidName = fluidName;
        this.input = input;
        this.output = output;
        this.toSpawn = new EntityInfo(entityName);
        this.spawnCount = spawnCount;
    }

}
