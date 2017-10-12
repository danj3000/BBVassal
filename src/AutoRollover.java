import VASSAL.build.AbstractConfigurable;
import VASSAL.build.Buildable;
import VASSAL.build.GameModule;
import VASSAL.build.module.*;
import VASSAL.build.module.documentation.HelpFile;
import VASSAL.command.Command;
import VASSAL.command.CommandEncoder;
import VASSAL.counters.GamePiece;
import VASSAL.counters.Stack;
import VASSAL.tools.LaunchButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AutoRollover extends AbstractConfigurable implements CommandEncoder,GameComponent {

    public class RolloverCommand extends Command{
        private String team;

        public RolloverCommand(String team) {
            this.team = team;
        }

        protected void executeCommand() {
            Map map = Map.getMapById("Grass Pitch");
            GamePiece playerPieces[] = MapHelper.getTeamPlayers(map, team);
//            GameModule.getGameModule().getChatter().show("players: " + playerPieces.length);

            for (GamePiece piece : playerPieces) {
                if(piece instanceof Stack){
//                    GameModule.getGameModule().getChatter().show("Stack: ");
                    Stack s = (Stack)piece;
                    for (int j = 0; j < s.getPieceCount(); j++) {
                        GamePiece p = (s.getPieceAt(j));
                        rollover(p);
                        break;
                    }
                }
                else{
                    rollover(piece);
                }
            }
        }

        private void rollover(GamePiece p) {
            Object status = p.getProperty("Status");
            if ((status != null) && (Integer.parseInt(status.toString()) == 2)) {
                p.setProperty("Status", 1);
            }
        }

        protected Command myUndoCommand() {
            return null;
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


    public void addTo(Buildable buildable) {
        GameModule mod = (GameModule)buildable;

        mod.getGameModule().addCommandEncoder(this);
        mod.getGameState().addGameComponent(this);

        // add button to toolbar
        redRolloverButton = new JButton("Rollover Reds");
        redRolloverButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rolloverButtonPressed("red");
            }
        });

        mod.getToolBar().add(redRolloverButton);

        // add button to toolbar
        blueRolloverButton = new JButton("Rollover Blues");
        blueRolloverButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rolloverButtonPressed("blue");
            }
        });

        mod.getToolBar().add(blueRolloverButton);
    }

    public void removeFrom(Buildable buildable) {
        GameModule mod = (GameModule)buildable;
        mod.removeCommandEncoder(this);
        mod.getGameState().removeGameComponent(this);

        mod.getToolBar().remove(redRolloverButton);
        mod.getToolBar().remove(blueRolloverButton);

    }

    private void rolloverButtonPressed(String team){
        GameModule mod = GameModule.getGameModule();

        RolloverCommand rc = new RolloverCommand(team);
        Command c = new Chatter.DisplayText(mod.getChatter(),
                "Automated Rollover for " + team)
                .append(rc);
        c.execute();

        mod.sendAndLog(c);
    }

    public Command decode(String s) {
        if (s.startsWith(COMMAND_PREFIX)) {
            return new RolloverCommand(s.substring(COMMAND_PREFIX.length()));
        } else {
            return null;
        }
    }

    public String encode(Command c) {
        if (c instanceof RolloverCommand) {
            return COMMAND_PREFIX + ((RolloverCommand)c).team;
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
