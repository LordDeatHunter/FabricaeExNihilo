package wraith.fabricaeexnihilo.modules.barrels.modes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.recipe.barrel.AlchemyRecipe;
import wraith.fabricaeexnihilo.recipe.util.EntityStack;
import wraith.fabricaeexnihilo.recipe.util.Loot;

import java.util.Optional;

public class AlchemyMode extends BarrelMode {
    public static final Codec<AlchemyMode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BarrelMode.CODEC
                    .fieldOf("before")
                    .forGetter(AlchemyMode::getBefore),
            BarrelMode.CODEC
                    .fieldOf("after")
                    .forGetter(AlchemyMode::getAfter),
            EntityStack.CODEC
                    .fieldOf("toSpawn")
                    .forGetter(AlchemyMode::getToSpawn),
            Codec.INT
                    .fieldOf("duration")
                    .forGetter(AlchemyMode::getDuration),
            Codec.INT
                    .optionalFieldOf("progress")
                    .forGetter(mode -> Optional.of(mode.progress)),
            Loot.CODEC
                    .optionalFieldOf("byproduct")
                    .forGetter(mode -> Optional.ofNullable(mode.byproduct))
    ).apply(instance, (before, after, toSpawn, duration, progress, byproduct) -> {
        var mode = new AlchemyMode(before, after, toSpawn, progress.orElse(0), duration);
        mode.byproduct = byproduct.orElse(null);
        return mode;
    }));
    
    private final BarrelMode before;
    private final BarrelMode after;
    private final EntityStack toSpawn;
    private int progress;
    private final int duration;
    private @Nullable Loot byproduct;
    public AlchemyMode(BarrelMode before, AlchemyRecipe recipe) {
        this(before, recipe.getResult().copy(), recipe.getToSpawn().copy(), recipe.getDelay());
        this.byproduct = recipe.getByproduct();
    }
    
    public AlchemyMode(BarrelMode before, BarrelMode after, int duration) {
        this(before, after, EntityStack.EMPTY, duration);
    }
    
    public AlchemyMode(BarrelMode before, BarrelMode after, EntityStack toSpawn, int duration) {
        this(before, after, toSpawn, 0, duration);
    }
    
    public AlchemyMode(BarrelMode before, BarrelMode after, EntityStack toSpawn, int progress, int duration) {
        super();
        this.before = before;
        this.after = after;
        this.toSpawn = toSpawn;
        this.progress = progress;
        this.duration = duration;
    }
    
    @Override
    public @NotNull String getId() {
        return "alchemy";
    }
    
    @Override
    public BarrelMode copy() {
        return new AlchemyMode(before.copy(), after.copy(), toSpawn.copy(), progress, duration);
    }

    @Override
    public EntryIngredient getReiResult() {
        var checking = after;
        while (checking instanceof AlchemyMode alchemyMode) {
            checking = alchemyMode.after;
            if (checking == this) {
                FabricaeExNihilo.LOGGER.warn("Recursive alchemy mode detected");
                return EntryIngredient.empty();
            }
        }
        return checking.getReiResult();
    }

    @Override
    public void tick(BarrelBlockEntity barrel) {
        progress += barrel.getEfficiencyMultiplier();
        var world = barrel.getWorld();
        if (world != null && !world.isClient && progress >= duration) {
            if (byproduct != null) {
                barrel.spawnByproduct(byproduct.createStack(world.random));
                byproduct = null;
            }
            barrel.spawnEntity(toSpawn);
            barrel.setMode(after);
        }
    }
    
    public int getDuration() {
        return duration;
    }
    
    public int getProgress() {
        return progress;
    }
    
    public BarrelMode getBefore() {
        return before;
    }
    
    public BarrelMode getAfter() {
        return after;
    }
    
    public EntityStack getToSpawn() {
        return toSpawn;
    }
}