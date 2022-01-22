package wraith.fabricaeexnihilo.modules.barrels.modes;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.ArrayUtils;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.FabricaeExNihiloConfig;
import wraith.fabricaeexnihilo.modules.barrels.BarrelFluidStorage;
import wraith.fabricaeexnihilo.modules.barrels.BarrelItemStorage;
import wraith.fabricaeexnihilo.recipe.barrel.CompostRecipe;

import java.util.Arrays;

@SuppressWarnings("UnstableApiUsage")
public class EmptyMode extends BarrelMode {
    public static final Codec<EmptyMode> CODEC = Codec.unit(new EmptyMode());
    
    public EmptyMode() {
        super();
    }
    
    @Override
    public String getId() {
        return "empty";
    }
    
    @Override
    public BarrelMode copy() {
        return new EmptyMode();
    }
    
    @Override
    public long insertItem(ItemVariant item, long maxAmount, TransactionContext transaction, BarrelItemStorage storage) {
        StoragePreconditions.notBlankNotNegative(item, maxAmount);
        var recipe = CompostRecipe.find(item.toStack(), storage.barrel.getWorld());
        if (recipe.isEmpty() || !FabricaeExNihilo.CONFIG.modules.barrels.compost.enabled) {
            return 0;
        }
        storage.updateSnapshots(transaction);
        storage.barrel.setMode(new CompostMode(recipe.get()));
        return 1;
    }
    
    @Override
    public long getItemCapacity() {
        return 1;
    }
    
    @Override
    public long insertFluid(FluidVariant fluid, long maxAmount, TransactionContext transaction, BarrelFluidStorage storage) {
        StoragePreconditions.notBlankNotNegative(fluid, maxAmount);
        var config = FabricaeExNihilo.CONFIG.modules.barrels;
        if (ArrayUtils.contains(config.woodenFluidFilter, Registry.FLUID.getId(fluid.getFluid()).toString()) != config.isFilterWhitelist)
            return 0;
        var amount = Math.min(maxAmount, FluidConstants.BUCKET);
        storage.updateSnapshots(transaction);
        storage.barrel.setMode(new FluidMode(fluid, amount));
        return amount;
    }
    
    @Override
    public long getFluidCapacity() {
        return FluidConstants.BUCKET;
    }
}
