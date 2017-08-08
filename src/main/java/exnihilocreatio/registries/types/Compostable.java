package exnihilocreatio.registries.types;

import exnihilocreatio.texturing.Color;
import exnihilocreatio.util.ItemInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Compostable {

    @Getter
    private float value;
    @Getter
    private Color color;
    @Getter
    private ItemInfo compostBlock;
}
