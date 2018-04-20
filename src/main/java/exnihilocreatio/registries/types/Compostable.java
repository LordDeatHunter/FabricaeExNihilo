package exnihilocreatio.registries.types;

import exnihilocreatio.texturing.Color;
import exnihilocreatio.util.BlockInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.Nonnull;

@AllArgsConstructor
public class Compostable {

    public static final Compostable EMPTY = new Compostable(0f, new Color(0), BlockInfo.EMPTY);

    @Getter
    private float value;
    @Getter
    private Color color;
    @Getter
    @Nonnull
    private BlockInfo compostBlock;
}
