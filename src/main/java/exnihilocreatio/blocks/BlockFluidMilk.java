package exnihilocreatio.blocks;

import exnihilocreatio.ModFluids;
import exnihilocreatio.util.Data;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;

public class BlockFluidMilk extends BlockFluidClassic {

    public BlockFluidMilk() {
        super(ModFluids.fluidMilk, Material.WATER);

        this.setRegistryName("milk");
        this.setUnlocalizedName("milk");

        Data.BLOCKS.add(this);
    }
}
