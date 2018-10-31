package ru.sbrf.vis.algorithm;

import ru.sbrf.vis.state.ElevatorState;

public interface Algorithm {

    public void setState(ElevatorState elevatorState);
    public ElevatorState getState();
    public Algorithm getAlgorithm();
    public void setAlgorithm(Algorithm algorithm);
    public void calculateState();

}
