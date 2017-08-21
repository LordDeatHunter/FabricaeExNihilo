package exnihilocreatio.command;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.config.ModConfig;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;

import javax.annotation.Nonnull;
import java.io.File;

public class CommandReloadConfig extends CommandBase {
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    @Nonnull
    public String getName() {
        return "enreloadconfig";
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "enreloadconfig";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        ExNihiloCreatio.loadConfigs();
        ConfigManager.load(ExNihiloCreatio.MODID, Config.Type.INSTANCE);
        // ModConfig.doNormalConfig(new File(ExNihiloCreatio.configDirectory, "ExNihiloCreatio.cfg"));
        sender.sendMessage(new TextComponentTranslation("commands.enreloadconfig.confirm"));
    }
}
