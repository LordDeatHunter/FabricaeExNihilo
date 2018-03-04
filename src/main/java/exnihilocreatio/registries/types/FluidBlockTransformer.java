package exnihilocreatio.registries.types;

import exnihilocreatio.util.IStackInfo;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
public class FluidBlockTransformer {

    @Getter
    private String fluidName;

    @Getter
    private IStackInfo input;

    @Getter
    private IStackInfo output;

}
