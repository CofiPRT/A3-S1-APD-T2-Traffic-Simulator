package com.apd.tema2.intersections;

import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Semaphore;

public class PriorityIntersection implements Intersection {

	private static final long highPriorityCarTraversalTime = 2000; // ms

	private Semaphore highPrioritySemaphore = new Semaphore(1);
	private Semaphore lowPrioritySemaphore = new Semaphore(1, true);
	private int highPriorityCarsInIntersection = 0;

	public PriorityIntersection(int highPriorityCarCount, int lowPriorityCarCount) {

	}

	@Override
	public void acceptCar(Car car) throws InterruptedException, BrokenBarrierException {
		int carId = car.getId();
		int carPriority = car.getPriority();

		if (carPriority > 1) {
			// this check is in a critical zone
			highPrioritySemaphore.acquire();

			if (++highPriorityCarsInIntersection == 1)
				lowPrioritySemaphore.acquire(); // don't allow low priority cars to pass

			highPrioritySemaphore.release();
			System.out.format("Car %d with high priority has entered the intersection\n", carId);

			Thread.sleep(highPriorityCarTraversalTime);
			System.out.format("Car %d with high priority has exited the intersection\n", carId);

			// this check is in a critical zone
			highPrioritySemaphore.acquire();

			if (--highPriorityCarsInIntersection == 0)
				lowPrioritySemaphore.release(); // allow low priority cars to pass

			highPrioritySemaphore.release();
		} else if (carPriority == 1) {
			// attempt to enter the intersection
			System.out.format("Car %d with low priority is trying to enter the intersection...\n", carId);

			lowPrioritySemaphore.acquire();
			System.out.format("Car %d with low priority has entered the intersection\n", carId);

			lowPrioritySemaphore.release();
		}
	}

}
