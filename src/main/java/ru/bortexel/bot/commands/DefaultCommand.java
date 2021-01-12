package ru.bortexel.bot.commands;

import net.dv8tion.jda.api.entities.Message;
import ru.bortexel.bot.core.AccessLevel;
import ru.bortexel.bot.core.Command;
import ru.bortexel.bot.util.Channels;

import java.util.ArrayList;
import java.util.List;

public abstract class DefaultCommand implements Command {
    private final String name;
    private final List<String> aliases = new ArrayList<>();

    protected DefaultCommand(String name) {
        this.name = name;
    }

    @Override
    public final String getName() {
        return this.name;
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public String getUsageExample() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public final String[] getAliases() {
        return this.aliases.toArray(new String[aliases.size()]);
    }

    public final void addAlias(String alias) {
        this.aliases.add(alias);
    }

    @Override
    public String[] getAllowedChannelIds() {
        return new String[] { Channels.BOTS_CHANNEL };
    }

    @Override
    public AccessLevel getAccessLevel() {
        return null;
    }

    @Override
    public int getMinArgumentCount() {
        return 0;
    }

    public abstract void onCommand(Message message);
}
