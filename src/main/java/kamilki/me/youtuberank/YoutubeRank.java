package kamilki.me.youtuberank;

import kamilki.me.youtuberank.command.YoutubeCommand;
import kamilki.me.youtuberank.logger.RankLogger;
import kamilki.me.youtuberank.setting.BlockedSettings;
import kamilki.me.youtuberank.setting.ConfigSettings;
import org.bukkit.plugin.java.JavaPlugin;

public final class YoutubeRank extends JavaPlugin {

    @Override
    public void onEnable() {
        final ConfigSettings configSettings = new ConfigSettings();
        configSettings.load(this);

        final BlockedSettings blockedSettings = new BlockedSettings();
        blockedSettings.load(this);

        final RankLogger rankLogger = new RankLogger(this);
        this.getCommand("youtube").setExecutor(new YoutubeCommand(this, rankLogger, configSettings, blockedSettings));
    }

}
