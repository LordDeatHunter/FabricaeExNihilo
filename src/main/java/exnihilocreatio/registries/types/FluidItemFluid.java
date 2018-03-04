package exnihilocreatio.registries.types;

import exnihilocreatio.util.IStackInfo;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
public class FluidItemFluid {

    @Getter
    private String inputFluid;

    @Getter
    private IStackInfo reactant;

    @Getter
    private String output;
}
