package wraith.fabricaeexnihilo.modules.barrels.modes;

import com.mojang.serialization.Codec;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.fluid.Fluids;
import net.minecraft.world.biome.Biome;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.ModTags;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.barrels.BarrelFluidStorage;
import wraith.fabricaeexnihilo.modules.barrels.BarrelItemStorage;
import wraith.fabricaeexnihilo.recipe.barrel.CompostRecipe;

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
    public EntryIngredient getReiResult() {
        return EntryIngredient.empty();
    }

    @Override
    public void precipitationTick(BarrelBlockEntity barrel, Biome.Precipitation precipitation) {
        if (precipitation == Biome.Precipitation.RAIN) {
            barrel.setMode(new FluidMode(FluidVariant.of(Fluids.WATER), FluidConstants.BUCKET / 100));
        }
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
        if (fluid.getFluid().isIn(ModTags.HOT) && !storage.barrel.isFireproof())
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
