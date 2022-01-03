package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;
import com.apd.tema2.entities.Pedestrians;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Crosswalk implements Intersection {

	private volatile boolean hadGreenLight = false;

	private CyclicBarrier barrier = new CyclicBarrier(Main.carsNo);

	public Crosswalk(int totalExecutionTime, int pedestrianPassCount) {

	}

	@Override
	public void acceptCar(Car car) throws InterruptedException, BrokenBarrierException {
		int carId = car.getId();

		while (!Main.pedestrians.isFinished()) {
			if (Main.pedestrians.isPass()) {
				System.out.format("Car %d has now red light\n", carId);

				synchronized (Pedestrians.lockObject) {
					Pedestrians.lockObject.wait();
				}

				hadGreenLight = false;
			}

			if (!hadGreenLight) {
				barrier.await();

				System.out.format("Car %d has now green light\n", carId);
				hadGreenLight = true;
			}
		}

	}

}
