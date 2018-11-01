package core;

public final class Constants {

    public static final int FLOORS_COUNT = 16;
    public static final int ELEVATORS_COUNT = 14;
    public static final int PLAYER_ELEVATORS_COUNT = 7;
    public static final float AVERAGE_PASSANGER_WEIGHT = 1.02f;
    public static final int TIME_TO_FLOOR = 50;
    public static final int SCORE_BY_FLOOR = 10;

    //Через это кол-во тиков пасажир идёт по лестнице
    public static final int PASSANGER_WAITING_TIMEOUT = 500;

    //Скорость пассажира по лестнице вверх
    public static final int UP_STAIR_SPEED_PER_FLOOR = 200;

    //Скорость пассажира по лестнице вниз
    public static final int DOWN_STAIR_SPEED_PER_FLOOR = 100;

    //Пассажир гуляет на этаже до следующего вызова
    public static final int PASSANGER_WALKING_PAUSE = 500;

    //Время пассажира на выход из лифта
    public static final int PASSANGER_EXIT_ELEVATOR_TIME = 40;

    //Время на открывание + закрывание дверей лифта на этаже
    public static final int ELEVATOR_OPEN_CLOSE_TIME = 100;

    //Минимальное время которое лифт ожидает после открытия дверей
    public static final int ELEVATOR_MIN_FLOOR_WAITING_TIME = 40;

    //Время ожидания лифта после закрытия дверей перед движением
    public static final int ELEVATOR_WAITING_AFTER_DOORS_CLOSURE_TIME = 1;

    //Вместимость лифта
    public static final int ELEVATOR_CAPACITY = 20;

    //Расстояние центрального лифта от центра этажа (или 40 ед. от места ожидания своих пассажиров)
    public static final int CENTRAL_ELEVATOR_DISTANCE_FROM_ENTER = 60;

    //Расстояние центрального лифта от места ожидания своих пассажиров
    public static final int CENTRAL_ELEVATOR_DISTANCE_FROM_WAITING_POINT = 40;

    //Расстояние между точками входа между соседними лифтами
    public static final int DISTANCE_BETWEEN_ELEVATORS = 70;

    //Максимальное число своих пассажиров
    public static final int MAX_PASSANGERS = 100;

    private Constants(){}

}
