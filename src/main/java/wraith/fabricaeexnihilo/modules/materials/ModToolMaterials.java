package wraith.fabricaeexnihilo.modules.materials;

import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;

public enum ModToolMaterials {
    WOODEN(ToolMaterial.WOOD),
    ANDESITE(ToolMaterial.STONE),
    DIORITE(ToolMaterial.STONE),
    GRANITE(ToolMaterial.STONE),
    DEEPSLATE(ToolMaterial.STONE),
    BLACKSTONE(ToolMaterial.STONE),
    BASALT(ToolMaterial.STONE),
    STONE(ToolMaterial.STONE),
    BONE(new ToolMaterial(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 131, 4.0f, 1.0f, 5, ItemTags.STONE_TOOL_MATERIALS)),
    BLAZE(new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1561, 6.0f, 2.0f, 22, ItemTags.STONE_TOOL_MATERIALS)),
    TERRACOTTA(new ToolMaterial(BlockTags.INCORRECT_FOR_STONE_TOOL, 250, 6.0f, 2.0f, 14, ItemTags.STONE_TOOL_MATERIALS)),
    CLAY(new ToolMaterial(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 1, 1.0f, 0.1f, 0, ItemTags.PLANKS)),
    PURPUR(new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1561, 8.0f, 3.0f, 10, ItemTags.STONE_TOOL_MATERIALS)),
    PRISMARINE(new ToolMaterial(BlockTags.INCORRECT_FOR_STONE_TOOL, 131, 4.0f, 1.0f, 5, ItemTags.STONE_TOOL_MATERIALS));

    private final ToolMaterial material;

    ModToolMaterials(ToolMaterial material) {
        this.material = material;
    }

    public ToolMaterial getMaterial() {
        return this.material;
    }
}
