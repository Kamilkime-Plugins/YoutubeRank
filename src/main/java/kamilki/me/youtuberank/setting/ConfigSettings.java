package kamilki.me.youtuberank.setting;

import kamilki.me.youtuberank.YoutubeRank;
import kamilki.me.youtuberank.setting.message.Message;
import kamilki.me.youtuberank.setting.message.impl.ListMessage;
import kamilki.me.youtuberank.setting.message.impl.StringMessage;
import kamilki.me.youtuberank.util.ColorUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConfigSettings {

    private String apiKey;
    private String videoTitle;

    private int minSubs;

    private final Set<RankSetting> rankSettings = new HashSet<>();
    private final Map<String, Message> messages = new HashMap<>();

    public void load(final YoutubeRank plugin) {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        final FileConfiguration cfg = plugin.getConfig();

        this.apiKey = cfg.getString("apiKey");
        this.videoTitle = cfg.getString("videoTitle");

        this.minSubs = Integer.MAX_VALUE;

        this.rankSettings.clear();
        this.messages.clear();

        final ConfigurationSection ranksSection = cfg.getConfigurationSection("ranks");
        for (final String rankName : ranksSection.getKeys(false)) {
            final ConfigurationSection rankSection = ranksSection.getConfigurationSection(rankName);

            final String[] subsSplit = rankSection.getString("subs").split("-");
            if (subsSplit.length < 2) {
                continue;
            }

            final int minSubs = "*".equals(subsSplit[0]) ? Integer.MIN_VALUE : Integer.parseInt(subsSplit[0]);
            final int maxSubs = "*".equals(subsSplit[1]) ? Integer.MAX_VALUE : Integer.parseInt(subsSplit[1]);

            this.rankSettings.add(new RankSetting(rankName, minSubs, maxSubs, rankSection.getStringList("commands")));

            if (minSubs < this.minSubs) {
                this.minSubs = minSubs;
            }
        }

        final ConfigurationSection messagesSection = cfg.getConfigurationSection("messages");
        for (final String messageName : messagesSection.getKeys(false)) {
            if (messagesSection.isString(messageName)) {
                this.messages.put(messageName, new StringMessage(ColorUtil.color(messagesSection.getString(messageName))));
            } else if (messagesSection.isList(messageName)) {
                this.messages.put(messageName, new ListMessage(ColorUtil.color(messagesSection.getStringList(messageName))));
            }
        }
    }

    public String getAPIKey() {
        return this.apiKey;
    }

    public String getVideoTitle() {
        return this.videoTitle;
    }

    public int getMinSubs() {
        return this.minSubs;
    }

    public RankSetting getRankSetting(final int subs) {
        for (final RankSetting rankSetting : this.rankSettings) {
            if (rankSetting.fulfilsRequirements(subs)) {
                return rankSetting;
            }
        }

        return null;
    }

    public Message getMessage(final String messageName) {
        return this.messages.get(messageName);
    }

}
