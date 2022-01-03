package wraith.fabricaeexnihilo.api;

import wraith.fabricaeexnihilo.util.Color;

public record OreDefinition(Color color,
                            PieceShape pieceShape,
                            ChunkShape chunkShape,
                            ChunkMaterial chunkMaterial) {
    public enum ChunkShape {
        CHUNK, FLINT, LUMP
    }
    
    public enum PieceShape {
        COARSE, NORMAL, FINE
    }
    
    public enum ChunkMaterial {
        ANDESITE, DIORITE, ENDSTONE, GRANITE, NETHERRACK, PRISMARINE, REDSAND, SAND, SOULSAND, STONE
    }
}
