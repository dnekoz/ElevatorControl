package ru.sbrf.vis.state;

import java.util.Collection;

public class ElevatorState {

    private Collection<Elevator> elevators;
    private Collection<Passenger> passengers;
    private Collection<Call> calls;

    public Collection<Elevator> getElevators() {
        return elevators;
    }

    public void setElevators(Collection<Elevator> elevators) {
        this.elevators = elevators;
    }

    public Collection<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(Collection<Passenger> passengers) {
        this.passengers = passengers;
    }

    public Collection<Call> getCalls() {
        return calls;
    }

    public void setCalls(Collection<Call> calls) {
        this.calls = calls;
    }
}
