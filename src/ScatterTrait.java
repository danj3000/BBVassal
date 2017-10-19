import VASSAL.build.GameModule;
import VASSAL.build.module.DieRoll;
import VASSAL.build.module.documentation.HelpFile;
import VASSAL.command.ChangePiece;
import VASSAL.command.Command;
import VASSAL.counters.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Created by Mic on 09/08/2017.
 *
 * use this class as a template to make useful ones.
 */
public class ScatterTrait extends Decorator implements EditablePiece  {
    public static final String ID = "ScatterTrait";
    private KeyStroke scatterCommand = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);

    public ScatterTrait(){
        this(null);
    }

    public ScatterTrait(GamePiece piece){
        setInner(piece);
    }

    @Override
    public Command keyEvent(KeyStroke stroke) {
        Command c = null;
        if (scatterCommand.equals(stroke)) {
            Decorator outer = this.getOuter();

            Random ran = GameModule.getGameModule().getRNG();

            int r = ran.nextInt(8) + 1;
            GameModule.getGameModule().getChatter().show("Dice rolled?: " + r);
            //Point point;
            //outer.setPosition(point);
            return piece.keyEvent(stroke);
        }
        return c;
    }

    @Override
    public void mySetState(String s) {
        // parse any state info
    }
    @Override
    public String myGetState() {
        // return state info as string
        return "";
    }

    @Override
    public String myGetType() {
        return ID;
    }
    @Override
    protected KeyCommand[] myGetKeyCommands() {

        return new KeyCommand[]{new KeyCommand("Scatter", scatterCommand, this)};
    }
    @Override
    public Command myKeyEvent(KeyStroke keyStroke) {
        return null;
    }

    public void draw(Graphics graphics, int x, int y, Component component, double zoom) {
        getInner().draw(graphics, x, y, component, zoom);
    }

    public Rectangle boundingBox() {
        return this.piece.boundingBox();
    }

    public Shape getShape() {
        return this.piece.getShape();
    }

    public String getName() {
        return this.piece.getName();
    }

    public String getDescription() {
        return "Can Scatter";
    }

    public void mySetType(String type) {
        // extract any property values from type string (begins with ID)
    }

    public HelpFile getHelpFile() {
        return null;
    }
}
