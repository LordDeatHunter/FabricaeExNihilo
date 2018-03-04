package exnihilocreatio.networking;

import exnihilocreatio.barrel.modes.compost.BarrelModeCompost;
import exnihilocreatio.texturing.Color;
import exnihilocreatio.tiles.TileBarrel;
import exnihilocreatio.util.ColorStealer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.nio.charset.Charset;

import static exnihilocreatio.util.Util.whiteColor;

public class MessageCompostUpdate implements IMessage {
    public static final Charset CHARSET = Charset.forName("UTF-8");


    private float fillAmount;
    private float compValue;
    private int x;
    private int y;
    private int z;
    private ItemStack stack;
    private Color color;
    private float progress;
    private boolean isFirst;

    public MessageCompostUpdate() {
    }

    public MessageCompostUpdate(float fillAmount, Color color, ItemStack stack, float progress, float compValue, BlockPos pos, boolean isFirst) {
        this.compValue = compValue;
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.color = color;
        this.fillAmount = fillAmount;
        this.stack = stack;
        this.progress = progress;
        this.isFirst = isFirst;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeFloat(fillAmount);
        buf.writeFloat(progress);
        buf.writeFloat(color.r);
        buf.writeFloat(color.g);
        buf.writeFloat(color.b);
        buf.writeFloat(color.a);
        buf.writeFloat(compValue);
        buf.writeBoolean(isFirst);
        String itemName = stack.getItem().getRegistryName().toString();
        buf.writeInt(itemName.length());
        buf.writeCharSequence(stack.getItem().getRegistryName().toString(), CHARSET);
        buf.writeInt(stack.getMetadata());

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.fillAmount = buf.readFloat();
        this.progress = buf.readFloat();
        this.color = new Color(buf.readFloat(),buf.readFloat(),buf.readFloat(),buf.readFloat());
        this.compValue = buf.readFloat();
        this.isFirst = buf.readBoolean();
        int length = buf.readInt();
        String name = buf.readCharSequence(length, Charset.defaultCharset()).toString();
        int meta = buf.readInt();

        Item item = Item.getByNameOrId(name);
        if (item != null) {
            this.stack = new ItemStack(item, 1, meta);
        } else {
            this.stack = ItemStack.EMPTY;
        }
    }

    @Override
    public String toString() {
        return "MessageCompostUpdate{" +
                "fillAmount=" + fillAmount +
                ", compValue=" + compValue +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", stack=" + stack +
                ", color=" + color +
                ", progress=" + progress +
                '}';
    }

    public static class MessageCompostAmountUpdateHandler implements IMessageHandler<MessageCompostUpdate, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(final MessageCompostUpdate msg, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                @SideOnly(Side.CLIENT)
                public void run() {
                    TileEntity entity = Minecraft.getMinecraft().player.getEntityWorld().getTileEntity(new BlockPos(msg.x, msg.y, msg.z));

                    if (entity instanceof TileBarrel) {
                        TileBarrel te = (TileBarrel) entity;
                        BarrelModeCompost mode = (BarrelModeCompost) te.getMode();
                        mode.setFillAmount(msg.fillAmount);

                        if (msg.stack.isEmpty() && msg.compValue == 0.0f){
                            // Progress is being made
                            mode.setColor(Color.average(mode.getOriginalColor(), whiteColor, msg.progress));
                        } else {
                            // A new item is getting added
                            Color compColor = msg.color;
                            // Dynamic color on invalid_color
                            if (compColor.equals(Color.INVALID_COLOR) && !msg.stack.isEmpty()) {
                                compColor = ColorStealer.getColor(msg.stack);
                            }

                            if (msg.fillAmount == 0 || msg.isFirst) {
                                mode.setColor(compColor);
                                mode.setOriginalColor(compColor);
                            } else {
                                Color col = Color.average(mode.getColorForRender(), compColor, msg.compValue);
                                mode.setColor(col);
                                mode.setOriginalColor(col);
                            }

                        }

                        mode.setProgress(msg.progress);
                    }
                }
            });
            return null;
        }
    }

}
