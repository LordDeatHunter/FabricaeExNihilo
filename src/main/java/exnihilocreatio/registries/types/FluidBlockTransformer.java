package exnihilocreatio.registries.types;

import exnihilocreatio.util.ItemInfo;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
public class FluidBlockTransformer {

    @Getter
    private String fluidName;

    @Getter
    private ItemInfo input;

    @Getter
    private ItemInfo output;

}
