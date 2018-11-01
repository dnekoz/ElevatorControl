package core;

public enum ElevatorState {

    WAITING (0, false, Constants.ELEVATOR_WAITING_AFTER_DOORS_CLOSURE_TIME),
    MOVING (1, false, 0),
    OPENING (2, false, Constants.ELEVATOR_OPEN_CLOSE_TIME),
    FILLING (3, true, Constants.ELEVATOR_MIN_FLOOR_WAITING_TIME),
    CLOSING (4, false, Constants.ELEVATOR_OPEN_CLOSE_TIME);

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
    private final boolean canBeInterrupted;
    private final int requiresAtLeastTicks;

    ElevatorState(int state, boolean canBeInterrupted, int requiresAtLeastTicks){
        this.state = state;
        this.canBeInterrupted = canBeInterrupted;
        this.requiresAtLeastTicks = requiresAtLeastTicks;
    }

    public int getInt(){
        return state;
    }

    public boolean isCanBeInterrupted() {
        return canBeInterrupted;
    }

    public int getRequiresAtLeastTicks() {
        return requiresAtLeastTicks;
    }
}
