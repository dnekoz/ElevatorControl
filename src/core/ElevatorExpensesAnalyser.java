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
     * @param takePassengersFromFloor этаж, с которого нужно забрать пассажиров
     * @param deliverPassengersToFloor этаж, на который нужно доставить пассажиров
     * @return количество тиков
     */
    int calculateExpenses(Elevator elevator,
                          int takePassengersFromFloor,
                          int deliverPassengersToFloor,
                          Passenger... passengers);

}
