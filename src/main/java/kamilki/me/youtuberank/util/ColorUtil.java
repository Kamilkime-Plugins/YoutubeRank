package kamilki.me.youtuberank.util;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public final class ColorUtil {

    public static String color(final String string) {
        if (string == null || string.isEmpty()) {
            return "";
        }

        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> color(final List<String> stringList) {
        final List<String> colored = new ArrayList<>();
        if (stringList == null || stringList.isEmpty()) {
            return colored;
        }

        for (final String string : stringList) {
            colored.add(color(string));
        }

        return colored;
    }

    private ColorUtil() {}

}
