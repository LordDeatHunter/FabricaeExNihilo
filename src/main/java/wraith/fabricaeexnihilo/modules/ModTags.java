package wraith.fabricaeexnihilo.modules;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public final class ModTags {
    
    private ModTags() {
    }
    
    public static final TagKey<Item> CROOK_TAG = TagKey.of(Registry.ITEM_KEY, id("crooks"));
    public static final TagKey<Item> HAMMER_TAG = TagKey.of(Registry.ITEM_KEY, id("hammers"));
    public static final TagKey<Block> HAMMERABLES = TagKey.of(Registry.BLOCK_KEY, id("hammerables"));
    public static final TagKey<Block> CROOKABLES = TagKey.of(Registry.BLOCK_KEY, id("crookables"));
}
