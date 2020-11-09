package kamilki.me.youtuberank.setting.message.impl;

import kamilki.me.youtuberank.setting.message.Message;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

public class StringMessage implements Message {

    private final String message;

    public StringMessage(final String message) {
        this.message = message;
    }

    @Override
    public void send(final CommandSender sender) {
        sender.sendMessage(this.message);
    }

    @Override
    public void send(final CommandSender sender, final Object... replacements) {
        if (replacements.length % 2 != 0) {
            return;
        }

        String message = this.message;
        for (int i = 0; i < replacements.length; i += 2) {
            message = StringUtils.replace(message, (String) replacements[i], replacements[i + 1].toString());
        }

        sender.sendMessage(message);
    }
}
