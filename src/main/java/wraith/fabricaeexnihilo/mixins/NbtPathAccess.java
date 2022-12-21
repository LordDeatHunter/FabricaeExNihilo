package wraith.fabricaeexnihilo.mixins;

import net.minecraft.command.argument.NbtPathArgumentType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NbtPathArgumentType.NbtPath.class)
public interface NbtPathAccess {
    @Accessor
    default String getString() {
        throw new IllegalStateException("Accessor called directly");
    }
}
