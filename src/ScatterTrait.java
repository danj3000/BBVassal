import VASSAL.build.GameModule;
import VASSAL.build.module.Chatter;
import VASSAL.build.module.DieRoll;
import VASSAL.build.module.documentation.HelpFile;
import VASSAL.command.ChangePiece;
import VASSAL.command.Command;
import VASSAL.command.MoveTracker;
import VASSAL.counters.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * This adds scatter behaviour - e.g. for the ball
 * Menu item gets added (probably) because of the key-stroke binding...(tbc)
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
        Command c = null;
        if (scatterCommand.equals(keyStroke)) {
            GameModule mod = GameModule.getGameModule();

            Random ran = GameModule.getGameModule().getRNG();
            int r = ran.nextInt(8) + 1;
            int xFactor = getXFactor(r);
            int yFactor = getYFactor(r);
            final int gridSize = 150;
            int yValue = gridSize * yFactor;
            int xValue = gridSize * xFactor;

            GamePiece outer = Decorator.getOutermost(this);

            MoveTracker moveTracker = new MoveTracker(outer);
            Point position = this.getPosition();
//            mod.getChatter().show("old position: " + position.toString());
            position.translate(xValue, yValue);
//            mod.getChatter().show("position: " + position.toString());
            this.getMap().placeAt(outer, position);
            Command moveCommand = moveTracker.getMoveCommand();
            c = new Chatter.DisplayText(mod.getChatter(),
                    "Scatter roll: " + r)
                    .append(moveCommand);

            c.execute();
        }
        return c;
    }

    private static int getYFactor(int r) {
        int yFactor = 0;
        switch (r){
            case 1:
            case 2:
            case 3:
                yFactor = -1;
                break;
            case 4:
            case 5:
                yFactor = 0;
                break;
            case 6:
            case 7:
            case 8:
                yFactor = 1;
                break;
        }
        return yFactor;
    }

    private static int getXFactor(int r) {
        int xFactor = 0;
        switch (r){
            case 1:
            case 4:
            case 6:
                xFactor = -1;
                break;
            case 2:
            case 7:
                xFactor = 0;
                break;
            case 3:
            case 5:
            case 8:
                xFactor = 1;
                break;
        }
        return xFactor;
    }

    public void draw(Graphics graphics, int x, int y, Component component, double zoom) {
        // this doesn't affect the draw behaviour of the piece (pass to next layer)
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
