package game;

import java.awt.*;
import java.util.ArrayList;

public class Team {

    private ArrayList<Player> players = new ArrayList<Player>();

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
}
