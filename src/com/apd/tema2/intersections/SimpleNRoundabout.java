package com.apd.tema2.intersections;

import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.Semaphore;

public class SimpleNRoundabout implements Intersection {

	private Semaphore semaphore;
	private long traversalTime;

	public SimpleNRoundabout(int capacity, long traversalTime) {
		semaphore = new Semaphore(capacity);
		this.traversalTime = traversalTime;
	}

	@Override
	public void acceptCar(Car car) throws InterruptedException {
		int carId = car.getId();
		System.out.format("Car %d has reached the roundabout, now waiting...\n", carId);

		semaphore.acquire();
		System.out.format("Car %d has entered the roundabout\n", carId);

		Thread.sleep(traversalTime);
		System.out.format("Car %d has exited the roundabout after %d seconds\n", carId, (int) traversalTime / 1000);

		semaphore.release();
	}

}
