import VASSAL.build.AbstractConfigurable;
import VASSAL.build.Buildable;
import VASSAL.build.GameModule;
import VASSAL.build.module.Map;
import VASSAL.build.module.documentation.HelpFile;
import VASSAL.counters.GamePiece;
import VASSAL.counters.Stack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        Map map = Map.activeMap;
        GamePiece pieces[] = map.getPieces();
        for (GamePiece piece : pieces) {
            if(piece instanceof Stack){
                Stack s = (Stack)piece;
                boolean movePiece = isPlayer(s);

                if (movePiece){
                    s.setPosition(new Point(300,300));
                }
            }
        }
    }

    private boolean isPlayer(Stack s) {
        for (int j = 0; j < s.getPieceCount(); j++) {
            GamePiece p = (s.getPieceAt(j));
            Object isPlayer = p.getProperty("IsPlayer");
            if ((isPlayer != null) && ("true".equalsIgnoreCase(isPlayer.toString()))) {
                return true;
            }
        }
        return false;
    }

}
