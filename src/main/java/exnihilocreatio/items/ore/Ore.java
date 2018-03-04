package exnihilocreatio.items.ore;

import exnihilocreatio.texturing.Color;
import exnihilocreatio.util.IStackInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class Ore {

    @Getter
    private String name;
    @Getter
    private Color color;
    @Getter
    private IStackInfo result;

    public Ore(String name, Color color, IStackInfo result) {
        this.name = name;
        this.color = color;
        this.result = result;
    }

}
