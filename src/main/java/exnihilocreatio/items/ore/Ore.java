package exnihilocreatio.items.ore;

import exnihilocreatio.texturing.Color;
import exnihilocreatio.util.ItemInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode
@ToString
public class Ore {

    @Getter
    private String name;
    @Getter
    private Color color;
    @Getter
    private ItemInfo result;
    @Getter
    private Map<String, String> translations;
    @Getter
    private String oredictName;

    public Ore(String name, Color color, ItemInfo result, Map<String, String> translations, String oredictName) {
        this.name = name;
        this.color = color;
        this.result = result;
        this.translations = translations;
        this.oredictName = oredictName;
    }

}
