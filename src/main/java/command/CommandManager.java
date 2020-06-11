package command;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    private final Map<String, AbstractCommand> commands = new HashMap<>();

    public CommandManager() {
        addCommand(new AutokickCommand());
        addCommand(new DecisionCommand());
        addCommand(new HelpCommand(commands.values()));
        addCommand(new LolRandomCompCommand());
    }

    private void addCommand(final AbstractCommand command) {
        if (commands.containsKey(command.getCommandName())) {
            throw new IllegalArgumentException("A command with this name is already present");
        }
        commands.put(command.getCommandName(), command);
    }

    public Map<String, AbstractCommand> getCommands() {
        return commands;
    }

}
