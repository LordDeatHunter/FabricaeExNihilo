package wraith.fabricaeexnihilo.modules.barrels.modes;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import net.minecraft.nbt.NbtCompound;

public class FluidMode implements BarrelMode {

    private FluidVolume fluid;

    public FluidMode(FluidVolume fluid) {
        this.fluid = fluid;
    }

    @Override
    public String nbtKey() {
        return "fluid_mode";
    }

    @Override
    public NbtCompound writeNbt() {
        var nbt = new NbtCompound();
        nbt.put(nbtKey(), fluid.toTag());
        return nbt;
    }

    public FluidVolume getFluid() {
        return fluid;
    }

    public void setFluid(FluidVolume fluid) {
        this.fluid = fluid;
    }

    public static FluidMode readNbt(NbtCompound nbt) {
        return new FluidMode(FluidVolume.fromTag(nbt.contains("fluid_mode") ? nbt.getCompound("fluid_mode") : nbt));
    }

}
