package exnihilofabrico.client

import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.entity.LivingEntity


fun renderEntity(x: Int, y: Int, z: Int, scale: Double, yaw: Double, pitch: Double, living: LivingEntity) {
    val renderer = MinecraftClient.getInstance().entityRenderManager.getRenderer<LivingEntity, EntityRenderer<LivingEntity>>(living) ?: return


    renderer.render(living, x.toDouble(), y.toDouble(), z.toDouble(), yaw.toFloat(), pitch.toFloat())
}