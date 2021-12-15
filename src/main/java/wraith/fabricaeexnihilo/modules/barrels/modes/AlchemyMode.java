package wraith.fabricaeexnihilo.modules.barrels.modes;

import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import wraith.fabricaeexnihilo.api.crafting.EntityStack;

public class AlchemyMode implements BarrelMode {

    private BarrelMode before;
    private BarrelMode after;
    private EntityStack toSpawn;
    private int countdown;

    public AlchemyMode(BarrelMode before, BarrelMode after, EntityStack toSpawn, int countdown) {
        this.before = before;
        this.after = after;
        this.toSpawn = toSpawn;
        this.countdown = countdown;
    }

    public AlchemyMode(BarrelMode before, BarrelMode after, int countdown) {
        this(before, after, EntityStack.EMPTY, countdown);
    }

    public AlchemyMode(BarrelMode before, BarrelMode after, EntityStack toSpawn) {
        this(before, after, toSpawn, 0);
    }

    public AlchemyMode(BarrelMode before, BarrelMode after) {
        this(before, after, EntityStack.EMPTY, 0);
    }

    @Override
    public @NotNull String nbtKey() {
        return "alchemy_mode";
    }

    @Override
    public @NotNull NbtCompound writeNbt() {
        var nbt = new NbtCompound();
        nbt.put("before", before.writeNbt());
        nbt.put("after", after.writeNbt());
        nbt.put("toSpawn", toSpawn.toTag());
        nbt.putInt("countdown", countdown);
        return nbt;
    }

    public BarrelMode getBefore() {
        return before;
    }

    public void setBefore(BarrelMode before) {
        this.before = before;
    }

    public BarrelMode getAfter() {
        return after;
    }

    public void setAfter(BarrelMode after) {
        this.after = after;
    }

    public EntityStack getToSpawn() {
        return toSpawn;
    }

    public void setToSpawn(EntityStack toSpawn) {
        this.toSpawn = toSpawn;
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public static AlchemyMode readNbt(NbtCompound nbt) {
        return new AlchemyMode(BarrelMode.of(nbt.getCompound("before")), BarrelMode.of(nbt.getCompound("after")), new EntityStack(nbt.getCompound("toSpawn")), nbt.getInt("countdown"));
    }

}