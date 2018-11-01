package core.user.alvas;

import core.API.Elevator;
import core.API.Passenger;
import core.BaseStrategy;

import java.util.*;

public class UpDownStrategy extends BaseStrategy {
    Map<Integer, TreeSet<Integer>> elevatorInfo; // сейчас null, но при первом вызове его инициализируем. list[0] содержит направление
    Map<Integer, Integer> direction; // сейчас null, но при первом вызове его инициализируем. 1 вверх, -1 вниз, 0 свободный

    public void onTick(List<Passenger> myPassengers, List<Elevator> myElevators, List<Passenger> enemyPassengers, List<Elevator> enemyElevators) {
        // первый вызов!!! Инициализируем нашу мапу
        if (elevatorInfo == null ) {
            elevatorInfo = new HashMap<>();
            direction = new HashMap<>();
            for (Elevator e : myElevators) {
                elevatorInfo.putIfAbsent(e.getId(), new TreeSet<>());
                direction.put(e.getId(), 0);    // ожидаем
            }
        }


        Collections.sort(myElevators,(e1, e2)->e1.getId()-e2.getId() );
        for (Elevator e: myElevators) {
            if (e.getState() != 3) {continue;}

            for (Passenger p : myPassengers) {
                // пропустим все, что нам не по пути
                int d = p.getDestFloor() > p.getFloor()? 1: -1;
                // разные напрвления
                if (d == 1 && direction.get(e.getId()) == -1) { continue; }
                if (d == -1 && direction.get(e.getId()) == 1) { continue; }
                //направления одинаковые, но уже проскочили
                if (d == 1 && direction.get(e.getId()) == 1 && e.getFloor() > p.getFloor()) {continue;}
                if (d == -1 && direction.get(e.getId()) == 1 && e.getFloor() < p.getFloor()) {continue;}
                // если ехать больше чем можно тиков, то не поедем
                if (getTravelTimeToPassenger(e, p) > p.getTimeToAway()) { continue; }
                // если к нам идет пассажир то ждем
                if (e.getId() == p.getElevator()) {continue;}
                 // если на этом этаже, есть место, и полно народу, то приглашаем
                if (p.getState() == 1 && e.getPassengers().size() < 20 && e.getFloor() == p.getFloor()) {
                    elevatorInfo.get(e.getId()).add(p.getDestFloor());
                    p.setElevator(e);
                    continue;
                }
                // если стоим просто так, то едем
                Passenger passenger = findNearestPassenger(myPassengers, e);
                if ( passenger != null) {
                    e.goToFloor(passenger.getFloor());
                    direction.put(e.getId(), p.getFloor() > e.getFloor()? 1: -1);
                } else {

                }
            }

        }
    }

    private Passenger findNearestPassenger(List<Passenger> passengers, Elevator elevator) {
        int min = Integer.MAX_VALUE;
        Passenger result = null;
        for (Passenger p: passengers) {
            if (Math.abs(elevator.getFloor() - p.getFloor()) < min) {
                min = Math.abs(elevator.getFloor() - p.getFloor());
                result = p;
            }
        }
        return result;
    }


    private int getTravelTimeToPassenger(Elevator e, Passenger p){
        int time = 0;
        int d = p.getDestFloor() > p.getFloor()? 1: -1;
        if (e.getFloor() == p.getFloor()) { // мы на одном этаже
            return 50; // время пока дотопает
        }
        if (e.getFloor() > p.getFloor()) {
            for (int i = p.getFloor(); i <= e.getFloor(); i++) {
                if(elevatorInfo.get(e.getId()).contains(i)) {
                    time += 100 + /*среднее время пешком до лифта*/
                            100 + /*открыть дверь*/
                            100 + /*закрыть дверь*/
                            40  /*надо ждать не менее этого времени на каждом этаже*/;
                    continue;
                }
                time += 50;
            }
        } else {
            for (int i = e.getFloor(); i <= p.getFloor() ; i++) {
                if(elevatorInfo.get(e.getId()).contains(i)) {
                    time += 100 + /*среднее время пешком до лифта*/
                            100 + /*открыть дверь*/
                            100 + /*закрыть дверь*/
                            40  /*надо ждать не менее этого времени на каждом этаже*/;
                    continue;
                }
                time += (int)(1/e.getSpeed());
            }
        }
        return time;
    }

}
