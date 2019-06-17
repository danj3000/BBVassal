import VASSAL.build.AbstractConfigurable;
import VASSAL.build.Buildable;
import VASSAL.build.GameModule;
import VASSAL.build.module.Chatter;
import VASSAL.build.module.GameComponent;
import VASSAL.build.module.Map;
import VASSAL.build.module.documentation.HelpFile;
import VASSAL.build.widget.PieceSlot;
import VASSAL.command.Command;
import VASSAL.command.CommandEncoder;
import VASSAL.counters.GamePiece;
import VASSAL.counters.PieceCloner;
import game.Player;
import game.Team;
import vassal.BBPalette;
import vassal.Chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;


public class TeamLoader extends AbstractConfigurable implements CommandEncoder,GameComponent {
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
        JMenuItem loadRedMenuItem = createTeamLoadMenuItem("Red");
        menu.add(loadRedMenuItem);

        JMenuItem loadBlueMenuItem = createTeamLoadMenuItem("Blue");
        menu.add(loadBlueMenuItem);

        loadTeamsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                menu.show(loadTeamsButton, 0, 0);
            }
        } );
        map.getToolBar().add(loadTeamsButton);
    }

    private JMenuItem createTeamLoadMenuItem(final String side) {
        String iconFile = String.format("%s-addteam.png", side.toLowerCase());
        String buttonText = String.format("Load %s team", side.toLowerCase());
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

            // name the end zone, add players to the pitch and set up Re-rolls
            Command c = MapHelper.nameEndZone(side, team.getName())
                    .append(addTeamToPitch(team, side))
                    .append(MapHelper.moveRerollCounter(side, team.getRerolls()))
                    .append(MapHelper.setCheerleaders(side, team.getCheerleaders()))
                    .append(MapHelper.setAssistantCoaches(side, team.getAssistantCoaches()));

            // Add to the log for playback
            GameModule.getGameModule().sendAndLog(c);
        }
    }

    private Command addTeamToPitch(Team team, String side) {

        String column = side.equalsIgnoreCase("red") ? "I" : "P";
        Command c = new Chatter.DisplayText(GameModule.getGameModule().getChatter(),
                "Creating Players for " + team.getName());

        int row = 1; // vertical offset for placing on pitch
        for (Player p : team.getPlayers()) {
            GamePiece piece = BBPalette.createPieceFromPalette(p, team.getRace(), side);
            if (piece != null) {
                PlayerPiece pp = new PlayerPiece(piece);
                pp.updatePieceProperties(p); // set stats, jersey etc

                if (p.getMissNextGame()) {
                    c.append(new Chatter.DisplayText(GameModule.getGameModule().getChatter(),
                            String.format("Player %s MNG", p.getNumber())
                    ));
                } else {
                    // get target coordinates...
                    String target = column + (row);

                    c.append(MapHelper.addPlayerToPitch(piece, target));
                }
                row++;
            }
        }

        // do all this locally then return command to be logged and sent
        c.execute();
        return c;
    }

    //todo:
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
