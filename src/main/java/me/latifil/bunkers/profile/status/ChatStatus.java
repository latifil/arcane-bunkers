package me.latifil.bunkers.profile.status;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Arrays;
import java.util.List;

public enum ChatStatus {

    TEAM(
            "<yellow>You have set your chat status to <green>team</green> <yellow>chat.</yellow>",
            List.of("t", "team")
    ),
    PUBLIC(
            "<yellow>You have set your chat status to <red>global</red> <yellow>chat.</yellow>",
            List.of("p", "public", "g", "global")
    );

    private final String rawMessage;
    private final List<String> aliases;
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    ChatStatus(String rawMessage, List<String> aliases) {
        this.rawMessage = rawMessage;
        this.aliases = aliases;
    }

    public Component getStatusMessage() {
        return MINI_MESSAGE.deserialize(rawMessage);
    }

    public List<String> getAliases() {
        return aliases;
    }

    public static ChatStatus getStatusFromString(String input) {
        return Arrays.stream(values())
                .filter(status -> status.aliases.contains(input.toLowerCase()))
                .findFirst()
                .orElse(null);
    }
}