import VASSAL.build.module.BasicCommandEncoder;
import VASSAL.counters.Decorator;
import VASSAL.counters.GamePiece;

public class BBCounterFactory extends BasicCommandEncoder {
    public Decorator createDecorator(String type, GamePiece inner) {
        Decorator piece = null;
        if (type.startsWith(ScatterTrait.ID)) {
            piece = new ScatterTrait(inner);
        }
        else {
            piece = super.createDecorator(type,inner);
        }
        return piece;
    }
}