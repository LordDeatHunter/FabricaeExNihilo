package exnihilocreatio.networking

import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

// When all else fails, do it yourself
// (I couldn't find any way to force a lighting update on the client without some blockstate hackery)
// (I thought packet hackery would be better)
class MessageCheckLight(): IMessage {
    var x: Int = 0
        private set
    var y: Int = 0
        private set
    var z: Int = 0
        private set

    constructor(pos: BlockPos) : this(){
        x = pos.x
        y = pos.y
        z = pos.z
    }

    override fun fromBytes(buffer: ByteBuf) {
        x = buffer.readInt()
        y = buffer.readInt()
        z = buffer.readInt()
    }

    override fun toBytes(buffer: ByteBuf) {
        buffer.writeInt(x)
        buffer.writeInt(y)
        buffer.writeInt(z)
    }

    class MessageCheckLightHandler : IMessageHandler<MessageCheckLight, IMessage> {
        @SideOnly(Side.CLIENT)
        override fun onMessage(message: MessageCheckLight, ctx: MessageContext): IMessage? {
            val pos = BlockPos(message.x, message.y, message.z)

            Minecraft.getMinecraft().addScheduledTask<Boolean> { Minecraft.getMinecraft().world?.checkLight(pos) }

            return null
        }
    }
}
