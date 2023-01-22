/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.service.command;

import java.util.ArrayList;
import vip.astroline.client.Astroline;
import vip.astroline.client.service.command.Command;
import vip.astroline.client.service.command.impl.BindCommand;

public class CommandManager {
    private static final ArrayList<Command> commands = new ArrayList();

    public CommandManager() {
        CommandManager.addCommand(new BindCommand(new String[]{"bind"}, "Bind modules"));
    }

    public static ArrayList<Command> getCommands() {
        return commands;
    }

    public static void addCommand(Command command) {
        commands.add(command);
    }

    public void callCommand(String input) {
        String[] split = input.split(" ");
        String command = split[0];
        String args = input.substring(command.length()).trim();
        for (Command c : CommandManager.getCommands()) {
            if (!c.getName().equals(command)) continue;
            try {
                c.executeCommand(args, args.split(" "));
            }
            catch (Exception e) {
                Astroline.INSTANCE.tellPlayer(c.getSyntax());
            }
        }
    }
}

