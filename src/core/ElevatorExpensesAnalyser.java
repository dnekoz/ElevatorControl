package core;

import core.API.Elevator;

/**
 * Рассчитывает стоимость действий лифта
 */
public interface ElevatorExpensesAnalyser {

    /**
     * Рассчитывает стоимость перемещения лифта до заданного этажа {@code desiredDestinationFloorNumber}
     * единицах времени (в тиках)
     * Учитывает текущее состояние лифта (открыты/закрыты ли двери и т.п.)
     * Учитывает необходимое время на открывание дверей в точке назначения
     *
     * @param elevator лифт, для которого будет произведён рассчёт
     * @param desiredDestinationFloorNumber целевой этаж, количество тиков до которого считается
     * @return количество тиков
     */
    int calculateExpenses(Elevator elevator, int desiredDestinationFloorNumber);
}
