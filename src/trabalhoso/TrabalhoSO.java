/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package trabalhoso;

import java.util.concurrent.Semaphore;

/**
 *
 * @author SadBo
 */
public class TrabalhoSO {

    private static final int NUM_FILOSOFOS = 5;
    private static final Semaphore[] garfos = new Semaphore[NUM_FILOSOFOS];
    private static final Semaphore mutex = new Semaphore(1);

    public static void main(String[] args) throws InterruptedException {
        // inicializa os semáforos dos garfos
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            garfos[i] = new Semaphore(1);
        }

        // cria as threads dos filósofos
        Thread[] filosofos = new Thread[NUM_FILOSOFOS];
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            final int id = i;
            filosofos[i] = new Thread(() -> {
                while (true) {
                    try {
                        // pensa um pouco
                        pensa(id);

                        // tenta pegar os garfos
                        mutex.acquire();
                        garfos[id].acquire();
                        garfos[(id + 1) % NUM_FILOSOFOS].acquire();
                        mutex.release();

                        // come um pouco
                        come(id);

                        // solta os garfos
                        garfos[id].release();
                        garfos[(id + 1) % NUM_FILOSOFOS].release();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            });
            filosofos[i].start();
        }

        // aguarda as threads terminarem
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            filosofos[i].join();
        }
    }

    private static void pensa(int num) throws InterruptedException {
        System.out.println("O filosofo " + num + " esta pensando");
        Thread.sleep((long) (Math.random() * 100));
    }

    private static void come(int num) throws InterruptedException {
        System.out.println("O filosofo " + num + " esta comendo");
        Thread.sleep((long) (Math.random() * 100));
    }
}
