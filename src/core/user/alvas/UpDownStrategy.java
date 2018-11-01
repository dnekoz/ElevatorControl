package core.user.alvas;

import core.API.Elevator;
import core.API.Passenger;
import core.BaseStrategy;

import java.util.*;
import java.util.stream.Collectors;

public class UpDownStrategy extends BaseStrategy {
    Map<Integer, Integer> direction;                // сейчас null, но при первом вызове его инициализируем. 1 вверх, -1 вниз, 0 свободный

    private int tick = 0;
    public void onTick(List<Passenger> myPassengers, List<Elevator> myElevators, List<Passenger> enemyPassengers, List<Elevator> enemyElevators) {
        if (direction == null) {
            direction = new HashMap<>();
            for (Elevator e : myElevators) { direction.put(e.getId(), 1); }
        }

        System.out.println("tick: " + ++tick);
        myElevators.forEach(t -> System.out.print(t.getState() + " "));
        System.out.println(" ");

        for (Elevator e : myElevators) {

            // лифт занят
            if (e.getState() != 3) {continue;}

            //если уперлись в границы
            if (direction.get(e.getId()) > 0 && e.getFloor() == 16) {
                direction.put(e.getId(), -1);
                continue;
            }
            if (direction.get(e.getId()) < 0 && e.getFloor() == 1) {
                direction.put(e.getId(), 1);
                continue;
            }

            // в лифте полно народу и уже долго стоим
            if (e.getPassengers().size() > 15 || e.getTimeOnFloor() > 500) {
                if (direction.get(e.getId()) > 0) {
                    e.goToFloor(e.getPassengers().stream().mapToInt(t -> t.getDestFloor()).min().orElse(16));
                    continue;
                } else {
                    e.goToFloor(e.getPassengers().stream().mapToInt(t -> t.getDestFloor()).max().orElse(1));
                    continue;
                }
            }

            // к лифту кто-то идет то ждем
            if (myPassengers.stream().filter(t -> (t.getState() == 2) && (t.getElevator() == e.getId())).count() != 0) { continue; }


            // если лифт двигается вверх
            if (direction.get(e.getId()) > 1) {
                // если на этаже кто-то еще хочет вверх то забираем
                List<Passenger> result = myPassengers.stream().filter(t -> t.getState() == 1 && t.getFromFloor() == e.getFloor() && t.getDestFloor() > t.getFromFloor()).collect(Collectors.toList());
                if (result != null && result.size() > 0) {
                    // забираем первого и уезжаем
                    result.get(0).setElevator(e);
                    continue;
                }
                // если есть кого везти вверх, то везем в лифте или выше
                result = myPassengers.stream().filter(t -> t.getState() == 1 && t.getFromFloor() > e.getFloor() && t.getDestFloor() > t.getFromFloor()).collect(Collectors.toList());
                int otherMin = result.stream().mapToInt(t -> t.getFromFloor()).min().orElse(17);
                int myMin = e.getPassengers().stream().mapToInt(t -> t.getDestFloor()).min().orElse(17);
                int min = Math.min(otherMin, myMin);
                if (min > 16) {
                    result = myPassengers.stream().filter(t -> t.getState() == 1 && t.getFromFloor() > e.getFloor() && t.getDestFloor() < t.getFromFloor()).collect(Collectors.toList());
                    if (result != null && result.size() > 0) {
                        e.goToFloor(result.stream().mapToInt(t -> t.getFromFloor()).max().orElse(16));
                    }
                    direction.put(e.getId(), -1);
                    continue;
                }
                e.goToFloor(min);
            } else {
                // если на этаже кто-то еще хочет вниз то забираем
                List<Passenger> result = myPassengers.stream().filter(t -> t.getState() == 1 && t.getFromFloor() == e.getFloor() && t.getDestFloor() < t.getFromFloor()).collect(Collectors.toList());
                if (result != null && result.size() > 0) {
                    result.get(0).setElevator(e);
                    continue;
                }
                // если естького везти вниз, то везем в лифте или ниже
                result = myPassengers.stream().filter(t -> t.getState() == 1 && t.getFromFloor() < e.getFloor() && t.getDestFloor() < t.getFromFloor()).collect(Collectors.toList());
                int otherMax = result.stream().mapToInt(t -> t.getFromFloor()).max().orElse(0);
                int myMax = e.getPassengers().stream().mapToInt(t -> t.getDestFloor()).max().orElse(0);
                int max = Math.max(otherMax, myMax);
                if (max < 1) {
                    result = myPassengers.stream().filter(t -> t.getState() == 1 && t.getFromFloor() < e.getFloor() && t.getDestFloor() > t.getFromFloor()).collect(Collectors.toList());
                    if (result != null && result.size() > 0) {
                        e.goToFloor(result.stream().mapToInt(t -> t.getFromFloor()).min().orElse(16));
                    }
                    direction.put(e.getId(), 1);
                    continue;
                }
                e.goToFloor(max);
            }

        }

    }

}
