package vassal;

import VASSAL.build.GameModule;
import VASSAL.build.module.Chatter;
import VASSAL.command.Command;

/**
 * Utility class to wrap the Vassal chat window
 * Prevents having to use GameModule.getGameModule().getChatter() everywhere
 */
public class Chat {
    /**
     * local chat logger - mostly useful in development
     * to send messaged use Chatter.DisplayText to create a command and append any related action commands
     * @param message the message to log
     */
    public static void log(String message) {
        GameModule.getGameModule().getChatter().show(message);
    }

    /**
     * Display the given message in the chat window and return in a command object
     * @param message the message to display
     * @return command object containing the chat message action
     */
    public static Command displayText(String message) {
        Chatter.DisplayText c = new Chatter.DisplayText(GameModule.getGameModule().getChatter(), message);
        c.execute();
        return c;
    }
}
