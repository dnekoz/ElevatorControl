package core;

import core.API.Elevator;
import core.API.Passenger;

import java.util.List;

public class Strategy extends BaseStrategy {
    private int tick = 0;

    public void onTick(List<Passenger> myPassengers, List<Elevator> myElevators, List<Passenger> enemyPassengers, List<Elevator> enemyElevators) {
        System.out.println("tick: " + ++tick);
        for (Elevator e : myElevators) {
            for (Passenger p : myPassengers) {
                if (p.getState() < 5) {
                    if (e.getState() != 1) {
                        e.goToFloor(p.getFromFloor());
                    }
                    if (e.getFloor() == p.getFromFloor()) {
                        p.setElevator(e);
                    }
                }
            }
            if (e.getPassengers().size() > 0 && e.getState() != 1) {
                e.goToFloor(e.getPassengers().get(0).getDestFloor());
            }

        }
    }
}
