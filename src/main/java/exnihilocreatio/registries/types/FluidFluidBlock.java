package exnihilocreatio.registries.types;

import exnihilocreatio.util.ItemInfo;
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
    private ItemInfo result;
}
