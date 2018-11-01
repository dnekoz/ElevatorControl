package core;

public enum ElevatorState {

    WAITING (0, true, Constants.ELEVATOR_WAITING_AFTER_DOORS_CLOSURE_TIME),
    MOVING (1, false, 0),
    OPENING (2, true, Constants.ELEVATOR_OPEN_CLOSE_TIME),
    FILLING (3, true, Constants.ELEVATOR_MIN_FLOOR_WAITING_TIME),
    CLOSING (4, true, Constants.ELEVATOR_OPEN_CLOSE_TIME);

    private static ElevatorState[] BY_CODE = new ElevatorState[]{
            WAITING,
            MOVING,
            OPENING,
            FILLING,
            CLOSING
    };

    public static ElevatorState fromCode(int code) {
        return code < BY_CODE.length ? BY_CODE[code] : null;
    }

    private final int state;
    private final boolean canChangeDestination;
    private final int requiresAtLeastTicks;

    ElevatorState(int state, boolean canChangeDestination, int requiresAtLeastTicks){
        this.state = state;
        this.canChangeDestination = canChangeDestination;
        this.requiresAtLeastTicks = requiresAtLeastTicks;
    }

    public int getInt(){
        return state;
    }

    public boolean canChangeDestination() {
        return canChangeDestination;
    }

    public int getRequiresAtLeastTicks() {
        return requiresAtLeastTicks;
    }

    public static ElevatorState getByInt(int stateId){
        for (ElevatorState state: values()){
            if (state.getInt() == stateId) return state;
        }
        return null;
    }
}
