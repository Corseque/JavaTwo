package lesson1;

public class Robot extends Participant implements Runable, Jumpable {
    private String model;

    public Robot(String model, int jumpHeight, int runDistance) {
        super(jumpHeight, runDistance, true);
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public void setPassObstacles(boolean passObstacles) {
        super.setPassObstacles(passObstacles) ;
    }

    public void jump() {
        System.out.println("Робот модели " + this.model + " начал прыгать.");
    }

    public void run() {
        System.out.println("Робот модели " + this.model + " побежал.");
    }

    @Override
    public void overcomeWall(Wall wall) {
        if (wall.getHeight() > super.getJumpHeight()) {
            System.out.println("Робот модели " + this.model + " не смог перепрыгнуть стену высотой " + wall.getHeight() + " м и выбыл из соревнования.");
        } else {
            System.out.println("Робот модели " + this.model + " смог перепрыгнуть стену высотой " + wall.getHeight());
        }
    }

    @Override
    public void runDistanceOnTreadmill(Treadmill treadmill) {
        if (treadmill.getDistanceToRun() > super.getRunDistance()) {
            System.out.println("Робот модели " + this.model + " не смог пробежать дистанцию в " + treadmill.getDistanceToRun() + " м и выбыл из соревнования.");
        } else {
            System.out.println("Робот модели " + this.model + " смог пробежать дистанцию в " + treadmill.getDistanceToRun() + " м.");
        }
    }
}
