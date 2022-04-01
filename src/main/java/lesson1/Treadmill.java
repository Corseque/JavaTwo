package lesson1;

public class Treadmill extends Obstacles {
    private int distanceToRun;

    public Treadmill(int distanceToRun) {
        this.distanceToRun = distanceToRun;
    }

    public int getDistanceToRun() {
        return distanceToRun;
    }

    @Override
    public int getObstaclesSize() {
        return this.distanceToRun;
    }
}
