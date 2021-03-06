package ru.bortexel.bot.listeners.bortexel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bortexel.bot.BortexelBot;
import ru.bortexel.bot.util.Channels;
import ru.ruscalworld.bortexel4j.listening.events.warning.GenericWarningEvent;
import ru.ruscalworld.bortexel4j.models.account.Account;
import ru.ruscalworld.bortexel4j.models.warning.Warning;

import java.awt.*;

public class WarningListener extends BotListener {
    public WarningListener(BortexelBot bot) {
        super(bot);
    }

    @Override
    public void onWarningCreated(GenericWarningEvent event) {
        try {
            Warning warning = event.getWarning();
            getLogger().info("Received info about warning #" + warning.getID());

            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(Color.decode("#FF5555"));
            builder.setTitle("**" + warning.getUsername().replace("_", "\\_") + "** получил предупреждение мощностью **" + warning.getPower() + "**");
            builder.setFooter("Параметры могут быть изменены со временем. В таком случае администратор обязан оставить сообщение ниже.");

            builder.addField("Идентификатор", "" + warning.getID(), true)
                    .addField("Причина", warning.getReason(), true)
                    .addField("Мощность", "" + warning.getPower(), true);

            JDA jda = this.getBot().getJDA();
            Account.getByID(warning.getAdminID(), this.getBot().getApiClient())
                    .executeAsync(account -> jda.retrieveUserById(account.getDiscordID()).queue(user -> {
                        builder.setAuthor(warning.getAdminName(), null, user.getAvatarUrl());
                        TextChannel channel = jda.getTextChannelById(Channels.PUNISHMENTS_CHANNEL);
                        if (channel != null) channel.sendMessage(builder.build()).queue();
                    }));
        } catch (Exception e) {
            BortexelBot.handleException(e);
        }
    }

    private static Logger getLogger() {
        return LoggerFactory.getLogger(WarningListener.class);
    }
}
