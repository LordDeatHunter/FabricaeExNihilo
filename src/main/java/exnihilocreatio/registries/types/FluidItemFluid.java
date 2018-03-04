package exnihilocreatio.registries.types;

import exnihilocreatio.util.StackInfo;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
public class FluidItemFluid {

    @Getter
    private String inputFluid;

    @Getter
    private StackInfo reactant;

    @Getter
    private String output;
}
