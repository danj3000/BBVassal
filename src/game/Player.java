package game;

public class Player {
    private String position;
    private String number;

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
}
