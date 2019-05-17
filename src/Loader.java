import VASSAL.build.AbstractConfigurable;
import VASSAL.build.Buildable;
import VASSAL.build.GameModule;
import VASSAL.build.module.*;
import VASSAL.build.module.documentation.HelpFile;
import VASSAL.build.module.map.boardPicker.board.MapGrid;
import VASSAL.build.widget.PieceSlot;
import VASSAL.command.Command;
import VASSAL.command.CommandEncoder;
import VASSAL.counters.GamePiece;
import VASSAL.counters.PieceCloner;

import game.Player;
import game.Team;
import vassal.Chat;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;


public class Loader extends AbstractConfigurable implements CommandEncoder,GameComponent {
    // add supporting Command here

    public static final String COMMAND_PREFIX = "LOAD:";

    private JButton loaderButton;

    public String[] getAttributeDescriptions() {
        return new String[0];
    }

    public Class<?>[] getAttributeTypes() {
        return new Class[0];
    }

    public String[] getAttributeNames() {
        return new String[0];
    }

    public void setAttribute(String s, Object o) {    }

    public String getAttributeValueString(String s) {
        return null;
    }

    public HelpFile getHelpFile() {
        return null;
    }

    public Class[] getAllowableConfigureComponents() {
        return new Class[0];
    }

    public void addTo(Buildable parent) {
        Map map = (Map)parent;

        GameModule mod = GameModule.getGameModule();
        mod.addCommandEncoder(this);
        mod.getGameState().addGameComponent(this);

        // add button to toolbar
        loaderButton = new JButton("LOAD IT NOW!!!");

        loaderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // DO STUFF HERE!!!
                String fileContent = GetTeamFileContent();

                Team team = NtbblTeamReader.loadTeam(fileContent);
                int i = 0;
                for (Player p : team.getPlayers()) {
                    String side = "blue";
                    GamePiece piece = createPieceFromPalette(p, team.getRace(), side);
                    if (piece == null){
                        continue;
                    }

                    // add piece to map
                    MapGrid grid = MapHelper.getPitchGrid();

                    String column = "D";
                    int yOffSet = 3;
                    Point location;
                    try {
                        location = grid.getLocation(column +(i+yOffSet));
                    } catch (MapGrid.BadCoords badCoords) {
                        badCoords.printStackTrace();
                        Chat.log("ERROR: bad coords :-(");
                        return;
                    }

                    Command placeCommand = MapHelper.getPitchMap().placeAt(piece, location);
                    placeCommand.execute();
                    GameModule.getGameModule().sendAndLog(placeCommand);

                    i++;
                }
            }
        });

        map.getToolBar().add(loaderButton);
    }

    private String GetTeamFileContent() {
        String fileContent = "";
        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(loaderButton);
        if (returnVal == JFileChooser.APPROVE_OPTION){
            File file = fc.getSelectedFile();
            BufferedReader reader;
            StringBuilder sb = new StringBuilder();
            try {
                reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                while(line != null) {
                    sb.append(line);
                    line = reader.readLine();
                }
                fileContent = sb.toString();
            }
            catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return fileContent;
    }

    private GamePiece createPieceFromPalette(Player player, String race, String side) {
        String position = getEntryName(player, race, side);

        GameModule mod = GameModule.getGameModule();
        java.util.List<PieceSlot> slots = mod.getAllDescendantComponentsOf(PieceSlot.class);
        for (PieceSlot ps : slots) {
            String name = ps.getConfigureName();
            if (name.equalsIgnoreCase(position)){
                // clonePiece expands the traits within the piece definition
                return PieceCloner.getInstance().clonePiece(ps.getPiece());
            }
        }

        Chat.log("ERROR: pieceSlot not found to match: " + position);
        return null;
    }

    private String getEntryName(Player player, String race, String side) {
        //todo: simplify this fudge later (rename pieces / ntbbl names or whatever)
        String position = player.getPosition();
        if (position.equalsIgnoreCase("Minotaur Lord"))
            position = "Minotaur";

        if (position.startsWith(race)) // prevent 'chaos chaos warrior'
            return String.format("%s (%s)", position, side);
        else
            return String.format("%s %s (%s)", race, position, side);
    }

    public void removeFrom(Buildable buildable) {
        GameModule mod = GameModule.getGameModule();
        mod.removeCommandEncoder(this);
        mod.getGameState().removeGameComponent(this);

        Map map = (Map)buildable;
        map.getToolBar().remove(loaderButton);
    }

    public Command decode(String s) {
        return null;
    }

    public String encode(Command c) {
        return null;
    }


    public void setup(boolean b) {
        // no initialization needed
    }

    public Command getRestoreCommand() {
        // no state to maintain
        return null;
    }
}
