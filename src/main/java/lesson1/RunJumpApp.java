package lesson1;

public class RunJumpApp {
    public static void main(String[] args) {
        Cat cat1 = new Cat("Барсик", 2, 3, 50);
        Cat cat2 = new Cat("Мотя", 10, 1, 20);
        Human human1 = new Human("Олег", 15, 1, 5000);
        Robot robot1 = new Robot("Андроид", 2, 10000);

//        cat1.run();
//        cat2.jump();
//        human1.jump();
//        robot1.run();

        Wall wall1 = new Wall(2);
        Wall wall2 = new Wall(3);
        Treadmill treadmill1 = new Treadmill(30);
        Treadmill treadmill2 = new Treadmill(5000);

        Participant[] participants = new Participant[] {cat1, cat2, human1, robot1};
        Obstacles[] obstacles = new Obstacles[] {wall1, wall2, treadmill1, treadmill2};

        passCompetition(participants, obstacles);

    }

    public static void passCompetition(Participant[] participants, Obstacles[] obstacles) {
        for (Obstacles obstacle : obstacles) {
            for (Participant participant : participants) {
                if (participant.isPassObstacles() && obstacle instanceof Wall) {
                    if (participant.getJumpHeight() < obstacle.getObstaclesSize() ) {
                        participant.overcomeWall((Wall) obstacle);
                        participant.setPassObstacles(false);
                    } else {
                        participant.overcomeWall((Wall) obstacle);
                    }
                }
                if (participant.isPassObstacles() && obstacle instanceof Treadmill) {
                    if (participant.getRunDistance() < obstacle.getObstaclesSize()) {
                        participant.runDistanceOnTreadmill((Treadmill) obstacle);
                        participant.setPassObstacles(false);
                    } else {
                        participant.runDistanceOnTreadmill((Treadmill) obstacle);
                    }
                }
            }
        }
    }
}
