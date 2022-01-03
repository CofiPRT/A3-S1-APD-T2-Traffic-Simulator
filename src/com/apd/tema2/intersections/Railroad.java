package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Railroad implements Intersection {

	private CyclicBarrier barrier = new CyclicBarrier(Main.carsNo, () -> System.out.format("The train has passed, cars can now proceed\n"));
	private Queue<Car> waitingQueue = new LinkedList<>();

	@Override
	public void acceptCar(Car car) throws InterruptedException, BrokenBarrierException {
		int carId = car.getId();
		int carLane = car.getStartDirection();

		// wait for the cars to reach the railroad
		synchronized (waitingQueue) {
			waitingQueue.offer(car);
			System.out.format("Car %d from side number %d has stopped by the railroad\n", carId, carLane);
		}

		// wait for the train to pass
		barrier.await();

		synchronized (waitingQueue) {
			// search for this specific car
			while (waitingQueue.peek().getId() != carId) {
				waitingQueue.wait();
			}

			// the first element of this queue is 100% this thread
			waitingQueue.poll();

			// traverse the railroad
			System.out.format("Car %d from side number %d has started driving\n", carId, carLane);
			waitingQueue.notifyAll();
		}


	}

}
