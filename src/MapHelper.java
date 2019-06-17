import VASSAL.build.GameModule;
import VASSAL.build.module.Chatter;
import VASSAL.build.module.Map;
import VASSAL.build.module.map.boardPicker.Board;
import VASSAL.build.module.map.boardPicker.board.MapGrid;
import VASSAL.build.module.map.boardPicker.board.Region;
import VASSAL.build.module.map.boardPicker.board.RegionGrid;
import VASSAL.build.module.map.boardPicker.board.ZonedGrid;
import VASSAL.build.module.map.boardPicker.board.mapgrid.Zone;
import VASSAL.command.ChangePiece;
import VASSAL.command.Command;
import VASSAL.command.MoveTracker;
import VASSAL.counters.Decorator;
import VASSAL.counters.Embellishment;
import VASSAL.counters.GamePiece;
import VASSAL.counters.Labeler;
import com.google.common.collect.Iterables;
import vassal.Chat;

import java.awt.*;
import java.util.ArrayList;

/**
 * Initially a wrapper for board/pitch/grid semantics, this is also becoming home to accessing pieces...
 *
 * Maybe this should be renamed 'the game' or 'Pitch' or 'err....'
 */
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

    static MapGrid getPitchGrid() {
        ZonedGrid zGrid = getBoardGrid();
        Zone pitchZone = zGrid.findZone("Pitch");
        return pitchZone.getGrid();
    }

    private static ZonedGrid getBoardGrid() {
        Map map = getPitchMap();
        Board board = Iterables.get(map.getBoardPicker().getSelectedBoards(), 0);
        return (ZonedGrid) board.getGrid();
    }

    private static RegionGrid getRerollGrid(String side, String index) {
        String zoneName = String.format("%s Re-Rolls (%s)", side, index);
        Zone zone = getBoardGrid().findZone(zoneName);
        return (RegionGrid) zone.getGrid();
    }

    public static Region getRerollRegion(String side, String numRerolls) {
        String regionName = String.format("%s Re-Rolls (%s)", side, numRerolls);

        return getRerollGrid(side, numRerolls).findRegion(regionName);
    }

    public static void logAllPieces(){
            // get endzone label stack
            Map pitchMap = MapHelper.getPitchMap();
            GamePiece[] allPieces = pitchMap.getAllPieces();
            for (GamePiece piece :
                    allPieces) {
                Chat.log(piece.getName());
            }
    }

    public static GamePiece getNonPlayerPiece(String name) {
        // get endzone label stack
        Map pitchMap = getPitchMap();
        GamePiece[] allPieces = pitchMap.getAllPieces();
        for (GamePiece piece :
                allPieces) {
            if(!PieceHelper.isPlayer(piece)){
                // startswith here also works for sevens pitch
                if (piece.getName().toLowerCase().startsWith(name.toLowerCase())) {
                    return piece;
                }
            }
        }
        return null;
    }

    static Command nameEndZone(String side, String name) {
        String endZoneLabelName = "Endzone_Label_" + side;
        GamePiece endZonePiece = getNonPlayerPiece(endZoneLabelName);
        String oldState = endZonePiece.getState();

        if (endZonePiece != null) {
            Labeler label = (Labeler) Decorator.getDecorator(endZonePiece, Labeler.class);
            label.setLabel(name);
        }

        String newState = endZonePiece.getState();
        Command ch = new ChangePiece(endZonePiece.getId(), oldState, newState);
        // no need to execute the command as the piece state has already changed locally.
        return ch;
    }

    static Command moveRerollCounter(String side, String numRerolls) {
        Region rerollRegion = getRerollRegion(side, numRerolls);
        Point target = rerollRegion.getOrigin();
        String markerName = String.format("%s Re-Rolls Marker", side);
        GamePiece rerollMarker = getNonPlayerPiece(markerName);
        // movements...
        MoveTracker moveTracker = new MoveTracker(rerollMarker);
        getPitchMap().placeAt(rerollMarker, target);
        Command moveCommand = moveTracker.getMoveCommand();

        Command c = new Chatter.DisplayText(GameModule.getGameModule().getChatter(),
                String.format("Setting %s Rerolls: %s", side, numRerolls))
                .append(moveCommand);
        c.execute();

        return c;
    }

    static Command addPlayerToPitch(GamePiece piece, String target) {
        MapGrid grid = getPitchGrid();
        try {
            Point location = grid.getLocation(target);
            // put the player on the pitch
//          return MapHelper.getPitchMap().placeAt(piece, location);
            return getPitchMap().placeOrMerge(piece, location); // why placeat doesn't work??

        } catch (MapGrid.BadCoords badCoords) {
            badCoords.printStackTrace();
            Chat.log("ERROR: bad coords :" + target);
            return null;
        }
    }

    public static Command setCheerleaders(String side, int newValue) {
        Command c = Chat.displayText(String.format("Setting %s Cheerleaders: %d", side, newValue));
        String name = String.format("%s Cheerleaders", side);
        return c.append(setLayeredPieceValue(name, newValue));
    }

    public static Command setAssistantCoaches(String side, int newValue) {
        Command c = Chat.displayText(String.format("Setting %s Assistant Coach: %d", side, newValue));
        String name = String.format("%s Assistant Coach", side);
        return c.append(setLayeredPieceValue(name, newValue));
    }

    private static Command setLayeredPieceValue(String name, int newValue) {
        GamePiece piece = getNonPlayerPiece(name);
        String oldState = piece.getState();
        Embellishment valueLayer = (Embellishment) Decorator.getDecorator(piece, Embellishment.class);
        valueLayer.setValue(newValue);
        String newState = piece.getState();
        // no need to execute command as local changes already made

        return new ChangePiece(piece.getId(), oldState, newState);
    }
}
