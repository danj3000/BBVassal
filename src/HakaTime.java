import VASSAL.build.AbstractConfigurable;
import VASSAL.build.Buildable;
import VASSAL.build.GameModule;
import VASSAL.build.module.Chatter;
import VASSAL.build.module.GameComponent;
import VASSAL.build.module.Map;
import VASSAL.build.module.documentation.HelpFile;
import VASSAL.build.module.map.boardPicker.Board;
import VASSAL.build.module.map.boardPicker.board.MapGrid;
import VASSAL.command.Command;
import VASSAL.command.CommandEncoder;
import VASSAL.counters.GamePiece;
import vassal.Chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HakaTime extends AbstractConfigurable implements CommandEncoder, GameComponent {
    public static final String RED_COLUMN = "RedColumn";
    public static final String BLUE_COLUMN = "BlueColumn";
    public static final String DELIMITER = ";";
    private final int yOffSet = 3;
    private String redColumn = "H";
    private String blueColumn = "Q";
    public static final String COMMAND_PREFIX = "HAKA:";

    public Command decode(String s) {
        if (s.startsWith(COMMAND_PREFIX)) {
            String teamString = s.substring(COMMAND_PREFIX.length());
            String[] params = teamString.split(DELIMITER);
            return new HakaCommand(params[0],Integer.parseInt(params[1]),params[2]);
        } else {
            return null;
        }

    }

    public String encode(Command command) {
        if (command instanceof HakaCommand) {
            HakaCommand hc = (HakaCommand)command;
            return COMMAND_PREFIX + hc.team + DELIMITER + hc.yOffSet + DELIMITER + hc.xColumn;
        }
        else
            return null;
    }


    public void setup(boolean gameStarting) {
        // no initialization needed?
    }

    public Command getRestoreCommand() {
        // no state to maintain
        return null;
    }

    public class HakaCommand extends Command{

        private final int yOffSet;
        private final String xColumn;
        private String team;

        public HakaCommand(String team, int yOffSet, String xColumn) {
            this.team = team;
            this.yOffSet = yOffSet;
            this.xColumn = xColumn;
        }

        protected void executeCommand() {

            Map map = MapHelper.getPitchMap();

            GamePiece pieces[] = MapHelper.getTeamPlayers(map, team);
            movePlayersToHaka(yOffSet, xColumn,  pieces);
            
        }

        protected Command myUndoCommand() {
            return null;
        }

        private void movePlayersToHaka(int yOffSet, String column, GamePiece[] pieces) {
            MapGrid grid = MapHelper.getPitchGrid();

            for (int i = 0; i < pieces.length; i++) {
                try {
                    Point point = grid.getLocation(column +(i+yOffSet));
                    pieces[i].setPosition(point);
                } catch (MapGrid.BadCoords badCoords) {
                    badCoords.printStackTrace();
                }
            }
        }
    }

    private JButton hakaButton;

    public String[] getAttributeDescriptions() {
        return new String[]{
                "Red Haka column", "Blue Haka column"
        };
    }

    public Class<?>[] getAttributeTypes() {
        return new Class[]{
                String.class, String.class
        };
    }

    public String[] getAttributeNames() {
        return new String[]{
                RED_COLUMN, BLUE_COLUMN
        };
    }

    public void setAttribute(String key, Object value) {
        if (RED_COLUMN.equals(key)) {
            redColumn = (String)value;
        }
        else if (BLUE_COLUMN.equals(key)) {
            blueColumn = (String)value;
        }
    }

    public String getAttributeValueString(String s) {
        if (RED_COLUMN.equals(s)){
            return redColumn.toString();
        }
        else if (BLUE_COLUMN.equals(s)){
            return blueColumn.toString();
        }
        else
            return null;
    }

    public HelpFile getHelpFile() {
        return null;
    }

    public Class[] getAllowableConfigureComponents() {
        return new Class[0];
    }

    public void addTo(Buildable parent) {
        GameModule mod = GameModule.getGameModule();
        mod.addCommandEncoder(this);
        mod.getGameState().addGameComponent(this);

        // add button to toolbar
        hakaButton = new JButton("Haka Time");
        hakaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hakaButtonPressed();
            }
        });

        Map map = (Map)parent;
        map.getToolBar().add(hakaButton);
    }

    public void removeFrom(Buildable buildable) {
        GameModule mod = GameModule.getGameModule();

        mod.removeCommandEncoder(this);
        mod.getGameState().removeGameComponent(this);
        Map map = (Map)buildable;

        map.getToolBar().remove(hakaButton);
    }

    private void hakaButtonPressed() {
        GameModule mod = GameModule.getGameModule();

        HakaCommand hcRed = new HakaCommand("red", yOffSet, redColumn);
        HakaCommand hcBlue = new HakaCommand("blue", yOffSet, blueColumn);

        Command c = new Chatter.DisplayText(mod.getChatter(),
                "Resetting teams")
                .append(hcRed)
                .append(hcBlue);

        c.execute();

        mod.sendAndLog(c);
    }



}
