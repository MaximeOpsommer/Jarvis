package main;

import command.AbstractCommand;
import command.CommandManager;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.Arrays;

public class JarvisBot extends ListenerAdapter {

    // PROD TOKEN
    // https://discordapp.com/developers/applications/669481253199675393/bots
//    private static final String TOKEN = "NjY5NDgxMjUzMTk5Njc1Mzkz.Xigh-Q.eSzvNmUpgZN6PH4Hzf0XqTFyNoI";
    // DEV TOKEN
    private static final String TOKEN = "NjY5NTg0MDc3MjU4NDg5ODc5.XinaXQ.qbcw5qYA67AtEelWu9l0-rAiY-g";

    private CommandManager commandManager;

    private JarvisBot() {
        this.commandManager = new CommandManager();
    }

    private void start() throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(TOKEN);
        builder.addEventListeners(new JarvisBot());
        builder.build();
    }


    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {

        final User author = event.getAuthor();

        if (author.isBot()) return;

        final Message message = event.getMessage();
        final String messageContentRaw = message.getContentRaw();

        if (messageContentRaw.charAt(0) != '/') return;

        System.out.println(String.format("Message received from %s (%s): %s",
                author.getName(),
                author.getId(),
                message.getContentDisplay()));

        final String[] commandLine = messageContentRaw.split(" ");
        final String commandName = commandLine[0];
        final String[] commandArgs = Arrays.copyOfRange(commandLine, 1, commandLine.length);

        final AbstractCommand abstractCommand = commandManager.getCommands().get(commandName);
        if (abstractCommand != null) {
            abstractCommand.execute(event, commandArgs);
        } else {
            System.err.println("Unknown command " + commandName);
        }
    }

    public static void main(String[] args) throws LoginException {
        final JarvisBot bot = new JarvisBot();
        bot.start();
    }
}
