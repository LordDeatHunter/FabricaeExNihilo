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

import static wraith.fabricaeexnihilo.FabricaeExNihilo.ID;

public final class ModTags {

    private ModTags() {}

    public static final Tag.Identified<Item> CROOK_TAG = TagFactory.ITEM.create(FabricaeExNihilo.ID("crook"));
    public static final Tag.Identified<Item> HAMMER_TAG = TagFactory.ITEM.create(FabricaeExNihilo.ID("hammer"));
    public static final Tag.Identified<Item> INFESTED_LEAVES = TagFactory.ITEM.create(FabricaeExNihilo.ID("infested_leaves"));
    public static final Tag.Identified<Block> INFESTED_LEAVES_BLOCK = TagFactory.BLOCK.create(FabricaeExNihilo.ID("infested_leaves"));

    public static JTag getnerateResourcePackTag(Collection<Identifier> identifiers) {
        return addAllTags(JTag.tag(), identifiers);
    }

    public static JTag getnerateResourcePackTag(Identifier identifier) {
        JTag tag = JTag.tag();
        tag.add(identifier);
        return tag;
    }

    public static void registerBlockAndItem(RuntimeResourcePack resourcePack, Tag.Identified<?> tag, Map<Identifier, ?> map) {
        resourcePack.addTag(ID("blocks/" + tag.getId().getPath()), ModTags.getnerateResourcePackTag(map.keySet()));
        resourcePack.addTag(ID("items/" + tag.getId().getPath()), ModTags.getnerateResourcePackTag(map.keySet()));
    }

    public static JTag addAllTags(JTag tag, Collection<Identifier> identifiers) {
        for (var identifier : identifiers) {
            tag.add(identifier);
        }
        return tag;
    }

}
