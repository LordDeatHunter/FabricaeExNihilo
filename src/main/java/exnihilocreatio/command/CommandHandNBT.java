package exnihilocreatio.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;

public class CommandHandNBT extends CommandBase {
    @Override
    public String getName() {
        return "enhandnbt";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "enhandnbt";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
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
