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
import java.util.HashMap;


public class Loader extends AbstractConfigurable implements CommandEncoder,GameComponent {
    // add supporting Command here if needed
    // public static final String COMMAND_PREFIX = "LOAD:";

    private JButton loadTeamsButton;

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
        loadTeamsButton = new JButton("Load Teams");

        final JPopupMenu menu = new JPopupMenu("Menu");
        JMenuItem loadRedMenuItem = createTeamLoadMenuItem("red");
        menu.add(loadRedMenuItem);

        JMenuItem loadBlueMenuItem = createTeamLoadMenuItem("blue");
        menu.add(loadBlueMenuItem);

        loadTeamsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                menu.show(loadTeamsButton, 0, 0);
            }
        } );
        map.getToolBar().add(loadTeamsButton);
    }

    private JMenuItem createTeamLoadMenuItem(final String side) {
        String iconFile = String.format("%s-addteam.png", side);
        String buttonText = String.format("Load %s team", side);
        JMenuItem loadRedMenuItem = new JMenuItem(buttonText, getIconImage(iconFile));
        loadRedMenuItem.setEnabled(true);
        loadRedMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doTeamLoad(side);
            }
        });
        return loadRedMenuItem;
    }

    private ImageIcon getIconImage(String imageName) {
        ImageIcon imageIcon;
        Image image;
        try {
            image = GameModule.getGameModule().getDataArchive().getCachedImage(imageName);
        } catch (IOException e) {
            image = null;
        }
        imageIcon = new ImageIcon(image);
        return imageIcon;
    }

    private void doTeamLoad(String side) {
        // present file chooser dialog and load file
        String fileContent = GetTeamFileContent();

        if (fileContent.length() > 0) {
            // load a team object
            Team team = new NtbblTeamReader().loadTeam(fileContent);

            // add players to pitch
            addTeamToPitch(team, side);
        }
    }

    private void addTeamToPitch(Team team, String side) {
        int i = 0;
        for (Player p : team.getPlayers()) {
            // red or blue
            String column = side == "red" ? "D" : "G"; // todo: depends on side

            GamePiece piece = createPieceFromPalette(p, team.getRace(), side);
            if (piece == null){
                continue;
            }

            PlayerPiece pp = new PlayerPiece(piece);
            pp.updatePiece(p);

            // get target coordinates...
            MapGrid grid = MapHelper.getPitchGrid();
            int yOffSet = 1;
            Point location;
            try {
                location = grid.getLocation(column +(i+yOffSet));
            } catch (MapGrid.BadCoords badCoords) {
                badCoords.printStackTrace();
                Chat.log("ERROR: bad coords :-(");
                return;
            }

            // put the player on the pitch
//            Command placeCommand = MapHelper.getPitchMap().placeAt(piece, location);
            Command placeCommand = MapHelper.getPitchMap().placeOrMerge(piece, location);
            placeCommand.execute();
            GameModule.getGameModule().sendAndLog(placeCommand);

            i++;
        }
    }

    private String GetTeamFileContent() {
        String fileContent = "";
        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(loadTeamsButton);
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

        PieceSlot pieceSlot = getPieceSlotByName(position);

        if (pieceSlot == null){
            Chat.log("ERROR: pieceSlot not found to match: " + position);
            pieceSlot = getPieceSlotByName(String.format("Unknown (%s)", side));
        }

        GamePiece piece = PieceCloner.getInstance().clonePiece(pieceSlot.getPiece());
        return piece;
    }

    private PieceSlot getPieceSlotByName(String position) {
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

    private String getEntryName(Player player, String race, String side) {
        //todo: simplify this fudge later
        String position = player.getPosition();
        HashMap<String, String> aMap = new HashMap<String, String>();
        aMap.put("Minotaur Lord", "Minotaur");
        // Norse
        aMap.put("Norse Werewolf", "Norse Ulfwerener");
        if (race.equalsIgnoreCase("Norse")) {
            aMap.put("Blitzer", "Beserker"); // note spelling mistake
            aMap.put("Catcher", "Runner");
        }
        aMap.put("Necromantic", "Necro");
        aMap.put("Halfling Hero", "Halfling");
        aMap.put("Chaos Troll", "Troll");
        aMap.put("Chaos Ogre", "Ogre");
        aMap.put("River Troll", "Troll");

        // Journeymen
        aMap.put(" [J]", "");
        for (String s :
                aMap.keySet()) {
            position = position.replace(s, aMap.get(s));
        }

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

    public void removeFrom(Buildable buildable) {
        GameModule mod = GameModule.getGameModule();
        mod.removeCommandEncoder(this);
        mod.getGameState().removeGameComponent(this);

        Map map = (Map)buildable;
        map.getToolBar().remove(loadTeamsButton);
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
