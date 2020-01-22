package main;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.Arrays;

public class Main extends ListenerAdapter {

    // https://discordapp.com/developers/applications/669481253199675393/bots
    private static final String TOKEN = "NjY5NDgxMjUzMTk5Njc1Mzkz.Xigh-Q.eSzvNmUpgZN6PH4Hzf0XqTFyNoI";

    public static void main(String[] args) throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(TOKEN);
        builder.addEventListeners(new Main());
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

        switch (commandName) {
            case "/autokick":
                autokick(event.getGuild(), event.getChannel(), event.getMember(), commandArgs);
                break;
            default:
                System.err.println("Unknown command " + commandName);
                break;
        }
    }

    private void autokick(final Guild guild, final MessageChannel messageChannel, final Member member,
                          final String[] commandArgs) {
        if (member.isOwner()) {
            messageChannel.sendMessage("Impossible de kick le propriétaire du serveur").complete();
        } else {
            guild.kick(member).complete();
        }
    }

}
