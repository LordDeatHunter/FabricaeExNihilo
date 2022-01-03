package wraith.fabricaeexnihilo.modules.barrels.modes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.barrels.BarrelModeStorage;

public abstract class BarrelMode implements BarrelModeStorage {
    // We really gotta do this? :concern:
    // Yes, since we need to have the switch...
    public static final Codec<BarrelMode> CODEC = Codec.PASSTHROUGH.xmap(dynamic -> {
        var id = dynamic.get("id").flatMap(Dynamic::asString).getOrThrow(false, FabricaeExNihilo.LOGGER::warn);
        return (switch (id) {
            case "item" -> ItemMode.CODEC.decode(dynamic);
            case "fluid" -> FluidMode.CODEC.decode(dynamic);
            case "alchemy" -> AlchemyMode.CODEC.decode(dynamic);
            case "compost" -> CompostMode.CODEC.decode(dynamic);
            case "empty" -> EmptyMode.CODEC.decode(dynamic);
            default -> throw new IllegalStateException("Unknown barrel mode!");
        }).getOrThrow(false, FabricaeExNihilo.LOGGER::warn).getFirst();
    }, mode -> {
        var nbt = (switch (mode.getId()) {
            case "item" -> ItemMode.CODEC.encodeStart(NbtOps.INSTANCE, (ItemMode) mode);
            case "fluid" -> FluidMode.CODEC.encodeStart(NbtOps.INSTANCE, (FluidMode) mode);
            case "alchemy" -> AlchemyMode.CODEC.encodeStart(NbtOps.INSTANCE, (AlchemyMode) mode);
            case "compost" -> CompostMode.CODEC.encodeStart(NbtOps.INSTANCE, (CompostMode) mode);
            case "empty" -> EmptyMode.CODEC.encodeStart(NbtOps.INSTANCE, (EmptyMode) mode);
            default -> throw new IllegalStateException("Unknown barrel mode!");
        }).getOrThrow(false, error -> {
            FabricaeExNihilo.LOGGER.warn(error);
            FabricaeExNihilo.LOGGER.warn(mode);
            FabricaeExNihilo.LOGGER.warn(mode.getId());
        });
        ((NbtCompound) nbt).putString("id", mode.getId());
        return new Dynamic<>(NbtOps.INSTANCE, nbt);
    });
    
    protected BarrelMode() {
    }
    
    public abstract String getId();
    
    public abstract BarrelMode copy();
    
    public void tick(BarrelBlockEntity barrel) {
    }
}