package lesson1;

public abstract class Participant {
    private int jumpHeight;
    private int runDistance;
    private boolean passObstacles;

    public Participant(int jumpHeight, int runDistance, boolean passObstacles) {
        this.jumpHeight = jumpHeight;
        this.runDistance = runDistance;
        this.passObstacles = passObstacles;
    }

    public int getJumpHeight() {
        return jumpHeight;
    }

    public int getRunDistance() {
        return runDistance;
    }

    public boolean isPassObstacles() {
        return passObstacles;
    }

    public void setPassObstacles(boolean passObstacles) {
        this.passObstacles = passObstacles;
    }

    public abstract void runDistanceOnTreadmill(Treadmill treadmill);

    public abstract void overcomeWall(Wall wall);
}
