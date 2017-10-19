import VASSAL.build.module.Map;
import VASSAL.command.Command;
import VASSAL.counters.GamePiece;
import VASSAL.counters.Stack;

public abstract class PlayerStateCommand extends Command {
    @Override
    protected void executeCommand() {
        Map map = Map.getMapById("Grass Pitch");
        GamePiece playerPieces[] = getMatchingPlayers(map);
//            GameModule.getGameModule().getChatter().show("players: " + playerPieces.length);

        for (GamePiece piece : playerPieces) {
            if(piece instanceof Stack){
//                    GameModule.getGameModule().getChatter().show("Stack: ");
                Stack s = (Stack)piece;
                for (int j = 0; j < s.getPieceCount(); j++) {
                    GamePiece p = (s.getPieceAt(j));
                    changePlayerState(p);
                    break;
                }
            }
            else{
                changePlayerState(piece);
            }
        }
    }

    protected abstract GamePiece[] getMatchingPlayers(Map map);

    protected abstract void changePlayerState(GamePiece p);

    protected abstract Command myUndoCommand();
}
