import VASSAL.build.AbstractConfigurable;
import VASSAL.build.Buildable;
import VASSAL.build.GameModule;
import VASSAL.build.module.Chatter;
import VASSAL.build.module.Map;
import VASSAL.build.module.documentation.HelpFile;
import VASSAL.build.module.map.StackMetrics;
import VASSAL.counters.Decorator;
import VASSAL.counters.GamePiece;
import VASSAL.counters.MovementMarkable;
import VASSAL.counters.Stack;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AutoRollover extends AbstractConfigurable {
    private JButton rolloverButton;

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

    public HelpFile getHelpFile() {
        return null;
    }

    public Class[] getAllowableConfigureComponents() {
        return new Class[0];
    }


    public void addTo(Buildable buildable) {
        GameModule mod = (GameModule)buildable;

        // add button to toolbar
        rolloverButton = new JButton("Rollover");
        rolloverButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rolloverButtonPressed();
            }
        });
        mod.getToolBar().add(rolloverButton);
    }

    public void removeFrom(Buildable buildable) {
        GameModule mod = (GameModule)buildable;

        mod.getToolBar().remove(rolloverButton);
    }

    private void rolloverButtonPressed(){
        Chatter chatter = GameModule.getGameModule().getChatter();

        Map map = Map.activeMap;
        GamePiece pieces[] = map.getPieces();
        for (int i = 0; i < pieces.length; i++) {
            GamePiece piece = Decorator.getOutermost(pieces[i]);
            if(piece instanceof Stack){
                chatter.show("stack: " + piece.getName());
                Stack s = (Stack)piece;
                for (int j = 0; j < s.getPieceCount(); j++) {
                    GamePiece p = (s.getPieceAt(j));
                    Object status = p.getProperty("PlayerStatus");
                    if ((status != null) && (((Integer) status) == 2)) {
                        chatter.show("Status: " + status.toString());
                        p.setProperty("PlayerStatus", 1);
                    }

                }

                chatter.show("----");
            }


        }
    }
}