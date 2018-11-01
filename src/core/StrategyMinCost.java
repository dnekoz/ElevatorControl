package core;

import core.API.Elevator;
import core.API.Passenger;
import core.elevator.ElevatorExpensesAnalyserBean;

import java.util.*;
import java.util.stream.Collectors;

import static core.ElevatorState.FILLING;
import static core.ElevatorState.OPENING;
import static core.PassangerState.RETURNING;
import static core.PassangerState.USING_ELEVATOR;
import static core.PassangerState.WAITING_FOR_ELEVATOR;

public class StrategyMinCost extends BaseStrategy {
    private int tick = 0;
    ElevatorExpensesAnalyser analyser = new ElevatorExpensesAnalyserBean();

    public void onTick(List<Passenger> myPassengers, List<Elevator> myElevators, List<Passenger> enemyPassengers, List<Elevator> enemyElevators) {
        try {
            System.out.println("StrategyMinCost: tickNo: " + ++tick);

            for (Passenger p : myPassengers) {

                PassangerState passangerState = PassangerState.getByInt(p.getState());
                EnumSet<PassangerState> initialStates = EnumSet.of(WAITING_FOR_ELEVATOR, RETURNING);

                if (initialStates.contains(passangerState)) {
                    TreeMap<Integer, Elevator> bestElevator = new TreeMap<Integer, Elevator>();
                    for (Elevator e : myElevators) {
                        if (e.getFloor().equals(p.getFromFloor())) {
                            p.setElevator(e);
                            break;
                        }

                        ElevatorState elevatorState = ElevatorState.getByInt(e.getState());
                        EnumSet<ElevatorState> readyStates = EnumSet.of(FILLING, OPENING);

                        if (readyStates.contains(elevatorState)) {    // Для всех лифтов не в движении считаем стоимость
                            bestElevator.put(analyser.calculateExpenses(e, p.getFromFloor(), p.getDestFloor(), p), e);
                        }
                    }

                    if (!bestElevator.isEmpty()) {
                        Elevator el = bestElevator.firstEntry().getValue();
                        if (p.getElevator() == null) {
                            // Тут отправляем лифт
                            p.setElevator(el);
                        }

                    }
                    for (Elevator elevator:myElevators) {
                        List<Passenger> walkingPassangers = myPassengers.stream()
                                .filter(passenger -> passenger.getElevator() != null
                                        && passenger.getElevator().equals(elevator.getId())
                                        && !passenger.getState().equals(USING_ELEVATOR.getInt()))
                                .collect(Collectors.toList());

                        if (elevator.getPassengers().size() >= Constants.ELEVATOR_CAPACITY
                                || walkingPassangers.size() == 0) {
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
}
