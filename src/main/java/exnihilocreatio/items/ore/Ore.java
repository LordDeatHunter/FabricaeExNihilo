package exnihilocreatio.items.ore;

import exnihilocreatio.texturing.Color;
import exnihilocreatio.util.ItemInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class Ore {

    @Getter
    private String name;
    @Getter
    private Color color;
    @Getter
    private ItemInfo result;

    public Ore(String name, Color color, ItemInfo result) {
        this.name = name;
        this.color = color;
        this.result = result;
    }

}
