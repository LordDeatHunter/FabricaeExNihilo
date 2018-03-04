package exnihilocreatio.registries.types;

import exnihilocreatio.util.BlockInfo;
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

}
