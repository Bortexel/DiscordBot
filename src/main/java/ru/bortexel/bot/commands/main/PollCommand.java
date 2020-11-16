package ru.bortexel.bot.commands.main;

import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import ru.bortexel.bot.BortexelBot;
import ru.bortexel.bot.core.AccessLevel;
import ru.bortexel.bot.core.Command;
import ru.bortexel.bot.util.AccessLevels;
import ru.bortexel.bot.util.EmbedUtil;
import ru.bortexel.bot.util.TextUtil;
import ru.bortexel.bot.util.poll.Poll;
import ru.bortexel.bot.util.poll.PollVariant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PollCommand implements Command {
    private final BortexelBot bot;

    public PollCommand(BortexelBot bot) {
        this.bot = bot;
    }

    @Override
    public void onCommand(Message message) {
        String[] lines = message.getContentRaw().split("\n");
        String[] args = TextUtil.getCommandArgs(lines[0]);
        String title = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        List<PollVariant> variants = new ArrayList<>();
        List<String> varEmojis = new ArrayList<>();

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            List<String> emojis = EmojiParser.extractEmojis(line);
            String emoji = TextUtil.getNumberEmoji(i);
            if (emojis.size() > 0) emoji = emojis.get(0);

            if (varEmojis.contains(emoji)) {
                EmbedBuilder builder = EmbedUtil.makeError("Ошибка", "Обозначение варианта, заданного в строке " + (i + 1) + ", " +
                        "совпадает с обозначением одного из других вариантов.");
                message.getChannel().sendMessage(builder.build()).queue();
                return;
            }

            varEmojis.add(emoji);
            variants.add(new PollVariant(TextUtil.removeSpacesInStart(EmojiParser.removeAllEmojis(line)), emoji));
        }

        if (variants.size() < 2) {
            EmbedBuilder builder = EmbedUtil.makeError("Ошибка", "Вы должны задать как минимум два варианта.");
            message.getChannel().sendMessage(builder.build()).queue();
            return;
        }

        Poll.create(message.getTextChannel(), title, variants);
    }

    @Override
    public String getName() {
        return "poll";
    }

    @Override
    public String getUsage() {
        return "<вопрос>\n<эмодзи> <название варианта>\n<эмодзи> <название варианта>\n...";
    }

    @Override
    public String getUsageExample() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Создаёт опрос с заданными параметрами";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public AccessLevel getAccessLevel() {
        AccessLevels levels = bot.getAccessLevels();
        return AccessLevel.make(levels.getHelperAccessLevel(), levels.getHeadBuilderAccessLevel());
    }

    @Override
    public int getMinArgumentCount() {
        return 1;
    }
}
