package kamilki.me.youtuberank.setting.message.impl;

import kamilki.me.youtuberank.setting.message.Message;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ListMessage implements Message {

    private final List<String> messages;

    public ListMessage(final List<String> messages) {
        this.messages = messages;
    }

    @Override
    public void send(final CommandSender sender) {
        for (final String message : this.messages) {
            sender.sendMessage(message);
        }
    }

    @Override
    public void send(final CommandSender sender, final Object... replacements) {
        if (replacements.length % 2 != 0) {
            return;
        }

        for (String message : this.messages) {
            for (int i = 0; i < replacements.length; i += 2) {
                message = StringUtils.replace(message, (String) replacements[i], replacements[i + 1].toString());
            }

            sender.sendMessage(message);
        }
    }

}
