package core.user.alvas;

import core.API.Passenger;

import java.util.ArrayList;
import java.util.List;

public class Task {
    // 1 - движение вверх, -1 - движение вниз, 0 - лифт свободен
    private int direction;
    // текущий этаж
    private int floor;
    // вес лифта
    private double weight;
    // список пассажиров
    private List<Passenger> passengers;
    // список входящих
    private List<Passenger> in;
    // список исходящих
    private List<Passenger> out;

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    private void calcWeight(){
        weight = passengers.stream().mapToDouble(e->e.getWeight()).reduce((e1, e2) -> e1 * e2).orElse(1.0);
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public List<Passenger> getPassengers() {
        if (passengers == null) {passengers = new ArrayList<>(); }
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public List<Passenger> getIn() {
        if (in == null) {in = new ArrayList<>(); }
        return in;
    }

    public void setIn(List<Passenger> in) {
        this.in = in;
    }

    public List<Passenger> getOut() {
        if (out == null) {out = new ArrayList<>(); }
        return out;
    }

    public void setOut(List<Passenger> out) {
        this.out = out;
    }
}
