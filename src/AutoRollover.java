import VASSAL.build.AbstractConfigurable;
import VASSAL.build.Buildable;
import VASSAL.build.GameModule;
import VASSAL.build.module.*;
import VASSAL.build.module.documentation.HelpFile;
import VASSAL.command.Command;
import VASSAL.command.CommandEncoder;
import VASSAL.counters.GamePiece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class AutoRollover extends AbstractConfigurable implements CommandEncoder,GameComponent {

    private ArrayList<String> victims;

    public class RolloverTeamCommand extends TeamPlayerStateCommand {

        public RolloverTeamCommand(String team) {
            super(team);
        }

        @Override
        protected void changePlayerState(GamePiece p) {
            Object status = p.getProperty("Status");
            if ((status != null) && (Integer.parseInt(status.toString()) == 2)) {
                p.setProperty("Status", 1);
                victims.add(p.getId());
            }
        }

        @Override
        protected Command myUndoCommand() {
            return new StunCommand(victims);
        }
    }

    public static final String COMMAND_PREFIX = "ROLLOVER:";

    private JButton blueRolloverButton;
    private JButton redRolloverButton;

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
        redRolloverButton = new JButton("Rollover Reds");
        Image redImage = null;
        try {
            redImage = mod.getDataArchive().getCachedImage("rotate_red.png");
            redRolloverButton.setIcon(new ImageIcon(redImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
        redRolloverButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rolloverButtonPressed("red");
            }
        });
        map.getToolBar().add(redRolloverButton);

        // add button to toolbar
        blueRolloverButton = new JButton("Rollover Blues");
        Image blueImage = null;
        try {
            blueImage = mod.getDataArchive().getCachedImage("rotate_blue.png");
            blueRolloverButton.setIcon(new ImageIcon(blueImage));
        } catch (IOException e) {
            e.printStackTrace();
        }

        blueRolloverButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rolloverButtonPressed("blue");
            }
        });

        map.getToolBar().add(blueRolloverButton);
    }

    public void removeFrom(Buildable buildable) {
        GameModule mod = GameModule.getGameModule();
        mod.removeCommandEncoder(this);
        mod.getGameState().removeGameComponent(this);

        Map map = (Map)buildable;
        map.getToolBar().remove(redRolloverButton);
        map.getToolBar().remove(blueRolloverButton);

    }

    private void rolloverButtonPressed(String team){
        GameModule mod = GameModule.getGameModule();

        RolloverTeamCommand rc = new RolloverTeamCommand(team);
        Command c = new Chatter.DisplayText(mod.getChatter(),
                "Automated Rollover for " + team)
                .append(rc);
        c.execute();

        mod.sendAndLog(c);
    }

    public Command decode(String s) {
        if (s.startsWith(COMMAND_PREFIX)) {
            String teamString = s.substring(COMMAND_PREFIX.length());
            return new RolloverTeamCommand(teamString);
        } else {
            return null;
        }
    }

    public String encode(Command c) {
        if (c instanceof RolloverTeamCommand) {
            return COMMAND_PREFIX + ((RolloverTeamCommand)c).team;
        }
        else
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
