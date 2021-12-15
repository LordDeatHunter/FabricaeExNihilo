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
    private final PieceShape pieceShape;
    private final ChunkShape chunkShape;
    private final ChunkMaterial chunkMaterial;

    public OreProperties(String material, Color color, PieceShape pieceShape, ChunkShape chunkShape, ChunkMaterial chunkMaterial) {
        this.material = material;
        this.displayName = "item.fabricaeexnihilo." + material;
        this.color = color;
        this.pieceShape = pieceShape;
        this.chunkShape = chunkShape;
        this.chunkMaterial = chunkMaterial;
    }

    public Identifier getChunkID() {
        return FabricaeExNihilo.id((material + "_chunk").toLowerCase());
    }

    public Identifier getPieceID() {
        return FabricaeExNihilo.id((material + "_piece").toLowerCase());
    }
    
    public Identifier getIngotID() {
        return isVanillaMaterial() ? new Identifier(material + "_ingot") : new Identifier("c", material + "_ingots");
    }

    public Identifier getNuggetID() {
        return isVanillaMaterial() ? new Identifier(material + "_nugget") : new Identifier("c", material + "_nuggets");
    }

    public Identifier getOreID() {
        return new Identifier("c", "raw_" + material + "_ores");
    }

    public boolean isVanillaMaterial() {
        // return Registry.ITEM.containsId(new Identifier(material + "_ingot")) || Registry.ITEM.containsId(new Identifier(material + "_nugget")) || Registry.ITEM.containsId(new Identifier("raw_" + material)); // generic
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

    public PieceShape getPieceShape() {
        return pieceShape;
    }

    public ChunkShape getChunkShape() {
        return chunkShape;
    }

    public ChunkMaterial getChunkMaterial() {
        return chunkMaterial;
    }

    public static OrePropertyBuilder Builder(String material) {
        return new OrePropertyBuilder(material);
    }

    public static class OrePropertyBuilder {

        private final String material;
        private Color color = Color.WHITE;
        private PieceShape pieceShape = PieceShape.NORMAL;
        private ChunkShape chunkShape = ChunkShape.CHUNK;
        private ChunkMaterial chunkMaterial = ChunkMaterial.STONE;

        public OrePropertyBuilder(String material) {
            this.material = material;
        }

        public OrePropertyBuilder setColor(Color color) {
            this.color = color;
            return this;
        }

        public OrePropertyBuilder setPieceShape(PieceShape pieceShape) {
            this.pieceShape = pieceShape;
            return this;
        }

        public OrePropertyBuilder setChunkShape(ChunkShape chunkShape) {
            this.chunkShape = chunkShape;
            return this;
        }

        public OrePropertyBuilder setChunkMaterial(ChunkMaterial chunkMaterial) {
            this.chunkMaterial = chunkMaterial;
            return this;
        }

        public OreProperties build() {
            return new OreProperties(material, color, pieceShape, chunkShape, chunkMaterial);
        }
    }

}

