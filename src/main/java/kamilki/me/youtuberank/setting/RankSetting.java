package kamilki.me.youtuberank.setting;

import java.util.List;

public class RankSetting {

    private final String rankName;

    private final int minSubs;
    private final int maxSubs;

    private final List<String> commands;

    public RankSetting(final String rankName, final int minSubs, final int maxSubs, final List<String> commands) {
        this.rankName = rankName;
        this.minSubs = minSubs;
        this.maxSubs = maxSubs;
        this.commands = commands;
    }

    public String getRankName() {
        return this.rankName;
    }

    public boolean fulfilsRequirements(final int subs) {
        return subs >= this.minSubs && subs <= this.maxSubs;
    }

    public List<String> getCommands() {
        return this.commands;
    }

}
