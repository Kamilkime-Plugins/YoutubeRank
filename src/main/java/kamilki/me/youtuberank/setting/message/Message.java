package kamilki.me.youtuberank.setting.message;

import org.bukkit.command.CommandSender;

public interface Message {

    void send(final CommandSender sender);
    void send(final CommandSender sender, final Object... replacements);

}
