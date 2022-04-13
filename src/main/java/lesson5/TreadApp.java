package lesson5;

import java.util.Arrays;

public class TreadApp {

    static final int SIZE = 10_000_000;
    static final int HALF_SIZE = SIZE / 2;
    static private final Object obj = new Object();

    public static void main(String[] args) {
        float[] array1 = new float[SIZE];
        Arrays.fill(array1, 1);
        long a = System.currentTimeMillis();
        for (int i = 0; i < array1.length; i++) {
            array1[i] = (float) (array1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        System.out.println("One thread time: " + (System.currentTimeMillis() - a) + " ms.");

        float[] array2 = new float[SIZE];
        Arrays.fill(array2, 1);
        a = System.currentTimeMillis();
        float[] arrayFirstHalf = new float[HALF_SIZE];
        float[] arraySecondHalf = new float[HALF_SIZE];
        System.arraycopy(array2, 0, arrayFirstHalf, 0, HALF_SIZE);
        System.arraycopy(array2, HALF_SIZE, arraySecondHalf, 0, HALF_SIZE);
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < arrayFirstHalf.length; i++) {
                arrayFirstHalf[i] = (float) (arrayFirstHalf[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        t1.start();
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < arraySecondHalf.length; i++) {
                arraySecondHalf[i] = (float) (arraySecondHalf[i] * Math.sin(0.2f + (i + HALF_SIZE) / 5) * Math.cos(0.2f + (i + HALF_SIZE) / 5) * Math.cos(0.4f + (i + HALF_SIZE) / 2));
            }
        });
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.arraycopy(arrayFirstHalf, 0, array2, 0, HALF_SIZE);
        System.arraycopy(arraySecondHalf, 0, array2, HALF_SIZE, HALF_SIZE);
        System.out.println("Two threads time: " + (System.currentTimeMillis() - a) + " ms.");
        System.out.println(Arrays.equals(array1, array2));
    }
}
