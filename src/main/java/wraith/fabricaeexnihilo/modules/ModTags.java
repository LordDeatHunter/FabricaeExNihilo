package wraith.fabricaeexnihilo.modules;

import net.devtech.arrp.json.tags.JTag;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

import java.util.Collection;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public final class ModTags {
    
    private ModTags() {
    }
    
    public static final Tag.Identified<Item> CROOK_TAG = TagFactory.ITEM.create(id("crooks"));
    public static final Tag.Identified<Item> HAMMER_TAG = TagFactory.ITEM.create(id("hammers"));
    public static final Tag.Identified<Block> HAMMERABLES = TagFactory.BLOCK.create(id("hammerables"));
    public static final Tag.Identified<Block> CROOKABLES = TagFactory.BLOCK.create(id("crookables"));
    public static final Tag.Identified<Item> ENCHANTABLE_HACK = TagFactory.ITEM.create(id("enchantables"));
    
    // TODO: get rid of this
    public static void addAllTags(JTag tag, Collection<Identifier> identifiers) {
        for (var identifier : identifiers) {
            tag.add(identifier);
        }
    }
    
}
