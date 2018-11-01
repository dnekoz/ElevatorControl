package core.user.alvas;

import core.API.Passenger;

import java.util.LinkedList;
import java.util.List;

public class EvalatorTask {

    private List<Task> queue = new LinkedList();
    private int workTime;

    private int calcTime(List<Task> localQueue) {
        return localQueue.stream().mapToInt(e ->
        {
            // на этаже не стоим
            if (e.getDirection() == -1) {return 50;}
            if (e.getDirection() == 1 ) { return (int)(50 * e.getWeight()*(e.getPassengers().size() >= 10? 1.1: 1.0)); }
            // на этаже стоим
            return 100 + /*среднее время пешком до лифта*/
                    100 + /*открыть дверь*/
                    100 + /*закрыть дверь*/
                    40  /*надо ждать не менее этого времени на каждом этаже*/;
        }
        ).sum();
    }

    public int checkWorkTime(List<Task> localQueue, Passenger passenger) {
        // указатель на место в очереди
        int counter = 0;
        // рассматриваем 2 направления
        int direction = passenger.getFloor() > passenger.getDestFloor() ? 1 : 2;
        // пассажир хочет вниз
        if (direction == 1) {
            // ищем первую остановку лифта по пути вниз
            while(counter < localQueue.size()) {
                if(localQueue.get(counter).getDirection() != 10 && localQueue.get(counter).getDirection() != 0) {
                    counter++;
                    continue;
                }
            }
        } else { // пассажир хочет верх
            // ищем первую остановку лифта по пути dth[
            while(counter < localQueue.size()) {
                if(localQueue.get(counter).getDirection() != 20 && localQueue.get(counter).getDirection() != 0) {
                    counter++;
                    continue;
                }
            }
        }
        if (counter > 10) { // не имеет смысла гнаться за ним. через 500 тикетов он убежит
            return Integer.MAX_VALUE;
        }

        return 0;


    }

    private void fillTask(List<Task> localQueue, Passenger passenger, int counter) {

        for (int i = passenger.getFloor(); i <= passenger.getDestFloor() ; i++) {
            counter++;
            if(localQueue.get(counter).getFloor() == i){ // этаж совпадает!
                localQueue.get(counter).getPassengers().add(passenger);
            }
            try {
                localQueue.add(counter, new Task());
            } catch (IndexOutOfBoundsException ioobe) {
                localQueue.add(new Task());
            }
        }

        localQueue.get(counter).setDirection(1);

    }




}
