package wraith.fabricaeexnihilo.modules.barrels.modes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.recipe.barrel.AlchemyRecipe;
import wraith.fabricaeexnihilo.recipe.util.EntityStack;
import wraith.fabricaeexnihilo.recipe.util.Loot;
import wraith.fabricaeexnihilo.util.CodecUtils;

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
                    .fieldOf("countdown")
                    .forGetter(AlchemyMode::getCountdown),
            Loot.CODEC
                    .optionalFieldOf("byproduct")
                    .forGetter(mode -> Optional.ofNullable(mode.byproduct))
    ).apply(instance, (before, after, toSpawn, countdown, byproduct) -> {
        var mode = new AlchemyMode(before, after, toSpawn, countdown);
        mode.byproduct = byproduct.orElse(null);
        return mode;
    }));
    
    private final BarrelMode before;
    private final BarrelMode after;
    private final EntityStack toSpawn;
    private int countdown;
    private @Nullable Loot byproduct;
    
    public AlchemyMode(BarrelMode before, AlchemyRecipe recipe) {
        this(before, recipe.getResult().copy(), recipe.getToSpawn().copy(), recipe.getDelay());
        this.byproduct = recipe.getByproduct();
    }
    
    public AlchemyMode(BarrelMode before, BarrelMode after, EntityStack toSpawn, int countdown) {
        super();
        this.before = before;
        this.after = after;
        this.toSpawn = toSpawn;
        this.countdown = countdown;
    }
    
    public AlchemyMode(BarrelMode before, BarrelMode after, int countdown) {
        this(before, after, EntityStack.EMPTY, countdown);
    }
    
    @Override
    public @NotNull String getId() {
        return "alchemy";
    }
    
    @Override
    public BarrelMode copy() {
        return new AlchemyMode(before.copy(), after.copy(), toSpawn.copy(), countdown);
    }
    
    @Override
    public void tick(BarrelBlockEntity barrel) {
        countdown -= barrel.getEfficiencyMultiplier();
        var world = barrel.getWorld();
        if (world != null && !world.isClient && countdown <= 0) {
            if (byproduct != null) {
                barrel.spawnByproduct(byproduct.createStack(world.random));
                byproduct = null;
            }
            barrel.spawnEntity(toSpawn);
            barrel.setMode(after);
        }
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
    
    public int getCountdown() {
        return countdown;
    }
    
    public static AlchemyMode readNbt(NbtCompound nbt) {
        return new AlchemyMode(CodecUtils.fromNbt(CODEC, nbt.getCompound("before")), CodecUtils.fromNbt(CODEC, nbt.getCompound("after")), new EntityStack(nbt.getCompound("toSpawn")), nbt.getInt("countdown"));
    }
    
}