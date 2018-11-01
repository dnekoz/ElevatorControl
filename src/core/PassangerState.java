package core;

public enum PassangerState {

    WAITING_FOR_ELEVATOR (1),
    MOVING_TO_ELEVATOR (2),
    RETURNING (3),
    MOVING_TO_FLOOR (4),
    USING_ELEVATOR (5),
    EXITING (6);


    private final int state;

    PassangerState(int state){
        this.state = state;
    }

    public int getInt(){
        return state;
    }

    public static PassangerState getByInt(int stateId){
        for (PassangerState state: values()){
            if (state.getInt() == stateId) return state;
        }
        return null;
    }
}
