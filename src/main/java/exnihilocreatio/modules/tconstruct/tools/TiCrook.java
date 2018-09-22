package exnihilocreatio.modules.tconstruct.tools;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.items.tools.ICrook;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.StringUtils;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.AoeToolCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tools.TinkerTools;

import javax.annotation.Nullable;
import java.util.List;

public class TiCrook extends AoeToolCore implements ICrook {
    public TiCrook() {
        this(PartMaterialType.handle(TinkerTools.toolRod),
                PartMaterialType.head(TinkerTools.toolRod),
                PartMaterialType.extra(TinkerTools.binding));

    }

    public TiCrook(PartMaterialType... requiredComponents) {
        super(requiredComponents);

        addCategory(Category.HARVEST);

        this.setHarvestLevel("crook", 0);

        this.setRegistryName("crook_tconstruct");
        this.setTranslationKey(ExNihiloCreatio.MODID+".crook_tconstruct");
    }

    @Override
    public float damagePotential() {
        return 0.5f;
    }

    @Override
    public double attackSpeed() {
        return 3f;
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState state) {
        if(StringUtils.isNullOrEmpty(toolClass) || state == null || ToolHelper.isBroken(stack))
            return -1;
        if(isEffective(state))
            return ToolHelper.getHarvestLevelStat(stack);
        return super.getHarvestLevel(stack, toolClass, player, state);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if(this.isInCreativeTab(tab)) {
            this.addDefaultSubItems(subItems, null, null, null);
        }
    }


    @Override
    public boolean isEffective(IBlockState state) {
        return ExNihiloRegistryManager.CROOK_REGISTRY.isRegistered(state);
    }

    @Override
    protected ToolNBT buildTagData(List<Material> materials) {
        return buildDefaultTag(materials);
    }

    @Override
    public boolean isCrook(ItemStack stack) {
        return true;
    }
}
