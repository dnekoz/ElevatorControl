package core;

import core.API.Elevator;
import core.API.Passenger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StrategyMod extends BaseStrategy {
    private int tick = 0;

    @Override
    public void onTick(List<Passenger> myPassengers, List<Elevator> myElevators, List<Passenger> enemyPassengers, List<Elevator> enemyElevators) {
        String myName = myElevators.get(0).getType() == "FIRST_PLAYER"? "1st":"2nd";
        System.out.println(myName+":StratagyMod:tick: " + ++tick);

        if(tick == 2){
            myElevators.get(6).goToFloor(8);
            //myElevators.get(5).goToFloor(5);
        }
        if(tick > 1300){
            myElevators.get(6).goToFloor(1);
        }

        for (Elevator e : myElevators) {
            sendFullEvalator(e);
        }
        // Первый этап
        if (tick < 2200) {
            for (Passenger p : myPassengers) {
                //System.out.print("pas: "+p.getId()+" fl: " + p.getFloor() + " st: " + p.getState() + " el: " + p.getElevator());
                //if ( p.getFloor() < 4)

                for (Elevator e : myElevators) {
                    if((e.getId() != 1) || (e.getId() != 2)) {
                        System.out.print("eID="+e.getId());
                        if (e.getFloor() == p.getFloor()) {
                            if ((e.getState() == 3) || (e.getState() == 2)) {
                                if (e.getPassengers().size() < 20) {
                                    p.setElevator(e);
                                    break;
                                } else {
                                    sendEvalator(e);
                                }
                            }
                        }
                    }
                }
                //System.out.println("!!!!!!!!!NO BREAK!")
            }
         }

        else
        {
            for (Elevator e : myElevators) {
                sendEvalator(e);
            }
            for (Elevator e : myElevators) {
                for (Passenger p : myPassengers) {

                    if (p.getState() < 4) {
                        if (e.getState() != 1) {
                            e.goToFloor(p.getFromFloor());
                        }
                        if (e.getFloor() == p.getFromFloor()) {
                            p.setElevator(e);
                        }
                    }
                }
                if (e.getPassengers().isEmpty() && e.getState() != 1) {
                    e.goToFloor(e.getPassengers().get(0).getDestFloor());
                }

            }
        }
        for (Passenger p : enemyPassengers) {
            for (Elevator e : myElevators) {
                if((p.getFloor() == e.getFloor() &&(p.getState() < 5)) && (e.getState() == 3)){
                    p.setElevator(e);
                }
            }
        }

    }
    boolean sendEvalator(Elevator e){
        if ((e.getState() == 3) && (e.getPassengers().size() > 9)) {
            List<Integer> passengerFloors = new ArrayList();
            List<Passenger> evPassengers = e.getPassengers();
            for (Passenger p : evPassengers) {
                passengerFloors.add(p.getDestFloor());
            }
            Collections.sort(passengerFloors);
            System.out.println(passengerFloors.size());
            System.out.println(passengerFloors);
            System.out.println("min Floor: " + passengerFloors.get(0));
            e.goToFloor(passengerFloors.get(0));
            return true;
        }
        return false;
    }
    boolean sendFullEvalator(Elevator e) {
        if ((e.getState() == 3) && (e.getPassengers().size() > 19)) {
            List<Passenger> evPassengers = e.getPassengers();
            List<Integer> passengerFloors = new ArrayList();
            for (Passenger p : evPassengers) {
                passengerFloors.add(p.getDestFloor());
            }
            Collections.sort(passengerFloors);
            e.goToFloor(passengerFloors.get(0));
            System.out.println("\n***Full SEND!!!*** minFloors: "+passengerFloors.get(0));
            System.out.println("all: "+ Arrays.toString(passengerFloors.toArray()));
            return true;
        }
        if((e.getPassengers().size() > 19)){
            System.out.print("sendFullEvalator:state: "+e.getState());
        }
        return false;
    }
}
