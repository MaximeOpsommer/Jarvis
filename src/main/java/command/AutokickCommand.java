package command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.concurrent.TimeUnit;

public class AutokickCommand extends AbstractCommand {

    private static final String CANT_KICK_OWNER = "Impossible de kick le propriétaire du serveur, je vous enverrai un message privé à la place";
    private static final String AUTOKICK_MESSAGE = "Vous avez demandé à être autokick il y a %d minutes.";

    @Override
    public java.lang.String getCommandName() {
        return "/autokick";
    }

    @Override
    public void execute(final MessageReceivedEvent event,final String[] commandArgs) {

        final Member member = event.getMember();
        if (member == null) {
            System.err.println("Member null");
            return;
        }

        final MessageChannel messageChannel = event.getChannel();
        final Guild guild = event.getGuild();

        if (commandArgs.length > 1) {
            messageChannel.sendMessage(wrongUsageOfCommand()).queue();
            return;
        }
        final long minutes;
        try {
            if (commandArgs.length == 1) {
                minutes = Long.parseLong(commandArgs[0]);
                if (minutes < 0) {
                    messageChannel.sendMessage(wrongUsageOfCommand()).queue();
                    return;
                }
            } else {
                minutes = 0;
            }
        } catch (final NumberFormatException e) {
            messageChannel.sendMessage(wrongUsageOfCommand()).queue();
            return;
        }
        if (member.isOwner()) {
            messageChannel.sendMessage(CANT_KICK_OWNER).queue();
        } else {
            guild.kickVoiceMember(member).queue();
        }
        member.getUser().openPrivateChannel().queueAfter(minutes, TimeUnit.MINUTES, (channel) ->
                channel.sendMessage(String.format(AUTOKICK_MESSAGE, minutes)).queue()
        );
    }

    @Override
    public String getDescription() {
        return "Commande permettant de demander à Jarvis de vous kick du vocal après X minutes.\n" +
                "Kick immédiat si aucun paramètre.";
    }

    @Override
    public String getUsage() {
        return "`/autokick [minutes]`";
    }
}
