import VASSAL.build.module.Map;
import VASSAL.command.Command;
import VASSAL.counters.GamePiece;
import VASSAL.counters.Stack;

public abstract class TeamPlayerStateCommand extends PlayerStateCommand {
    protected String team;

    public TeamPlayerStateCommand(String team) {
        this.team = team;
    }

    @Override
    protected GamePiece[] getMatchingPlayers(Map map) {
        return MapHelper.getTeamPlayers(map, team);
    }

}
