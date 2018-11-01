package core;

import core.API.Elevator;
import core.API.Passenger;

import java.util.*;
import java.util.stream.Collectors;

import static core.ElevatorState.FILLING;
import static core.ElevatorState.OPENING;
import static core.PassangerState.*;

public class UpStrategy extends BaseStrategy {
    private int tick = 0;
    private static final Map<Integer, TreeSet<Integer>> channeling = new HashMap<>();
    private PlayerType playerType;

    public void onTick(List<Passenger> myPassengers, List<Elevator> myElevators, List<Passenger> enemyPassengers, List<Elevator> enemyElevators) {

        try {


            if (playerType == null) {
                initChannelling(myElevators);
            }

            System.out.println(this.getClass().getName() + " " + playerType.toString() + " tick: " + ++tick);

            for (Elevator e : myElevators) {

                ElevatorState elevatorState = ElevatorState.getByInt(e.getState());
                EnumSet<ElevatorState> readyStates = EnumSet.of(FILLING, OPENING);

                // Направляем пассажиров по лифтам с учетом секторов
                if (e.getFloor().equals(1)) {
                    if (readyStates.contains(elevatorState)) {
                        for (Passenger p : myPassengers) {
                            PassangerState passangerState = PassangerState.getByInt(p.getState());
                            EnumSet<PassangerState> initialStates = EnumSet.of(WAITING_FOR_ELEVATOR, RETURNING);

                            if (initialStates.contains(passangerState) && channeling.get(e.getId()).contains(p.getDestFloor())) {
                                p.setElevator(e);
                            }
                        }
                    }

                    //Если пассажиров для данного лифта больше не будет и все сели, едем на ближайший этаж
                    if (myPassengers.size() == Constants.MAX_PASSANGERS) {
                        List<Passenger> walkingPassangers = myPassengers.stream()
                                .filter(passenger -> passenger.getElevator() != null
                                        && passenger.getElevator().equals(e.getId())
                                        && !passenger.getState().equals(USING_ELEVATOR.getInt()))
                                .collect(Collectors.toList());

                        //Если все сели в лифт, то поехали
                        if (walkingPassangers.size() == 0) {
                            e.goToFloor(channeling.get(e.getId()).first());
                        }
                    }
                    // Если высадил всех на этаже, продолжить
                } else if (elevatorState == FILLING) {
                    List<Passenger> exitingInElevator = myPassengers.stream()
                            .filter(passenger -> passenger.getElevator()!= null
                                    && passenger.getElevator().equals(e.getId()))
                            .filter(passenger -> passenger.getFloor().equals(e.getFloor())
                                    && !passenger.getState().equals(EXITING.getInt())
                                    && !passenger.getState().equals(USING_ELEVATOR.getInt()))
                            .collect(Collectors.toList());

                    //Если все вышли, едем на следующий этаж
                    if (exitingInElevator.size() == 0) {
                        Integer nextFloor = getNextFloor(e);
                        if (!nextFloor.equals(e.getFloor()))
                            e.goToFloor(nextFloor);
                    }
                }
            }
        }catch (Exception e){
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private Integer getNextFloor(Elevator elevator){
        for (Integer floor: channeling.get(elevator.getId())) {
            if (floor > elevator.getFloor())
                return floor;
        }
        return channeling.get(elevator.getId()).last();
    }

    private void initChannelling(List<Elevator> myElevators) {
        playerType = getPlayer(myElevators);

        if (playerType == PlayerType.FIRST_PLAYER) {
            channeling.put(1, initFloors(2, 3, 4));
            channeling.put(3, initFloors(5, 6));
            channeling.put(5, initFloors(7, 8));
            channeling.put(7, initFloors(9, 10));
            channeling.put(9, initFloors(11, 12));
            channeling.put(11, initFloors(13, 14));
            channeling.put(13, initFloors(15, 16));
        } else {
            channeling.put(2, initFloors(2, 3, 4));
            channeling.put(4, initFloors(5, 6));
            channeling.put(6, initFloors(7, 8));
            channeling.put(8, initFloors(9, 10));
            channeling.put(10, initFloors(11, 12));
            channeling.put(12, initFloors(13, 14));
            channeling.put(14, initFloors(15, 16));
        }
    }

    private TreeSet<Integer> initFloors(Integer... floors) {
        return new TreeSet<>(Arrays.asList(floors));
    }

    private PlayerType getPlayer(List<Elevator> myElevators) {
        Elevator firstElevator = myElevators.get(0);
        switch (firstElevator.getType()) {
            case "FIRST_PLAYER":
                return PlayerType.FIRST_PLAYER;
            case "SECOND_PLAYER":
                return PlayerType.SECOND_PLAYER;
            default:
                System.err.println("Не определён тип лифта");
                return null;
        }
    }

    private Elevator getById(List<Elevator> elevators, Integer id) {
        for (Elevator el : elevators) {
            if (el.getId().equals(id))
                return el;
        }
        return null;
    }
}
