package exnihilocreatio.registries.types;

import exnihilocreatio.util.BlockInfo;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
public class FluidFluidBlock {

    @Getter
    private String fluidInBarrel;

    @Getter
    private String fluidOnTop;

    @Getter
    private BlockInfo result;
}
