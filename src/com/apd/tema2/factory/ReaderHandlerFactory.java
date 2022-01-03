package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Pedestrians;
import com.apd.tema2.entities.ReaderHandler;
import com.apd.tema2.intersections.ComplexMaintenance;
import com.apd.tema2.intersections.Crosswalk;
import com.apd.tema2.intersections.PriorityIntersection;
import com.apd.tema2.intersections.Railroad;
import com.apd.tema2.intersections.SimpleMaintenance;
import com.apd.tema2.intersections.SimpleMaxXCarRoundabout;
import com.apd.tema2.intersections.SimpleNRoundabout;
import com.apd.tema2.intersections.SimpleSemaphore;
import com.apd.tema2.intersections.SimpleStrict1CarRoundabout;
import com.apd.tema2.intersections.SimpleStrictXCarRoundabout;

/**
 * Returneaza sub forma unor clase anonime implementari pentru metoda de citire din fisier.
 */
public class ReaderHandlerFactory {

    public static ReaderHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of them)
        // road in maintenance - 1 lane 2 ways, X cars at a time
        // road in maintenance - N lanes 2 ways, X cars at a time
        // railroad blockage for T seconds for all the cars
        // unmarked intersection
        // cars racing
        return switch (handlerType) {
            case "simple_semaphore" -> br -> {
            	Main.intersection = new SimpleSemaphore();
            };
            case "simple_n_roundabout" -> br -> {
            	String[] input = br.readLine().split(" ");

            	int capacity 		= Integer.parseInt(input[0]);
            	long traversalTime 	= Integer.parseInt(input[1]);

            	Main.intersection = new SimpleNRoundabout(capacity, traversalTime);
            };
            case "simple_strict_1_car_roundabout" -> br -> {
            	String[] input = br.readLine().split(" ");

            	int numberOfLanes 	= Integer.parseInt(input[0]);
            	long traversalTime 	= Integer.parseInt(input[1]);

            	Main.intersection = new SimpleStrict1CarRoundabout(numberOfLanes, traversalTime);
            };
            case "simple_strict_x_car_roundabout" -> br -> {
            	String[] input = br.readLine().split(" ");

            	int numberOfLanes 	= Integer.parseInt(input[0]);
            	long traversalTime 	= Integer.parseInt(input[1]);
            	int carsPerLane 	= Integer.parseInt(input[2]);

            	Main.intersection = new SimpleStrictXCarRoundabout(numberOfLanes, carsPerLane, traversalTime);
            };
            case "simple_max_x_car_roundabout" -> br -> {
            	String[] input = br.readLine().split(" ");

            	int numberOfLanes 	= Integer.parseInt(input[0]);
            	long traversalTime 	= Integer.parseInt(input[1]);
            	int carsPerLane 	= Integer.parseInt(input[2]);

            	Main.intersection = new SimpleMaxXCarRoundabout(numberOfLanes, carsPerLane, traversalTime);
            };
            case "priority_intersection" -> br -> {
            	String[] input = br.readLine().split(" ");

            	int highPriorityCarsCount 	= Integer.parseInt(input[0]);
            	int lowPriorityCarsCount 	= Integer.parseInt(input[1]);

            	Main.intersection = new PriorityIntersection(highPriorityCarsCount, lowPriorityCarsCount);
            };
            case "crosswalk" -> br -> {
            	String[] input = br.readLine().split(" ");

            	int totalExecutionTime 		= Integer.parseInt(input[0]);
            	int pedestrianPassCount 	= Integer.parseInt(input[1]);

            	Main.pedestrians = new Pedestrians(totalExecutionTime, pedestrianPassCount);
            	Main.intersection = new Crosswalk(totalExecutionTime, pedestrianPassCount);
            };
            case "simple_maintenance" -> br -> {
            	String[] input = br.readLine().split(" ");

            	int carsPerGroup = Integer.parseInt(input[0]);

            	Main.intersection = new SimpleMaintenance(carsPerGroup);
            };
            case "complex_maintenance" -> br -> {
            	String[] input = br.readLine().split(" ");

            	int finalLaneCount 		= Integer.parseInt(input[0]);
            	int initialLaneCount 	= Integer.parseInt(input[1]);
            	int carsPerGroup 		= Integer.parseInt(input[2]);

            	Main.intersection = new ComplexMaintenance(initialLaneCount, finalLaneCount, carsPerGroup);
            };
            case "railroad" -> br -> {
            	Main.intersection = new Railroad();
            };
            default -> null;
        };
    }

}
