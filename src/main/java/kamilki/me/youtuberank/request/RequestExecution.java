package kamilki.me.youtuberank.request;

import kamilki.me.youtuberank.YoutubeRank;
import kamilki.me.youtuberank.logger.RankLogger;
import kamilki.me.youtuberank.setting.BlockedSettings;
import kamilki.me.youtuberank.setting.ConfigSettings;
import kamilki.me.youtuberank.setting.RankSetting;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.regex.Pattern;

public final class RequestExecution {

    private static final Pattern URL_SPLIT_PATTERN = Pattern.compile("\\?v=");

    public static void executeRequest(final YoutubeRank plugin, final ConfigSettings configSettings, final BlockedSettings blockedSettings,
                                      final RankLogger rankLogger, final CommandSender sender, final String url) {
        final String[] urlSplit = URL_SPLIT_PATTERN.split(url);
        if (urlSplit.length == 1) {
            configSettings.getMessage("wrongUrl").send(sender);
            return;
        }

        if (urlSplit[1].length() < 11) {
            configSettings.getMessage("wrongUrl").send(sender);
            return;
        }

        final String videoID = urlSplit[1].substring(0, 11);
        if (blockedSettings.isVideoBlocked(videoID)) {
            configSettings.getMessage("videoAlreadyUsed").send(sender);
            return;
        }

        final JSONObject videoRequestResult = APIRequest.VIDEO.makeRequest(videoID, configSettings.getAPIKey());
        if (videoRequestResult == null) {
            configSettings.getMessage("videoGetFailed").send(sender);
            return;
        }

        final JSONArray videoRequestItems = (JSONArray) videoRequestResult.get("items");
        if (videoRequestItems == null) {
            configSettings.getMessage("videoGetFailed").send(sender);
            return;
        }

        final JSONObject snippet = (JSONObject) ((JSONObject) videoRequestItems.get(0)).get("snippet");
        if (snippet == null) {
            configSettings.getMessage("videoGetFailed").send(sender);
            return;
        }

        final String title = (String) snippet.get("title");
        if (!configSettings.getVideoTitle().equals(title)) {
            configSettings.getMessage("wrongTitle").send(sender, "%title%", configSettings.getVideoTitle());
            return;
        }

        final String channelID = (String) snippet.get("channelId");
        if (blockedSettings.isChannelBlocked(channelID)) {
            configSettings.getMessage("channelAlreadyUsed").send(sender);
            return;
        }

        final JSONObject channelRequestResult = APIRequest.CHANNEL.makeRequest(channelID, configSettings.getAPIKey());
        if (channelRequestResult == null) {
            configSettings.getMessage("channelGetFailed").send(sender);
            return;
        }

        final JSONArray channelRequestItems = (JSONArray) channelRequestResult.get("items");
        if (channelRequestItems == null) {
            configSettings.getMessage("channelGetFailed").send(sender);
            return;
        }

        final JSONObject channelStatistics = (JSONObject) ((JSONObject) channelRequestItems.get(0)).get("statistics");
        if (channelStatistics == null) {
            configSettings.getMessage("channelGetFailed").send(sender);
            return;
        }

        final boolean hidden = (boolean) channelStatistics.get("hiddenSubscriberCount");
        if (hidden) {
            configSettings.getMessage("subCountHidden").send(sender);
            return;
        }

        final int subs = Integer.parseInt((String) channelStatistics.get("subscriberCount"));
        if (subs < configSettings.getMinSubs()) {
            configSettings.getMessage("minSubs").send(sender, "%subs%", configSettings.getMinSubs());
            return;
        }

        final RankSetting rankSetting = configSettings.getRankSetting(subs);
        if (rankSetting == null) {
            configSettings.getMessage("noRankSetting").send(sender);
            return;
        }

        Bukkit.getScheduler().runTask(plugin, () -> {
            for (final String command : rankSetting.getCommands()) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), StringUtils.replace(command, "%player%", sender.getName()));
            }
        });

        blockedSettings.blockVideo(videoID);
        blockedSettings.blockChannel(channelID);
        blockedSettings.save(plugin);

        rankLogger.log("videoID = " + videoID + ", channelID = " + channelID + ", player = " + sender.getName() + ", rank = " +
                rankSetting.getRankName());
    }

    private RequestExecution() {}

}
