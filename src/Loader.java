import VASSAL.build.AbstractConfigurable;
import VASSAL.build.Buildable;
import VASSAL.build.GameModule;
import VASSAL.build.module.*;
import VASSAL.build.module.documentation.HelpFile;
import VASSAL.build.widget.PieceSlot;
import VASSAL.command.Command;
import VASSAL.command.CommandEncoder;
import VASSAL.counters.GamePiece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

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
                GameModule.getGameModule().getChatter().show("muppet");
                java.util.List<PieceSlot> slots = GameModule.getGameModule().getAllDescendantComponentsOf(PieceSlot.class);
                for (PieceSlot ps :
                        slots) {
                    GameModule.getGameModule().getChatter().show(ps.getConfigureName());
                }

            }
        });

        map.getToolBar().add(loaderButton);
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
