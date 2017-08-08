package exnihilocreatio.entities;

import exnihilocreatio.ExNihiloCreatio;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ENEntities {
    public static void init() {
        EntityRegistry.registerModEntity(new ResourceLocation(ExNihiloCreatio.MODID, "Thrown Stone"), ProjectileStone.class, "Thrown Stone", 0, ExNihiloCreatio.instance, 64, 10, true);
    }
}
