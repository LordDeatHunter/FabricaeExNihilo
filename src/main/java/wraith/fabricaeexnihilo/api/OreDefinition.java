package wraith.fabricaeexnihilo.api;

import wraith.fabricaeexnihilo.modules.ore.ChunkMaterial;
import wraith.fabricaeexnihilo.modules.ore.ChunkShape;
import wraith.fabricaeexnihilo.modules.ore.PieceShape;
import wraith.fabricaeexnihilo.util.Color;

public record OreDefinition(Color color,
                            PieceShape pieceShape,
                            ChunkShape chunkShape,
                            ChunkMaterial chunkMaterial) {
}
