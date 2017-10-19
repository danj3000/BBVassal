import VASSAL.build.module.Map;
import VASSAL.command.Command;
import VASSAL.counters.GamePiece;

import java.util.ArrayList;

public class StunCommand extends PlayerStateCommand {
    private ArrayList<String> victims;

    public StunCommand(ArrayList<String> victims) {
        this.victims = victims;
    }

    protected GamePiece[] getMatchingPlayers(Map map) {
        ArrayList<GamePiece> pieces = new ArrayList<GamePiece>();
        for (String p : victims) {
            pieces.add(MapHelper.getPlayerById(p));
        }
        return (GamePiece[]) pieces.toArray();
    }

    protected void changePlayerState(GamePiece p) {

    }

    protected Command myUndoCommand() {
        return null;
    }
}
