package core;

import core.API.Elevator;
import core.API.Passenger;

import java.util.*;
import java.util.stream.Collectors;

import static core.ElevatorState.*;
import static core.ElevatorState.FILLING;
import static core.ElevatorState.OPENING;
import static core.PassangerState.*;

public class StrategyMinCost extends BaseStrategy {
    private int tick = 0;
    ElevatorExpensesAnalyser analyser = new ElevatorExpensesAnalyserBean();
    Set<Passenger> busyPassengers = new HashSet<>();
    Set<Elevator> busyElevators = new HashSet<>();


    public void onTick(List<Passenger> myPassengers, List<Elevator> myElevators, List<Passenger> enemyPassengers, List<Elevator> enemyElevators) {
        try {
            System.out.println("StrategyMinCost: tickNo: " + ++tick);

            List<Passenger> readyEnemyPassangers = enemyPassengers.stream()
                    .filter(passenger -> passenger.getState().equals(PassangerState.RETURNING.getInt()))
                    .collect(Collectors.toList());


            for (Elevator elevator: myElevators) {
                ElevatorState elevatorState = ElevatorState.getByInt(elevator.getState());
                if (elevatorState == FILLING) {
                    List<Passenger> exitingInElevator = myPassengers.stream()
                            .filter(passenger -> passenger.getElevator() != null
                                    && passenger.getElevator().equals(elevator.getId()))
                            .filter(passenger -> passenger.getFloor().equals(elevator.getFloor())
                                    && !passenger.getState().equals(EXITING.getInt())
                                    && !passenger.getState().equals(USING_ELEVATOR.getInt()))
                            .collect(Collectors.toList());

                    //Если все вышли, едем на следующий этаж
                    if (exitingInElevator.size() == 0) {
                        if (!elevator.getPassengers().isEmpty()) {
                            Integer nextFloor = getNearestPassenger(elevator).getDestFloor();
                            elevator.goToFloor(nextFloor);
                            busyElevators.add(elevator);
                        }else {
                            busyElevators.remove(elevator);
                        }
                    }
                }
            }

            List<Passenger> allPassangers = new ArrayList<>();
            allPassangers.addAll(myPassengers);
            allPassangers.addAll(readyEnemyPassangers);

            for (Passenger p : allPassangers) {

                PassangerState passangerState = PassangerState.getByInt(p.getState());
                EnumSet<PassangerState> initialStates = EnumSet.of(WAITING_FOR_ELEVATOR, RETURNING);

                if (initialStates.contains(passangerState)) {
                    TreeMap<Integer, Elevator> bestElevator = new TreeMap<Integer, Elevator>();
                    for (Elevator e : myElevators) {
                        if (e.getFloor().equals(p.getFromFloor())) {
                            p.setElevator(e);
                            busyPassengers.remove(p);
                            break;
                        } else
                            busyPassengers.add(p);

                        ElevatorState elevatorState = ElevatorState.getByInt(e.getState());
                        EnumSet<ElevatorState> readyStates = EnumSet.of(FILLING, OPENING);

                        if (readyStates.contains(elevatorState) && !busyPassengers.contains(p)) {    // Для всех лифтов не в движении считаем стоимость
                            bestElevator.put(analyser.calculateExpenses(e, p.getFromFloor(), p.getDestFloor(), p), e);
                        }
                    }

                    if (!bestElevator.isEmpty()) {
                        for (Elevator el: bestElevator.values()){
                            ElevatorState state = ElevatorState.getByInt(el.getState());
//                            if (EnumSet.of(FILLING, MOVING, CLOSING).contains(state)){
                            if (EnumSet.of(FILLING, MOVING, CLOSING).contains(state)){
                                continue;
                            }
                            p.setElevator(el);

                        }
                    }
                    for (Elevator elevator : myElevators) {
                        if (elevator.getPassengers().size() > 0
                                && getElevatorLoad(elevator, myPassengers) >= Constants.ELEVATOR_CAPACITY){
                            Passenger nearest = getNearestPassenger(elevator);

                            elevator.goToFloor(nearest.getDestFloor());
                        }
                        ElevatorState state = ElevatorState.getByInt(elevator.getState());
                        if (EnumSet.of(FILLING, MOVING, CLOSING).contains(state)){
                            continue;
                        }
                        List<Passenger> walkingPassangers = myPassengers.stream()
                                .filter(passenger -> passenger.getElevator() != null
                                        && passenger.getElevator().equals(elevator.getId())
                                        && !passenger.getState().equals(USING_ELEVATOR.getInt()))
                                .collect(Collectors.toList());

                        if (elevator.getPassengers().size() >= Constants.ELEVATOR_CAPACITY
                                || (walkingPassangers.size() == 0 && elevator.getPassengers().contains(p))) {
                            elevator.goToFloor(p.getDestFloor());
                        }
                    }
                }
            }
//            if (e.getPassengers().size() > 0 && e.getState() != 1) {
//                e.goToFloor(e.getPassengers().get(0).getDestFloor());
//            }
//
//        }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Passenger getNearestPassenger(Elevator elevator) {
        Passenger nearest = elevator.getPassengers().get(0);
        for (Passenger pas : elevator.getPassengers()){
            if (Math.abs(pas.getDestFloor() - elevator.getFloor()) <= Math.abs(nearest.getDestFloor() - elevator.getFloor()))
                nearest = pas;
        }
        return nearest;
    }

    private int getElevatorLoad(Elevator e, List<Passenger> passengers){
            List<Passenger> elevatorPassengers = passengers.stream()
                    .filter((passenger -> passenger.getElevator()!= null
                            && passenger.getElevator().equals(e.getId())))
                    .collect(Collectors.toList());
            return elevatorPassengers.size();
    }
}
