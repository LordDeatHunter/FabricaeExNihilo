package exnihilocreatio.registries.types;

import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.IStackInfo;
import exnihilocreatio.util.ItemInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Siftable {

    @Getter
    private IStackInfo drop;
    @Getter
    private float chance;
    @Getter
    private int meshLevel;

    //ExCompressum support, please update to IStackInfo
    @Deprecated
    public ItemInfo getDrop(){
        return drop instanceof ItemInfo ? (ItemInfo)drop : new ItemInfo((BlockInfo)drop);
    }
}
