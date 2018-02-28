package exnihilocreatio.registries.types;

import exnihilocreatio.texturing.Color;
import exnihilocreatio.util.ItemInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Compostable {

    public static final Compostable EMPTY = new Compostable(0f, new Color(0), ItemInfo.EMPTY);

    @Getter
    private float value;
    @Getter
    private Color color;
    @Getter
    private ItemInfo compostBlock;
}
