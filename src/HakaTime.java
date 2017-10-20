import VASSAL.build.AbstractConfigurable;
import VASSAL.build.Buildable;
import VASSAL.build.GameModule;
import VASSAL.build.module.Chatter;
import VASSAL.build.module.Map;
import VASSAL.build.module.documentation.HelpFile;
import VASSAL.build.module.map.boardPicker.Board;
import VASSAL.build.module.map.boardPicker.board.MapGrid;
import VASSAL.command.Command;
import VASSAL.command.CommandEncoder;
import VASSAL.counters.GamePiece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HakaTime extends AbstractConfigurable implements CommandEncoder  {
    public static final String RED_COLUMN = "RedColumn";
    public static final String BLUE_COLUMN = "BlueColumn";
    private final int yOffSet = 3;
    private String redColumn = "H";
    private String blueColumn = "Q";

    public Command decode(String s) {
        return null;
    }

    public String encode(Command command) {
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
