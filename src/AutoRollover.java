import VASSAL.build.AbstractConfigurable;
import VASSAL.build.Buildable;
import VASSAL.build.GameModule;
import VASSAL.build.module.Chatter;
import VASSAL.build.module.Map;
import VASSAL.build.module.documentation.HelpFile;
import VASSAL.command.Command;
import VASSAL.command.CommandEncoder;
import VASSAL.counters.GamePiece;
import VASSAL.counters.Stack;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AutoRollover extends AbstractConfigurable {
    public class RolloverCommand extends Command implements CommandEncoder{
        public static final String COMMAND_PREFIX = "ROLLOVER:";



        public RolloverCommand() {

        }

        protected void executeCommand() {
            Map map = Map.activeMap;
            GamePiece playerPieces[] = MapHelper.getPlayers(map);
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

        public Command decode(String s) {
            if (s.startsWith(COMMAND_PREFIX)) {
                return new RolloverCommand();
            } else {
                return null;
            }
        }

        public String encode(Command c) {
            if (c instanceof RolloverCommand) {
                return COMMAND_PREFIX;
            }
            else
                return null;
        }
    }

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
        GameModule mod = GameModule.getGameModule();

        RolloverCommand rc = new RolloverCommand();
        Command c = new Chatter.DisplayText(mod.getChatter(),
                "Performing automated rollover").append(rc);
        c.execute();

        mod.sendAndLog(c);
    }
}
