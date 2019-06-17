package game;

import java.util.ArrayList;

public class Team {

    private ArrayList<Player> players = new ArrayList<Player>();
    private String race;
    private String name;
    private String coach;
    private String rerolls;
    private int cheerleaders = 0;
    private int assistantCoaches;

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public String getCoach() {
        return coach;
    }

    public String getRerolls() {
        return rerolls;
    }

    public void setRerolls(String rerolls) {
        this.rerolls = rerolls;
    }

    public int getCheerleaders() {
        return cheerleaders;
    }

    public void setCheerleaders(int cheerleaders) {
        this.cheerleaders = cheerleaders;
    }

    public void setAssistantCoaches(int value) {
        this.assistantCoaches = value;
    }

    public int getAssistantCoaches() {
        return assistantCoaches;
    }
}
