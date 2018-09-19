package exnihilocreatio.compatibility.tconstruct;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.items.tools.IHammer;
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

public class SledgeHammer extends AoeToolCore implements IHammer {
    public SledgeHammer() {
        this(PartMaterialType.handle(TinkerTools.toolRod),
                PartMaterialType.head(TinkerTools.hammerHead),
                PartMaterialType.extra(TinkerTools.binding));

    }

    public SledgeHammer(PartMaterialType... requiredComponents) {
        super(requiredComponents);

        addCategory(Category.HARVEST);

        this.setHarvestLevel("crushing", 0);

        this.setRegistryName("hammer_tconstruct");
        this.setTranslationKey(ExNihiloCreatio.MODID+".hammer_tconstruct");
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState state) {
        if(StringUtils.isNullOrEmpty(toolClass) || state == null)
            return -1;
        if(isEffective(state))
            return getMiningLevel(stack);
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
        return ExNihiloRegistryManager.HAMMER_REGISTRY.isRegistered(state);
    }

    @Override
    public float damagePotential() {
        return 1.1f;
    }

    @Override
    public double attackSpeed() {
        return 1.1f;
    }

    @Override
    public float knockback() {
        return 1.3f;
    }

    @Override
    protected ToolNBT buildTagData(List<Material> materials) {
        return buildDefaultTag(materials);
    }

    @Override
    public boolean isHammer(ItemStack stack) {
        return true;
    }

    @Override
    public int getMiningLevel(ItemStack stack) {
        if(ToolHelper.isBroken(stack))
            return -1;
        return ToolHelper.getHarvestLevelStat(stack);
    }
}
