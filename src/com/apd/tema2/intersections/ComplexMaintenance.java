package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ComplexMaintenance implements Intersection {

	private QueueHashMap<Integer, QueueHashMap<Integer, Queue<Car>>> newLanesQueue;
	private CyclicBarrier barrier = new CyclicBarrier(Main.carsNo);
	private int polledCars = 0;

	private int initialLaneCount;
	private int finalLaneCount;
	private int carsPerGroup;

	public ComplexMaintenance(int initialLaneCount, int finalLaneCount, int carsPerGroup) {
		newLanesQueue = new QueueHashMap<>();
		for (int finalLane = 0; finalLane < finalLaneCount; finalLane++) {

			int start = (int) (finalLane * (double) initialLaneCount / finalLaneCount);
			int end = Math.min((int) ((finalLane + 1) * (double) initialLaneCount / finalLaneCount), initialLaneCount);

			QueueHashMap<Integer, Queue<Car>> oldLanesQueue = new QueueHashMap<>();

			for (int initialLane = start; initialLane < end; initialLane++) {
				Queue<Car> lane = new LinkedList<>();

				oldLanesQueue.put(initialLane, lane);
			}

			newLanesQueue.put(finalLane, oldLanesQueue);
		}

		this.finalLaneCount = finalLaneCount;
		this.initialLaneCount = initialLaneCount;
		this.carsPerGroup = carsPerGroup;
	}

	@Override
	public void acceptCar(Car car) throws InterruptedException, BrokenBarrierException {
		int carId = car.getId();
		int oldLane = car.getStartDirection();
		int newLane = getNewLane(oldLane);

		// the cars are reaching the bottleneck
		synchronized (newLanesQueue.get(newLane).get(oldLane)) {
			newLanesQueue.get(newLane).get(oldLane).offer(car);
			System.out.format("Car %d has come from the lane number %d\n", carId, oldLane);
		}

		// wait for every car to reach the bottleneck
		barrier.await();

		synchronized (newLanesQueue) {
			QueueHashMap<Integer, Queue<Car>> currOldLanesQueue = null;
			Queue<Car> currOldLane = null;
			while (true) {
				currOldLanesQueue = newLanesQueue.getFirstValue();
				currOldLane = currOldLanesQueue.getFirstValue();
				if (currOldLane.peek().getId() == carId)
					break;

				newLanesQueue.wait();
			}

			// exit the waiting queue, enter the bottleneck
			polledCars++;
			currOldLane.poll();
			System.out.format("Car %d from the lane %d has entered lane number %d\n", carId, oldLane, newLane);

			// do operations
			if (currOldLane.isEmpty()) {
				// this old lane has been emptied
				currOldLanesQueue.remove(oldLane);
				System.out.format("The initial lane %d has been emptied and " +
								"removed from the new lane queue\n", oldLane);

				if (currOldLanesQueue.isEmpty()) {
					// all the old lanes in this queue have been removed
					newLanesQueue.remove(newLane);
				} else {
					newLanesQueue.requeueFirstEntry();
				}

				// reset for future reiterations
				polledCars = 0;

			} else if (polledCars == carsPerGroup) {
				// this old lane doesn't have any permits left
				currOldLanesQueue.requeueFirstEntry();
				System.out.format("The initial lane %d has no permits and " +
								"is moved to the back of the new lane queue\n", oldLane);

				// reset for future reiterations
				polledCars = 0;

				newLanesQueue.requeueFirstEntry();
			}

			newLanesQueue.notifyAll();
		}

	}

	private int getNewLane(int oldLane) {
		for (int newLane = 0; newLane < finalLaneCount; newLane++) {
			int start = (int) (newLane * (double) initialLaneCount / finalLaneCount);
			int end = Math.min((int) ((newLane + 1) * (double) initialLaneCount / finalLaneCount), initialLaneCount);

			if (oldLane >= start && oldLane < end)
				return newLane;
		}

		return -1;
	}

	private static class QueueHashMap<K, V> extends LinkedHashMap<K, V> {

		private static final long serialVersionUID = 1L;

		public Entry<K, V> getFirstEntry() {
			for (Entry<K, V> entry : entrySet())
				return entry;

			return null;
		}

		public V getFirstValue() {
			Entry<K, V> firstEntry = getFirstEntry();

			if (firstEntry == null)
				return null;

			return firstEntry.getValue();
		}

		public void requeueFirstEntry() {
			Entry<K, V> entry = getFirstEntry();
			remove(entry.getKey());
			put(entry);
		}

		public void put(Entry<K, V> entry) {
			put(entry.getKey(), entry.getValue());
		}
	}

}
