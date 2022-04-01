package lesson1;

public class Wall extends Obstacles {
    private int height;

    public Wall(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public int getObstaclesSize() {
        return this.height;
    }
}
