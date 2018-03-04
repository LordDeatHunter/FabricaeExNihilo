package exnihilocreatio.registries.types;

import exnihilocreatio.util.IStackInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Siftable {

    @Getter
    private IStackInfo drop;
    @Getter
    private float chance;
    @Getter
    private int meshLevel;
}
