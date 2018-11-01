package core;

import core.API.Elevator;
import core.API.Passenger;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class IlyaStrategy extends Strategy {
    public void onTick(List<Passenger> myPassengers, List<Elevator> myElevators, List<Passenger> enemyPassengers,
                       List<Elevator> enemyElevators) {
        for (Elevator e : myElevators) {
            if (e.getState() != 1 && e.getPassengers().size() <= 20) {
                Passenger passenger = getNearestPassenger(e, myPassengers);
                if (e.getFloor() == passenger.getFromFloor()) {
                    passenger.setElevator(e);
                } else {
                    e.goToFloor(passenger.getFloor());
                }
            }
            if (e.getPassengers().size() > 0 && e.getState() != 1) {
                e.goToFloor(getNearestFloor(e, e.getPassengers()
                        .stream()
                        .map(p -> p.getDestFloor())
                        .collect(Collectors.toList())));
            }

        }
    }

    private Integer getNearestFloor(Elevator e, List<Integer> destFloors) {
        List<Integer> destList = destFloors
                .stream()
                .map(d -> Math.abs(d - e.getFloor()))
                .collect(Collectors.toList());
        int minIndex = destList.indexOf(Collections.min(destList));
        return destFloors.get(minIndex);
    }

    private Passenger getNearestPassenger(Elevator elevator, List<Passenger> passengerList) {
        List<Double> destList = passengerList
                .stream()
                .filter(p -> p.getState() < 5)
                .map(p -> Math.abs(p.getY() - elevator.getY()))
                .collect(Collectors.toList());
        int minIndex = destList.indexOf(Collections.min(destList));
        return passengerList.get(minIndex);
    }
}