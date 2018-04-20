package exnihilocreatio.client.models.event;

import exnihilocreatio.client.models.InfestedLeavesBakedModel;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderEvent {

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent evt) {
        IBakedModel model = evt.getModelRegistry().getObject(InfestedLeavesBakedModel.variantTag);
        if (model != null) {
            IBakedModel customModel = new InfestedLeavesBakedModel(model);
            evt.getModelRegistry().putObject(InfestedLeavesBakedModel.variantTag, customModel);
        }
    }
}
