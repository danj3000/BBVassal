import VASSAL.build.module.Map;
import VASSAL.counters.GamePiece;
import VASSAL.counters.Stack;

import java.util.ArrayList;
import java.util.List;

public class MapHelper {
    public static GamePiece[] getPlayers(Map map) {
        ArrayList<GamePiece> players = new ArrayList<GamePiece>();
        for (GamePiece piece: map.getPieces()) {
            if (PieceHelper.isPlayer(piece)){
                players.add(piece);
            }
        }
        return players.toArray(new GamePiece[0]);
    }
}
