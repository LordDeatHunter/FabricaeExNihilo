package wraith.fabricaeexnihilo.api;

import wraith.fabricaeexnihilo.util.Color;

public record OreDefinition(Color color,
                            PieceShape pieceShape,
                            ChunkShape chunkShape,
                            BaseMaterial baseMaterial) {
    public enum ChunkShape {
        CHUNK, FLINT, LUMP
    }
    
    public enum PieceShape {
        COARSE, NORMAL, FINE
    }
    
    public enum BaseMaterial {
        ANDESITE, DIORITE, ENDSTONE, GRANITE, NETHERRACK, PRISMARINE, REDSAND, SAND, SOULSAND, STONE
    }
}
