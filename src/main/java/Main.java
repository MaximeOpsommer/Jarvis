import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {

    // https://discordapp.com/developers/applications/669481253199675393/bots
    private static final String TOKEN = "NjY5NDgxMjUzMTk5Njc1Mzkz.Xigh-Q.eSzvNmUpgZN6PH4Hzf0XqTFyNoI";

    public static void main(String[] args) throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(TOKEN);
        builder.addEventListeners(new Main());
        builder.build();
    }

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {

        if (event.getAuthor().isBot()) return;

        System.out.println(String.format("We received a message from %s: %s",
                event.getAuthor().getName(),
                event.getMessage().getContentDisplay()));

        if (event.getMessage().getContentRaw().equals("!ping")) {
            event.getChannel().sendMessage("Pong!").queue();
        }
    }

}
