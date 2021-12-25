package wraith.fabricaeexnihilo.modules.barrels.modes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.barrels.BarrelFluidStorage;
import wraith.fabricaeexnihilo.modules.barrels.BarrelItemStorage;
import wraith.fabricaeexnihilo.recipe.barrel.AlchemyRecipe;
import wraith.fabricaeexnihilo.recipe.barrel.FluidCombinationRecipe;
import wraith.fabricaeexnihilo.recipe.barrel.FluidTransformationRecipe;
import wraith.fabricaeexnihilo.recipe.barrel.LeakingRecipe;
import wraith.fabricaeexnihilo.util.CodecUtils;

@SuppressWarnings("UnstableApiUsage")
public class FluidMode extends BarrelMode {
    public static final Codec<FluidMode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    CodecUtils.FLUID_VARIANT
                            .fieldOf("fluid")
                            .forGetter(FluidMode::getFluid),
                    Codec.LONG
                            .fieldOf("amount")
                            .forGetter(FluidMode::getAmount))
            .apply(instance, FluidMode::new));
    
    private long amount;
    private final FluidVariant fluid;
    
    public FluidMode(FluidVariant fluid, long amount) {
        super();
        this.fluid = fluid;
        this.amount = amount;
    }
    
    @Override
    public String getId() {
        return "fluid";
    }
    
    @Override
    public BarrelMode copy() {
        return new FluidMode(fluid, amount);
    }
    
    @Override
    public void tick(BarrelBlockEntity barrel) {
        var config = FabricaeExNihilo.CONFIG.modules.barrels;
        var world = barrel.getWorld();
        var pos = barrel.getPos();
    
        if (amount >= FluidConstants.BUCKET) {
            var fluidState = world.getFluidState(pos.up());
            if (!fluidState.isEmpty()) {
                var fluid = fluidState.getFluid();
                var recipe = FluidCombinationRecipe.find(this.fluid, FluidVariant.of(fluid), world);
                recipe.ifPresent(fluidCombinationRecipe -> barrel.setMode(fluidCombinationRecipe.getResult().copy()));
            }
            var blockState = world.getBlockState(pos.down());
            if (!blockState.isAir()) {
                var block = blockState.getBlock();
                var recipe = FluidTransformationRecipe.find(this.fluid, block, world);
                if (recipe.isPresent()) {
                    var num = barrel.countBelow(block, config.transformBoostRadius);
                    barrel.setMode(new AlchemyMode(this, recipe.get().getResult(), config.transformRate - (num - 1) * config.transformBoost));
                }
            }
        }
        var leakPos = barrel.getLeakPos();
        if (leakPos == null) {
            return;
        }
        var recipe = LeakingRecipe.find(world.getBlockState(leakPos).getBlock(), this.fluid, world);
        if (recipe.isEmpty()) {
            return;
        }
        var leakAmount = recipe.get().getAmount();
        world.setBlockState(leakPos, recipe.get().getResult().getDefaultState());
        if (amount >= leakAmount) {
            amount -= leakAmount;
        } else {
            barrel.setMode(new EmptyMode());
        }
        barrel.markDirty();
    }
    
    public long getAmount() {
        return amount;
    }
    
    public void setAmount(long amount) {
        this.amount = amount;
    }
    
    @Override
    public FluidVariant getFluid() {
        return fluid;
    }
    
    @Override
    public long insertItem(ItemVariant item, long maxAmount, TransactionContext transaction, BarrelItemStorage storage) {
        StoragePreconditions.notBlankNotNegative(item, maxAmount);
        var result = AlchemyRecipe.find(fluid, item.getItem(), storage.barrel.getWorld());
        if (result.isEmpty()) {
            return 0;
        }
        storage.updateSnapshots(transaction);
        storage.barrel.setMode(new AlchemyMode(this, result.get()));
        return 1;
    }
    
    @Override
    public long insertFluid(FluidVariant fluid, long maxAmount, TransactionContext transaction, BarrelFluidStorage storage) {
        StoragePreconditions.notBlankNotNegative(fluid, maxAmount);
        if (!this.fluid.equals(fluid)) return 0;
    
        var amount = Math.min(FluidConstants.BUCKET - FluidMode.this.amount, maxAmount);
        storage.updateSnapshots(transaction);
        this.amount += amount;
        return amount;
    }
    
    @Override
    public long extractFluid(FluidVariant fluid, long maxAmount, TransactionContext transaction, BarrelFluidStorage storage) {
        StoragePreconditions.notBlankNotNegative(fluid, maxAmount);
        if (!this.fluid.equals(fluid)) return 0;
    
        var amount = Math.min(maxAmount, FluidMode.this.amount);
        storage.updateSnapshots(transaction);
        FluidMode.this.amount -= amount;
        if (FluidMode.this.amount == 0) storage.barrel.setMode(new EmptyMode());
        return amount;
    }
    
    @Override
    public long getFluidAmount() {
        return amount;
    }
    
    @Override
    public long getItemCapacity() {
        return 1;
    }
    
    @Override
    public long getFluidCapacity() {
        return FluidConstants.BUCKET;
    }
}
