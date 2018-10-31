package ru.sbrf.vis.state;

import java.util.Queue;

public class Elevator {

    private Direction direction;
    private Integer capacity;
    private Integer currentCapacity;
    private Floor currentFloor;
    private Queue<Floor> taskQueue;

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(Integer currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public Floor getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(Floor currentFloor) {
        this.currentFloor = currentFloor;
    }

    public Queue<Floor> getTaskQueue() {
        return taskQueue;
    }

    public void setTaskQueue(Queue<Floor> taskQueue) {
        this.taskQueue = taskQueue;
    }
}
