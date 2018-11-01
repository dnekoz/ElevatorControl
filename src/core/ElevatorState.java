package core;

public enum ElevatorState {

    WAITING (0),
    MOVING (1),
    OPENING (2),
    FILLING (3),
    CLOSING (4);

    private final int state;

    ElevatorState(int state){
        this.state = state;
    }

    public int getInt(){
        return state;
    }
}
