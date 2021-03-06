import VASSAL.counters.*;
import game.Player;
import vassal.Chat;

public class PlayerPiece {
    private GamePiece gamePiece;

    public PlayerPiece(GamePiece gamePiece) {
        this.gamePiece = gamePiece;
    }

    public void updateJerseyNumber(String number) {
        // get the jerseynumber text label
        Labeler label = (Labeler) Decorator.getDecorator(gamePiece, Labeler.class);
        if (label == null) {
            Chat.log("ERROR: not a labeler: " + gamePiece.getClass().getName());
            return;
        }

        if (label.getPropertyNames().get(0).equalsIgnoreCase("JerseyNumber")) {
            label.setLabel(number);
        }
        else
        {
            Chat.log("ERROR: no jerseyNumber found");
        }
    }

    public void updateProperties(int value, Player player) {

        PropertySheet ps = (PropertySheet) Decorator.getDecorator(gamePiece, PropertySheet.class);
        String newState = String.format("%s~%s~%d~%d~%d~%d~%s~",
                player.getName(),
                player.getDisplayPosition(),
                player.getMovement(),
                player.getStrength(),
                player.getAgility(),
                player.getArmour(),
                player.getSkills());
        ps.mySetState(newState);
    }

    /**
     * Updates the piece stats, jersey number, name etc
     * @param p the Player object with stats etc. to apply to the GamePiece
     */
    public void updatePieceProperties(Player p) {
        updateJerseyNumber(p.getNumber());
        updateProperties(6, p);
    }
}
