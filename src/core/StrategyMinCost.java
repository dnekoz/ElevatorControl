package core;

import core.API.Elevator;
import core.API.Passenger;
import core.elevator.ElevatorExpensesAnalyserBean;

import java.util.*;

public class StrategyMinCost extends BaseStrategy {
    private int tick = 0;
    ElevatorExpensesAnalyser analyser = new ElevatorExpensesAnalyserBean();

    public void onTick(List<Passenger> myPassengers, List<Elevator> myElevators, List<Passenger> enemyPassengers, List<Elevator> enemyElevators) {
        System.out.println("StrategyMinCost: tickNo: " + tick++);
        for (Passenger p : myPassengers) {
            if (p.getState() < 5) {            // Для всех пассажиров не в лифте ищем лифт с мин. стоимостью перевозки
                Map bestElevator = new TreeMap<Integer, Elevator>();
                for (Elevator e : myElevators) {
                    if (e.getFloor() == p.getFromFloor()) {
                        p.setElevator(e);
                        break;
                    }

                    if (e.getState() != 1) {    // Для всех лифтов не в движении считаем стоимость
                        bestElevator.put(analyser.calculateExpenses(e, p.getFromFloor(), p.getDestFloor()), e);
                    }
                }
                
                //p.setElevator((Elevator)((TreeMap) bestElevator).pollFirstEntry().getKey());
                // Тут отправляем лифт

            }
        }
//            if (e.getPassengers().size() > 0 && e.getState() != 1) {
//                e.goToFloor(e.getPassengers().get(0).getDestFloor());
//            }
//
//        }
    }
}
