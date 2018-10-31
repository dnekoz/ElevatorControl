package ru.sbrf.vis.controller;

import ru.sbrf.vis.algorithm.Algorithm;
import ru.sbrf.vis.state.ElevatorState;

public class Controller {

    private ElevatorState elevatorState;
    private Algorithm algorithm;

    public ElevatorState getElevatorState() {
        return elevatorState;
    }

    public void setElevatorState(ElevatorState elevatorState) {
        this.elevatorState = elevatorState;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }
}
