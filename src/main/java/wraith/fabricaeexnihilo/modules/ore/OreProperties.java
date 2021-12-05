package wraith.fabricaeexnihilo.modules.ore;

import net.devtech.arrp.json.recipe.*;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.util.Color;

public class OreProperties {

    private final String material;
    private final String displayName;
    private final Color color;
    private final EnumPieceShape pieceShape;
    private final EnumChunkShape chunkShape;
    private final EnumChunkMaterial chunkMaterial;

    public OreProperties(String material, Color color, EnumPieceShape pieceShape, EnumChunkShape chunkShape, EnumChunkMaterial chunkMaterial) {
        this.material = material;
        this.displayName = "item.fabricaeexnihilo." + material;
        this.color = color;
        this.pieceShape = pieceShape;
        this.chunkShape = chunkShape;
        this.chunkMaterial = chunkMaterial;
    }

    public Identifier getChunkID() {
        return FabricaeExNihilo.ID((material + "_chunk").toLowerCase());
    }

    public Identifier getPieceID() {
        return FabricaeExNihilo.ID((material + "_piece").toLowerCase());
    }
    public Identifier getIngotID() {
        return isVanillaMaterial() ? new Identifier(material + "_ingot") : new Identifier("c", material + "_ingots");
    }

    public Identifier getNuggetID() {
        return isVanillaMaterial() ? new Identifier(material + "_nugget") : new Identifier("c", material + "_nuggets");
    }

    public Identifier getOreID() {
        return FabricaeExNihilo.ID(material + "_ore");
    }

    public Identifier getCommonRawOreID() {
        return new Identifier("c", "raw_" + material + "_ores");
    }

    public boolean isVanillaMaterial() {
        // return Registry.ITEM.containsId(new Identifier("minecraft", material + "_ingot")) || Registry.ITEM.containsId(new Identifier("minecraft", material + "_nugget")) || Registry.ITEM.containsId(new Identifier("minecraft", "raw_" + material)); // universal
        return material == "copper" || material == "iron" || material == "gold"; //fast
    }

    public Item getChunkItem() {
        return Registry.ITEM.get(getChunkID());
    }

    public Item getPieceItem() {
        return Registry.ITEM.get(getPieceID());
    }

    public JRecipe generateRecipe() {
        return JRecipe.shaped(
                JPattern.pattern(
                        "xx",
                        "xx"
                ),
                JKeys.keys()
                        .key("x",
                                JIngredient.ingredient()
                                        .item(getPieceID().toString())
                        ),
                JResult.result(getChunkID().toString())
        );
    }
    public JRecipe generateNuggetCookingRecipe() {
        return JRecipe.smelting(JIngredient.ingredient().item(getPieceID().toString()), JResult.result(getNuggetID().toString()));
    }

    public JRecipe generateIngotCookingRecipe() {
        return JRecipe.smelting(JIngredient.ingredient().item(getChunkID().toString()), JResult.result(getIngotID().toString()));
    }
    
    public String getMaterial() {
        return material;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Color getColor() {
        return color;
    }

    public EnumPieceShape getPieceShape() {
        return pieceShape;
    }

    public EnumChunkShape getChunkShape() {
        return chunkShape;
    }

    public EnumChunkMaterial getChunkMaterial() {
        return chunkMaterial;
    }

    public static OrePropertyBuilder Builder(String material) {
        return new OrePropertyBuilder(material);
    }

    public static class OrePropertyBuilder {

        private final String material;
        private Color color = Color.WHITE;
        private EnumPieceShape pieceShape = EnumPieceShape.NORMAL;
        private EnumChunkShape chunkShape = EnumChunkShape.CHUNK;
        private EnumChunkMaterial chunkMaterial = EnumChunkMaterial.STONE;

        public OrePropertyBuilder(String material) {
            this.material = material;
        }

        public OrePropertyBuilder setColor(Color color) {
            this.color = color;
            return this;
        }

        public OrePropertyBuilder setPieceShape(EnumPieceShape pieceShape) {
            this.pieceShape = pieceShape;
            return this;
        }

        public OrePropertyBuilder setChunkShape(EnumChunkShape chunkShape) {
            this.chunkShape = chunkShape;
            return this;
        }

        public OrePropertyBuilder setChunkMaterial(EnumChunkMaterial chunkMaterial) {
            this.chunkMaterial = chunkMaterial;
            return this;
        }

        public OreProperties build() {
            return new OreProperties(material, color, pieceShape, chunkShape, chunkMaterial);
        }
    }

}

