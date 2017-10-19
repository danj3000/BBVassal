import VASSAL.build.AbstractConfigurable;
import VASSAL.build.Buildable;
import VASSAL.build.GameModule;
import VASSAL.build.module.Map;
import VASSAL.build.module.documentation.HelpFile;
import VASSAL.build.module.map.BoardPicker;
import VASSAL.build.module.map.boardPicker.Board;
import VASSAL.build.module.map.boardPicker.board.MapGrid;
import VASSAL.build.module.map.boardPicker.board.ZonedGrid;
import VASSAL.build.module.map.boardPicker.board.mapgrid.Zone;
import VASSAL.counters.GamePiece;
import VASSAL.counters.Stack;
import com.google.common.collect.Iterables;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

public class HakaTime extends AbstractConfigurable {
    private JButton hakaButton;

    public String[] getAttributeDescriptions() {
        return new String[0];
    }

    public Class<?>[] getAttributeTypes() {
        return new Class[0];
    }

    public String[] getAttributeNames() {
        return new String[0];
    }

    public void setAttribute(String s, Object o) {

    }

    public String getAttributeValueString(String s) {
        return null;
    }

    public void removeFrom(Buildable buildable) {
        GameModule mod = (GameModule)buildable;

        mod.getToolBar().remove(hakaButton);
    }

    public HelpFile getHelpFile() {
        return null;
    }

    public Class[] getAllowableConfigureComponents() {
        return new Class[0];
    }

    public void addTo(Buildable buildable) {
        GameModule mod = (GameModule)buildable;

        // add button to toolbar
        hakaButton = new JButton("Haka");
        hakaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hakaButtonPressed();
            }
        });
        mod.getToolBar().add(hakaButton);
    }

    private void hakaButtonPressed() {
        Map map = MapHelper.getPitchMap();
        Collection<Board> boards = map.getBoardPicker().getSelectedBoards();
        Board board = Iterables.get(boards, 0);
        MapGrid grid = board.getGrid();
        ZonedGrid zGrid = (ZonedGrid) grid;
        Zone z = zGrid.findZone("Pitch");
        MapGrid pitchGrid = z.getGrid();
        Point point = null;
        try {
            point = pitchGrid.getLocation("A1");
        } catch (MapGrid.BadCoords badCoords) {
            GameModule.getGameModule().getChatter().show("grid ref not valid");

            badCoords.printStackTrace();
        }
        GamePiece pieces[] = MapHelper.getPlayers(map);
        for (GamePiece piece : pieces) {


            piece.setPosition(point);
        }
    }

}
