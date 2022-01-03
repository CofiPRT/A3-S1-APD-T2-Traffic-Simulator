package com.apd.tema2.intersections;

import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Semaphore;

public class SimpleMaxXCarRoundabout implements Intersection {

	private Semaphore[] semaphores;
	private long traversalTime;

	public SimpleMaxXCarRoundabout(int numberOfLanes, int carsPerLane, long traversalTime) {
		semaphores = new Semaphore[numberOfLanes];
		for (int i = 0; i < numberOfLanes; i++)
			semaphores[i] = new Semaphore(carsPerLane);

		this.traversalTime = traversalTime;
	}

	@Override
	public void acceptCar(Car car) throws InterruptedException, BrokenBarrierException {
		int carId = car.getId();
		int carLane = car.getStartDirection();
		System.out.format("Car %d has reached the roundabout from lane %d\n", carId, carLane);

		// attempt to be in the first 'carsPerLane' cars at the roundabout
		semaphores[carLane].acquire();
		System.out.format("Car %d has entered the roundabout from lane %d\n", carId, carLane);

		// enter the roundabout
		Thread.sleep(traversalTime);
		System.out.format("Car %d has exited the roundabout after %d seconds\n", carId, (int) traversalTime / 1000);

		// allow other cars to enter the roundabout
		semaphores[carLane].release();
	}

}
