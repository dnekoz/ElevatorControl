package core;

import core.API.Elevator;
import core.API.Passenger;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

import static core.ElevatorState.FILLING;
import static core.ElevatorState.OPENING;
import static core.PassangerState.*;

public class Strategy extends BaseStrategy {
    private int tick = 0;
    private static final Map<Integer, TreeSet<Integer>> channeling = new HashMap<>();
    private PlayerType playerType;
    private final BaseStrategy strategy = new StrategyMinCost();
    private static final Set<Elevator> freeElevators = new HashSet<>();

    public void onTick(List<Passenger> myPassengers, List<Elevator> myElevators, List<Passenger> enemyPassengers, List<Elevator> enemyElevators) {

        if (tick > 3100) {
            strategy.onTick(myPassengers, myElevators, enemyPassengers, enemyElevators);
            return;
        }

        try {

            if (playerType == null) {
                initChannelling(myElevators);
            }

            System.out.println(this.getClass().getName() + " " + playerType.toString() + " tick: " + ++tick);

            List<Passenger> readyEnemyPassangers = enemyPassengers.stream()
                    .filter(passenger -> passenger.getState().equals(PassangerState.RETURNING.getInt()))
                    .collect(Collectors.toList());
            List<Passenger> allPassangers = new ArrayList<>();
            allPassangers.addAll(myPassengers);
            allPassangers.addAll(readyEnemyPassangers);

            for (Elevator e : myElevators) {

                if (!freeElevators.contains(e) && e.getPassengers().size() == Constants.ELEVATOR_CAPACITY) {
                    e.goToFloor(channeling.get(e.getId()).first());
                }

                ElevatorState elevatorState = ElevatorState.getByInt(e.getState());
                EnumSet<ElevatorState> readyStates = EnumSet.of(FILLING, OPENING);

                // Направляем пассажиров по лифтам с учетом секторов
                if (!freeElevators.contains(e) && e.getFloor().equals(1)) {
                    if (readyStates.contains(elevatorState)) {

                        for (Passenger p : allPassangers) {
                            PassangerState passangerState = PassangerState.getByInt(p.getState());
                            EnumSet<PassangerState> initialStates = EnumSet.of(WAITING_FOR_ELEVATOR, RETURNING);

                            if (initialStates.contains(passangerState) && channeling.get(e.getId()).contains(p.getDestFloor())) {
                                p.setElevator(e);
                            }
                        }
                    }

                    //Если пассажиров для данного лифта больше не будет и все сели, едем на ближайший этаж
                    if (myPassengers.size() == Constants.MAX_PASSANGERS ) {
                        List<Passenger> walkingPassangers = allPassangers.stream()
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
                    List<Passenger> exitingInElevator = allPassangers.stream()
                            .filter(passenger -> passenger.getElevator() != null
                                    && passenger.getElevator().equals(e.getId()))
                            .filter(passenger -> passenger.getFloor().equals(e.getFloor())
                                    && !passenger.getState().equals(EXITING.getInt())
                                    && !passenger.getState().equals(USING_ELEVATOR.getInt()))
                            .collect(Collectors.toList());

                    //Если все вышли, едем на следующий этаж
                    if (exitingInElevator.size() == 0) {
                        Integer nextFloor = getNextFloor(e);
                        if (!nextFloor.equals(e.getFloor())) {
                            e.goToFloor(nextFloor);

                        } else { //Лифт освободился

                            freeElevators.add(e);
                            List<Passenger> freePassangers = myPassengers.stream()
                                    .filter(passenger -> passenger.getState().equals(WAITING_FOR_ELEVATOR.getInt())
                                            || passenger.getState().equals(RETURNING.getInt()))
                                    .collect(Collectors.toList());
                            if (freePassangers.size() > 0 && freeElevators.size() > 0) {
                                //Получаем команды от другой стратегии по свободным лифтам и пассажирам
                                strategy.onTick(freePassangers, new ArrayList<>(freeElevators), enemyPassengers, enemyElevators);

                                updateElevatorCommand(myElevators);
                                updatePassangersCommand(myPassengers, freePassangers);
                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void updatePassangersCommand(List<Passenger> myPassangers, List<Passenger> freePassangers) {
        for (Passenger passenger : freePassangers) {

            Integer passId = passenger.getId();
            Passenger myPassanger = getPassangerById(myPassangers, passId);
            List<JSONObject> messages = myPassanger.getMessages();

            if (messages != null)
                messages = passenger.getMessages();
        }
    }

    private void updateElevatorCommand(List<Elevator> myElevators) {
        for (Elevator elevator : freeElevators) {

            Integer elId = elevator.getId();
            Elevator myElevator = getElevatorById(myElevators, elId);
            List<JSONObject> messages = myElevator.getMessages();

            if (messages != null)
                messages = elevator.getMessages();
        }
    }

    private Integer getNextFloor(Elevator elevator) {
        for (Integer floor : channeling.get(elevator.getId())) {
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

    private Elevator getElevatorById(List<Elevator> elevators, Integer id) {
        for (Elevator el : elevators) {
            if (el.getId().equals(id))
                return el;
        }
        return null;
    }

    private Passenger getPassangerById(List<Passenger> passengers, Integer id) {
        for (Passenger pass : passengers) {
            if (pass.getId().equals(id))
                return pass;
        }
        return null;
    }
}
