package core.elevator;

import core.API.Elevator;
import core.API.Passenger;
import core.ElevatorExpensesAnalyser;
import core.ElevatorState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static core.ElevatorState.*;

public class ElevatorExpensesAnalyserBean implements ElevatorExpensesAnalyser {

    private final MovementExpensesAnalyser movementExpensesAnalyser;
    private final FillingExpensesAnalyser fillingExpensesAnalyser;

    /**
     * Стандартный конструктор
     */
    public ElevatorExpensesAnalyserBean() {
        movementExpensesAnalyser = new MovementExpensesAnalyser();
        fillingExpensesAnalyser = new FillingExpensesAnalyser();
    }

    /**
     * Конструктор для тестов
     */
    ElevatorExpensesAnalyserBean(MovementExpensesAnalyser movementExpensesAnalyser,
                                        FillingExpensesAnalyser fillingExpensesAnalyser) {
        this.movementExpensesAnalyser = movementExpensesAnalyser;
        this.fillingExpensesAnalyser = fillingExpensesAnalyser;
    }

    @Override
    public int calculateExpenses(Elevator elevator,
                                 int takePassengersFromFloor,
                                 int deliverPassengersToFloor,
                                 Passenger... passengers) {
        int result =  0;

        // завершаем текущее действие
        int timeToFinishCurrentAction = timeToFinishCurrentAction(elevator);
        result += timeToFinishCurrentAction;

        // возвращаемся на этаж, откуда нужно забрать пассажиров (если надо)
        int timeToReturnFromTheNextFloor = timeToReturnFromTheNextFloor(elevator, takePassengersFromFloor);
        result += timeToReturnFromTheNextFloor;

        // время на посадку пассажиров
        int timeToFillTheElevator = fillingExpensesAnalyser.calculateFillingExpenses(
                elevator.getId(),
                passengers
        );
        if (!isActionFinished(elevator)) {
            timeToFillTheElevator = Math.max(requiresToWait(FILLING), timeToFillTheElevator);
        } else {
            timeToFillTheElevator = Math.max(
                    requiresToWait(FILLING),
                    elevator.getTimeOnFloor() - requiresToWait(OPENING) + timeToFillTheElevator
            );
        }
        result += timeToFillTheElevator;

        // поправка закрытия дверей и отправления при посадке пассажиров
        int extraFillingTime = requiresToWait(CLOSING, WAITING);
        result += extraFillingTime;

        // время чтобы довезти пассажиров до целевого этажа
        Passenger[] remainingPassengers = filterRemainingPassengers(
                elevator.getPassengers(),
                elevator.getNextFloor(),
                takePassengersFromFloor
        );
        result += movementExpensesAnalyser.calculateMovementTime(
                elevator.getY(),
                deliverPassengersToFloor,
                merge(remainingPassengers, passengers)
        );

        return result;
    }

    Passenger[] merge(Passenger[]... passengers) {
        List<Passenger> result = new ArrayList<>();
        for (Passenger[] array : passengers) {
            result.addAll(Arrays.asList(array));
        }
        return result.toArray(new Passenger[0]);
    }

    int timeToReturnFromTheNextFloor(Elevator elevator, int toFloor) {
        if (elevator.getNextFloor().equals(toFloor)) {
            return 0;
        }
        int result = 0;
        Passenger[] remainingPassengers = remainingPassengers(elevator.getPassengers(), elevator.getNextFloor());
        result += movementExpensesAnalyser.calculateMovementTime(
                    elevator.getY(),
                    toFloor,
                remainingPassengers
                );
            return result;
    }

    Passenger[] filterRemainingPassengers(List<Passenger> passengers, int... disembarkingFloors) {
        Set<Integer> excludeFloors = new HashSet<>();
        for (int floor : disembarkingFloors) {
            excludeFloors.add(floor);
        }
        List<Passenger> result = new ArrayList<>();
        for (Passenger passenger : passengers) {
            if (!excludeFloors.contains(passenger.getDestFloor())) {
                result.add(passenger);
            }
        }
        return result.toArray(new Passenger[0]);
    }

    Passenger[] remainingPassengers(List<Passenger> passengers, int disembarkingFloor) {
        return filterRemainingPassengers(passengers, disembarkingFloor);
    }

    int timeToFinishCurrentAction(Elevator elevator) {
        int result = 0;
        if (isActionFinished(elevator)) {
            return result;
        }
        if (elevator.getFloor().equals(elevator.getNextFloor()) &&
                stateOf(elevator).equals(OPENING)) {
            return OPENING.getRequiresAtLeastTicks() - elevator.getTimeOnFloor();
        }
        if (!elevator.getFloor().equals(elevator.getNextFloor())) {
            result += movementExpensesAnalyser.calculateMovementTime(
                        elevator.getY(),
                        elevator.getNextFloor(),
                        (Passenger[]) elevator.getPassengers().toArray()
            );
            result += timeToStartMoving(elevator);
        }

        return result;
    }

    boolean isActionFinished(Elevator elevator) {
        return (elevator.getFloor().equals(elevator.getNextFloor()) &&
                stateOf(elevator).equals(FILLING));
    }

    int timeToStartMoving(Elevator elevator) {
        if (stateOf(elevator).equals(MOVING)) {
            return 0;
        }
        if (stateOf(elevator).equals(WAITING)) {
            return requiresToWait(WAITING);
        }
        return requiresToWait(OPENING, FILLING, CLOSING, WAITING) - elevator.getTimeOnFloor();
    }

    int requiresToWait(ElevatorState... states) {
        int result = 0;
        for (ElevatorState state : states) {
            result += state.getRequiresAtLeastTicks();
        }
        return result;
    }

    ElevatorState stateOf(Elevator elevator) {
        return fromCode(elevator.getState());
    }
}
