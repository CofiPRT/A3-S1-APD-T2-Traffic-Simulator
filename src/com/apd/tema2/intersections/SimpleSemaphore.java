package com.apd.tema2.intersections;

import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

public class SimpleSemaphore implements Intersection {

	@Override
	public void acceptCar(Car car) throws InterruptedException {
		int carId = car.getId();
		System.out.format("Car %d has reached the semaphore, now waiting...\n", carId);

		Thread.sleep(car.getWaitingTime());
        System.out.format("Car %d has waited enough, now driving...\n", carId);
	}

}
