package wraith.fabricaeexnihilo.modules;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.tags.JTag;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.FabricaeExNihilo;

import java.util.Collection;
import java.util.Map;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public final class ModTags {
    
    private ModTags() {
    }
    
    public static final Tag.Identified<Item> CROOK_TAG = TagFactory.ITEM.create(FabricaeExNihilo.id("crook"));
    public static final Tag.Identified<Item> HAMMER_TAG = TagFactory.ITEM.create(FabricaeExNihilo.id("hammer"));
    public static final Tag.Identified<Item> INFESTED_LEAVES = TagFactory.ITEM.create(FabricaeExNihilo.id("infested_leaves"));
    public static final Tag.Identified<Block> INFESTED_LEAVES_BLOCK = TagFactory.BLOCK.create(FabricaeExNihilo.id("infested_leaves"));
    public static final Tag.Identified<Block> HAMMERABLES = TagFactory.BLOCK.create(id("hammerables"));
    public static final Tag.Identified<Block> CROOKABLES = TagFactory.BLOCK.create(id("crookables"));
    
    public static JTag generateResourcePackTag(Collection<Identifier> identifiers) {
        return addAllTags(JTag.tag(), identifiers);
    }
    
    public static JTag generateResourcePackTag(Identifier identifier) {
        JTag tag = JTag.tag();
        tag.add(identifier);
        return tag;
    }
    
    public static void registerBlockAndItem(RuntimeResourcePack resourcePack, Tag.Identified<?> tag, Map<Identifier, ?> map) {
        register(Category.BLOCKS, resourcePack, tag, map);
        register(Category.ITEMS, resourcePack, tag, map);
    }
    
    public static void register(Category category, RuntimeResourcePack resourcePack, Tag.Identified<?> tag, Map<Identifier, ?> map) {
        resourcePack.addTag(id(category.asString() + "/" + tag.getId().getPath()), ModTags.generateResourcePackTag(map.keySet()));
    }
    
    public static JTag addAllTags(JTag tag, Collection<Identifier> identifiers) {
        for (var identifier : identifiers) {
            tag.add(identifier);
        }
        return tag;
    }
    
    public enum Category {
        BLOCKS("blocks"),
        ITEMS("items");
        
        public final String type;
        
        Category(String type) {
            this.type = type;
        }
        
        public String asString() {
            return type;
        }
    }
    
}
