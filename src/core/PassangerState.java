package core;

public enum PassangerState {

    WAITING_FOR_ELEVATOR (1),
    MOVING_TO_ELEVATOR (2),
    RETURNING (3),
    MOVING_TO_FLOOR (4),
    USING_ELEVATOR (5),
    EXITING (6);


    private final int state;

    private PassangerState(int state){
        this.state = state;
    }

    public int getInt(){
        return state;
    }
}
