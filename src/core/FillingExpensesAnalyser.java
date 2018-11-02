package core;

import core.API.Passenger;

public class FillingExpensesAnalyser {

    public int calculateFillingExpenses(int elevatorId, Passenger... passengers) {
        if (passengers == null || passengers.length == 0) {
            return 0;
        }
        int elevatorX = getElevatorX(elevatorId);
        int maxWalkingTime = Integer.MIN_VALUE;

        for (Passenger passenger : passengers) {
            maxWalkingTime = Math.max(calculateWalkingTime(passenger, elevatorX), maxWalkingTime);
        }
        return maxWalkingTime;
    }

    private int calculateWalkingTime(Passenger passenger, int elevatorX) {
        double passengerX = passenger.getX();
        if (passengerX == 0.0d) {
            return Math.abs(elevatorX);
        }
        if(passengerX < 0) {
            return Math.abs((int)Math.round(passengerX * ElevatorPositionHolder.ELEVATOR_MIN_POSITION) - elevatorX);
        } else {
            return Math.abs((int)Math.round(passengerX * ElevatorPositionHolder.ELEVATOR_MAX_POSITION) - elevatorX);
        }
    }

    private int getElevatorX(int elevatorId) {
        return ElevatorPositionHolder.ELEVATOR_POSITION_BY_ID.get(elevatorId);
    }
}
