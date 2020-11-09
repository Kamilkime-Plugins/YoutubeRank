package kamilki.me.youtuberank.command;

import kamilki.me.youtuberank.YoutubeRank;
import kamilki.me.youtuberank.logger.RankLogger;
import kamilki.me.youtuberank.request.RequestExecution;
import kamilki.me.youtuberank.setting.BlockedSettings;
import kamilki.me.youtuberank.setting.ConfigSettings;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class YoutubeCommand implements CommandExecutor {

    private final YoutubeRank plugin;
    private final RankLogger rankLogger;
    private final ConfigSettings configSettings;
    private final BlockedSettings blockedSettings;

    public YoutubeCommand(final YoutubeRank plugin, final RankLogger rankLogger, final ConfigSettings configSettings,
                          final BlockedSettings blockedSettings) {
        this.plugin = plugin;
        this.rankLogger = rankLogger;
        this.configSettings = configSettings;
        this.blockedSettings = blockedSettings;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission("youtube.cmd")) {
            this.configSettings.getMessage("noPermission").send(sender);
            return true;
        }

        if (args.length == 0) {
            this.configSettings.getMessage("correctUsage").send(sender);
            return true;
        }

        final String arg = args[0];
        if ("reload".equalsIgnoreCase(arg) || "rl".equalsIgnoreCase(arg)) {
            if (!sender.hasPermission("youtube.reload")) {
                this.configSettings.getMessage("noPermission").send(sender);
                return true;
            }

            this.configSettings.getMessage("reloadCompleted").send(sender);
            this.configSettings.load(this.plugin);

            return true;
        }

        if (!(sender instanceof Player)) {
            this.configSettings.getMessage("notForConsole").send(sender);
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            this.configSettings.getMessage("startingRequest").send(sender);
            RequestExecution.executeRequest(this.plugin, this.configSettings, this.blockedSettings, this.rankLogger, sender, arg);
        });

        return true;
    }

}
