package command;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Random;

public class DecisionCommand extends AbstractCommand {

    @Override
    public String getCommandName() {
        return "/decision";
    }

    @Override
    public void execute(final MessageReceivedEvent event, final String[] commandArgs) {

        final MessageChannel channel = event.getChannel();

        if (commandArgs.length < 2) {
            channel.sendMessage(wrongUsageOfCommand()).queue();
            return;
        }

        final Random random = new Random();
        channel.sendMessage(commandArgs[random.nextInt(commandArgs.length)]).queue();

    }

    @Override
    public String getDescription() {
        return "Commande permettant de demander à Jarvis de choisir pour vous parmi une liste de choix.\n" +
                "Au moins 2 choix doivent être indiqués.";
    }

    @Override
    public String getUsage() {
        return "`/decision <option 1> <option 2> [option 3] ...`";
    }
}
