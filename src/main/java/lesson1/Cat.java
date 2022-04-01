package lesson1;

public class Cat extends Participant implements Runable, Jumpable {
    private String name;
    private int age;

    public Cat(String name, int age, int jumpHeight, int runDistance) {
        super(jumpHeight, runDistance, true);
        this.name = name;
        this.age = age;

    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }


    public void setPassObstacles(boolean passObstacles) {
        super.setPassObstacles(passObstacles);
    }

    public void jump() {
        System.out.println("Кот по кличке " + this.name + " начал прыгать.");
    }

    public void run() {
        System.out.println("Кот по кличке " + this.name + " побежал.");
    }

    @Override
    public void overcomeWall(Wall wall) {
        if (wall.getHeight() > super.getJumpHeight()) {
            System.out.println("Кот по кличке " + this.name + " не смог перепрыгнуть стену высотой " + wall.getHeight() + " м и выбыл из соревнования.");
        } else {
            System.out.println("Кот по кличке " + this.name + " смог перепрыгнуть стену высотой " + wall.getHeight());
        }
    }

    @Override
    public void runDistanceOnTreadmill(Treadmill treadmill) {
        if (treadmill.getDistanceToRun() > super.getRunDistance()) {
            System.out.println("Кот по кличке " + this.name + " не смог пробежать дистанцию в " + treadmill.getDistanceToRun() + " м и выбыл из соревнования.");
        } else {
            System.out.println("Кот по кличке " + this.name + " смог пробежать дистанцию в " + treadmill.getDistanceToRun() + " м.");
        }
    }
}
