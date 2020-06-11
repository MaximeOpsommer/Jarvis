package command;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Collection;

public class HelpCommand extends AbstractCommand {

    private final Collection<AbstractCommand> commands;

    HelpCommand(final Collection<AbstractCommand> commands) {
        this.commands = commands;
    }

    @Override
    public String getCommandName() {
        return "/help";
    }

    @Override
    public void execute(final MessageReceivedEvent event, final String[] commandArgs) {

        final MessageChannel channel = event.getChannel();

        if (commandArgs.length > 0) {
            channel.sendMessage(wrongUsageOfCommand()).queue();
        }

        final StringBuilder builder = new StringBuilder("Liste des commandes disponibles:\n");
        for (final AbstractCommand command : commands) {
            builder.append(String.format("\n%s", command.getUsage()));
        }
        channel.sendMessage(builder.toString()).queue();
    }

    @Override
    public String getDescription() {
        return "Commande qui résume l'ensemble des commandes de Jarvis.";
    }

    @Override
    public String getUsage() {
        return "`/help`";
    }
}
