package com.apd.tema2.factory;

import static java.lang.Thread.sleep;

import com.apd.tema2.Main;
import com.apd.tema2.entities.IntersectionHandler;

/**
 * Clasa Factory ce returneaza implementari ale InterfaceHandler sub forma unor
 * clase anonime.
 */
public class IntersectionHandlerFactory {

    public static IntersectionHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of
        // them)
        // road in maintenance - 2 ways 1 lane each, X cars at a time
        // road in maintenance - 1 way, M out of N lanes are blocked, X cars at a time
        // railroad blockage for s seconds for all the cars
        // unmarked intersection
        // cars racing
        return switch (handlerType) {
            case "simple_semaphore" 				-> car -> Main.intersection.acceptCar(car);
            case "simple_n_roundabout" 				-> car -> Main.intersection.acceptCar(car);
            case "simple_strict_1_car_roundabout" 	-> car -> Main.intersection.acceptCar(car);
            case "simple_strict_x_car_roundabout" 	-> car -> Main.intersection.acceptCar(car);
            case "simple_max_x_car_roundabout" 		-> car -> {
            	try {
                    sleep(car.getWaitingTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } // NU MODIFICATI

                // Continuati de aici
                Main.intersection.acceptCar(car);
            };
            case "priority_intersection" 			-> car -> {
            	try {
                    sleep(car.getWaitingTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } // NU MODIFICATI

            	// Continuati de aici
            	Main.intersection.acceptCar(car);
            };
            case "crosswalk" 						-> car -> Main.intersection.acceptCar(car);
            case "simple_maintenance" 				-> car -> Main.intersection.acceptCar(car);
            case "complex_maintenance" 				-> car -> Main.intersection.acceptCar(car);
            case "railroad" 						-> car -> Main.intersection.acceptCar(car);
            default -> null;
        };
    }
}
