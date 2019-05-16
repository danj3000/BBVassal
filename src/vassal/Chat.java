package vassal;

import VASSAL.build.GameModule;

/**
 * Utility class to wrap the Vassal chat window
 * Prevents having to use GameModule.getGameModule().getChatter() everywhere
 */
public class Chat {
    /**
     * local chat logger - mostly useful in development
     * to send messaged use Chatter.DisplayText to create a command and append any related action commands
     * @param message
     */
    public static void log(String message) {
        GameModule.getGameModule().getChatter().show(message);
    }
}
