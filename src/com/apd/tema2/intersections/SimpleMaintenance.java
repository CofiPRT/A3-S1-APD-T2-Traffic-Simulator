package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class SimpleMaintenance implements Intersection {

	private Semaphore firstLaneSemaphore, secondLaneSemaphore;
	private CyclicBarrier bottleneckReachedBarrier, bottleneckPassedBarrier;

	public SimpleMaintenance(int carsPerGroup) {
		bottleneckReachedBarrier = new CyclicBarrier(Main.carsNo);
		bottleneckPassedBarrier = new CyclicBarrier(carsPerGroup);

		firstLaneSemaphore = new Semaphore(carsPerGroup);
		secondLaneSemaphore = new Semaphore(0);
	}

	@Override
	public void acceptCar(Car car) throws InterruptedException, BrokenBarrierException {
		int carId = car.getId();
		int carLane = car.getStartDirection();

		// wait for all the cars to reach the bottleneck
		System.out.format("Car %d from side number %d has reached the bottleneck\n", carId, carLane);
		bottleneckReachedBarrier.await();

		// cocktail shake the semaphores
		Semaphore ownSemaphore = carLane == 0 ? firstLaneSemaphore : secondLaneSemaphore;
		Semaphore oppositeSemaphore = carLane == 1 ? firstLaneSemaphore : secondLaneSemaphore;

		// allow 'carsPerGroup' cars to pass from this side
		ownSemaphore.acquire();
		System.out.format("Car %d from side number %d has passed the bottleneck\n", carId, carLane);

		// wait for them to group up
		bottleneckPassedBarrier.await();

		// prepare for the opposite side
		oppositeSemaphore.release();

	}

}
