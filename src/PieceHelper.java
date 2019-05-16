import VASSAL.counters.GamePiece;
import VASSAL.counters.Stack;
import vassal.Chat;

// provides helper methods for reading properties because pieces are usually a Stack in BBVassal
public class PieceHelper {
    // strategy pattern or lambdas would be nice here!
    public static boolean isPlayer(GamePiece piece) {

        if (piece instanceof Stack) {
            Stack s = (Stack) piece;
            int pieceCount = s.getPieceCount();
            for (int j = 0; j < pieceCount; j++) {
                GamePiece p = (s.getPieceAt(j));
                if(isAPlayer(p)){
                    return true;
                }
            }
        }
        else
            return isAPlayer(piece);

        return false;
    }

    public static boolean isTeam(GamePiece piece, String team) {

        if (piece instanceof Stack) {
            Stack s = (Stack) piece;
            for (int j = 0; j < s.getPieceCount(); j++) {
                GamePiece p = (s.getPieceAt(j));
                if(isInTeam(p, team)){
                    return true;
                }
            }
        }
        else
            return isInTeam(piece, team);

        return false;
    }

    private static boolean isAPlayer(GamePiece piece) {
        return hasPropertyValue(piece, "IsPlayer", "true");
    }

    private static boolean hasPropertyValue(GamePiece piece, String propName, String value) {
        Object isPlayer = piece.getProperty(propName);
        if ((isPlayer != null) && (value.equalsIgnoreCase(isPlayer.toString()))) {
            return true;
        }

        return false;
    }

    public static boolean isInTeam(GamePiece piece, String team) {
        return hasPropertyValue(piece, "Team", team);
    }
}
