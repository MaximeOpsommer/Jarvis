package command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class AbstractCommand {

    public abstract String getCommandName();

    public abstract void execute(final MessageReceivedEvent event, final String[] commandArgs);

    public abstract String getDescription();

    public abstract String getUsage();

    String getHelp() {
        return String.format("%s\n%s", getDescription(), getUsage());
    }

    String wrongUsageOfCommand() {
        return String.format("Mauvais usage de la commande %s\n%s", getCommandName(), getUsage());
    }

    // Role.getPosition()

}
