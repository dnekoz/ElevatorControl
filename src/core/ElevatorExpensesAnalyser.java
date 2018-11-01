package core;

import core.API.Elevator;
import core.API.Passenger;

/**
 * Рассчитывает стоимость действий лифта
 */
public interface ElevatorExpensesAnalyser {

    /**
     * Рассчитывает стоимость перемещения лифта до заданного этажа {@code desiredDestinationFloorNumber}
     * единицах времени (в тиках)
     * Учитывает время, необходимое для завершения текущего действия
     * Учитывает текущее состояние лифта (открыты/закрыты ли двери и т.п.)
     * Учитывает необходимое время на открывание дверей в точке назначения
     *
     * @param elevator лифт, для которого будет произведён рассчёт
     * @param desiredDestinationFloorNumber целевой этаж, количество тиков до которого считается
     * @return количество тиков
     */
    int calculateExpenses(Elevator elevator, int desiredDestinationFloorNumber);

    /**
     * Рассчитывает стоимость перемещения лифта до заданного этажа {@code desiredDestinationFloorNumber}
     * единицах времени (в тиках)
     * Учитывает время, необходимое для завершения текущего действия
     * Учитывает текущее состояние лифта (открыты/закрыты ли двери и т.п.)
     * Учитывает необходимое время на открывание дверей в точке назначения
     *
     * @param elevator лифт, для которого будет произведён рассчёт
     * @param desiredDestinationFloorNumber целевой этаж, количество тиков до которого считается
     * @param passengers пассажиры, время на посадку и перемещение которых следует учесть дополнительно
     *                   к пассажирам уже находящемся в лифте
     * @return количество тиков
     */
    int calculateExpenses(Elevator elevator, int desiredDestinationFloorNumber, Passenger... passengers);
}
