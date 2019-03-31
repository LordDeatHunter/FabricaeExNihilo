package exnihilocreatio.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import org.jetbrains.annotations.NotNull;

public class CommandHandNBT extends CommandBase {
    @NotNull
    @Override
    public String getName() {
        return "enhandnbt";
    }

    @NotNull
    @Override
    public String getUsage(@NotNull ICommandSender iCommandSender) {
        return "enhandnbt";
    }

    @Override
    public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, @NotNull String[] args) {
        if(args.length > 0)
            return;
        if(sender instanceof EntityPlayer){
            ItemStack stack = ((EntityPlayer) sender).getHeldItem(EnumHand.MAIN_HAND);
            if(stack.isEmpty() || !stack.hasTagCompound())
                return;
            String dump = stack.getTagCompound().toString();
            sender.sendMessage(new TextComponentString(dump));
        }
    }
}
