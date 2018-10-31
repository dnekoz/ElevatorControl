package ru.sbrf.vis.state;

public class Call {

    private Floor fromFloor;
    private Floor toFloor;
    private Integer awaitingTime;
    private Integer travelTime;
    private CallState callState;

    public Floor getFromFloor() {
        return fromFloor;
    }

    public void setFromFloor(Floor fromFloor) {
        this.fromFloor = fromFloor;
    }

    public Floor getToFloor() {
        return toFloor;
    }

    public void setToFloor(Floor toFloor) {
        this.toFloor = toFloor;
    }

    public Integer getAwaitingTime() {
        return awaitingTime;
    }

    public void setAwaitingTime(Integer awaitingTime) {
        this.awaitingTime = awaitingTime;
    }

    public Integer getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(Integer travelTime) {
        this.travelTime = travelTime;
    }

    public CallState getCallState() {
        return callState;
    }

    public void setCallState(CallState callState) {
        this.callState = callState;
    }
}
