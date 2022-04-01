package lesson1;

public interface Runable {
    default void run() {
        System.out.println("Кто-то бежит.");
    };
}
