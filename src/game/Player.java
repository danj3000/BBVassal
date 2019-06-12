package game;

public class Player {
    private String position;
    private String number;
    private String name;
    private int movementAllowance;
    private int strength;
    private int agility;
    private int armour;
    private String skills;
    private boolean missNextGame;
    private String displayPosition;

    public Player(String position) {
        this.position = position;
    }

    public String getPosition(){
        return position;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMovement() {
        return movementAllowance;
    }

    public void setMovement(int movementAllowance) {
        this.movementAllowance = movementAllowance;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getArmour() {
        return armour;
    }

    public void setArmour(int armour) {
        this.armour = armour;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public boolean getMissNextGame() {
        return missNextGame;
    }

    public void setMissNextGame(boolean value) {
        missNextGame = value;
    }

    public String getDisplayPosition() {
        return displayPosition;
    }

    public void setDisplayPosition(String displayPosition) {
        this.displayPosition = displayPosition;
    }
}
