package wraith.fabricaeexnihilo.modules;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public final class ModTags {
    
    private ModTags() {
    }
    
    public static final Tag.Identified<Item> CROOK_TAG = TagFactory.ITEM.create(id("crooks"));
    public static final Tag.Identified<Item> HAMMER_TAG = TagFactory.ITEM.create(id("hammers"));
    public static final Tag.Identified<Block> HAMMERABLES = TagFactory.BLOCK.create(id("hammerables"));
    public static final Tag.Identified<Block> CROOKABLES = TagFactory.BLOCK.create(id("crookables"));
}
