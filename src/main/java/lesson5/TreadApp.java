package lesson5;

import java.util.Arrays;

public class TreadApp {

    static final int SIZE = 10_000_000;
    static final int HALF_SIZE = SIZE / 2;

    public static void main(String[] args) {
        float[] array = new float[SIZE];
        initArray(array);
        long a = System.currentTimeMillis();
        array = calculateArray(array);
        System.out.println("One thread time: " + (System.currentTimeMillis() - a) + " ms.");

        a = System.currentTimeMillis();
        float[] finalArray = initArray(array);
        new Thread(() -> {
            float[] arrayFirstHalf = new float[HALF_SIZE];
            System.arraycopy(finalArray, 0, arrayFirstHalf, 0, HALF_SIZE);
            arrayFirstHalf = calculateArray(arrayFirstHalf);
            System.arraycopy(arrayFirstHalf, 0, finalArray, 0, HALF_SIZE);
        });
        new Thread(() -> {
            float[] arraySecondHalf = new float[HALF_SIZE];
            System.arraycopy(finalArray, HALF_SIZE, arraySecondHalf, 0, HALF_SIZE);
            arraySecondHalf = calculateArray(arraySecondHalf);
            System.arraycopy(arraySecondHalf, 0, finalArray, HALF_SIZE, HALF_SIZE);
        });
        System.out.println("Two threads time: " + (System.currentTimeMillis() - a) + " ms.");
    }

    static synchronized float[] initArray(float[] array) {
        Arrays.fill(array, 1);
        return array;
    }

    static synchronized float[] calculateArray(float[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = (float)(array[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        return array;
    }
}
