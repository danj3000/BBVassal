import VASSAL.build.module.Map;
import VASSAL.counters.GamePiece;
import VASSAL.counters.Stack;

import java.util.ArrayList;
import java.util.List;

public class MapHelper {
    public static GamePiece[] getPlayers(Map map) {
        return getTeamPlayers(map, "");
    }

    public static GamePiece[] getTeamPlayers(Map map, String team){
        ArrayList<GamePiece> players = new ArrayList<GamePiece>();
        for (GamePiece piece: map.getPieces()) {
            if (PieceHelper.isPlayer(piece)){
                if (team.equals("") || PieceHelper.isTeam(piece, team)) {
                    players.add(piece);
                }
            }
        }
        return players.toArray(new GamePiece[0]);
    }

    public static GamePiece getPlayerById(String playerId) {
        return null;
    }

    public static Map getPitchMap() {
        return Map.getMapById("Grass Pitch");
    }
}
