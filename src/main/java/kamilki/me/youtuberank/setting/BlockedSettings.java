package kamilki.me.youtuberank.setting;

import kamilki.me.youtuberank.YoutubeRank;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BlockedSettings {

    private final Set<String> blockedVideoIDs = new HashSet<>();
    private final Set<String> blockedChannelIDs = new HashSet<>();

    public void load(final YoutubeRank plugin) {
        final File blockedFile = new File(plugin.getDataFolder(), "blocked.yml");
        if (!blockedFile.exists()) {
            return;
        }

        final YamlConfiguration blockedYaml = YamlConfiguration.loadConfiguration(blockedFile);

        this.blockedVideoIDs.addAll(blockedYaml.getStringList("blockedVideoIDs"));
        this.blockedChannelIDs.addAll(blockedYaml.getStringList("blockedChannelIDs"));
    }

    public synchronized void save(final YoutubeRank plugin) {
        final File blockedFile = new File(plugin.getDataFolder(), "blocked.yml");
        if (!blockedFile.exists()) {
            try {
                if (!blockedFile.createNewFile()) {
                    throw new IOException();
                }
            } catch (final IOException exception) {
                plugin.getLogger().warning("Failed to save blocked data!");
                return;
            }
        }

        final YamlConfiguration blockedYaml = YamlConfiguration.loadConfiguration(blockedFile);

        blockedYaml.set("blockedVideoIDs", new ArrayList<>(this.blockedVideoIDs));
        blockedYaml.set("blockedChannelIDs", new ArrayList<>(this.blockedChannelIDs));

        try {
            blockedYaml.save(blockedFile);
        } catch (final IOException exception) {
            plugin.getLogger().warning("Failed to save blocked data!");
        }
    }

    public synchronized boolean isVideoBlocked(final String videoID) {
        return this.blockedVideoIDs.contains(videoID);
    }

    public synchronized void blockVideo(final String videoID) {
        this.blockedVideoIDs.add(videoID);
    }

    public synchronized boolean isChannelBlocked(final String channelID) {
        return this.blockedChannelIDs.contains(channelID);
    }

    public synchronized void blockChannel(final String channelID) {
        this.blockedChannelIDs.add(channelID);
    }

}
