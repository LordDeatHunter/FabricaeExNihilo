package exnihilocreatio.util;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

// Credit goes to >>> https://github.com/thraaawn/CompactMachines/blob/1.12.1/src/main/java/org/dave/compactmachines3/misc/RenderTickCounter.java
@Mod.EventBusSubscriber
public class RenderTickCounter {
    public static int renderTicks = 0;

    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.RenderTickEvent.Phase.START) {
            renderTicks++;
        }
    }
}
