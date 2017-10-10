import VASSAL.counters.GamePiece;
import VASSAL.counters.Stack;

public class PieceHelper {
    public static boolean isPlayer(GamePiece piece) {
        if (piece instanceof Stack) {
            Stack s = (Stack) piece;
            for (int j = 0; j < s.getPieceCount(); j++) {
                GamePiece p = (s.getPieceAt(j));
                if(isAPlayer(p)){
                    return true;
                };
            }
        }
        else
            return isAPlayer(piece);
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
}
