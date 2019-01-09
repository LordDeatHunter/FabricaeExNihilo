package exnihilocreatio.modules;

import exnihilocreatio.blocks.BlockBaseFalling;
import lombok.Getter;
import net.minecraft.block.SoundType;

public class AppliedEnergistics2 implements IExNihiloCreatioModule {
    @Getter
    public final String MODID = "appliedenergistics2";
    // BlockBaseFalling adds the block to Data.Blocks
    public static final BlockBaseFalling skystoneCrushed = (BlockBaseFalling) new BlockBaseFalling(SoundType.GROUND, "block_skystone_crushed").setHardness(0.7F);
}
