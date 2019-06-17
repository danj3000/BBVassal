package vassal;

import VASSAL.build.GameModule;
import VASSAL.build.widget.PieceSlot;
import VASSAL.counters.GamePiece;
import VASSAL.counters.PieceCloner;
import game.Player;

public class BBPalette {
    public static GamePiece createPieceFromPalette(Player player, String race, String side) {
        String position = getEntryName(player, race, side);

        PieceSlot pieceSlot = getPieceSlotByName(position);

        if (pieceSlot == null){
            Chat.log("ERROR: pieceSlot not found to match: " + position);
            pieceSlot = getPieceSlotByName(String.format("Unknown (%s)", side));
        }

        GamePiece piece = PieceCloner.getInstance().clonePiece(pieceSlot.getPiece());
        return piece;
    }

    private static PieceSlot getPieceSlotByName(String position) {
        GameModule mod = GameModule.getGameModule();
        java.util.List<PieceSlot> slots = mod.getAllDescendantComponentsOf(PieceSlot.class);
        PieceSlot pieceSlot = null;
        for (PieceSlot ps : slots) {
            String name = ps.getConfigureName();
            if (name.equalsIgnoreCase(position)){
                // clonePiece expands the traits within the piece definition
                pieceSlot = ps;
                break;
            }
        }
        return pieceSlot;
    }

    private static String getEntryName(Player player, String race, String side) {
        String position = player.getPosition();

        // non-positional formatting
        String formattedRace = race;
        if (race.equalsIgnoreCase("Necromantic"))
            formattedRace = race.replace("Necromantic", "Necro");

        // prevent 'chaos chaos warrior'
        if (position.startsWith(formattedRace))
            return String.format("%s (%s)", position, side);
        else
            return String.format("%s %s (%s)", formattedRace, position, side);
    }

}
