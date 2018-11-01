package core;

public enum PlayerType {

    FIRST_PLAYER ("FIRST_PLAYER"),
    SECOND_PLAYER ("SECOND_PLAYER");

    private final String name;

    PlayerType(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
