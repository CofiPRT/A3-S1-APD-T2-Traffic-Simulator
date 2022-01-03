package com.apd.tema2.entities;

import java.util.concurrent.BrokenBarrierException;

/**
 * Utilizata pentru a uniformiza tipul de date ce ajuta la definirea unei intersectii / a unui task.
 * Implementarile acesteia vor contine variabile specifice task-ului, respectiv mecanisme de sincronizare.
 */
public interface Intersection {
	void acceptCar(Car car) throws InterruptedException, BrokenBarrierException;
}
