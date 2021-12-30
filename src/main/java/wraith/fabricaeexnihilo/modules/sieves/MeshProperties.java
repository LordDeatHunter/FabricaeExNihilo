package wraith.fabricaeexnihilo.modules.sieves;

import net.devtech.arrp.json.recipe.*;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.util.Color;

public class MeshProperties {

    private final Identifier identifier;
    private final int enchantability;
    private final String displayName;
    private final Color color;
    private final Identifier keyIngredient;

    public MeshProperties(Identifier identifier, int enchantability, String displayName, Color color, Identifier keyIngredient) {
        this.identifier = identifier;
        this.enchantability = enchantability;
        this.displayName = displayName;
        this.color = color;
        this.keyIngredient = keyIngredient;
        this.item = new MeshItem(color, enchantability);
    }

    protected final MeshItem item;

    public JRecipe generateRecipe() {
        return JRecipe.shaped(
                JPattern.pattern(
                        "xox",
                        "oxo",
                        "xox"
                ),
                JKeys.keys()
                        .key("x",
                                JIngredient.ingredient()
                                        .item("minecraft:string")
                        )
                        .key("o",
                                JIngredient.ingredient()
                                        .item(keyIngredient.toString())
                        ),
                JResult.result(identifier.toString())
        );
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public int getEnchantability() {
        return enchantability;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Color getColor() {
        return color;
    }

    public Identifier getKeyIngredient() {
        return keyIngredient;
    }

    public MeshItem getItem() {
        return item;
    }

}