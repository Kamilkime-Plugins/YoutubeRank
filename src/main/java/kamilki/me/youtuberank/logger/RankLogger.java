package kamilki.me.youtuberank.logger;

import kamilki.me.youtuberank.YoutubeRank;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RankLogger {

    private final File logFile;

    public RankLogger(final YoutubeRank plugin) {
        this.logFile = new File(plugin.getDataFolder(), "logs.txt");
    }

    public synchronized void log(final String log) {
        try {
            final FileWriter fileWriter = new FileWriter(this.logFile, true);
            fileWriter.append(log).append("\n");
            fileWriter.close();
        } catch (final IOException exception) {
            Bukkit.getLogger().info("Failed to access logs.txt, writing log here: " + log);
        }
    }

}
