package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class SimpleStrictXCarRoundabout implements Intersection {

	private Semaphore[] semaphores;
	private CyclicBarrier initialBarrier, groupBarrier;
	private long traversalTime;

	public SimpleStrictXCarRoundabout(int numberOfLanes, int carsPerLane, long traversalTime) {
		semaphores = new Semaphore[numberOfLanes];
		for (int i = 0; i < numberOfLanes; i++)
			semaphores[i] = new Semaphore(carsPerLane);

		initialBarrier = new CyclicBarrier(Main.carsNo);
		groupBarrier = new CyclicBarrier(numberOfLanes * carsPerLane);

		this.traversalTime = traversalTime;
	}

	@Override
	public void acceptCar(Car car) throws InterruptedException, BrokenBarrierException {
		int carId = car.getId();
		int carLane = car.getStartDirection();

		// wait for all these messages to print
		System.out.format("Car %d has reached the roundabout, now waiting...\n", carId);
		initialBarrier.await();

		// attempt to be in the first 'carsPerLane' cars at the roundabout
		semaphores[carLane].acquire();
		System.out.format("Car %d was selected to enter the roundabout from lane %d\n", carId, carLane);

		// wait until every lane has at least 'carsPerLane' cars
		groupBarrier.await();
		System.out.format("Car %d has entered the roundabout from lane %d\n", carId, carLane);

		// enter the roundabout
		Thread.sleep(traversalTime);
		System.out.format("Car %d has exited the roundabout after %d seconds\n", carId, (int) traversalTime / 1000);

		// release the following semaphores at the same time
		groupBarrier.await();

		// allow other cars to enter the roundabout
		semaphores[carLane].release();
	}

}
