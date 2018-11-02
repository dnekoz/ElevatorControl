package core;

import core.API.Passenger;
import core.Constants;

/**
 * Анализатор стоимости движения лифта
 */
public class MovementExpensesAnalyser {

    private static final double EMPTY_ELEVATOR_SPEED_PER_TICK = 1.0d/(double)(long)Constants.TIME_TO_FLOOR;

    /**
     * Рассчитывает стоимость движения лифта с пассажирами от текущего положения до целевого
     * этажа. Рассчитывает ТОЛЬКО стоимость движения.
     *
     * @param currentElevatorPosition текущая позиция лифта
     * @param desiredDestinationFloorNumber целевой этаж
     * @param passengers пассажиры, вес которых требуется учесть
     * @return количество тиков на перемещение (только движение) с текущей позиции
     *      до целевого этажа
     */
    public int calculateMovementTime(
            double currentElevatorPosition,
            int desiredDestinationFloorNumber,
            Passenger... passengers) {
        double speed = calculateSpeedPerTick(passengers);
        double distance = calculateDistance(currentElevatorPosition, desiredDestinationFloorNumber);
        return (int) Math.ceil(distance / speed);
    }

    double calculateSpeedPerTick(Passenger... passengers) {
        double speed = EMPTY_ELEVATOR_SPEED_PER_TICK;
        if (passengers != null) {
            for (Passenger passenger : passengers) {
                speed /= passenger.getWeight();
            }
            if (passengers.length > 10) {
                speed /= 1.1d;
            }
        }
        return speed;
    }

    double calculateDistance(double currentElevatorPosition, int desiredDestinationFloorNumber) {
        return Math.abs(currentElevatorPosition - (double)(long)desiredDestinationFloorNumber);
    }
}
